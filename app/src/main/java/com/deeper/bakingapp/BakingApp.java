package com.deeper.bakingapp;

import android.app.Application;
import android.content.Context;

public class BakingApp extends Application {

    public static BakingApp get(Context context) {
        return (BakingApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}