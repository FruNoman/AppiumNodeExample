package com.github.frunoman.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Tests {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;
    private static Hub gridServer;
    private static String localhost;
    private static int port;
    private AppiumDriver driver;
    private WebDriver webDriver;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {
        if (gridServer == null) {
            localhost = InetAddress.getLocalHost().getHostAddress();
            port = nextFreePort(4444, 4500);

            GridHubConfiguration hubConfiguration = new GridHubConfiguration();
            hubConfiguration.host = localhost;
            hubConfiguration.port = port;

            gridServer = new Hub(hubConfiguration);
            gridServer.start();
            System.setProperty("webdriver.chrome.driver", this.getClass().getClassLoader().getResource("chromedriver").getPath());


            webDriver = new ChromeDriver();
            webDriver.get(gridServer.getUrl().toString() + "/grid/console");
        }
    }

    @BeforeTest
    public void beforeTest() throws IOException, InterruptedException {
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

    }

    @Test
    public void testing() throws MalformedURLException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Android");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "device");
        capabilities.setCapability("automationName", "UIAutomator2");
        capabilities.setCapability(MobileCapabilityType.APP, getClass().getClassLoader().getResource("rozetka.apk").getPath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "ua.com.rozetka.shop.*");


        driver = new AppiumDriver<MobileElement>(new URL(gridServer.getUrl().toString() + "/wd/hub"), capabilities);
        webDriver.navigate().refresh();
    }

    @AfterTest
    public void afterClass() {
        driver.quit();
        service.stop();
    }

    @AfterSuite
    public void afterSuite() {
        gridServer.stop();
//        driver.quit();
        webDriver.quit();
    }

    private boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int nextFreePort(int min, int max) {
        int port = new Random().nextInt((max - min) + 1) + min;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            }
        }
    }
}
