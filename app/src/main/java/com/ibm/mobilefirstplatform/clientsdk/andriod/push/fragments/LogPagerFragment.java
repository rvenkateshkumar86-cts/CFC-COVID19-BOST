/*******************************************************************************
 * Copyright (c) 2014-2015 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *    Mike Robertson - initial contribution
 *******************************************************************************/
package com.ibm.mobilefirstplatform.clientsdk.andriod.push.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ibm.mobilefirstplatform.clientsdk.andriod.push.IoTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.utils.Constants;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;



/**
 * The Log fragment displays text command messages that have been received by the application.
 */
public class LogPagerFragment extends ListFragment {
    private final static String TAG = LogPagerFragment.class.getName();
    private IoTStarterApplication app;
    private BroadcastReceiver broadcastReceiver;

    private ArrayAdapter<String> listAdapter;

    /**************************************************************************
     * Fragment functions for establishing the fragment
     **************************************************************************/

    public static LogPagerFragment newInstance() {
        return new LogPagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    /**
     * Called when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, ".onDestroy() entered");

        try {
            getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException iae) {
            // Do nothing
        }
        super.onDestroy();
    }

    /**
     * Initializing onscreen elements and shared properties
     */
    private void initializeLogActivity() {
        Log.d(TAG, ".initializeLogActivity() entered");
    }

    private void processIntent(Intent intent) {
        Log.d(TAG, ".processIntent() entered");

        app.setUnreadCount(0);

        String data = intent.getStringExtra(Constants.INTENT_DATA);
        assert data != null;
        if (data.equals(Constants.TEXT_EVENT)) {
            listAdapter.notifyDataSetInvalidated();
        } else if (data.equals(Constants.ALERT_EVENT)) {
            listAdapter.notifyDataSetInvalidated();
            String message = intent.getStringExtra(Constants.INTENT_DATA_MESSAGE);
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.alert_dialog_title))
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        }
    }

    /**************************************************************************
     * Functions to handle the iot_menu bar
     **************************************************************************/

    private void openProfiles() {
        Log.d(TAG, ".openProfiles() entered");
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion < Build.VERSION_CODES.HONEYCOMB) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Profiles Unavailable")
                    .setMessage("Android 3.0 or greater required for profiles.")
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        } /*else {
            Intent profilesIntent = new Intent(getActivity().getApplicationContext(), ProfilesActivity.class);
            startActivity(profilesIntent);
        }*/
    }





}
