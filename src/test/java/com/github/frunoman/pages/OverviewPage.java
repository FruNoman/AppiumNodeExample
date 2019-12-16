package com.github.frunoman.pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class OverviewPage extends BasePage {
    @AndroidFindBy(id = "com.github.allure:id/top_result_name")
    private MobileElement topResultName;
    @AndroidFindBy(id = "com.github.allure:id/duration_time")
    private MobileElement durationTime;
    @AndroidFindBy(id = "com.github.allure:id/count_tests")
    private MobileElement testCount;
    public TopMenu topMenu;

    public OverviewPage(AndroidDriver driver) {
        super(driver);
        topMenu = new TopMenu(driver);
    }

    public String getTopResultName() {
        return topResultName.getText();
    }

    public String getDurationTime() {
        return durationTime.getText();
    }

    public String getTestCount() {
        return testCount.getText();
    }
}
