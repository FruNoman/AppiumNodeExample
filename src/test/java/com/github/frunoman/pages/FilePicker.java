package com.github.frunoman.pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;

import java.util.List;

public class FilePicker extends BasePage {
    @AndroidFindBy(uiAutomator = "com.github.allure:id/file_mark")
    private List<MobileElement> fileMark;
    @AndroidFindBy(id = "com.github.allure:id/fname")
    private List<MobileElement> fileName;
    @AndroidFindBy(id = "com.github.allure:id/select")
    private MobileElement select;
    @AndroidFindBy(id = "com.github.allure:id/cancel")
    private MobileElement cancel;

    public FilePicker(AndroidDriver driver) {
        super(driver);
    }

    @Step("Select file by name '{0}'")
    public void selectFileByName(String str) {
       driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + str + "\").instance(0))").click();
    }

    @Step("Click 'Select' button")
    public void clickSelect(){
        select.click();
    }

    @Step("Click 'Cancel' button")
    public void clickCancel(){
        cancel.click();
    }

}
