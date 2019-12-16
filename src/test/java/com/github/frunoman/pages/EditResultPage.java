package com.github.frunoman.pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class EditResultPage extends BasePage {
    @AndroidFindBy(id = "com.github.allure:id/result_name")
    private MobileElement resultName;
    @AndroidFindBy(id = "com.github.allure:id/result_path")
    private MobileElement resultPath;
    @AndroidFindBy(id = "com.github.allure:id/add_result")
    private MobileElement saveButton;

    public EditResultPage(AndroidDriver driver) {
        super(driver);
    }

    public String getResultName() {
        return resultName.getText();
    }

    public void setResultName(String resultName) {
        this.resultName.sendKeys(resultName);
    }

    public String getResultPath() {
        return resultPath.getText();
    }

    public void setResultPath(String resultPath) {
        this.resultPath.sendKeys(resultPath);
    }

    public void clickSave(){
        saveButton.click();
    }
}
