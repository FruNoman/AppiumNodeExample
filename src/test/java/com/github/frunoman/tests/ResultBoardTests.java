package com.github.frunoman.tests;

import com.github.frunoman.pages.PopupPage;
import io.appium.java_client.MobileElement;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import static org.hamcrest.core.Is.is;

public class ResultBoardTests extends BaseTest {
    private static final String SDCARD = "sdcard";
    private static final String UNEXPECTED_CARD_STATE = "Unexpected card state";

    @Test
    public void successAddResultTest() {
        mainPage.clickOnAddResultButton();
        PopupPage popupPage = new PopupPage(driver);
        popupPage.selectFileByName(SDCARD);
        popupPage.selectFileByName(ALLURE_RESULT_FILE);
        popupPage.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, true, is(card.isEnabled()));
    }

    @Test
    public void successDeleteResultTest() {
        mainPage.clickOnAddResultButton();
        PopupPage popupPage = new PopupPage(driver);
        popupPage.selectFileByName(SDCARD);
        popupPage.selectFileByName(ALLURE_RESULT_FILE);
        popupPage.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, true, is(card.isEnabled()));
        mainPage.deleteCard(card);
        card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, null, is(card));

    }
}
