package com.github.frunoman.listeners;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.*;
import com.github.frunoman.utils.Utils;
import com.google.common.io.Resources;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class TestrailListener implements ITestListener {
    private static final String ENDPOINT = "testrail.endpoint";
    private static final String USERNAME = "testrail.username";
    private static final String PASSWORD = "testrail.password";
    private static final String PROJECT = "testrail.project";
    private static final String SUITE = "testrail.suite";
    private static final String RUN_NAME = "testrail.run.name";
    private static final String DATE_FORMAT_PATTERN = "MM-dd-yyyy HH:mm:ss";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    private static final String PROJECT_PROPERTIES = "project.properties";

    private TestRail testRail;
    private Properties properties;
    private Project currentProject;
    private Suite currentSuite;
    private Run run;
    private Integer caseId;

    public TestrailListener() throws IOException {
        properties = new Properties();
        properties.load(Resources.getResource(PROJECT_PROPERTIES).openStream());
        this.testRail = TestRail.builder(
                properties.getProperty(ENDPOINT),
                properties.getProperty(USERNAME),
                properties.getProperty(PASSWORD)).build();

        List<Project> projectList = testRail.projects().list().execute();
        this.currentProject = Utils.findProjectByName(projectList, properties.getProperty(PROJECT));
        List<Suite> suiteList = testRail.suites().list(currentProject.getId()).execute();
        this.currentSuite = Utils.findSuiteByName(suiteList, properties.getProperty(SUITE));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Test testAnnotation = iTestResult.getMethod().getMethod().getAnnotation(Test.class);
        String caseName = testAnnotation.testName();
        if (Utils.isNumeric(caseName)) {
            this.caseId = Integer.parseInt(caseName);
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        AllureLifecycle allureLifecycle = Allure.getLifecycle();
        if (caseId != null) {
            List<ResultField> customResultFields = testRail.resultFields().list().execute();
            testRail.results().addForCase(run.getId(), caseId, new Result().setStatusId(1), customResultFields).execute();
        }
        caseId = null;
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (caseId != null) {
            List<ResultField> customResultFields = testRail.resultFields().list().execute();
            testRail.results().addForCase(run.getId(), caseId, new Result().setStatusId(5), customResultFields).execute();
        }
        caseId = null;
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (caseId != null) {
            List<ResultField> customResultFields = testRail.resultFields().list().execute();
            testRail.results().addForCase(run.getId(), caseId, new Result().setStatusId(3), customResultFields).execute();
        }
        caseId = null;
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        run = testRail
                .runs()
                .add(currentProject.getId(),
                        new Run()
                                .setSuiteId(currentSuite.getId())
                                .setName(properties.getProperty(RUN_NAME) + " " + SIMPLE_DATE_FORMAT.format(new Date())))
                .execute();
    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
