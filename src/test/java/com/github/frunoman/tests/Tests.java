package com.github.frunoman.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Tests {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;

    @BeforeSuite
    public void beforeSuite() throws UnknownHostException {
        builder = new AppiumServiceBuilder();
        builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
        builder.usingAnyFreePort();
        builder.withIPAddress("0.0.0.0");
        builder.withArgument(GeneralServerFlag.CONFIGURATION_FILE,"/home/dfrolov/IdeaProjects/AppiumUnixExample/src/main/resources/android.json");
        builder.build();
        service = AppiumDriverLocalService.buildService( builder);
        service.start();
    }

    @Test
    public void testing() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Android");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "device");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.APP, getClass().getClassLoader().getResource("rozetka.apk").getPath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "ua.com.rozetka.shop.*");




        AppiumDriver driver = new AppiumDriver<MobileElement>(new URL("http://172.22.89.63:4445/wd/hub"), capabilities);
        System.out.println("");
        driver.quit();
        service.stop();
    }
}
