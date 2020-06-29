package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;
/**
 * Copyright 2015, 2016 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.NotificationConnector;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MFPPush push; // Push client
    private MFPPushNotificationListener notificationListener; // Notification listener to handle a push sent to the phone
    List<String> subscribedTags;
    String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize core SDK with IBM Bluemix application Region, TODO: Update region if not using Bluemix US SOUTH
        BMSClient.getInstance().initialize(this, BMSClient.REGION_US_SOUTH);

        // Grabs push client sdk instance
        push = MFPPush.getInstance();
        String appGuid = getResources().getString(R.string.appGUID);
        String clientSecret = getResources().getString(R.string.pushClientSecret);
        String pushBackendURL = getResources().getString(R.string.pushBackUrl);
        // Initialize Push client
        // You can find your App Guid and Client Secret by navigating to the Configure section of your Push dashboard, click Mobile Options (Upper Right Hand Corner)
        // TODO: Please replace <APP_GUID> and <CLIENT_SECRET> with a valid App GUID and Client Secret from the Push dashboard Mobile Options
        push.initialize(this, appGuid, clientSecret);
        NotificationConnector.initialize(pushBackendURL, getApplicationContext());
        // Create notification listener and enable pop up notification when a message is received
        notificationListener = new MFPPushNotificationListener() {
            @Override
            public void onReceive(final MFPSimplePushNotification message) {
                Log.i(TAG, "Received a Push Notification: " + message.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Received a Push Notification")
                                .setMessage(message.getAlert())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                                .show();
                    }
                });
            }
        };
        this.registerDevice();
        this.createSchedulerForSubscribe();
    }

    public void handleChatBot(View view) {
        Intent chatbotIntent = new Intent(getApplicationContext(), ChatbotActivity.class);
        startActivity(chatbotIntent);
    }

    public void handleTemperature(View view) {
        Intent temperatureIntent = new Intent(getApplicationContext(), CheckTemperatureActivity.class);
        startActivity(temperatureIntent);
    }

    public void handlePriortizePatient(View view) {
        Intent medicalAssistanceIntent = new Intent(getApplicationContext(), MedicalAssistanceActivity.class);
        startActivity(medicalAssistanceIntent);
    }

    public void handleUserTracker(View view) {
        Intent userTrackerIntent = new Intent(getApplicationContext(), UserTrackerActivity.class);
        startActivity(userTrackerIntent);
    }

    public void registerDevice() {
        TextView responseText = (TextView) findViewById(R.id.response_text);
        responseText.setText(R.string.Registering);
        Log.i(TAG, "Registering for notifications");

        // Creates response listener to handle the response when a device is registered.
        MFPPushResponseListener registrationResponselistener = new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                // Split response and convert to JSON object to display User ID confirmation from the backend
                String[] resp = response.split("Text: ");
                try {
                    JSONObject responseJSON = new JSONObject(resp[1]);
                    setStatus("Device Registered Successfully with USER ID " + responseJSON.getString("userId"), true);
                    getSubscriptions();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Successfully registered for push notifications, " + response);
                // Start listening to notification listener now that registration has succeeded
                push.listen(notificationListener);
            }

            @Override
            public void onFailure(MFPPushException exception) {
                String errLog = "Error registering for push notifications: ";
                String errMessage = exception.getErrorMessage();
                int statusCode = exception.getStatusCode();

                // Set error log based on response code and error message
                if(statusCode == 401){
                    errLog += "Cannot authenticate successfully with Bluemix Push instance, ensure your CLIENT SECRET was set correctly.";
                } else if(statusCode == 404 && errMessage.contains("Push GCM Configuration")){
                    errLog += "Push GCM Configuration does not exist, ensure you have configured GCM Push credentials on your Bluemix Push dashboard correctly.";
                } else if(statusCode == 404 && errMessage.contains("PushApplication")){
                    errLog += "Cannot find Bluemix Push instance, ensure your APPLICATION ID was set correctly and your phone can successfully connect to the internet.";
                } else if(statusCode >= 500){
                    errLog += "Bluemix and/or your Push instance seem to be having problems, please try again later.";
                }

                setStatus(errLog, false);
                Log.e(TAG,errLog);
                // make push null since registration failed
                push = null;
            }
        };

        // Attempt to register device using response listener created above
        // Include unique sample user Id instead of Sample UserId in order to send targeted push notifications to specific users
        //TODO : Registartion with UserId
        deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.i(TAG, deviceId);
        push.registerDeviceWithUserId("Guest-" + deviceId , registrationResponselistener);
    }

    private void createSchedulerForSubscribe() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                final String tagBuilt = buildTagName("Guest-" + deviceId);
                if (tagBuilt != null && !tagBuilt.equals("")) {
                    tagFromService(tagBuilt, new Tags() {
                        @Override
                        public void isExisting(Boolean exists) {
                            if (exists != null) {
                                if (exists) {
                                    subscribeToTag(tagBuilt);
                                } else {
                                    // create new tag
                                    NotificationConnector.getInstance().createTagAndTrigger(tagBuilt, new NotificationConnector.OnCompletion() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            subscribeToTag(tagBuilt);
                                        }

                                        @Override
                                        public void onError(VolleyError error) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }},4000,1000 * 60 * 60 * 30);
    }


    //TODO: Call this method to get all the Tags

    /**
     * Call for getting the tags.
     */
    public void getTags() {

        push.getTags(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> tags) {
                setStatus("Retrieved Tags : " + tags, true);
            }

            @Override
            public void onFailure(MFPPushException ex) {
                setStatus("Error getting tags..." + ex.getMessage(), false);
            }
        });
    }

    //TODO: Call this method to get all the subscribed tags
    /**
     * Call for getting the subscribed tags.
     */
    public void getSubscriptions() {

        push.getSubscriptions(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> tags) {
                setStatus("Retrieved subscriptions : " + tags, true);
                subscribedTags = tags;
            }

            @Override
            public void onFailure(MFPPushException ex) {
                setStatus("Error getting subscriptions.. "
                        + ex.getMessage(), false);
            }
        });
    }

    //TODO: Call this method for subscribing to a tag
    /**
     * Call for subscribing to a tag.
     */
    public void subscribeToTag(final String tag) {

        push.getSubscriptions(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> response) {
                Log.d("subscrpitions", response.toString());
                boolean alreadySubscribed = false;
                for (String subscribedTag: response) {
                    if (!subscribedTag.equals("Push.ALL") && !subscribedTag.equals(tag)) {
                        unsubscribe(subscribedTag);
                    }
                    if (subscribedTag.equals(tag)) {
                        alreadySubscribed = true;
                    }
                }
                if (!alreadySubscribed) {
                    subscribe(tag);
                }
            }

            @Override
            public void onFailure(MFPPushException exception) {
                Log.d("exception ", "error");
            }
                });
    }

    private void subscribe(final String tag) {
        push.subscribe(tag, new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("Subscribed to", tag);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setStatus("Successfully subscribed to tag: " + tag, true);
                    }
                });
            }

            @Override
            public void onFailure(final MFPPushException exception) {
                Log.d("FAILED to subscribe", tag + " - " + exception.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setStatus("Failed to subscribe to " + tag + ". " + exception.toString(), false);
                    }
                });
            }
        });
    }

    private void unsubscribe(final String tag) {
        push.unsubscribe(tag, new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("Unsubscribed from", tag + " - " + response);
            }

            @Override
            public void onFailure(MFPPushException exception) {
                Log.d("FAILED to unsubscribe", tag + " - " + exception.toString());
            }
        });
    }

    //TODO: Call this
    // If the device has been registered previously, hold push notifications when the app is paused
    @Override
    protected void onPause() {
        super.onPause();

        if (push != null) {
            push.hold();
        }
    }

    // If the device has been registered previously, ensure the client sdk is still using the notification listener from onCreate when app is resumed
    @Override
    protected void onResume() {
        super.onResume();
        if (push != null) {
            push.listen(notificationListener);
        }
    }

    /**
     * Manipulates text fields in the UI based on initialization and registration events
     * @param messageText String main text view
     * @param wasSuccessful Boolean dictates top 2 text view texts
     */
    private void setStatus(final String messageText, boolean wasSuccessful){
        final TextView responseText = (TextView) findViewById(R.id.response_text);
        final TextView topText = (TextView) findViewById(R.id.top_text);
        final TextView bottomText = (TextView) findViewById(R.id.bottom_text);
        // final String topStatus = wasSuccessful ? "Yay!" : "Bummer";
        final String bottomStatus = wasSuccessful ? "You Are Connected" : "Something Went Wrong. Please check your internet connection";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(messageText);
                // topText.setText(topStatus);
                bottomText.setText(bottomStatus);
            }
        });
    }

    private String buildTagName(String deviceId) {
        return deviceId.toLowerCase().replace(" ", "-").concat("-any");
    }

    public void tagFromService(final String tag, final Tags tags) {
        push.getTags(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> response) {
                Log.d("Existing tags are", response.toString());
                tags.isExisting(response.contains(tag));
            }

            @Override
            public void onFailure(MFPPushException exception) {
                Log.d("Failed to get tags", exception.getErrorMessage());
                tags.isExisting(null);
            }
        });
    }

    interface Tags {
        void isExisting(Boolean exists);
    }
}
