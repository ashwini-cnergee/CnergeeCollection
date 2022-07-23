package com.broadbandcollection.billing.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.database.DatabaseHandler;
import com.broadbandcollection.billing.obj.Constants;
import com.broadbandcollection.billing.obj.LocationRecord;
import com.broadbandcollection.billing.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * Created by jyoti on 18-12-2018.
 */

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();


    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    ArrayList<LocationRecord> al_location = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by LocationRecord Services if the connection to the
     * location client drops because of an error.
     */
    
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }


    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "LocationRecord changed");

        if (location != null) {
            Log.d(TAG, "== location != null");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Log.e("latitude",":"+location.getLatitude()  +  "  "+formattedDate);
            //Log.e("longitude",":"+location.getLongitude());

            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

            getLocationRecord(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
    }


    public void getLocationRecord(String latitude, String longitude){

        DatabaseHandler db1= new DatabaseHandler(getBaseContext());
        Cursor mCur=db1.getLocation();
        if(mCur.getCount()>0) {
            while (mCur.moveToNext()) {
                //Log.e("DATABASE LAT",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE)));
                //Log.e("DATABASE LONG",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE)));
            }
        }

        String new_latitude = null,new_longitude =null;
        DatabaseHandler db2= new DatabaseHandler(getBaseContext());
        Cursor mCur1=db2.getLastRecord();

        if(mCur1.getCount()>0) {
            while (mCur1.moveToNext()) {

                Log.e(" last lat",":"+mCur1.getString(mCur1.getColumnIndex(DatabaseHandler.LATITUDE)));
                Log.e(" last long",":"+mCur1.getString(mCur1.getColumnIndex(DatabaseHandler.LONGITUDE)));

                new_latitude =  mCur1.getString(mCur1.getColumnIndex(DatabaseHandler.LATITUDE));
                new_latitude = new_latitude.substring(0, new_latitude.length() - 2);
                //Log.e("new_latitude" , ":"+new_latitude);

                new_longitude =  mCur1.getString(mCur1.getColumnIndex(DatabaseHandler.LONGITUDE));
                new_longitude = new_longitude.substring(0, new_longitude.length() - 2);
               // Log.e("new_longitude" , ":"+new_longitude);

                if(new_latitude.equalsIgnoreCase(latitude) && new_longitude.equalsIgnoreCase(longitude)){
                    insert_record(latitude,longitude);
                }
            }
        }else{
            insert_record(latitude,longitude);
        }

    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }


    public void insert_record(String latitude, String longitude){
        String provider;
        String str_gps_status="";
        LocationManager lm =(LocationManager) getSystemService(LOCATION_SERVICE);
        Utils utils = new Utils();
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("CNERGEE", 0);
        utils.setSharedPreferences(sharedPreferences);

       // Log.e("MobLoginId",":"+utils.getMobLoginId());

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            str_gps_status="Yes";
        else
            str_gps_status="No";


        Calendar cal=Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        String formattedDate=dateFormatter.format(cal.getTime());


        DatabaseHandler db= new DatabaseHandler(getBaseContext());

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }
        else{
            provider=LocationManager.NETWORK_PROVIDER;
        }

       //db.insertNewLocation(String.valueOf(latitude), String.valueOf(longitude), utils.getMobLoginId(), formattedDate, provider, str_gps_status,"roaming");
        db.insertNewLocation(String.valueOf("19.0148802"), String.valueOf("73.0418355"), "test", "", "network", "0","roaming");
        db.insertNewLocation(String.valueOf("19.0148787"), String.valueOf("73.0418305"), "test", "", "network", "0","roaming");
        db.insertNewLocation(String.valueOf("19.0148787"), String.valueOf("73.0418305"), "test", "", "network", "0","roaming");


    }
}