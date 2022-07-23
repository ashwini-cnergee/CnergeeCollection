/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  JAN 2013
 *
 * Version 1.0
 *
 */
package com.broadbandcollection.billing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.SOAP.LoginCaller;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastReceiver;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.Utils;

import java.util.Calendar;


public class Login extends Activity implements LocationListener {

	public static String rslt = "";
	Button btnsignin = null, settingBtn = null;
	EditText username, password;
	Utils utils = new Utils();
	public static boolean isVaildUser = false;

	public static MemberData memberData;
	public static Context context;
	private TextView deviceView;
	LocationManager locationManager;
	private ValidUserWebService validUserWebService = null;
	Location loc;
	SharedPreferences sharedPreferences;
	AlertDialog alert;
	AlertDialog.Builder alertDialogBuilder;
	String Dynamic_Url = "'";

	protected void onPause() {
		super.onPause();
		//Log.i(" >>>> "," IN PAUSE ");
		if (alert != null) {
			if (alert.isShowing()) {
				alert.dismiss();
			}
		}
		finish();

		AppConstants1.APP_OPEN = false;
		locationManager.removeUpdates(this);
		//AlertsBoxFactory.Dismiss();

		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//	Log.i(" >>>> "," IN RESUME ");
		/*if (sharedPreferences.getBoolean("check_first", true)) {
			Intent intentService1 = new Intent(Login.this, AlarmBroadcastReceiver.class);
			intentService1.putExtra("activity", "login");
			PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, 0);
			Calendar cal = Calendar.getInstance();
			AlarmManager alarm1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);15*60*1000
			// Start every 1mon..
			alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 60 * 1000, pintent1);
			Editor edit = sharedPreferences.edit();
			edit.putBoolean("check_first", false);
			edit.commit();
		}*/
		AppConstants1.APP_OPEN = true;
		if (AppConstants1.GPS_AVAILABLE) {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
			} else {
				// showGPSDisabledAlertToUser();
			}
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.runFinalizersOnExit(true);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}


	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { return true; } return
	 * super.onKeyDown(keyCode, event); }
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		if (AppConstants1.hasGPSDevice(this)) {
			AppConstants1.GPS_AVAILABLE = true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		} else {
			AppConstants1.GPS_AVAILABLE = false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Login.this);
		sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMessageReceiver, new IntentFilter("GpsStatus"));

		if (utils.getMobileNumber().equals("") ||
				utils.getMobLoginId().equals("") ||
				utils.getMobUserPass().equals("") || utils.getDynamic_Url().equals("")) {
			Toast.makeText(Login.this,
					"Please setup the WebService Authentication.",
					Toast.LENGTH_LONG).show();
			finish();
			Intent intent = new Intent(Login.this, AppSettingctivity.class);
			//startActivityForResult(intent, 0);
			startActivity(intent);
		} else {
			if (sharedPreferences.getBoolean("save_password", false)) {

				if (utils.getAppUserName().equals("") || utils.getAppPassword().equals("")) {

				} else {
					finish();
					Intent intent = new Intent(Login.this, SearchVendorActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", utils.getAppUserName());
					bundle.putString("password", utils.getAppPassword());
					intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
					startActivity(intent);
				}
			}
		}

		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);

		if (getString(R.string.app_run_mode).equalsIgnoreCase("dev")) {
			//username.setText("Mobile.Test");
			//password.setText("s123");
			//username.setText("Ravi.Pal");
			//password.setText("1550");

			//username.setText("Jitendra.Yadav");
			//password.setText("jitubhai");

		}
		TextView versionView = (TextView) findViewById(R.id.versionView);
		String app_ver;
		try {
			app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			versionView.setText("Ver." + app_ver);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		btnsignin = (Button) findViewById(R.id.loginBtn);
		context = this;
		deviceView = (TextView) findViewById(R.id.phoneInfo);

		settingBtn = (Button) findViewById(R.id.setting);
		settingBtn.setOnClickListener(btnSettingOnClickListener);

		btnsignin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (TextUtils.isEmpty(username.getText().toString().trim())) {
					AlertsBoxFactory.showAlert("Please enter valid data.", context);
					/*
					 * AlertDialog dialog =
					 * AlertsBoxFactory.getDialog("Unable to authenticate: ",
					 * getApplicationContext());
					 * dialog.setOwnerActivity(Login.this); dialog.show();
					 */
					return;
				} else {
					validUserWebService = new ValidUserWebService();
					validUserWebService.execute((String) null);
					//new ValidUserWebService().execute();
					// new LoginWebService().execute();
					// btnsignin.setClickable(false);
				}
			}
		});

		displayTelephoneInfo();
		
		
	/*	locationManager.addGpsStatusListener(new android.location.GpsStatus.Listener()
		{
		    public void onGpsStatusChanged(int event)
		    {
		    	
		    	Utils.log("Gps ",""+event);
		        switch(event)
		        {
		        case GPS_EVENT_STARTED:
		            // do your tasks
		            break;
		        case GPS_EVENT_STOPPED:
		            // do your tasks
		            break;
		        }
		    }
		});*/
	}

	//*************************Bradcast receiver for GPS**************************starts
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//   Utils.log("Service","Message");
			//  ... react to local broadcast message
			if (AppConstants1.GPS_AVAILABLE) {
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					if (alert != null)
						alert.dismiss();
				} else {
					//showGPSDisabledAlertToUser();
				}
			}
		}
	};
	//*************************Bradcast receiver for GPS**************************ends

	OnClickListener btnSettingOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			finish();
			Intent intent = new Intent(Login.this, AppSettingctivity.class);
			//startActivityForResult(intent, 0);
			startActivity(intent);
		}
	};

	private class ValidUserWebService extends AsyncTask<String, Void, Void> {

		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();
		String AppVersion = Utils.GetAppVersion(Login.this);


		private ProgressDialog Dialog = new ProgressDialog(Login.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			//Utils.log("Check", "AppVeresion :"+AppVersion);
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			btnsignin.setClickable(true);
			validUserWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				if (isVaildUser) {

					SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CNERGEE", 0); // 0 - for
					// private
					// mode
					Editor editor = sharedPreferences.edit();
					editor.putString("USER_NAME", username.getText().toString());
					editor.putString("USER_PASSWORD", password.getText()
							.toString());
					editor.commit();

					finish();
					Intent intent = new Intent(Login.this, SearchVendorActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", username.getText().toString());
					bundle.putString("password", password.getText().toString());
					intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
					/*intent.putExtra("username", "" + );
					intent.putExtra("password", "" + password.getText());*/
					/*
					 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					 * Intent.FLAG_ACTIVITY_NO_HISTORY);
					 */
					startActivity(intent);
					//startActivityForResult(intent, 0);

					Intent intentService1 = new Intent(Login.this, AlarmBroadcastReceiver.class);
					intentService1.putExtra("activity", "login collection");
					sendBroadcast(intentService1);
					/*PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, 0);
					Calendar cal = Calendar.getInstance();
					AlarmManager alarm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					// Start every 30 seconds
					//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*1000, pintent);15*60*1000
					// Start every 1mon..
					alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*60*1000, pintent1);*/
				} else {
					username.requestFocus();
					username.setError(Html
							.fromHtml("<font color='red'>Invalid Username or Password. Please try again</font>"));
				}
			} else {
				AlertsBoxFactory.showAlert(rslt, context);
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Login.this);

				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				Authobj.setAppVersion(AppVersion);
				/*Utils.log("Check Login",""+utils.getIMEINo());*/
				LoginCaller loginCaller = new LoginCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						utils.getDynamic_Url(), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_VALIDATE_USER),
						Authobj);

				loginCaller.username = username.getText().toString().trim();
				loginCaller.password = password.getText().toString().trim();

				loginCaller.join();
				loginCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				//	AlertsBoxFactory.showErrorAlert(e.toString(),context );
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			btnsignin.setClickable(true);
			validUserWebService = null;
		}
	}

	/**
	 * @return the memberData
	 */
	public MemberData getMemberData() {
		return memberData;
	}

	/**
	 * @param memberData
	 *            the memberData to set
	 */
	public void setMemberData(MemberData memberData) {
		this.memberData = memberData;
	}

	private void displayTelephoneInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// get IMEI

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		String imei = "";
		String phone = "";
