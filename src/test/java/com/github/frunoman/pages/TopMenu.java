package com.github.frunoman.pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class TopMenu extends BasePage {
    @AndroidFindBy(id = "com.github.allure:id/action_exit")
    private MobileElement exitButton;

    public TopMenu(AndroidDriver driver) {
        super(driver);
    }

    public void clickExit(){
        exitButton.click();
    }
}
