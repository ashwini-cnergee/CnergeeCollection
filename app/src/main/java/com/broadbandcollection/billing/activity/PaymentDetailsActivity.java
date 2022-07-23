/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.broadbandcollection.R;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.KillProcess;
import com.broadbandcollection.billing.utils.Utils;

public class PaymentDetailsActivity extends Activity {

	String username, password;
	Utils utils = new Utils();
	public static Context context;
	public static final String backBundelPackage2 = "com.cnergee.billing.search.screen.INTENT";
	Bundle extras2;
	boolean isLogout = false;
	LocationManager locationManager;
//	public static final String backBundelPackage = "com.cnergee.billing.login.screen.INTENT";
//	public static final String currentBundelPackage = "com.cnergee.billing.search.screen.INTENT";
//	BundleHelper bundleHelper;

AlertDialog  alert ;
AlertDialog.Builder   alertDialogBuilder;
	@Override
	protected void onPause() {
		super.onPause();
	/*Log.i(" >>>> "," IN PAUSE ");*/
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
		}
		finish();
		AppConstants1.APP_OPEN=false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	@Override
	protected void onResume() {
		super.onResume();
		/*Log.i(" >>>> "," IN RESUME ");*/

		AppConstants1.APP_OPEN=true;
		if(AppConstants1.GPS_AVAILABLE){
		        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		          //  Toast.makeText(SearchVendorActivity.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
		        }else{
		        	 // showGPSDisabledAlertToUser();
		        }
		}
	}	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}/*else if (keyCode == KeyEvent.KEYCODE_HOME) {
			isLogout = true;
			finish();
			return true;
		}*/
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
	        super.onDestroy();
	        if(isLogout){
	        	ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	        	KillProcess kill = new KillProcess(context,am);
	        	kill.killAppsProcess();
	        }else{
	        	System.runFinalizersOnExit(true);
	        }
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	   }
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment_details);
		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else
		{
			AppConstants1.GPS_AVAILABLE=false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("GpsStatus"));
		//bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		Bundle extras = this.getIntent().getBundleExtra("com.cnergee.billing.payment.screen.INTENT");
		extras2 = this.getIntent().getBundleExtra(backBundelPackage2);
		
		context = this;
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);

		if (extras == null) {
			return;
		}
		
		username = extras.getString("username");
		// password = extras.getString("password");
		final String refNo = extras.getString("REF_NO");

		if (refNo.equals("0")) {
			AlertsBoxFactory.showAlert("Authentication Check Fail. ",context );
		} else if (refNo.equals("1")) {
			AlertsBoxFactory.showAlert("All ok  (Renewal done with immidiate effect) ",context );
		} else if (refNo.equals("2")) {
			AlertsBoxFactory.showAlert("Renewal Schedule For Next Cycle. ",context );
		} else if (refNo.equals("3")) {
			AlertsBoxFactory.showAlert("Error in renewal, Send to pipeline. ",context );
		} else if (refNo.equals("4")) {
			AlertsBoxFactory.showAlert("Invaild Date. ",context );
		}

		final TextView refTxt = (TextView) findViewById(R.id.RefText);
		refTxt.setText(refNo);

		final Button search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isLogout = false;
				finish();
			}
		});

		final Button logout = (Button) findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isLogout = true;
				finish();
				Intent finishIntent = new Intent("finish_search");
				LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
				Intent i= new Intent (PaymentDetailsActivity.this,Login.class);
				startActivity(i);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_payment_details, menu);
		return true;
	}
	
	//*************************Bradcast receiver for GPS**************************starts
			private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			    //    Utils.log("Service","Message");
			        //  ... react to local broadcast message
			        if(AppConstants1.GPS_AVAILABLE){
			        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			    	{
			    		if(alert!=null)
			    			alert.dismiss();
			    	}
			    	else{
			    		 //showGPSDisabledAlertToUser();
			    		 }
			        }
			        
			    }
			};
			//*************************Bradcast receiver for GPS**************************ends
			
			 public  void showGPSDisabledAlertToUser(){
				 alertDialogBuilder  = new AlertDialog.Builder(PaymentDetailsActivity.this);
			        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
			        .setCancelable(false)
			        .setPositiveButton("Goto Settings Page To Enable GPS",
			                new DialogInterface.OnClickListener(){
			            public void onClick(DialogInterface dialog, int id){
			                Intent callGPSSettingIntent = new Intent(
			                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			                startActivity(callGPSSettingIntent);
			            }
			        });
			        /*alertDialogBuilder.setNegativeButton("Cancel",
			                new DialogInterface.OnClickListener(){
			            public void onClick(DialogInterface dialog, int id){
			                dialog.cancel();
			            }
			        });*/
			        alert  = alertDialogBuilder.create();
			        alert.show();
			       
			        
			    }
}
