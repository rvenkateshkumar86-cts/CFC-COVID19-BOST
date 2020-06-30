package com.ibm.mobilefirstplatform.clientsdk.andriod.push.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.IAMToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenConnector {

    private static final String  IAMTOKENURL = "https://iam.cloud.ibm.com/identity/token";

    private final ObjectMapper mapper = new ObjectMapper();

    private static TokenConnector instance;

    private TokenConnector() {
    }

    public static TokenConnector getInstance() {
        if (instance == null) {
            instance = new TokenConnector();
        }
        return instance;
    }

    public IAMToken getAccessToken(String IAM_API_KEY) throws IOException, JSONException {
        HttpURLConnection scoringConnection = null;
        BufferedReader scoringBuffer = null;
        StringBuffer jsonStringScoring = new StringBuffer();
        URL scoringUrl = new URL(IAMTOKENURL);
        scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
        scoringConnection.setDoInput(true);
        scoringConnection.setDoOutput(true);
        scoringConnection.setRequestMethod("POST");
        scoringConnection.setRequestProperty("Accept", "application/json");
        scoringConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");
        String payload = "grant_type=urn:ibm:params:oauth:grant-type:apikey&apikey=" + IAM_API_KEY;
        writer.write(payload);
        writer.close();

        scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
        String lineScoring;
        while ((lineScoring = scoringBuffer.readLine()) != null) {
            jsonStringScoring.append(lineScoring);
        }
        IAMToken token = mapper.readValue(jsonStringScoring.toString(), IAMToken.class);
        return token;
    }
}
