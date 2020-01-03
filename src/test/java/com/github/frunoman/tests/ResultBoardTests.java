package com.github.frunoman.tests;

import com.github.frunoman.listeners.TestrailListener;
import com.github.frunoman.pages.OverviewPage;
import com.github.frunoman.pages.EditResultPage;
import com.github.frunoman.pages.FilePicker;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
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

    @TmsLink("1")
    @Severity(value = SeverityLevel.BLOCKER)
    @Test(description = "Success add result test",
            priority = 1,
            testName = "1")
    public void successAddResultTest() {
        Assert.fail();
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
    }

    @TmsLink("2")
    @Severity(value = SeverityLevel.NORMAL)
    @Test(description = "Success delete result",
            priority = 2,
            testName = "2")
    public void successDeleteResultTest() {
        mainPage.clickOnAddResultButton();
        FilePicker filePicker = new FilePicker(driver);
        filePicker.selectFileByName(SDCARD);
        filePicker.selectFileByName(ALLURE_RESULT_FILE);
        filePicker.clickSelect();
        MobileElement card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, card.isEnabled(), is(true));
        mainPage.deleteCard(card);
        card = mainPage.findCardByName(ALLURE_RESULT_FILE);
        MatcherAssert.assertThat(UNEXPECTED_ELEMENT_STATE, null, is(card));
    }

    @TmsLink("3")
    @Severity(value = SeverityLevel.NORMAL)
    @Test(description = "Open edit result page",
            priority = 3,
            testName = "3")
    public void openEditResultPage() {
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

    @TmsLink("4")
    @Severity(value = SeverityLevel.BLOCKER)
    @Test(description = "Success select allure result",
            priority = 4,
            testName = "4")
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
