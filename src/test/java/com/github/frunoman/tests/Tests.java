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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

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
    private Hub gridServer;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {
        String localhost = InetAddress.getLocalHost().getHostAddress();
        int port = nextFreePort(4444,4500);

        GridHubConfiguration hubConfiguration = new GridHubConfiguration();
        hubConfiguration.host = localhost;
        hubConfiguration.port = port;

        gridServer = new Hub(hubConfiguration);
        gridServer.start();


        GridNodeConfiguration nodeConfiguration = new GridNodeConfiguration();
        nodeConfiguration.hubHost =localhost;
        nodeConfiguration.hubPort = port;

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "Android");

        List<MutableCapabilities> capabilitiesList = new ArrayList<MutableCapabilities>();
        capabilitiesList.add(capabilities);
        nodeConfiguration.capabilities = capabilitiesList;

        ObjectMapper mapper = new ObjectMapper();
        File file = File.createTempFile("tmp"+new Date().getTime(),".json");
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
    public void testing() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Android");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "device");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability(MobileCapabilityType.APP, getClass().getClassLoader().getResource("rozetka.apk").getPath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "ua.com.rozetka.shop.*");


        AppiumDriver driver = new AppiumDriver<MobileElement>(new URL(gridServer.getUrl().toString() + "/wd/hub"), capabilities);
        System.out.println("");
        driver.quit();
        service.stop();
        gridServer.stop();
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
