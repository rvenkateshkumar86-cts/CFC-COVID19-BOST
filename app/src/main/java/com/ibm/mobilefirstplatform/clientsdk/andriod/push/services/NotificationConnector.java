package com.ibm.mobilefirstplatform.clientsdk.andriod.push.services;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.IAMToken;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.NewsResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public final class NotificationConnector {
    private final String backendUrl;
    private final String functionDiscoveryUrl;
    private final String clientSecret;
    private static NotificationConnector instance;
    private final ObjectMapper mapper = new ObjectMapper();


    private NotificationConnector(String backendUrl, String functionDiscoveryUrl, String clientSecret, Context context) {
        this.backendUrl = backendUrl;
        this.functionDiscoveryUrl = functionDiscoveryUrl;
        this.clientSecret = clientSecret;
    }

    public static void initialize(String backendUrl, String functionDiscoveryUrl, String clientSecret, Context context) {
        instance = new NotificationConnector(backendUrl, functionDiscoveryUrl, clientSecret, context);
    }

    public static NotificationConnector getInstance() {
        return instance;
    }

    public String getLatestNews() throws IOException, JSONException {
        HttpURLConnection scoringConnection = null;
        BufferedReader scoringBuffer = null;
        StringBuffer jsonStringScoring = new StringBuffer();
        URL scoringUrl = new URL(functionDiscoveryUrl);
        scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
        scoringConnection.setDoInput(true);
        scoringConnection.setDoOutput(true);
        scoringConnection.setRequestMethod("GET");
        scoringConnection.setRequestProperty("Accept", "application/json");
        scoringConnection.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");

        scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
        String lineScoring;
        while ((lineScoring = scoringBuffer.readLine()) != null) {
            jsonStringScoring.append(lineScoring);
        }
        NewsResponse response = mapper.readValue(jsonStringScoring.toString(), NewsResponse.class);
        return response.getResult();
    }

    public void sendNotificationToALL(String text) throws IOException, JSONException {
        HttpURLConnection scoringConnection = null;
        BufferedReader scoringBuffer = null;
        StringBuffer jsonStringScoring = new StringBuffer();
        URL scoringUrl = new URL(backendUrl);
        scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
        scoringConnection.setDoInput(true);
        scoringConnection.setDoOutput(true);
        scoringConnection.setRequestMethod("POST");
        scoringConnection.setRequestProperty("Accept", "application/json");
        scoringConnection.setRequestProperty("Content-Type", "application/json");
        scoringConnection.setRequestProperty("clientSecret", clientSecret);
        JSONObject data = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("alert", text);
        data.put("message", message);
        OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");
        String payload = data.toString();
        writer.write(payload);
        writer.close();

        scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
        String lineScoring;
        while ((lineScoring = scoringBuffer.readLine()) != null) {
            jsonStringScoring.append(lineScoring);
        }
    }

    public void sendNotificationToAdmin(String text, List<String> deviceIDs) throws IOException, JSONException {
        HttpURLConnection scoringConnection = null;
        BufferedReader scoringBuffer = null;
        StringBuffer jsonStringScoring = new StringBuffer();
        URL scoringUrl = new URL(backendUrl);
        scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
        scoringConnection.setDoInput(true);
        scoringConnection.setDoOutput(true);
        scoringConnection.setRequestMethod("POST");
        scoringConnection.setRequestProperty("Accept", "application/json");
        scoringConnection.setRequestProperty("Content-Type", "application/json");
        scoringConnection.setRequestProperty("clientSecret", clientSecret);
        JSONObject data = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("alert", text);
        data.put("message", message);
        JSONObject target = new JSONObject();
        JSONArray deviceArrays = new JSONArray();
        for (String deviceId: deviceIDs) {
            deviceArrays.put(deviceId);
        }
        target.put("deviceIds", deviceArrays);
        data.put("target", target);
        OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");
        String payload = data.toString();
        writer.write(payload);
        writer.close();

        scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
        String lineScoring;
        while ((lineScoring = scoringBuffer.readLine()) != null) {
            jsonStringScoring.append(lineScoring);
        }
    }
}
