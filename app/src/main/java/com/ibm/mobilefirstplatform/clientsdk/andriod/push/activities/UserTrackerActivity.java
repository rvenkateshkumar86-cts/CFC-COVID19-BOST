package com.ibm.mobilefirstplatform.clientsdk.andriod.push.activities;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.BOSTStarterApplication;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.NotificationConnector;
import com.ibm.mobilefirstplatform.clientsdk.android.push.R;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserTrackerActivity extends FragmentActivity implements OnMapReadyCallback {


    LocationManager locationManager;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Double[] pastLatitude = {0.0};
        final Double[] pastLongitude = {0.0};
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Location");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getValue() != null) {
                        if(ds.getKey().equalsIgnoreCase("PastLatitude")) {
                            pastLatitude[0] = (Double) ds.getValue();
                        }
                        if(ds.getKey().equalsIgnoreCase("PastLongitude")) {
                            pastLongitude[0] = (Double) ds.getValue();
                        }
                    }
                }
                Log.d("Data From FireBase", "latitude and longitude:" + pastLatitude[0] +  pastLongitude[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final List<String> publicServiceDeviceIds = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("PublicServiceDevice").child("DeviceId");
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for (DataSnapshot child : children) {
                        Object value = child.getValue();
                        publicServiceDeviceIds.add(String.valueOf(value));
                    }
                    Log.d("Data From FireBase", "publicServiceDeviceIds:" + publicServiceDeviceIds);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //To get last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("permission fine", String.valueOf(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));
        Log.d("permission coarse", String.valueOf(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        fusedLocationClient.getLastLocation() .addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    //latitude
                    double latitude = location.getLatitude();
                    //longitude
                    double longitude = location.getLongitude();
                    Log.d("fusedLocationClient","latitude and longitude:" + latitude +"and" +longitude);

                    BOSTStarterApplication app = (BOSTStarterApplication) getApplication();
                    String deviceid =  app.getAndriodDeviceId();
                    /*ref = database.getReference("PublicServiceDevice");
                    ref.keepSynced(true);
                    ref.child("DeviceId").push().setValue(deviceid);*/

                    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        LatLng latLng = new LatLng(latitude,longitude);
                        String currentLocation = "";
                        List<Address> addressList =  myLocation.getFromLocation(latitude,longitude,1);
                        String result
                                = addressList.get(0).getLocality() + ":";
                        result += addressList.get(0).getCountryName();
                        currentLocation = addressList.get(0).getLocality();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String pastCity = "";
                    double dist = 0.0;
                    if((pastLatitude[0] != 0 && pastLatitude[0] != latitude) && (pastLongitude[0] != 0 && pastLongitude[0] != longitude)) {
                        try {
                            Geocoder geo = new Geocoder(UserTrackerActivity.this.getApplicationContext(), Locale.getDefault());

                            //To get past location in city
                            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                            if (addresses.isEmpty()) {
                                Log.d("Past", "Location not found");
                            }
                            else {
                                if (addresses.size() > 0) {
                                    pastCity =  addresses.get(0).getLocality();
                                    // yourtextfieldname.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                                }
                            }

                            //code to get distance between past and current location
                            double theta = pastLongitude[0] - longitude;
                            dist = Math.sin(Math.toRadians(pastLatitude[0])) * Math.sin(Math.toRadians(latitude)) + Math.cos(Math.toRadians(pastLatitude[0])) * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(theta));
                            dist = Math.acos(dist);
                            dist = Math.toDegrees(dist);
                            dist = dist * 60 * 1.1515;
                            dist = dist * 1.609344;
                        }
                        catch (Exception e) {
                            e.printStackTrace(); // getFromLocation() may sometimes fail
                        }

                        ref = database.getReference("Location");
                        ref.keepSynced(true);
                        ref.child("PastLatitude").setValue(latitude);
                        ref.child("PastLongitude").setValue(longitude);


                        String phoneNumber = app.getPhoneNumber();

                        try {
                            Log.d("Moved", "You're Moved");
                            if(dist >= 50) {
                                NotificationConnector.getInstance().sendNotificationToALL("We recently monitor you that you have travel from current location to far more than 50 km. \n" +
                                        "We inform you that you will be monitor after every 1 km from here if you already have EPASS, Please ignore it");
                                NotificationConnector.getInstance().sendNotificationToAdmin( "We recently monitor the below device id : " + app.getAndriodDeviceId() +" has been travelled more than 50 km from his/her current location.Phone number: " + app.getPhoneNumber(),
                                        publicServiceDeviceIds );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        // super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        if(isGPSEnabled(getApplicationContext())) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7200000, 30, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    String currentLocation = "";
                    //latitude
                    final double latitude = location.getLatitude();
                    //longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addressList =  myLocation.getFromLocation(latitude,longitude,1);
                        String result
                                = addressList.get(0).getLocality() + ":";
                        result += addressList.get(0).getCountryName();
                        currentLocation = addressList.get(0).getLocality();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String pastCity = "";
                    double dist = 0.0;
                    if((pastLatitude[0] != 0 && pastLatitude[0] != latitude) && (pastLongitude[0] != 0 && pastLongitude[0] != longitude)) {
                        try {
                            Geocoder geo = new Geocoder(UserTrackerActivity.this.getApplicationContext(), Locale.getDefault());

                            //To get past location in city
                            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                            if (addresses.isEmpty()) {
                                Log.d("Past", "Location not found");
                            }
                            else {
                                if (addresses.size() > 0) {
                                    pastCity =  addresses.get(0).getLocality();
                                    // yourtextfieldname.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                                }
                            }

                            //code to get distance between past and current location
                            double theta = pastLongitude[0] - longitude;
                            dist = Math.sin(Math.toRadians(pastLatitude[0])) * Math.sin(Math.toRadians(latitude)) + Math.cos(Math.toRadians(pastLatitude[0])) * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(theta));
                            dist = Math.acos(dist);
                            dist = Math.toDegrees(dist);
                            dist = dist * 60 * 1.1515;
                            dist = dist * 1.609344;
                        }
                        catch (Exception e) {
                            e.printStackTrace(); // getFromLocation() may sometimes fail
                        }

                        ref = database.getReference("Location");
                        ref.keepSynced(true);
                        ref.child("PastLatitude").setValue(latitude);
                        ref.child("PastLongitude").setValue(longitude);

                        BOSTStarterApplication app = (BOSTStarterApplication) getApplication();
                        final String deviceid =  app.getAndriodDeviceId();
                        String phoneNumber = app.getPhoneNumber();

                        try {
                            Log.d("Moved", "You're Moved");
                            if(dist >= 50) {
                                NotificationConnector.getInstance().sendNotificationToALL("We recently monitor you that you have travel from current location to far more than 50 km. \n" +
                                        "We inform you that you will be monitor after every 1 km from here if you already have EPASS, Please ignore it");
                                NotificationConnector.getInstance().sendNotificationToAdmin( "We recently monitor the below device id : " + app.getAndriodDeviceId() +" has been travelled more than 50 km from his/her current location.Phone number: " + app.getPhoneNumber(),
                                        publicServiceDeviceIds );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(latitude != 0 && longitude !=0) {
                        Log.d("InsideGPS", "latitude and longitude:" + latitude + " and " + longitude);
                    }
                }
            });
        } else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //latitude
                    double latitude = location.getLatitude();
                    //longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList =  geocoder.getFromLocation(latitude,1,1);
                        String str = addressList.get(0).getPostalCode();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }
            });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(11.0168, 76.9558);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Tamil Nadu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.2f));*/

    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
