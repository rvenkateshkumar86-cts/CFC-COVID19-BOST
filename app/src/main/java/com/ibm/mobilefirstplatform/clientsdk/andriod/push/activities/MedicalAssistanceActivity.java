package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;

import android.app.Activity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.security.ProviderInstaller;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.AutoAIResponse;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.PatientDTO;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.AutoAIConnector;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class MedicalAssistanceActivity extends Activity {
    private AutoAIConnector connector = AutoAIConnector.getInstance();
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_assistance);
        Button submitButton=(Button) findViewById(R.id.submitButton);
        final EditText gender = (EditText) findViewById(R.id.gender);
        final EditText age = (EditText) findViewById(R.id.age);
        final EditText temp = (EditText) findViewById(R.id.temp);
        final EditText respRate = (EditText) findViewById(R.id.respRate);
        final EditText pulseRate = (EditText) findViewById(R.id.pulseRate);
        final EditText oxySaturation = (EditText) findViewById(R.id.oxySaturation);
        final TextView resultTextView =(TextView) findViewById(R.id.result);
        resultTextView.setText("");
        awesomeValidation.addValidation(this,R.id.gender, "[FM]$", R.string.gendererror);
        awesomeValidation.addValidation(this,R.id.age, "^0*(?:[1-9][0-9]?|100)$", R.string.ageerror);
        awesomeValidation.addValidation(this,R.id.temp, "^0*(?:[1-9][0-9]?|100)$", R.string.temperror);
        awesomeValidation.addValidation(this,R.id.respRate, "^0*(?:[1-9][0-9]?|100)$", R.string.resprsteerror);
        awesomeValidation.addValidation(this,R.id.pulseRate, "^0*(?:[1-9][1-9][0-9]?|100)$", R.string.pulseRateerror);
        awesomeValidation.addValidation(this,R.id.oxySaturation, "^0*(?:[1-9][0-9]?|100)$", R.string.oxySaterror);


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
                        if(awesomeValidation.validate()){
                        PatientDTO patientDto = new PatientDTO(gender.getText().toString(), Integer.parseInt(age.getText().toString()), Integer.parseInt(temp.getText().toString()), Integer.parseInt(respRate.getText().toString()), Integer.parseInt(pulseRate.getText().toString()), Integer.parseInt(oxySaturation.getText().toString()));
                        AutoAIResponse response = connector.submitData(patientDto);
                        if (null != response) {
                            String result = String.valueOf(response.getPredictions().get(0).getValues().get(0));
                            resultTextView.setText(result != null ? result.toLowerCase().contains("yes") ? "Chance of Affected" : "You are safe" : "");
                        }
                    }
                   }});

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
