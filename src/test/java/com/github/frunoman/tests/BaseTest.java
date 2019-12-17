package com.github.frunoman.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoman.pages.MainPage;
import com.github.frunoman.utils.Utils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;
    private static Hub gridServer;
    private static String localhost;
    private static int port;
    protected AndroidDriver driver;
    protected MainPage mainPage;
    private Properties properties;
    private static final String APPIUM_MAIN_JS_PATH = "appium.main.js.path";
    private static final String PROJECT_PROPERTIES = "project.properties";
    private static final String APP_WAIT_ACTIVITY = "com.github.allureadvanced.*";
    private static final String ALLURE_RESULTS_ZIP = "/sdcard/allure-results.zip";
    protected static final String ALLURE_RESULT_FILE = "allure-results.zip";

    private ClassLoader classLoader;

    @BeforeSuite
    public void beforeSuite() throws IOException {
        classLoader = getClass().getClassLoader();
        properties = new Properties();
        properties.load(new FileInputStream(new File(classLoader.getResource(PROJECT_PROPERTIES).getPath())));

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

    @Parameters({"udid"})
    @BeforeTest
    public void beforeTest(@Optional String udid) throws IOException, InterruptedException {
        GridNodeConfiguration nodeConfiguration = new GridNodeConfiguration();
        nodeConfiguration.hubHost = localhost;
        nodeConfiguration.hubPort = port;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");

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

        Thread.sleep(8000);

        DesiredCapabilities appiumCapabilities = new DesiredCapabilities();
        appiumCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        appiumCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "device");
        appiumCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "UiAutomator2");
        appiumCapabilities.setCapability(MobileCapabilityType.APP, classLoader.getResource("app-debug.apk").getPath());
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, APP_WAIT_ACTIVITY);
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, Utils.nextFreePort(5672, 5690));

        driver = new AndroidDriver<MobileElement>(new URL(gridServer.getUrl().toString() + "/wd/hub"), appiumCapabilities);
        driver.allowInvisibleElements(true);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        driver.pushFile(ALLURE_RESULTS_ZIP, new File(classLoader.getResource(ALLURE_RESULT_FILE).getPath()));

    }

    @BeforeMethod
    public void beforeMethod() {
        mainPage = new MainPage(driver);
    }


    @AfterMethod
    public void afterMethod() {
        driver.resetApp();
    }

    @AfterSuite
    public void afterSuite() throws InterruptedException {
        driver.quit();
        service.stop();
        gridServer.stop();
        Thread.sleep(5000);
    }


}
