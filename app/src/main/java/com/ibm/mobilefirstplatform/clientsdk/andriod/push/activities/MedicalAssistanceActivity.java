package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;

import android.app.Activity;

import com.google.android.gms.security.ProviderInstaller;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.AutoAIResponse;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.PatientDTO;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.AutoAIConnector;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class MedicalAssistanceActivity extends Activity {
    private AutoAIConnector connector = AutoAIConnector.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_assistance);
        Button submitButton=(Button) findViewById(R.id.submitButton);

        SSLContext sslContext;
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                submitButton.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        EditText gender = (EditText) findViewById(R.id.gender);
                        EditText age = (EditText) findViewById(R.id.age);
                        EditText temp = (EditText) findViewById(R.id.temp);
                        EditText respRate = (EditText) findViewById(R.id.respRate);
                        EditText pulseRate = (EditText) findViewById(R.id.pulseRate);
                        EditText oxySaturation = (EditText) findViewById(R.id.oxySaturation);
                        TextView resultTextView =(TextView) findViewById(R.id.result);
                        resultTextView.setText("");
                        PatientDTO patientDto = new PatientDTO(gender.getText().toString(), Integer.parseInt(age.getText().toString()), Integer.parseInt(temp.getText().toString()), Integer.parseInt(respRate.getText().toString()), Integer.parseInt(pulseRate.getText().toString()), Integer.parseInt(oxySaturation.getText().toString()));
                        AutoAIResponse response = connector.submitData(patientDto);
                        if (null != response) {
                            String result = String.valueOf(response.getPredictions().get(0).getValues().get(0));
                            resultTextView.setText(result != null ? result.toLowerCase().contains("yes") ? "Patient is critical" : "Patient is not critical" : "");
                        }
                    }});

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
