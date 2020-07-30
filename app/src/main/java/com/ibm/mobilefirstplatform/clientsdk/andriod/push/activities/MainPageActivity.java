package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.BOSTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.UserType;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.NotificationConnector;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.content.ContentValues.TAG;

public class MainPageActivity extends Activity {


    EditText phnumber;
    Button loginButton;
    public static final String Phone = "phoneKey-";

    private SharedPreferences sharedpreferences;
    private BOSTStarterApplication app;
    private Spinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String deviceId;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    /*String mPhoneNumber;*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        app = (BOSTStarterApplication) getApplicationContext();
        sharedpreferences = app.getSettings();
        spinner =  (Spinner)findViewById(R.id.selectUser);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainPageActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.userCategory));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        phnumber =  findViewById(R.id.phoneNumber);
        loginButton =(Button)findViewById(R.id.login);

        database = FirebaseDatabase.getInstance();
        String appGuid = getResources().getString(R.string.appGUID);
        String clientSecret = getResources().getString(R.string.pushClientSecret);
        String pushBackendURL = getResources().getString(R.string.pushBackUrl);
        String functionDiscoveryURL = getResources().getString(R.string.discoveryFunctionUrl);
        NotificationConnector.initialize(pushBackendURL, functionDiscoveryURL, clientSecret, getApplicationContext());
        awesomeValidation.addValidation(this,R.id.phoneNumber, "^[+]?[0-9]{10,13}$", R.string.phoneerror);
        deviceId = app.getAndriodDeviceId();
        String registeredPhNumber = sharedpreferences.getString(deviceId, "");
        if(null != registeredPhNumber && !registeredPhNumber.isEmpty()) {
            goToMainActivity();
        } else {

            if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                phnumber.setText(mPhoneNumber);
                Log.i(TAG, "Ph.No"+mPhoneNumber);
                return;
            } else {
                requestPermission();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE},
                    100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                phnumber.setText(mPhoneNumber);
                Log.i(TAG, "Ph.No"+mPhoneNumber);
                break;
        }
    }

    public void handleHome(View view) {
        if (awesomeValidation.validate()) {
            String selectedUser = (String) spinner.getSelectedItem();
            String phoneNumber = phnumber.getText().toString();
            app.setPhoneNumber(phoneNumber);
            if (null != selectedUser && !selectedUser.isEmpty()) {
                app.setUserType(UserType.getUserType(selectedUser));
            }
            UserType userType = app.getUserType();
            if(userType.equals(UserType.OFFICER)){
                ref = database.getReference("PublicServiceDevice");
                ref.keepSynced(true);
                ref.child("DeviceId").push().setValue(deviceId);
            }
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(deviceId, phoneNumber);
            editor.commit();
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(),CheckTemperatureActivity.class);
        startActivity(intent);
    }
}






