package com.github.frunoman.tests;

import com.github.frunoman.pages.EditResultPage;
import com.github.frunoman.pages.FilePicker;
import io.appium.java_client.MobileElement;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import static org.hamcrest.core.Is.is;

public class ResultBoardTests extends BaseTest {
    private static final String SDCARD = "sdcard";
    private static final String UNEXPECTED_CARD_STATE = "Unexpected card state";
    private static final String UNEXPECTED_CARD_NAME = "Unexpected card name";


    @Test(priority = 1)
    public void successAddResultTest() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, true, is(card.isEnabled()));
    }

    @Test(priority = 2)
    public void successDeleteResultTest() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, true, is(card.isEnabled()));
        mainPage.deleteCard(card);
        card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, null, is(card));
    }

    @Test(priority = 3)
    public void openEditResultPage() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_CARD_STATE, true, is(card.isEnabled()));
        mainPage.editCard(card);
        EditResultPage editResultPage = new EditResultPage(driver);
        MatcherAssert.assertThat(UNEXPECTED_CARD_NAME, ALLURE_RESULT_FILE, is(editResultPage.getResultName()));
    }
}
