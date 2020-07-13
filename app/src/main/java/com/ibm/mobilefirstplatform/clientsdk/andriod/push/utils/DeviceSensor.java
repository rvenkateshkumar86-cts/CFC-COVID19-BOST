/*******************************************************************************
 * Copyright (c) 2014-2016 IBM Corp.
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
 *    Aldo Eisma - add bearing and speed to acceleration message
 *******************************************************************************/
package com.ibm.mobilefirstplatform.clientsdk.andriod.push.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.andriod.push.BOSTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.iot.IoTClient;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.NotificationConnector;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements the SensorEventListener interface. When the application creates the MQTT
 * connection, it registers listeners for the accelerometer and magnetometer sensors.
 * Output from these sensors is used to publish accel event messages.
 */
public class DeviceSensor implements SensorEventListener {
    private final String TAG = DeviceSensor.class.getName();
    private static DeviceSensor instance;
    private final BOSTStarterApplication app;
    private final SensorManager sensorManager;
    private Sensor temperatureSensor = null;
    private final Context context;
    private Timer timer;
    private boolean isEnabled = false;

    private DeviceSensor(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        app = (BOSTStarterApplication) context.getApplicationContext();
        temperatureSensor= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
    }

    /**
     * @param context The application context for the object.
     * @return The MqttHandler object for the application.
     */
    public static DeviceSensor getInstance(Context context) {
        if (instance == null) {
            Log.i(DeviceSensor.class.getName(), "Creating new DeviceSensor");
            instance = new DeviceSensor(context);
        }
        return instance;
    }

    /**
     * Register the listeners for the sensors the application is interested in.
     */
    public void enableSensor() {
        Log.i(TAG, ".enableSensor() entered");
        if (!isEnabled) {
            if(temperatureSensor != null) {
                sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }else {
                Toast.makeText(context,"Sensor temperature not supported for your device", Toast.LENGTH_LONG).show();
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new SendTimerTask(), 1000, 1000 * 5);
            isEnabled = true;
        }
    }

    /**
     * Disable the listeners.
     */
    public void disableSensor() {
        Log.d(TAG, ".disableSensor() entered");
        if (timer != null && isEnabled) {
            timer.cancel();
            sensorManager.unregisterListener(this);
            isEnabled = false;
        }
    }

    private String  temperature;

    /**
     * Callback for processing data from the registered sensors. Accelerometer and magnetometer
     * data are used together to get orientation data.
     *
     * @param sensorEvent The event containing the sensor data values.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v(TAG, "onSensorChanged() entered");
        if (Sensor.TYPE_AMBIENT_TEMPERATURE > 0) {
            Log.v(TAG, "temperature -- " + Sensor.TYPE_AMBIENT_TEMPERATURE);
            float ambient_temperature = sensorEvent.values[0];
            double temp = (ambient_temperature * 1.8 ) + 32;
            temperature = String.valueOf(temp);
        }
    }

    /**
     * Callback for the SensorEventListener interface. Unused.
     *
     * @param sensor The sensor that changed.
     * @param i The change in accuracy for the sensor.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged() entered");
    }

    /**
     * Timer task for sending accel data on 1000ms intervals
     */
    private class SendTimerTask extends TimerTask {

        /**
         * Publish an accel event message.
         */
        @Override
        public void run() {
            Log.v(TAG, "SendTimerTask.run() entered");
            if (temperatureSensor == null) {
              /* temperature = app.getRandomAmbientTemperature();*/
                float ambient_temperature =Float.parseFloat(app.getRandomAmbientTemperature());
                DecimalFormat df = new DecimalFormat("#.##");
                double temp = (ambient_temperature * 1.8 ) + 32;

                temperature = String.valueOf( df.format(temp));
            }
            float temp = Float.parseFloat(temperature);

            List<String> publicServiceDeviceIds = new ArrayList<String>();
            publicServiceDeviceIds.add("64c08e7671db4996");

            if (temp>98) {
                String messageData = MessageFactory.getAccelMessage(temperature);

                try {
                    // create ActionListener to handle message published results
                    MyIoTActionListener listener = new MyIoTActionListener(context, Constants.ActionStateStatus.PUBLISH);
                    IoTClient iotClient = IoTClient.getInstance(context);
                    if (app.getConnectionType() == Constants.ConnectionType.QUICKSTART) {
                        iotClient.publishEvent(Constants.STATUS_EVENT, "json", messageData, 0, false, listener);
                    } else {
                        iotClient.publishEvent(Constants.ACCEL_EVENT, "json", messageData, 0, false, listener);
                    }

                    String deviceId = app.getAndriodDeviceId();
                    String phoneNumber = app.getPhoneNumber();

                    NotificationConnector.getInstance().sendNotificationToAdmin("We recently monitor a user whose temperature is more \n" +
                            " than 98 degree.Phone number: " + phoneNumber + "  deviceId:" + deviceId,publicServiceDeviceIds) ;

                    app.setAndriodDeviceId(deviceId);

                    int count = app.getPublishCount();
                    app.setPublishCount(++count);

                    //String runningActivity = app.getCurrentRunningActivity();
                    //if (runningActivity != null && runningActivity.equals(IoTPagerFragment.class.getName())) {
                    Intent actionIntent = new Intent(Constants.APP_ID + Constants.INTENT_IOT);
                    actionIntent.putExtra(Constants.INTENT_DATA, Constants.INTENT_DATA_PUBLISHED);
                    context.sendBroadcast(actionIntent);
                    //}
                } catch (MqttException e) {
                    Log.d(TAG, ".run() received exception on publishEvent()");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            app.setAccelDataTemp(temperature);

            //String runningActivity = app.getCurrentRunningActivity();
            //if (runningActivity != null && runningActivity.equals(IoTPagerFragment.class.getName())) {
                Intent actionIntent = new Intent(Constants.APP_ID + Constants.INTENT_IOT);
                actionIntent.putExtra(Constants.INTENT_DATA, Constants.ACCEL_EVENT);
                context.sendBroadcast(actionIntent);
            //}
        }
    }
}
