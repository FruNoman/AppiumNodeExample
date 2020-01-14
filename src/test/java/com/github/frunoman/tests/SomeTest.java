package com.github.frunoman.tests;

import com.android.SdkConstants;
import com.android.prefs.AndroidLocation;
import com.android.repository.api.LocalPackage;
import com.android.repository.api.ProgressIndicator;
import com.android.repository.api.RepoManager;
import com.android.sdklib.BuildToolInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.AndroidSdkHandler;
import com.android.sdklib.repository.installer.SdkInstallerUtil;
import com.android.sdklib.repository.targets.AndroidTargetManager;
import com.android.sdklib.repository.targets.SystemImage;
import com.android.sdklib.repository.targets.SystemImageManager;
import com.android.sdklib.tool.SdkManagerCli;
import com.android.utils.ILogger;
import com.google.common.io.Files;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SomeTest {
    @Test
    public void some() throws AndroidLocation.AndroidLocationException, IOException {
        ProgressIndicator progressIndicator = new ProgressIndicator() {
            @Override
            public void setText(String s) {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public void setCancellable(boolean b) {

            }

            @Override
            public boolean isCancellable() {
                return false;
            }

            @Override
            public void setIndeterminate(boolean b) {

            }

            @Override
            public boolean isIndeterminate() {
                return false;
            }

            @Override
            public void setFraction(double v) {

            }

            @Override
            public double getFraction() {
                return 0;
            }

            @Override
            public void setSecondaryText(String s) {

            }

            @Override
            public void logWarning(String s) {

            }

            @Override
            public void logWarning(String s, Throwable throwable) {

            }

            @Override
            public void logError(String s) {

            }

            @Override
            public void logError(String s, Throwable throwable) {

            }

            @Override
            public void logInfo(String s) {

            }
        };
        ILogger iLogger = new ILogger() {
            @Override
            public void error(Throwable throwable, String s, Object... objects) {
                System.out.println("FUCK");
            }

            @Override
            public void warning(String s, Object... objects) {

            }

            @Override
            public void info(String s, Object... objects) {
                System.out.println("GOOOD");

            }

            @Override
            public void verbose(String s, Object... objects) {

            }
        };

        AndroidSdkHandler sdkHandler = AndroidSdkHandler.getInstance(new File("/home/dfrolov/Android/Sdk"));
        RepoManager sdkManager = sdkHandler.getSdkManager(progressIndicator);
        AvdManager avdManager = AvdManager.getInstance(sdkHandler, iLogger);
        SystemImageManager systemImageManager = sdkHandler.getSystemImageManager(progressIndicator);
        SystemImage currentSystemImage = null;
        for (SystemImage systemImage : systemImageManager.getImages()) {
            if (systemImage.getAndroidVersion().getApiLevel() == 27) {
                currentSystemImage = systemImage;
                break;
            }
        }
        BuildToolInfo buildToolInfo = sdkHandler.getLatestBuildTool(progressIndicator, true);
        IAndroidTarget iAndroidTarget = sdkHandler
                .getAndroidTargetManager(progressIndicator)
                .getTargetOfAtLeastApiLevel(27, progressIndicator);


        Map<String, String> hardwareProperties = new HashMap<>();
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("/home/dfrolov/IdeaProjects/RemoteAppiumGrid/src/main/resources/my.properties")));
        hardwareProperties.putAll(((Map) properties));


        Map<String, String> bootConfig = new HashMap<>();
        Collection<LocalPackage> sourcePackages = sdkManager.getPackages().getLocalPackagesForPrefix(SdkConstants.FD_ANDROID_SOURCES);
        LocalPackage ndk = sdkHandler.getLocalPackage(SdkConstants.FD_NDK, progressIndicator);

        DeviceManager deviceManager = DeviceManager.createInstance(new File("/home/dfrolov/Android/Sdk"), iLogger);
        deviceManager.getDevices(DeviceManager.DeviceFilter.DEFAULT);

        AvdInfo[] avdInfos = avdManager.getAllAvds();

        if (avdInfos.length == 0) {
            AvdInfo avdInfo = avdManager.createAvd(new File(avdManager.getBaseAvdFolder().getAbsolutePath() + File.separator + "avd_" + new Date().getTime()), "test_device", currentSystemImage, new File("/home/dfrolov/Android/Sdk/skins"), "pixel_2", "512M", hardwareProperties, bootConfig, false, false, false, iLogger);
            System.out.println(avdInfo);
        }
    }
}
