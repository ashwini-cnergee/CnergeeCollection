/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.broadbandcollection.R;

import com.broadbandcollection.billing.SOAP.AppSettingCaller;
import com.broadbandcollection.billing.SOAP.GetDynamicUrlSOAP;
import com.broadbandcollection.billing.SOAP.LogOutButtonSettingSOAP;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastForPayment;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;
import com.traction.ashok.util.AlertsBoxFactory;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

public class AppSettingctivity extends Activity {

	Button btnSave = null;
	EditText username, password, etCustId;
	private String imei, simserialno;
	public static Context context;
	public static String rslt;
	public static Boolean AccessCodeFlag;
	public static ArrayList<String> alMobilSettingName = new ArrayList<String>();
	public static ArrayList<String> alMobilSettingValue = new ArrayList<String>();
	SharedPreferences sharedPreferences;
	private ProgressDialog Dialog;
	public static String DynamicUrl;
	String MacAddress = "", PhoneUniqueId = "";
	EditText et_Ezetap_Code;


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onPause() {
		super.onPause();
	/*Log.i(" >>>> "," IN PAUSE ");*/
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*Log.i(" >>>> "," IN RESUME ");*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_settingctivity);
		context = this;
		Dialog = new ProgressDialog(AppSettingctivity.this);
		btnSave = (Button) findViewById(R.id.save);
		//btnReset = (Button) findViewById(R.id.reset);

		btnSave.setOnClickListener(btnSaveOnClickListener);
		//btnReset.setOnClickListener(btnResetOnClickListener);

		//mobileNo = (EditText) findViewById(R.id.mobileNo);
		username = (EditText) findViewById(R.id.username);
		//username.setBackgroundColor(Color.LTGRAY);

		password = (EditText) findViewById(R.id.password);
		//password.setBackgroundColor(Color.LTGRAY);

		etCustId = (EditText) findViewById(R.id.etCustId);

		et_Ezetap_Code = (EditText) findViewById(R.id.et_tid);
		//etCustId.setBackgroundColor(Color.LTGRAY);


		TextView versionView = (TextView) findViewById(R.id.versionView);
		String app_ver;
		try {
			app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			versionView.setText("Ver." + app_ver);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);


		WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (manager != null) {
			WifiInfo info = manager.getConnectionInfo();
			MacAddress = info.getMacAddress();
			//Utils.log("Wifi address","is: "+MacAddress);
		}
		PhoneUniqueId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		Log.e("PhoneUnique",":-"+PhoneUniqueId);

//		imei = tm.getDeviceId().toString();
//		simserialno = tm.getSimSerialNumber();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
		//imei = telephonyManager.getDeviceId();
		//simserialno = tm.getSimSerialNumber();

		Log.e("imei",""+imei);

		imei="869875042058938";
		simserialno="89918740400208305908";

		if(getString(R.string.app_run_mode).equalsIgnoreCase("dev")){
			//imei = "911211850733560";
			/*//for DVOIS
			imei = "353863073458550";
			simserialno = "89918030116125749890";*/

			/* //FOR Development
			simserialno="8991200014805547107f";
			imei="911367804438316";*/


			/*//FOR NET9ONLINE
			simserialno="8991200050100769418f";++
			imei="911425208672029";*/


			/*//For GIGANTIC
			simserialno="8991200014805547107f";
			imei="911367804438316";*/

		/*	//FOR Ravi.Pal DVOIS
			simserialno="8991200010303976541f";
			imei="911367808188891";*/


			/*//FOR Yashtel
			imei = "357146059105734";
			simserialno = "8991860044601691834";*/


			/*//FOR LS
			simserialno="89910722401301407558";
			imei="911336600641142";*/

	/*		//FOR VCPL
			simserialno="8991200014805547107f";
			imei="911367804438316";
		*/
			/*//FOR Prakash.Kamble DVOIS
			simserialno="8991200010303964554f";
			imei="911367808441233";*/

		    /*//FOR Net9Online

				simserialno="8991921599901423003";
				imei="352116069648465";
			*/


			/*//For Trple PLay
			simserialno="89911100017009905576";
			imei="864465028104529";*/

			/*//FOR DWAN
			simserialno="89910351110008792893";
			imei="868415020410743";*/

			/*//FOR N9STARs
			simserialno="8991921310278556676f";
			imei="359843052634664";*/



			/*//FOR Apple Broadband
			simserialno="89910722401404149339";
			imei="911474351698274";*/
		}

