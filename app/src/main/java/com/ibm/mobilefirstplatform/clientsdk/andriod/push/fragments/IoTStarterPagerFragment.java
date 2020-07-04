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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.BOSTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities.CheckTemperatureActivity;


/**
 * This class provides common properties and functions for fragment subclasses used in the application.
 */
public class IoTStarterPagerFragment extends Fragment {
    private final static String TAG = IoTStarterPagerFragment.class.getName();
    Context context;
    BOSTStarterApplication app;
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Update strings in the fragment based on IoTStarterApplication values.
     */
    void updateViewStrings() {
        Log.d(TAG, ".updateViewStrings() entered");
        // TODO: Update badge value
        //int unreadCount = app.getUnreadCount();
        //((MainPagerActivity) getActivity()).updateBadge(getActivity().getActionBar().getTabAt(2), unreadCount);
    }

    /**************************************************************************
     * Functions to handle the menu bar
     **************************************************************************/

    /**
     * Switch to the IoT fragment.
     */
    void openIoT() {
        Log.d(TAG, ".openIoT() entered");
        ((CheckTemperatureActivity) getActivity()).setCurrentItem(1);
    }



}
