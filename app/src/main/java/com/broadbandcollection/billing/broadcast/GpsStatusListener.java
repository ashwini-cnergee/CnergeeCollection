package com.broadbandcollection.billing.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.utils.Utils;


public class GpsStatusListener extends BroadcastReceiver implements LocationListener {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		LocationManager	locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		//if(intent!=null){
       //if (intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
       // { 
            // react on GPS provider change action 
        	//Utils.log("Provider","is onReceive: "+intent);
        	
        	 if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // Utils.log("Billing GPS", "GPS is Enabled in your devide");
                 if(AppConstants1.APP_OPEN){
                 Intent GpsIntent = new Intent("GpsStatus");	
                 LocalBroadcastManager.getInstance(context).sendBroadcast(GpsIntent);
                 }
             } else {
                 //showAlert 
            	// Utils.log("Billing GPS", "GPS is Disabled in your devide");
            	 if(AppConstants1.APP_OPEN){
            		 Intent GpsIntent = new Intent("GpsStatus");					   
					 LocalBroadcastManager.getInstance(context).sendBroadcast(GpsIntent);
            	
             }
            	 else{
            		
            	 Toast.makeText(context, "Please turn on Device GPS!!", Toast.LENGTH_LONG).show();
            	 }
           }
        
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Utils.log("Provider","is:"+provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