			/*
			 * MobileNumber 	    IMEINo	        MobLoginID	MobUserPass
				8991200014805547107	911211850733560	mt	        mt123

			 */
//		kartik
//		1234
//
		/*imei = "352272051556909";
		simserialno = "8991200017305470027";*/

		//simserialno = "12345678901234567890";
	/*	//imei = "00000000000000000000";
	 *
		AuthOBJ.MobileNumber = "8991200017305470027";
        AuthOBJ.MobLoginId = "PR14012013312";
        AuthOBJ.IMEINo = "352272051556909";
        AuthOBJ.MobUserPass = "PR312$";
        //AuthOBJ.IsActive = true;

Prakash.Kamble/12*/

		//simserialno = tm.getLine1Number().toString();

		/*if(simserialno == null ){
			simserialno = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
		}*/

		/*String operatorname = tm.getNetworkOperatorName();
		String simcountrycode = tm.getSimCountryIso();
        String simoperator = tm.getSimOperatorName();
        String simserialno = tm.getSimSerialNumber();
        String subscriberid = tm.getSubscriberId();*/

		sharedPreferences 	= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode

		//	mobileNo.setText(sharedPreferences.getString("MobileNumber", ""));
		username.setText(sharedPreferences.getString("MobLoginId", ""));
		password.setText(sharedPreferences.getString("MobUserPass", ""));
		etCustId.setText(sharedPreferences.getString("CliectAccessId", ""));
		et_Ezetap_Code.setText(sharedPreferences.getString("EzeTap_Id", "0"));

