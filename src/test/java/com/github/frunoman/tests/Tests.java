package com.github.frunoman.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoman.pages.MainPage;
import com.github.frunoman.pages.PopupPage;
import com.github.frunoman.utils.Utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class Tests {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;
    private static Hub gridServer;
    private static String localhost;
    private static int port;
    private AndroidDriver driver;
    private MainPage mainPage;

    @BeforeSuite
    public void beforeSuite() throws IOException {
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

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "Android");

        List<MutableCapabilities> capabilitiesList = new ArrayList<MutableCapabilities>();
        capabilitiesList.add(capabilities);
        nodeConfiguration.capabilities = capabilitiesList;

        ObjectMapper mapper = new ObjectMapper();
        File file = File.createTempFile("tmp" + new Date().getTime(), ".json");
        mapper.writeValue(file, nodeConfiguration.toJson());

        builder = new AppiumServiceBuilder();
        builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
        builder.usingAnyFreePort();
        builder.withIPAddress(InetAddress.getLocalHost().getHostAddress());
        builder.withArgument(GeneralServerFlag.CONFIGURATION_FILE, file.getAbsolutePath());
        builder.build();
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        Thread.sleep(5000);

        DesiredCapabilities appiumCapabilities = new DesiredCapabilities();
//        if (udid != null) {
//            appiumCapabilities.setCapability(MobileCapabilityType.UDID, udid);
//        }
        appiumCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Android");
        appiumCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        appiumCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "device");
        appiumCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "UIAutomator2");
        appiumCapabilities.setCapability(MobileCapabilityType.APP, getClass().getClassLoader().getResource("app-debug.apk").getPath());
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "com.github.allureadvanced.*");
        appiumCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, Utils.nextFreePort(5672, 5690));

        driver = new AndroidDriver<MobileElement>(new URL(gridServer.getUrl().toString() + "/wd/hub"), appiumCapabilities);
        driver.allowInvisibleElements(true);
    }

    @BeforeMethod
    public void beforeMethod() {
        mainPage = new MainPage(driver);
    }

    @Test
    public void testAddResultButton() throws InterruptedException {
        mainPage.clickOnAddResultButton();
        PopupPage popupPage = new PopupPage(driver);
        popupPage.selectFileByName("sdcard");
        popupPage.selectFileByName("allure-results(3).zip");
        popupPage.clickSelect();
        MobileElement card = mainPage.findCardByName("allure-results(3).zip");
        MatcherAssert.assertThat(true,is(card.isEnabled()));
        Thread.sleep(10000);
    }

    @AfterMethod
    public void afterMethod() {
        driver.resetApp();
    }

    @AfterSuite
    public void afterSuite() throws InterruptedException {
        service.stop();
        gridServer.stop();
        Thread.sleep(10000);
    }


}
