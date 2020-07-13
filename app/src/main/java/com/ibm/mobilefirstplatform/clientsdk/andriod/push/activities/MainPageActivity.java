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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.ibm.mobilefirstplatform.clientsdk.android.push.R;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.content.ContentValues.TAG;

public class MainPageActivity extends Activity implements View.OnClickListener{


    TextView phnumber;
    Button loginButton;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";

    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    public void setSharedpreferences(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
    }

    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    /*String mPhoneNumber;*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Log.d("created","hi");
        Spinner spinner =  (Spinner)findViewById(R.id.selectUser);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainPageActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.userCategory));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        phnumber =  findViewById(R.id.phoneNumber);
        loginButton =(Button)findViewById(R.id.login);

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
        String phoneNumber  = phnumber.getText().toString();
      //  loginButton.setOnClickListener(this);
        sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       editor = sharedpreferences.edit();
        editor.putString(Phone, phoneNumber);
      editor.commit();

        /*sharedpreferences = PreferenceManager
              .getDefaultSharedPreferences(this);*/
       String registeredPhNumber =sharedpreferences.getString(Phone,phoneNumber);
       if(registeredPhNumber.equals(phnumber)) {

           Intent intent = new Intent(MainPageActivity.this,MainActivity.class);
           startActivity(intent);
        }

      /* Log.i(TAG, "Ph.No"+bostStarterApplication.getPhnumber());*/
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


    @Override
    public void onClick(View view) {
       String phoneNumber  = phnumber.getText().toString();
       editor.putString(Phone, phoneNumber);
        editor.commit();
        Intent intent = new Intent(MainPageActivity.this,MainActivity.class);
        startActivity(intent);
    }
    }






