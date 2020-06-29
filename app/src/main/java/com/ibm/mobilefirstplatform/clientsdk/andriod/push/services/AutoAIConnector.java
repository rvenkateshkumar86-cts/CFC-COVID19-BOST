package com.ibm.mobilefirstplatform.clientsdk.andriod.push.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.AutoAIResponse;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.IAMToken;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.PatientDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public final class AutoAIConnector {

    private static final String  IAMTOKENURL = "https://iam.cloud.ibm.com/identity/token";

    private static final String IAM_API_KEY = "xsXOUE2hmyU3Iqun9QF53e2b_s4jV_Ylgf-76s973dGb";

    private static final String ml_instance_id = "9f3dac87-25d7-4cb7-aeee-11e9265d345c";

    private static final String predictionURL = "https://us-south.ml.cloud.ibm.com/v4/deployments/bffea2cc-1e84-4c3f-b313-3f4e7b86eb26/predictions";

    private final ObjectMapper mapper = new ObjectMapper();

    private static AutoAIConnector instance;

    private AutoAIConnector() {
    }

    public static AutoAIConnector getInstance() {
        if (instance == null) {
            instance = new AutoAIConnector();
        }
        return instance;
    }

    public AutoAIResponse submitData(PatientDTO patient) {
        HttpURLConnection scoringConnection = null;
        BufferedReader scoringBuffer = null;
        AutoAIResponse response = null;
        StringBuffer jsonStringScoring = new StringBuffer();
        try {
            // Scoring request
            IAMToken token = getAccessToken();
            URL scoringUrl = new URL(predictionURL);
            scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
            scoringConnection.setDoInput(true);
            scoringConnection.setDoOutput(true);
            scoringConnection.setRequestMethod("POST");
            scoringConnection.setRequestProperty("Accept", "application/json");
            scoringConnection.setRequestProperty("Authorization", token.getToken_type() + " " + token.getAccess_token());
            scoringConnection.setRequestProperty("ML-Instance-ID", ml_instance_id);
            scoringConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");

            // NOTE: manually define and pass the array(s) of values to be scored in the next line
            String payload = "{\"input_data\":[{\"fields\":[\"age\",\"job\",\"marital\",\"education\",\"default\",\"balance\",\"housing\",\"loan\",\"contact\",\"day\",\"month\",\"duration\",\"campaign\",\"pdays\",\"previous\",\"poutcome\"],\"values\":[[" + patient.getAge() + ",\"" + patient.getJob() + "\",\"" + patient.getMarital() + "\"," + patient.getEducation() + "," + "null,"+ patient.getBalance() +"," + patient.getHousing() + "," + patient.getLoan() + "," + patient.getContact() + "," + patient.getDay() + "," + patient.getMonth() + "," + patient.getDuration() + "," + patient.getCampaign() + "," + patient.getPdays() + "," + patient.getPrevious() + "," + patient.getPoutcome() + "]]}]}";
            writer.write(payload);
            writer.close();
            String pay = "\"" + patient.getCampaign() + "\"";

            scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
            String lineScoring;
            while ((lineScoring = scoringBuffer.readLine()) != null) {
                jsonStringScoring.append(lineScoring);
            }
            response = mapper.readValue(jsonStringScoring.toString(), AutoAIResponse.class);

        } catch (IOException | JSONException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (scoringConnection != null) {
                scoringConnection.disconnect();
            }
            if (scoringBuffer != null) {
                try {
                    scoringBuffer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return response;
    }

    public IAMToken getAccessToken() throws IOException, JSONException {
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