//		String phone = tm.getLine1Number();
		String operatorname = tm.getNetworkOperatorName();
		String simcountrycode = tm.getSimCountryIso();
        String simoperator = tm.getSimOperatorName();
        String simserialno = "";
        String subscriberid = "";

//		String simserialno = tm.getSimSerialNumber();
//		String subscriberid = tm.getSubscriberId();

		Log.e("Phone",":-"+phone);
		Log.e("simserialno",":-"+simserialno);
		Log.e("subscriberid",":-"+subscriberid);
        StringBuffer sb = new StringBuffer();
        sb.append("IMEI : "+imei);
        sb.append("\nPhone No. : "+phone);
        sb.append("\nOperator : "+operatorname);
        sb.append("\nSIM Operator : "+simoperator);
        sb.append("\nSIM Ser. No. : "+simserialno);
        sb.append("\nSubscriber Id. : "+subscriberid);
      
        deviceView.setText(sb.toString());
        //ToastViewFactory toastViewFactory = new ToastViewFactory(context,inflater,viewGroup,sb.toString(),Toast.LENGTH_LONG);
		//toastViewFactory.showToast();
	}
	

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Utils.log("Lattitude","is: "+location.getLatitude());
		//Utils.log("Longitude","is:"+location.getLongitude());
		Toast.makeText(getApplicationContext(), "Lattitude and Longitude"+location.getLatitude()+" and "+location.getLongitude(), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	  public  void showGPSDisabledAlertToUser(){
		  alertDialogBuilder  = new AlertDialog.Builder(context);
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
