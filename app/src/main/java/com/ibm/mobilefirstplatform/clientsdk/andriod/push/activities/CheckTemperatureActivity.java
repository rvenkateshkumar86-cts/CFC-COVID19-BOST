package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;

import android.app.Activity;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;
import android.os.Bundle;

public class CheckTemperatureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_temperature);
    }
}