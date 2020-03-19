package com.example.karan.winedine;

import android.app.Application;
import android.content.Context;

import com.andremion.counterfab.CounterFab;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by karan on 12/28/2016.
 */

public class MyApplication extends Application {
    static Context maincontext;
    static String foodtype;
    static Map<String,CartTemp> cart_item;
    static CounterFab counterFab;

    @Override
    public void onCreate() {
        super.onCreate();
        cart_item  = new HashMap<String,CartTemp>();
        maincontext = getApplicationContext();
    }
}
