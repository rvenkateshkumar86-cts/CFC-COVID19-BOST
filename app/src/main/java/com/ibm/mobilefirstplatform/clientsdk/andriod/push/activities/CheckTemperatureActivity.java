package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;

import com.ibm.mobilefirstplatform.clientsdk.andriod.push.BOSTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.fragments.IoTPagerFragment;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.fragments.LoginPagerFragment;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.utils.Constants;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CheckTemperatureActivity extends FragmentActivity {

    public static final String TAG = CheckTemperatureActivity.class.getName();

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pagertabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        if(savedInstanceState != null) {
            int tabIndex = savedInstanceState.getInt("tabIndex");
            pager.setCurrentItem(tabIndex, false);
            Log.d(TAG, "savedinstancestate != null: " + tabIndex);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to self assessment?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MedicalAssistanceActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*dialogInterface.cancel();*/
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BOSTStarterApplication app = (BOSTStarterApplication) getApplication();
    }

    /**
     * Save the current state of the activity. This is used to store the index of the currently
     * selected tab.
     * @param outState The state of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int tabIndex = pager.getCurrentItem();
        outState.putInt("tabIndex", tabIndex);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, ".onConfigurationChanged entered()");
        super.onConfigurationChanged(newConfig);
    }

    public void setCurrentItem(int item) {
        Log.d(TAG, ".setCurrentItem() entered");
        pager.setCurrentItem(item, true);
    }

    public int getCurrentItem() {
        Log.d(TAG, ".getCurrentItem() entered");
        return pager.getCurrentItem();
    }

    /**
     * Adapter for the ViewPager. Adds the tutorial fragments to the pager.
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    Log.d(TAG, "init loginpagerfragment");
                    return LoginPagerFragment.newInstance();
                case 1:
                    Log.d(TAG, "init iotpagerfragment");
                    return IoTPagerFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return Constants.LOGIN_LABEL;
                case 1:
                    return Constants.IOT_LABEL;
                default:
                    return null;
            }
        }
    }
}