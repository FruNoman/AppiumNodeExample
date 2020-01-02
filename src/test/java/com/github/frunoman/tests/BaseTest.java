package com.github.frunoman.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoman.listeners.TestrailListener;
import com.github.frunoman.pages.MainPage;
import com.github.frunoman.utils.Utils;
import com.google.common.io.Resources;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Listeners({TestrailListener.class})
public class BaseTest {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;
    private static Hub gridServer;
    private static String localhost;
    private static int port;
    protected AndroidDriver driver;
    protected MainPage mainPage;
    private static Properties properties;
    private static final String APPIUM_MAIN_JS_PATH = "appium.main.js.path";
    private static final String PROJECT_PROPERTIES = "project.properties";
    private static final String SCREEN_RECORD_RESOLUTION = "screen.record.resolution";
    private static final String SCREEN_RECORD_BITRATE = "screen.record.bitrate";
    private static final String SCREEN_RECORD_NAME = "screen.record.name";
    private static final String APP_WAIT_ACTIVITY = "com.github.allureadvanced.*";
    private static final String ALLURE_RESULTS_ZIP = "/sdcard/allure-results.zip";
    private static final String APK = "app-debug.apk";
    protected static final String ALLURE_RESULT_FILE = "allure-results.zip";


    @BeforeSuite
    public void beforeSuite() throws IOException {
        if(properties==null) {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Resources.getResource(PROJECT_PROPERTIES).getPath())));
        }
        if (gridServer == null) {
            localhost = InetAddress.getLocalHost().getHostAddress();
            port = Utils.nextFreePort(4444, 4500);

            GridHubConfiguration hubConfiguration = new GridHubConfiguration();
            hubConfiguration.host = localhost;
            hubConfiguration.port = port;

            gridServer = new Hub(hubConfiguration);
            gridServer.start();
        }
    }

    @Parameters({"udid", "version"})
    @BeforeTest
    public void beforeTest(@Optional String udid, @Optional String version) throws IOException, InterruptedException {
        GridNodeConfiguration nodeConfiguration = new GridNodeConfiguration();
        nodeConfiguration.hubHost = localhost;
        nodeConfiguration.hubPort = port;

        DesiredCapabilities capabilities = DesiredCapabilities.android();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "Android");

        if (version != null) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
            capabilities.setCapability("version", version);

        }
        List<MutableCapabilities> capabilitiesList = new ArrayList<MutableCapabilities>();
        capabilitiesList.add(capabilities);
        nodeConfiguration.capabilities = capabilitiesList;

        ObjectMapper mapper = new ObjectMapper();
        File file = File.createTempFile("tmp" + new Date().getTime(), ".json");
        mapper.writeValue(file, nodeConfiguration.toJson());

        builder = new AppiumServiceBuilder();
        builder.withAppiumJS(new File(properties.getProperty(APPIUM_MAIN_JS_PATH)));
        builder.usingAnyFreePort();
        builder.withIPAddress(InetAddress.getLocalHost().getHostAddress());
        builder.withArgument(GeneralServerFlag.CONFIGURATION_FILE, file.getAbsolutePath());
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.build();
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        Thread.sleep(10000);
        DesiredCapabilities appiumCapabilities = new DesiredCapabilities();
        if (udid != null) {
            appiumCapabilities.setCapability(MobileCapabilityType.UDID, udid);
        }
        if (version != null) {
            appiumCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
        }
        appiumCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        appiumCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "device");
        appiumCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");

        appiumCapabilities.setCapability(MobileCapabilityType.APP, Resources.getResource(APK).getPath());
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, APP_WAIT_ACTIVITY);
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, Utils.nextFreePort(5672, 5690));

        driver = new AndroidDriver<MobileElement>(new URL(gridServer.getUrl().toString() + "/wd/hub"), appiumCapabilities);
        driver.allowInvisibleElements(true);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        driver.pushFile(ALLURE_RESULTS_ZIP, new File(Resources.getResource(ALLURE_RESULT_FILE).getPath()));

    }

    @BeforeMethod
    public void beforeMethod() {
        mainPage = new MainPage(driver);
        driver.startRecordingScreen(
                new AndroidStartScreenRecordingOptions()
                        .withVideoSize(properties.getProperty(SCREEN_RECORD_RESOLUTION))
                        .withBitRate(Integer.parseInt(properties.getProperty(SCREEN_RECORD_BITRATE)))
        );
    }


    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException {
        String video = driver.stopRecordingScreen();
        if (result.getStatus() == ITestResult.FAILURE) {
            byte[] decode = Base64.getDecoder().decode(video);
            String fileName = "./allure-results/"
                    + properties.getProperty(SCREEN_RECORD_NAME)
                    + "_"
                    + new Date().getTime()
                    + ".mp4";
            File file = new File(fileName);
            FileUtils.writeByteArrayToFile(file, decode);
            Utils.attachVideo(file);
        }
        driver.resetApp();
    }

    @AfterSuite
    public void afterSuite() throws InterruptedException {
        if(driver!=null) {
            driver.quit();
        }if(service.isRunning()) {
            service.stop();
        }if(gridServer.getUrl()!=null) {
            gridServer.stop();
        }
        Thread.sleep(5000);
    }


}
