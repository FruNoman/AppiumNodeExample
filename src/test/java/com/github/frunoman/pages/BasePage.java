package com.github.frunoman.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class BasePage {
    protected AndroidDriver driver;

    public BasePage(AndroidDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver),this);
        this.driver = driver;
    }
}
