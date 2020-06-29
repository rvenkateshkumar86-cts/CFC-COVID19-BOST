package com.ibm.mobilefirstplatform.clientsdk.andriod.push;

import android.app.Application;
import android.util.Log;

public class BOSTStarterApplication extends Application {

    private final static String TAG = BOSTStarterApplication.class.getName();

    @Override
    public void onCreate() {
        Log.d(TAG, ".onCreate() entered");
        super.onCreate();
    }
}
