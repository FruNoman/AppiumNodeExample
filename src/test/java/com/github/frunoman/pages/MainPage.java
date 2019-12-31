package com.github.frunoman.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

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

    @Step("Click on 'ADD RESULT' button")
    public void clickOnAddResultButton() {
        addResultButton.click();
    }

    @Step("Click on search button")
    public void clickSearchButton() {
        searchButton.click();
    }

    @Step("Search result  by text '{0}'")
    public void searchResult(String text) {
        topSearchField.sendKeys(text);
    }

    @Step("Select card by name '{0}'")
    public void selectCardByName(String cardName) {
        for (MobileElement element : cardViews) {
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)) {
                element.click();
            }
        }
    }

    @Step("Edit card by name '{0}'")
    public void editCardByName(String cardName) {
        for (MobileElement element : cardViews) {
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)) {
                element.findElement(By.id(editButtonId)).click();
            }
        }
    }

    @Step("Delete card by name '{0}'")
    public void deleteCardByName(String cardName) {
        for (MobileElement element : cardViews) {
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)) {
                element.findElement(By.id(deleteButtonId)).click();
            }
        }
    }

    @Step("Find card by name '{0}'")
    public MobileElement findCardByName(String cardName) {
        MobileElement card = null;
        for (MobileElement element : cardViews) {
            if (element.findElement(By.id(resultTextId)).getText().equals(cardName)) {
                card = element;
            }
        }
        return card;
    }

    @Step("Select card by mobile element")
    public void selectCard(MobileElement card) {
        card.click();
    }

    @Step("Edit card by mobile element")
    public void editCard(MobileElement card) {
        card.findElement(By.id(editButtonId)).click();
    }

    @Step("Delete card by mobile element")
    public void deleteCard(MobileElement card) {
        card.findElement(By.id(deleteButtonId)).click();
    }
}
