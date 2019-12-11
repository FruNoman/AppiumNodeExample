package com.github.frunoman.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Tests {
    private AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {


        GridNodeConfiguration configuration = new GridNodeConfiguration();
        configuration.hubHost = "localhost";
        configuration.hubPort = 4449;

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,"Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM,"Android");
//        capabilities.setCapability(MobileCapabilityType.VERSION,"5.1");

        List<MutableCapabilities> capabilitiesList = new ArrayList<MutableCapabilities>();
        capabilitiesList.add(capabilities);
        configuration.capabilities = capabilitiesList;

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("papa.json");
        file.deleteOnExit();
        mapper.writeValue(file,configuration.toJson());

        builder = new AppiumServiceBuilder();
        builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
        builder.usingAnyFreePort();
        builder.withIPAddress(InetAddress.getLocalHost().getHostAddress());
        builder.withArgument(GeneralServerFlag.CONFIGURATION_FILE,file.getAbsolutePath());
        builder.build();
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        Thread.sleep(10000);
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




        AppiumDriver driver = new AppiumDriver<MobileElement>(new URL("http://172.22.89.63:4449/wd/hub"), capabilities);
        System.out.println("");
        driver.quit();
        service.stop();
    }
}
