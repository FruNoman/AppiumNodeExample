package com.github.frunoman.utils;

import com.codepine.api.testrail.model.Project;
import com.codepine.api.testrail.model.Suite;
import io.qameta.allure.Attachment;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;
import java.util.Random;

public class Utils {

    public static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static int nextFreePort(int min, int max) {
        int port = new Random().nextInt((max - min) + 1) + min;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            }
        }
    }

    public static Project findProjectByName(List<Project> projectList,String projectName){
        Project currentProject = null;
        for (Project project : projectList){
            if (project.getName().equals(projectName)){
                currentProject = project;
                break;
            }
        }
        return currentProject;
    }

    public static Suite findSuiteByName(List<Suite> suiteList, String suiteName){
        Suite currentSuite = null;
        for (Suite suite : suiteList){
            if (suite.getName().equals(suiteName)){
                currentSuite = suite;
                break;
            }
        }
        return currentSuite;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Attachment(value = "Video", type = "video/mp4")
    public static byte[] attachVideo(File video) {
        byte[] screenShot = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(video);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];

            for (int readNum; (readNum = fis.read(b)) != -1; ) {
                bos.write(b, 0, readNum);
            }
            screenShot = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenShot;
    }

    @Attachment(value = "Attachment", type = "text/html")
    public static byte[] attachText(File video) {
        byte[] screenShot = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(video);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];

            for (int readNum; (readNum = fis.read(b)) != -1; ) {
                bos.write(b, 0, readNum);
            }
            screenShot = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenShot;
    }
}
