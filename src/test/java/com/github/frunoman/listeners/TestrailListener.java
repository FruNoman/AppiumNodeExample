package com.github.frunoman.listeners;

import com.codepine.api.testrail.TestRail;
import com.google.common.io.Resources;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.Properties;

public class TestrailListener implements ITestListener {
    private static final String ENDPOINT= "testrail.endpoint";
    private static final String USERNAME= "testrail.username";
    private static final String PASSWORD= "testrail.password";
    private static final String PROJECT_PROPERTIES = "project.properties";

    private TestRail testRail;
    private Properties properties;

    public TestrailListener() throws IOException {
        properties = new Properties();
        properties.load(Resources.getResource(PROJECT_PROPERTIES).openStream());
        this.testRail = TestRail.builder(
                properties.getProperty(ENDPOINT),
                properties.getProperty(USERNAME),
                properties.getProperty(PASSWORD)).build();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
