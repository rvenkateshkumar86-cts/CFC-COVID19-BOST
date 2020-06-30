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
package com.ibm.iot.android.iotstarter.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.ibm.iot.android.iotstarter.IoTStarterApplication;
import com.ibm.iot.android.iotstarter.iot.IoTClient;
import org.eclipse.paho.client.mqttv3.MqttException;

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
    private final IoTStarterApplication app;
    private final SensorManager sensorManager;
    private final Sensor accelerometer ;
    private final Sensor temperatureSensor;
    private final Context context;
    private Timer timer;
    private long tripId;
    private boolean isEnabled = false;

    private DeviceSensor(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        app = (IoTStarterApplication) context.getApplicationContext();
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
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            if(temperatureSensor != null) {
                sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }else {
                Toast.makeText(context  ,"No ambient temperature Sensor!",Toast.LENGTH_SHORT);
            }
            tripId = System.currentTimeMillis()/1000;
            timer = new Timer();
            timer.scheduleAtFixedRate(new SendTimerTask(), 1000, 1000);
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

    // Values used for accelerometer, magnetometer, orientation sensor data
    private float[] G = new float[3]; // gravity x,y,z
    /*private float[] M = new float[3]; // geomagnetic field x,y,z*/
    private final float[] R = new float[9]; // rotation matrix
    private final float[] I = new float[9]; // inclination matrix
    private float[] O = new float[3]; // orientation azimuth, pitch, roll
    private float yaw;
    private float[] T;
    private float  temperature;

    /**
     * Callback for processing data from the registered sensors. Accelerometer and magnetometer
     * data are used together to get orientation data.
     *
     * @param sensorEvent The event containing the sensor data values.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v(TAG, "onSensorChanged() entered");
        /*if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.v(TAG, "Accelerometer -- x: " + sensorEvent.values[0] + " y: "
                    + sensorEvent.values[1] + " z: " + sensorEvent.values[2]);
            Log.v(TAG, "temperature -- " + Sensor.TYPE_AMBIENT_TEMPERATURE);
            G = sensorEvent.values;
            *//*temperature = Sensor.TYPE_AMBIENT_TEMPERATURE;*//*

        } */
        if (Sensor.TYPE_AMBIENT_TEMPERATURE > 0) {
            Log.v(TAG, "temperature -- " + Sensor.TYPE_AMBIENT_TEMPERATURE);
            T = sensorEvent.values;
            temperature = Sensor.TYPE_AMBIENT_TEMPERATURE;
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

            double lon = 0.0;
            double lat = 0.0;
            float heading = 0.0f;
            float speed = 0.0f;
            if (app.getCurrentLocation() != null) {
                lon = app.getCurrentLocation().getLongitude();
                lat = app.getCurrentLocation().getLatitude();
                heading = app.getCurrentLocation().getBearing();
                speed = app.getCurrentLocation().getSpeed() * 3.6f;
            }
            String messageData = MessageFactory.getAccelMessage(G,temperature,T, O, yaw, lon, lat, heading, speed, tripId);
            /*String messageData = MessageFactory.getAccelMessage(temperature);*/

            try {
                // create ActionListener to handle message published results
                MyIoTActionListener listener = new MyIoTActionListener(context, Constants.ActionStateStatus.PUBLISH);
                IoTClient iotClient = IoTClient.getInstance(context);
                if (app.getConnectionType() == Constants.ConnectionType.QUICKSTART) {
                    iotClient.publishEvent(Constants.STATUS_EVENT, "json", messageData, 0, false, listener);
                } else {
                    iotClient.publishEvent(Constants.ACCEL_EVENT, "json", messageData, 0, false, listener);
                }

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
            }

            app.setAccelData(G);
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
