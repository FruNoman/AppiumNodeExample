package com.github.frunoman.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;

import java.util.List;

public class MainPage extends BasePage {

    @AndroidFindBy(id = "com.github.allure:id/action_search")
    private MobileElement searchButton;
    @AndroidFindBy(id = "com.github.allure:id/add_result")
    private MobileElement addResultButton;
    @AndroidFindBy(id = "com.github.allure:id/search_src_text")
    private MobileElement topSearchField;
    @AndroidFindBy(className = "android.widget.ImageButton")
    private MobileElement backButton;
    @AndroidFindBy(id = "com.github.allure:id/result_recycle_fragment")
    private MobileElement recyclerView;
    @AndroidFindBy(id = "com.github.allure:id/card_view")
    private List<MobileElement> cardViews;

    private String resultTextId = "com.github.allure:id/resultText";
    private String editButtonId = "com.github.allure:id/editResultButton";
    private String deleteButtonId = "com.github.allure:id/deleteResultButton";


    public MainPage(AndroidDriver driver) {
        super(driver);
    }

    public void clickOnAddResultButton() {
        addResultButton.click();
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    public void searchResult(String text) {
        topSearchField.sendKeys(text);
    }

    public void selectCardByName(String cardName) {
        for (MobileElement element:cardViews){
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)){
                element.click();
            }
        }
    }

    public void editCardByName(String cardName) {
        for (MobileElement element:cardViews){
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)){
                element.findElement(By.id(editButtonId)).click();
            }
        }
    }

    public void deleteCardByName(String cardName) {
        for (MobileElement element:cardViews){
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)){
                element.findElement(By.id(deleteButtonId)).click();
            }
        }
    }

    public MobileElement findCardByName(String cardName){
        MobileElement card = null;
        for (MobileElement element:cardViews){
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)){
               card = element;
            }
        }
        return card;
    }
}
