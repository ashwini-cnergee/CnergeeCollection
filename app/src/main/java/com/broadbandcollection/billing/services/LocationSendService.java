package com.broadbandcollection.billing.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.SOAP.SendBulkLocationCaller;

import com.broadbandcollection.billing.database.DatabaseHandler;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jyoti on 12/18/2018.
 */

public class LocationSendService extends Service {

    Utils utils= new Utils();
    String AppVersion="",activity;
    public static String rslt="";
    String new_latitude,new_longitude,member_id,date,provider,is_gps;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // sendLocatonRecord();
        AppVersion= Utils.GetAppVersion(this);
        //activity = intent.getStringExtra("activity");
        new SendLocationAsyncTask().execute();
        return START_STICKY;
    }

    public class SendLocationAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences("CNERGEE", 0); // 0 - for private mode
                utils.setSharedPreferences(sharedPreferences);
                //Utils.log("doInBackground ","executed");

                AuthenticationMobile Authobj = new AuthenticationMobile();
                Authobj.setMobileNumber(utils.getMobileNumber());
                Authobj.setMobLoginId(utils.getMobLoginId());
                Authobj.setMobUserPass(utils.getMobUserPass());
                Authobj.setIMEINo(utils.getIMEINo());
                Authobj.setCliectAccessId(utils.getCliectAccessId());
                Authobj.setMacAddress(utils.getMacAddress());
                Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
                Authobj.setAppVersion(AppVersion);

                DatabaseHandler db1= new DatabaseHandler(getBaseContext());
                Cursor mCur=db1.getLocationNew();

                if(mCur.getCount()>0) {
                    while (mCur.moveToNext()) {
                        new_latitude =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE));
                        new_longitude =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE));
                        member_id =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.MEMBER_ID));
                        date =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.DATE));
                        provider =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.PROVIDER));
                        is_gps =  mCur.getString(mCur.getColumnIndex(DatabaseHandler.GPS_STATUS));
                    }
               }

                SendBulkLocationCaller caller = new SendBulkLocationCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        utils.getDynamic_Url(), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_SEND_LOCATION),Authobj,LocationSendService.this,
                        new_latitude,new_longitude,member_id,date,provider,is_gps,activity);

                caller.username=sharedPreferences.getString("USER_NAME", "0");

                caller.join();
                caller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {

                    }
                }
            }
            catch (Exception e) {
                //	AlertsBoxFactory.showErrorAlert(e.toString(),context );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private void sendLocatonRecord(){

        try {
        //Log.e("--------API CALL--------","STARTED");
        JsonObject postParam2 = null;
            JsonObject postParam = new JsonObject();
            JsonArray jsonArray =new JsonArray();

            DatabaseHandler db1= new DatabaseHandler(getBaseContext());
            Cursor mCur=db1.getLastRecord();
            if(mCur.getCount()>0) {
                while (mCur.moveToNext()) {
                    //Log.e("DATABASE LAT",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE)));
                    //Log.e("DATABASE LONG",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE)));

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    try {

                        postParam.addProperty("latitude",mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE)));
                        postParam.addProperty("longitude", mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE)));
                        postParam.addProperty("dateTime", formattedDate);

                        DatabaseHandler db2= new DatabaseHandler(getBaseContext());
                        db2.deleteAllRow();

                        DatabaseHandler db3= new DatabaseHandler(getBaseContext());
                        Cursor mCur1=db3.getLastRecord();
                        if(mCur1.getCount()>0) {
                            Log.e("database having record",":");
                        }else{
                            Log.e("Empty database",":");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                jsonArray.add(postParam);
                Log.e("jsonArray",":"+jsonArray);
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