		final Button btnBack = (Button) findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(AppSettingctivity.this, Login.class);
				startActivity(intent);
			}
		});
	}

	/*MobileNumber = sharedPreferences.getString("MobileNumber", "");
	MobLoginId = sharedPreferences.getString("MobLoginId", "");
	MobUserPass = sharedPreferences.getString("MobUserPass", "");
	IMEINo = sharedPreferences.getString("IMEINo", "");
	*/
	OnClickListener btnSaveOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (etCustId.length() > 0) {
				if (username.length() > 0) {
					if (password.length() > 0) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage(
								Html.fromHtml("<font color='#FFA500'><b>Are you sure you want to process?</b></font>"))
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle(
										Html.fromHtml("<font color='#FFA500'>Change Web Service Authentication</font>"))
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												//new ValidateAccessCode().execute();
												//new SettingBasedLogout().execute();
												new GetDynamicURLAsyncTask().execute();
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
												// isConfirm = false;
											}
										});

						AlertDialog alert = builder.create();
						alert.show();

					} else {
						password.requestFocus();
						password.setError(Html
								.fromHtml("<font color='red'>Please Enter Mobile Password</font>"));
					}
				} else {
					username.requestFocus();
					username.setError(Html
							.fromHtml("<font color='red'>Please Enter Mobile Login Id</font>"));
				}
			} else {
				etCustId.requestFocus();
				etCustId.setError(Html
						.fromHtml("<font color='red'>Please Enter Access Code</font>"));
			}
		}
	};
	/*Button.OnClickListener btnResetOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			LoadPreferences();
		}
	};*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_app_settingctivity, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.runFinalizersOnExit(true);
	}

	protected void SavePreferences(String key, String value) {
		// SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		// editor.apply();
		editor.commit();
	}

	/*	private void LoadPreferences() {
            Utils utils = new Utils();
            SharedPreferences sharedPreferences = getApplicationContext()
                    .getSharedPreferences("CNERGEE", 0); // 0 - for private mode
            utils.setSharedPreferences(sharedPreferences);

            if (utils.getMobileNumber().equals("")) {
                imei = "00000000000000000000";
                phone = "1234567890";
                //mobileNo.setText("1234567890");
                username.setText("mobuser1");
                password.setText("1234");
            }else{
                imei = utils.getIMEINo();
                phone = utils.getMobileNumber();
                username.setText(utils.getMobLoginId());
                password.setText(utils.getMobUserPass());
            }
        }*/

	protected class ValidateAccessCode extends AsyncTask<String, Void, Void>{

		String AppVersion= Utils.GetAppVersion(AppSettingctivity.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//	Dialog.setMessage("Please Wait.....");
			//Dialog.show();
			//fontTypeface.dialogFontOverride(context,Dialog);
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(simserialno);
				Authobj.setMobLoginId(username.getText().toString());
				Authobj.setMobUserPass(password.getText().toString());
				Authobj.setIMEINo(imei);
				Authobj.setCliectAccessId(etCustId.getText().toString());
				Authobj.setMacAddress(MacAddress);
				Authobj.setPhoneUniqueId(PhoneUniqueId);
				Authobj.setAppVersion(AppVersion);
				AppSettingCaller appSettingCaller= new AppSettingCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						DynamicUrl, getApplicationContext()
						.getResources().getString(
								R.string.METHOD_APP_SETTING),
						Authobj);

				appSettingCaller.join();
				appSettingCaller.start();
				rslt="START";
				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			}
			catch(Exception e){
				AlertsBoxFactory.showErrorAlert(e.toString(),context );
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Dialog.dismiss();
			//	Utils.log("Result ",""+rslt);
			if (rslt.trim().equalsIgnoreCase("OK")) {

				if (AccessCodeFlag) {

					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("MobileNumber",simserialno.toString().trim());
					editor.putString("MobLoginId", username.getText().toString().trim());
					editor.putString("MobUserPass", password.getText().toString().trim());
					editor.putString("IMEINo", imei.trim());
					editor.putString("CliectAccessId", etCustId.getText().toString().trim());
					editor.putString("MacAddress", MacAddress);
					editor.putString("PhoneUniqueId", PhoneUniqueId);
					editor.putString("EzeTap_Id", et_Ezetap_Code.getText().toString());
					editor.commit();

					/*
					 * SavePreferences("VENDOR_CODE", vendorcode.getText().toString());
					 * SavePreferences("AUT_USERNAME", username.getText().toString());
					 * SavePreferences("AUT_PASSWORD", password.getText().toString());
					 */

					/*Log.i("MobileNumber", simserialno);
					Log.i("MobLoginId", username.getText().toString());
					Log.i("MobUserPass", password.getText().toString());
					Log.i("IMEINo", imei);*/

					if(sharedPreferences.getBoolean("check_first", true)){
						//***********************
						//	Utils.log("Alarm","Set");
						Intent intentService1 = new Intent(AppSettingctivity.this, AlarmBroadcastForPayment.class);


						PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, PendingIntent.FLAG_IMMUTABLE);
						Calendar cal = Calendar.getInstance();
						AlarmManager alarm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
						// Start every 30 seconds
						//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
						// Start every 1mon..
						alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent1);

						SharedPreferences.Editor edit= sharedPreferences.edit();
						edit.putBoolean("check_first", false);
						edit.commit();
						//********************************
					}

					Toast.makeText(AppSettingctivity.this,
							"WebService Authentication set. Please login!",
							Toast.LENGTH_SHORT).show();
					finish();
					Intent intent = new Intent(AppSettingctivity.this, Login.class);
					startActivity(intent);

				}
				else{
					etCustId.requestFocus();
					etCustId.setError(Html.fromHtml("<font color='red'>Please Check Access Code,Mobile Login Id and Mobile Password</font>"));
				}
			}
			else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
		}
	}


	public class SettingBasedLogout extends AsyncTask<String, Void, Void>{
		ProgressDialog prg;
		String Settingrslt="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Dialog.setMessage("Please Wait.....");
			Dialog.setCancelable(false);
			Dialog.show();
			/*prg= new ProgressDialog(AppSettingctivity.this);
			prg.setCancelable(false);
			prg.setMessage("Please Wat...");
			prg.show();*/
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			AuthenticationMobile Authobj = new AuthenticationMobile();
			Authobj.setMobileNumber(simserialno);
			Authobj.setMobLoginId(username.getText().toString());
			Authobj.setMobUserPass(password.getText().toString());
			Authobj.setIMEINo(imei);
			Authobj.setCliectAccessId(etCustId.getText().toString());
			Authobj.setMacAddress(MacAddress);
			Authobj.setPhoneUniqueId(PhoneUniqueId);
			Authobj.setAppVersion(Utils.GetAppVersion(AppSettingctivity.this));
			LogOutButtonSettingSOAP logOutButtonSettingSOAP= new LogOutButtonSettingSOAP(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					DynamicUrl, getApplicationContext()
					.getResources().getString(
							R.string.METHOD_GENERAL_SETTING)
			);
			try {
				Settingrslt=logOutButtonSettingSOAP.getButtonSetting(Authobj,"Collection",et_Ezetap_Code.getText().toString());
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//prg.dismiss();
			new ValidateAccessCode().execute();
			if(Settingrslt.length()>0){
				Utils.log("Result","is: "+Settingrslt);
				if(Settingrslt.equalsIgnoreCase("ok")){
					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences("CNERGEE", 0); // 0 - for private mode

					if(alMobilSettingName.size()>0){
						//	Utils.log("size","is: "+alMobilSettingName.size());
						for(int i=0;i<alMobilSettingName.size();i++){
							if(alMobilSettingName.get(i).equalsIgnoreCase("SavePassword")){
								//	Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("save_password", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false );
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("CreditCard")){
								//Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("creditcard", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false);
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("AllotedCreditCard")){
								Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));

								SharedPreferences.Editor editor= sharedPreferences.edit();
								editor.putBoolean("user_card_allow", Boolean.valueOf(alMobilSettingValue.get(i)));   //user_card_allow
								editor.putBoolean("check_app",false);
								editor.commit();
							}
							if(alMobilSettingName.get(i).equalsIgnoreCase("InformCustomer")){

								//Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("inform_customer", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false );
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("IdCard")){

								//Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("id_card", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false );
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("IPRenewal")){

								//Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("ip_renewal", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false );
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("PGSMSRequest")){

								//Utils.log("Value From","SOAP:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("sms_pg", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false );
								editor.commit();
							}

							if(alMobilSettingName.get(i).equalsIgnoreCase("Postpaid")){

								Utils.log("Value From","Postpaid:"+Boolean.valueOf(alMobilSettingValue.get(i)));
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putBoolean("check_postpaid", Boolean.valueOf(alMobilSettingValue.get(i)));
								editor.putBoolean("check_app",false);
								editor.commit();
							}
						}
					}
					else{
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putBoolean("inform_customer",false);
						editor.putBoolean("check_app",false );
						editor.putBoolean("creditcard", false);
						editor.putBoolean("check_app",false );
						editor.putBoolean("id_card",false );
						editor.putBoolean("user_card_allow", false);
						editor.putBoolean("ip_renewal", false);
						editor.putBoolean("sms_pg", false);
						editor.putBoolean("cheeck_postpaid", false);
						editor.commit();
					}
				}
				else{
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putBoolean("inform_customer",false);
					editor.putBoolean("check_app",false );
					editor.putBoolean("creditcard", false);
					editor.putBoolean("check_app",false );
					editor.putBoolean("id_card",false );
					editor.putBoolean("user_card_allow", false);
					editor.putBoolean("ip_renewal", false);
					editor.putBoolean("sms_pg", false);
					editor.commit();
				}
			}
			else{
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putBoolean("inform_customer",false);
				editor.putBoolean("check_app",false );
				editor.putBoolean("creditcard", false);
				editor.putBoolean("check_app",false );
				editor.putBoolean("id_card",false );
				editor.putBoolean("user_card_allow", false);
				editor.putBoolean("ip_renewal", false);
				editor.putBoolean("sms_pg", false);
				editor.putBoolean("cheeck_postpaid", false);
				editor.commit();
			}
		}
	}

	public class GetDynamicURLAsyncTask extends AsyncTask<String, Void, Void>{
		ProgressDialog prgDialog;
		String rsltDynamicUrl="";
		GetDynamicUrlSOAP soap;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prgDialog= new ProgressDialog(AppSettingctivity.this);
			prgDialog.setMessage("Please wait...");
			prgDialog.setCancelable(false);
			prgDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			soap = new GetDynamicUrlSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.GLOBAL_SOAP_URL),getString(R.string.METHOD_GET_DYNAMIC_URL));
			try {
				rsltDynamicUrl=soap.getDynamicUrl(etCustId.getText().toString());
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(prgDialog!=null)
			   prgDialog.dismiss();
			try{

				if(rsltDynamicUrl.length()>0){
					if(rsltDynamicUrl.equalsIgnoreCase("ok")){
						if(DynamicUrl.length()>0){
							if(!DynamicUrl.equalsIgnoreCase("anyType{}")){

								Utils.log("Url","is:"+DynamicUrl);
								SharedPreferences sharedPreferences = getApplicationContext()
										.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putString("Dynamic_Url",DynamicUrl);
								editor.commit();
								new SettingBasedLogout().execute();
							}
						}
					}
					else{
						AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
				}

			}catch(Exception e){
				prgDialog.dismiss();
			}

		}
	}
}
