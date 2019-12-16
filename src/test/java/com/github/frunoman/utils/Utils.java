package com.github.frunoman.utils;

import java.io.IOException;
import java.net.ServerSocket;
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
}
