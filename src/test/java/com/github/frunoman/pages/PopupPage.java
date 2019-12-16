package com.github.frunoman.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.List;

public class PopupPage extends BasePage {
    @AndroidFindBy(uiAutomator = "com.github.allure:id/file_mark")
    private List<MobileElement> fileMark;
    @AndroidFindBy(id = "com.github.allure:id/fname")
    private List<MobileElement> fileName;
    @AndroidFindBy(id = "com.github.allure:id/select")
    private MobileElement select;
    @AndroidFindBy(id = "com.github.allure:id/cancel")
    private MobileElement cancel;

    public PopupPage(AndroidDriver driver) {
        super(driver);
    }


    public void selectFileByName(String str) {
       driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + str + "\").instance(0))").click();
    }

    public void clickSelect(){
        select.click();
    }

    public void clickCancel(){
        cancel.click();
    }

}
