package com.github.frunoman.tests;

import com.github.frunoman.pages.PopupPage;
import io.appium.java_client.MobileElement;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import static org.hamcrest.core.Is.is;

public class AddResultTests extends BaseTest {

    @Test
    public void testAddResultButton() {
        mainPage.clickOnAddResultButton();
        PopupPage popupPage = new PopupPage(driver);
        popupPage.selectFileByName("sdcard");
        popupPage.selectFileByName("allure-results(3).zip");
        popupPage.clickSelect();
        MobileElement card = mainPage.findCardByName("allure-results(3).zip");
        MatcherAssert.assertThat("",true,is(card.isEnabled()));
    }
}
