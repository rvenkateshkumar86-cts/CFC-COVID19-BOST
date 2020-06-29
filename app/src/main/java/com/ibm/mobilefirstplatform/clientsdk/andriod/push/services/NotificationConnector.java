package com.ibm.mobilefirstplatform.clientsdk.andriod.push.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

public final class NotificationConnector {
    private final String backendUrl;
    private RequestQueue queue;
    private TimeZone timeZone;
    private static NotificationConnector instance;


    private NotificationConnector(String backendUrl, Context context) {
        this.backendUrl = backendUrl;
        this.queue = Volley.newRequestQueue(context);
        this.timeZone = Calendar.getInstance().getTimeZone();
    }

    public static void initialize(String backendUrl, Context context) {
        instance = new NotificationConnector(backendUrl, context);
    }

    public static NotificationConnector getInstance() {
        return instance;
    }

    public void createTagAndTrigger(String tag, final OnCompletion onCompletion) {
        JSONObject params = new JSONObject();
        try {
            params.put("tag", tag);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, backendUrl, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    onCompletion.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onCompletion.onError(error);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            onCompletion.onError(null);
        }
    }

    private String createCronTab(int seekBarValue) {
        return "0 " + String.valueOf(17 + seekBarValue) + " * * *";
    }

    public interface OnCompletion {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }
}
