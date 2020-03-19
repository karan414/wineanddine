package com.example.karan.winedine.utils;

import android.util.Log;

import com.example.karan.winedine.BuildConfig;

public class AppLogger {

    public static void LogD(String TAG, String message) {
        if (BuildConfig.LOG) {
            Log.d(TAG, message);
        }
    }

    public static void LogI(String TAG, String message) {
        if (BuildConfig.LOG) {
            Log.i(TAG, message);
        }
    }

    public static void LogE(String TAG, String message) {
        if (BuildConfig.LOG) {
            Log.e(TAG, message);
        }
    }

    public static void LogV(String TAG, String message) {
        if (BuildConfig.LOG) {
            Log.v(TAG, message);
        }
    }
}
