package com.github.frunoman.tests;

import com.github.frunoman.listeners.TestrailListener;
import com.github.frunoman.pages.OverviewPage;
import com.github.frunoman.pages.EditResultPage;
import com.github.frunoman.pages.FilePicker;
import io.appium.java_client.MobileElement;
import io.qameta.allure.TmsLink;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;


public class ResultBoardTests extends BaseTest {
    private static final String SDCARD = "sdcard";
    private static final String UNEXPECTED_ELEMENT_STATE = "Unexpected element state";
    private static final String UNEXPECTED_ELEMENT_NAME = "Unexpected element name";
    private static final String TOP_NAME = "ALLURE REPORT";

    @Test(priority = 1,testName = "1")
    public void successAddResultTest() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
    }

    @Test(priority = 2,testName = "2")
    public void successDeleteResultTest() throws Exception {
        throw new Exception("FUCK");
//        mainPage.clickOnAddResultButton();
//        FilePicker filePicker = new FilePicker(driver);
//        filePicker.selectFileByName(SDCARD);
//        filePicker.selectFileByName(ALLURE_RESULT_FILE);
//        filePicker.clickSelect();
//        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
//        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
//        mainPage.deleteCard(card);
//        card = mainPage.findCardByName(ALLURE_RESULT_FILE);
//        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, null, is(card));
    }

    @Test(priority = 3,testName = "3")
    public void openEditResultPage() {
        Assert.fail();
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
        mainPage.editCard(card);
        EditResultPage editResultPage = new EditResultPage(driver);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_NAME, editResultPage.getResultName(), is(ALLURE_RESULT_FILE));
    }

    @Test(priority = 4,testName = "4")
    public void successSelectAllureResult() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
        mainPage.selectCard(card);
        OverviewPage overviewPage = new OverviewPage(driver);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_NAME, overviewPage.getTopResultName(), containsString(TOP_NAME));
    }
}
