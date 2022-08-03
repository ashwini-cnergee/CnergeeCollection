
package com.broadbandcollection.billing.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.GetIdCardSOAP;
import com.broadbandcollection.billing.SOAP.PaymentRequestCaller;
import com.broadbandcollection.billing.SOAP.SearchMemberCaller;
import com.broadbandcollection.billing.SOAP.TodaysCollectionCaller;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastForLocation;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastForPayment;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastReceiver;
import com.broadbandcollection.billing.database.DatabaseHandler;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.InternalStorage;
import com.broadbandcollection.billing.obj.MemberData;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.KillProcess;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.iprenewal.SearchSubscriber_Ip_Activity;
import com.ezetap.sdk.AppConstants;
import com.ezetap.sdk.EzetapPayApis;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchVendorActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

	boolean mAlreadyStartedService=false;

	private Button searchBtn,resetBtn,collectBtn;
	public String username, password;
	//public EditText searchTxt;
	public AutoCompleteTextView searchTxt;
	public TextView hdPayCount;
	public static String KEY="ID_CARD";
	public static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	public static String todayCollection = "";
	public static String payCount = "0";
	ImageView IdImage;
	private ImageView imgPayCnt;
	LocationManager locationManager;
	public static ArrayList<Boolean> al_is_postpaid=new ArrayList<Boolean>();
	public static Map<String, MemberData> mapMemberData;
	public static String Message=null;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	private GetTodaysCollectionWebService getTodaysCollectionWebService = null;
	private GetPaymentRequestWebService getPaymentRequestWebService =null;
	RelativeLayout btnShowId;
	boolean isLogout = false;
	public static boolean is_paymentpickup_running=false;
	public static final String backBundelPackage = "com.cnergee.billing.login.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.search.screen.INTENT";
	BundleHelper bundleHelper;
	AlertDialog  alert ;
	AlertDialog.Builder   alertDialogBuilder;
	ArrayList <String> alSearchList;
	SharedPreferences sharedPreferences;
	DatabaseHandler databaseHandler;
	boolean Check;
	Button btn_ip_renewal,btn_postpaid;
	 public static ArrayList<String>alSubId= new ArrayList<String>();
		public static ArrayList<String>alPickupId= new ArrayList<String>();
		public static ArrayList<HashMap<String, String>> paymentPickList=new ArrayList<HashMap<String,String>>();
		public static ArrayList<String> alMessage;
		
		TextView tv_app_version;
	 
	protected void onPause() {
		super.onPause();
	/*Log.i(" >>>> "," IN PAUSE ");*/
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
			
		}
		searchTxt.setText("");
	//	finish();
		AppConstants1.APP_OPEN=false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	
	}
	@Override
	protected void onResume() {
		super.onResume();
		/*Log.i(" >>>> "," IN RESUME ");*/
		//databaseHandler= new DatabaseHandler(SearchVendorActivity.this);

		startStep1();

		getPaymentRequestWebService = new GetPaymentRequestWebService();
		  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			  getPaymentRequestWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		  }
		  else{
			  getPaymentRequestWebService.execute((String) null);
		  }
//		AppConstants1.APP_OPEN=true;
		
	//	if(sharedPreferences.getBoolean("check_first", true)){
			
			//***********************
			
			Intent intentService1 = new Intent(SearchVendorActivity.this, AlarmBroadcastForPayment.class);
			intentService1.putExtra("activity", "alarm");
			PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, PendingIntent.FLAG_IMMUTABLE);
			Calendar cal = Calendar.getInstance();
			AlarmManager alarm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
			// Start every 1mon..
			alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 120*1000, pintent1);
			
			Editor edit= sharedPreferences.edit();
			edit.putBoolean("check_first", false);
			edit.commit();
			
			//******************************** 
		//}
			Check =	sharedPreferences.getBoolean("Id_Card_Check", false);
		 Utils.log("Check Executed",""+Check);
         if (Check) { 
       	  try {
				String s= InternalStorage.readObject(SearchVendorActivity.this, KEY);
				Utils.log("Id Card Executed", "Id Card");
				ParseJsonResponse(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	  
       	//maybe you want to check it by getting the sharedpreferences. Use this instead if (locked)
       	// if (prefs.getBoolean("locked", locked) {
       	
       	 Utils.log("Check Executed",""+Check);
             
       	} else {

       	   Utils.log("Check Executed",""+Check);
              
       	}
		
		//if(sharedPreferences.getBoolean("check_id_card", true)){
		//	new GetIdCardAsyncTask().execute();
		//}
	}

	private void startStep1() {

		//Check whether this user has installed Google play service which is being used by LocationRecord updates.
		if (isGooglePlayServicesAvailable()) {

			//Passing null to indicate that it is executing for the first time.
			startStep2(null);

		} else {
			Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
		}
	}
	public boolean isGooglePlayServicesAvailable() {
		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
		if (status != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(status)) {
				googleApiAvailability.getErrorDialog(this, status, 2404).show();
			}
			return false;
		}
		return true;
	}

	private Boolean startStep2(DialogInterface dialog) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
			promptInternetConnect();
			return false;
		}


		if (dialog != null) {
			dialog.dismiss();
		}

		//Yes there is active internet connection. Next check LocationRecord is granted by user or not.

		if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
			startStep3();
		} else {  //No user has not granted the permissions yet. Request now.
			requestPermissions();
		}
		return true;
	}

	private void promptInternetConnect() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SearchVendorActivity.this);
		builder.setTitle(R.string.title_alert_no_intenet);
		builder.setMessage(R.string.msg_alert_no_internet);

		String positiveText = getString(R.string.btn_label_refresh);
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {


						//Block the Application Execution until user grants the permissions
						if (startStep2(dialog)) {

							//Now make sure about location permission.
							if (checkPermissions()) {

								//Step 2: Start the LocationRecord Monitor Service
								//Everything is there to start the service.
								startStep3();


							} else if (!checkPermissions()) {
								requestPermissions();
							}

						}
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private boolean checkPermissions() {
		int permissionState1 = ActivityCompat.checkSelfPermission(this,
				android.Manifest.permission.ACCESS_FINE_LOCATION);

		int permissionState2 = ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION);

		return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

	}

	private void requestPermissions() {

		boolean shouldProvideRationale =
				ActivityCompat.shouldShowRequestPermissionRationale(this,
						android.Manifest.permission.ACCESS_FINE_LOCATION);

		boolean shouldProvideRationale2 =
				ActivityCompat.shouldShowRequestPermissionRationale(this,
						Manifest.permission.ACCESS_COARSE_LOCATION);


		// Provide an additional rationale to the img_user. This would happen if the img_user denied the
		// request previously, but didn't check the "Don't ask again" checkbox.
		if (shouldProvideRationale || shouldProvideRationale2) {
			Log.i(TAG, "Displaying permission rationale to provide additional context.");
			showSnackbar(R.string.permission_rationale,
					android.R.string.ok, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Request permission
							ActivityCompat.requestPermissions(SearchVendorActivity.this,
									new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
									REQUEST_PERMISSIONS_REQUEST_CODE);
						}
					});
		} else {
			Log.i(TAG, "Requesting permission");
			// Request permission. It's possible this can be auto answered if device policy
			// sets the permission in a given state or the img_user denied the permission
			// previously and checked "Never ask again".
			ActivityCompat.requestPermissions(SearchVendorActivity.this,
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}
	private void showSnackbar(final int mainTextStringId, final int actionStringId,
							  View.OnClickListener listener) {
		Snackbar.make(
				findViewById(android.R.id.content),
				getString(mainTextStringId),
				Snackbar.LENGTH_INDEFINITE)
				.setAction(getString(actionStringId), listener).show();
	}


	private void startStep3() {

		//And it will be keep running until you close the entire application from task manager.
		//This method will executed only once.

		//if (!mAlreadyStartedService ) {

			//Start location sharing service to app server.........
//			Intent intent = new Intent(this, LocationMonitoringService.class);
//			startService(intent);

			Intent intentService1 = new Intent(this, AlarmBroadcastForLocation.class);
		    intentService1.putExtra("activity", "roaming");
			PendingIntent pintent1 = PendingIntent.getBroadcast(this, 0, intentService1, PendingIntent.FLAG_IMMUTABLE);

			Calendar cal = Calendar.getInstance();
			AlarmManager alarm1 = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 60 * 1000, pintent1);
			//Ends................................................
		//}

	}


	@Override
	protected void onDestroy() {
	        super.onDestroy();

			//stopService(new Intent(this, LocationMonitoringService.class));
		    stopService(new Intent(this, AlarmBroadcastReceiver.class));

			mAlreadyStartedService = false;

	        if(isLogout){
	 			ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	 			KillProcess kill = new KillProcess(context,am);
	 			kill.killAppsProcess();

				//stopService(new Intent(this, LocationMonitoringService.class));
				stopService(new Intent(this, AlarmBroadcastReceiver.class));
				mAlreadyStartedService = false;
	        }else{
	            System.runFinalizersOnExit(true);
	        }
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_vendor);
		BaseApplication.getEventBus().register(this);
		
		/*alSearchList= new ArrayList<String>();
		alSearchList.add("ratneshnamdeo");
		alSearchList.add("raju");
		alSearchList.add("rahul");
		alSearchList.add("cnergee");*/
		
		
		/*EzetapPayApis.create(CardActivity.API_CONFIG).initializeDevice(SearchVendorActivity.this, AppConstants.REQ_CODE_PAY_CARD, "Username");

		EzetapPayApis.create(CardActivity.API_CONFIG).checkForUpdate(SearchVendorActivity.this, AppConstants.REQ_CODE_PAY_CARD, "Username");*/
			
		databaseHandler = new DatabaseHandler(SearchVendorActivity.this);
		databaseHandler.exists(DatabaseHandler.TABLE_ID_CARD);
		tv_app_version=(TextView) findViewById(R.id.tv_app_version);
		tv_app_version.setVisibility(View.VISIBLE);
		tv_app_version.setText("Version "+ Utils.GetAppVersion(SearchVendorActivity.this));
		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else{
			AppConstants1.GPS_AVAILABLE=false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		context = this;
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("finish_search"));
		sharedPreferences = getApplicationContext().getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);

		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		//Bundle extras = getIntent().getExtras();
		
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}

		username = bundleHelper.getCurrentExtras().getString("username");
		password = bundleHelper.getCurrentExtras().getString("password");
	
		hdPayCount = (TextView)findViewById(R.id.hdPayCnt);
	//	searchTxt = (EditText) findViewById(R.id.searchTxt);
		searchTxt = (AutoCompleteTextView) findViewById(R.id.searchTxt);
		//ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_item, alSearchList);
		//searchTxt.setAdapter(adapter);
		//searchTxt.setThreshold(1);
		if(getString(R.string.app_run_mode).equalsIgnoreCase("dev")){
			//searchTxt.setText("crm_mobiletest1");
		}
		
		searchBtn = (Button) findViewById(R.id.search);
		searchBtn.setOnClickListener(searchOnClickListener);
		
		resetBtn = (Button) findViewById(R.id.reset);
		resetBtn.setOnClickListener(resetOnClickListener);
		
		collectBtn =  (Button) findViewById(R.id.collectBtn);
		collectBtn.setOnClickListener(todayCollectClickListener);
		
		btnShowId=(RelativeLayout)findViewById(R.id.btnShowId);
		//btnShowId.setVisibility(View.GONE);
		btn_ip_renewal= (Button) findViewById(R.id.btn_ip_renewal);
		btn_postpaid=(Button) findViewById(R.id.btn_postpaid);
		
		if(sharedPreferences.getBoolean("ip_renewal", false)){
			btn_ip_renewal.setVisibility(View.VISIBLE);
		}
		else{
			btn_ip_renewal.setVisibility(View.GONE);
		}
		
		
		if(sharedPreferences.getBoolean("check_postpaid", false)){
			btn_postpaid.setVisibility(View.VISIBLE);
		}
		else{
			btn_postpaid.setVisibility(View.GONE);
		}
		
		
		
		btn_ip_renewal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ip_rewnewal= new Intent(SearchVendorActivity.this, SearchSubscriber_Ip_Activity.class);
				startActivity(ip_rewnewal);
				
			    //	AlertsBoxFactory.showAlert("Work in progress", SearchVendorActivity.this);
			}
		});
		
		btn_postpaid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent postpaid_intent= new Intent(SearchVendorActivity.this, Postpaid_Activity.class);
				postpaid_intent.putExtra("postpaid_user", "");
				startActivity(postpaid_intent);
			}
		});
		
		
		if(sharedPreferences.getBoolean("id_card", false)){
			btnShowId.setVisibility(View.VISIBLE);
		}
		else{
			btnShowId.setVisibility(View.GONE);
		}
		
		Utils.log("Login Mobile","LoginMobile  :"+utils.getMobileNumber());
		Check  =	sharedPreferences.getBoolean("Id_Card_Check", false);

	       

	        
          Utils.log("Check Executed",""+Check);
          if (Check) { 
        	  try {
				String s= InternalStorage.readObject(SearchVendorActivity.this, KEY);
				Utils.log("Id Card Executed", "Id Card");
				ParseJsonResponse(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  
        	//maybe you want to check it by getting the sharedpreferences. Use this instead if (locked)
        	// if (prefs.getBoolean("locked", locked) {
        	
        	 Utils.log("Check Executed",""+Check);
              
        	} else {
        		        	
        	   Utils.log("Check Executed",""+Check);
               
        	}
          
		
		btnShowId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(SearchVendorActivity.this,IdCardActivity.class);
				startActivity(i);
			}
		});
		
		final Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isLogout = true;
				finish();
				/*Intent intent = new Intent(Intent.ACTION_MAIN, null);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);*/
				sharedPreferences = getApplicationContext()
						.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
				Editor edit= sharedPreferences.edit();
				Utils.log("Login Mobile","LoginMobile  :"+utils.getMobileNumber());
				edit.remove("USER_NAME");
				edit.remove("USER_PASSWORD");
				utils.clearSharedPreferences(sharedPreferences);
				
			
				edit.commit();
				
			}
		});
		
		imgPayCnt = (ImageView)findViewById(R.id.imgPayCnt);
		
		imgPayCnt.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	//Log.i(" >>> ","CLICK ON IMG... ");
		    	//finish();
				Intent intent = new Intent(SearchVendorActivity.this,
						PaymentPickupActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("username",username);
				bundle.putString("password",password);
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra("com.cnergee.billing.showpaymentpickup.screen.INTENT", bundle);
				startActivity(intent);
		    }
		});
		
	/*	getPaymentRequestWebService = new GetPaymentRequestWebService();
		getPaymentRequestWebService.execute((String) null);*/

		Utils.log("Login Mobile","LoginMobile  :"+utils.getMobileNumber());
		
		//searchTxt.setText("crm_mobiletest1");
		
		
	databaseHandler = new DatabaseHandler(context);
		
		Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy");
			String strDate = sdf.format(c.getTime());
			
			Utils.log("Current Date", ""+strDate);
		String stored_date="",flag="";
		
	try{
			/*
			Cursor cur = databaseHandler.readingEzetap()*/
		Cursor cur = databaseHandler.readingEzetap();
		
				if (cur != null) {
				    if (cur.moveToFirst()) {
				        do {
				        	Utils.log("First "," record");
				            //Date.add(cur.getString(cur.getColumnIndex("Title"))); // "Title" is the field name(column) of the Table
				        	stored_date = cur.getString(cur.getColumnIndex(DatabaseHandler.CU_DATE));
				        	flag=cur.getString(cur.getColumnIndex(DatabaseHandler.FLAG));
				        	Utils.log("Stored Date","is:"+stored_date);
				        	Utils.log("Flag","is:"+flag );
				        } while (cur.moveToNext());
			
				      
				    }
				}
				else{
					databaseHandler.close();
				}
	
				Utils.log("StroredDate", "Stored Date:"+stored_date);
				
				
	}catch (Exception e) {
		Utils.log("Error", "Error:"+e);
	
	}
			
	if(sharedPreferences.getBoolean("user_card_allow", true)){
			
		if (stored_date != null) {

			if (stored_date.length() > 0) {
				
				if (strDate.equalsIgnoreCase(stored_date)) {

					Utils.log("Data matched", "dataMatched Executed");
					if(flag.equalsIgnoreCase("1")){
						
					}
					else{
						Utils.log("Eztap","executed");
						Utils.log("Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);
						
						EzetapPayApis.create(CardActivity.API_CONFIG)
								.initializeDevice(SearchVendorActivity.this,
										AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());

						EzetapPayApis.create(CardActivity.API_CONFIG)
								.checkForUpdate(SearchVendorActivity.this,
										AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());
						databaseHandler.updateEzetap(strDate);
						Utils.log("UpDate Data",""+databaseHandler);
						Utils.log("Updatedate 1 st time", "1 st Executed");
					}
				} else {
					
					Utils.log("Eztap","executed");
					Utils.log("else update Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);
					
					EzetapPayApis.create(CardActivity.API_CONFIG)
							.initializeDevice(SearchVendorActivity.this,
									AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());

					EzetapPayApis.create(CardActivity.API_CONFIG)
							.checkForUpdate(SearchVendorActivity.this,
									AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());
					databaseHandler.updateEzetap(strDate);
					Utils.log("UpDate Data",""+databaseHandler);
					Utils.log("Updatedate 1 st time", "1 st Executed");
				}
			} else {
				
				Utils.log("Eztap","executed");
				Utils.log("Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);

				EzetapPayApis.create(CardActivity.API_CONFIG).initializeDevice(
						SearchVendorActivity.this,
						AppConstants.REQ_CODE_PAY_CARD,utils.getMobLoginId());

				EzetapPayApis.create(CardActivity.API_CONFIG).checkForUpdate(
						SearchVendorActivity.this,
						AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());
				//sqdata.update(strDate, null,null, null);
				databaseHandler.addEzetap(strDate);  // showing Error Null pointer Execption
				Utils.log("Updated 2nd ", "Updated 2 time");
				
			}

		} else {
			Utils.log("Eztap","executed");
			Utils.log("Inseret Date", "InsertingDate:"+databaseHandler+strDate+stored_date);
			 
			EzetapPayApis.create(CardActivity.API_CONFIG).initializeDevice(
					SearchVendorActivity.this, AppConstants.REQ_CODE_PAY_CARD,
					utils.getMobLoginId());
			

			EzetapPayApis.create(CardActivity.API_CONFIG).checkForUpdate(
					SearchVendorActivity.this, AppConstants.REQ_CODE_PAY_CARD,
					utils.getMobLoginId());
			databaseHandler.addEzetap(strDate);
          Utils.log("Inseret Date", "InsertingDate:"+databaseHandler+strDate+stored_date);
			
		}
	}


//		LocalBroadcastManager.getInstance(this).registerReceiver(
//				new BroadcastReceiver() {
//					@Override
//					public void onReceive(Context context, Intent intent) {
//						String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
//						String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
//
//						if (latitude != null && longitude != null) {
//							//Log.e("latitude Search",":"+latitude);
//							//Log.e("longitude Search",":"+longitude);
//						}
//					}
//				}, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
//		);

	}

	
	/***********************ONCreate end here	*******************************************/
	private void ParseJsonResponse(String s) {
		// TODO Auto-generated method stub
	try{
			
			JSONObject main = new JSONObject(s);
			JSONObject newJsonData = main.getJSONObject("NewDataSet");
			
			if(newJsonData.has("UserDetails")){
				
				
				if(newJsonData.get("UserDetails") instanceof JSONObject){
					JSONObject userJsonObject = newJsonData.getJSONObject("UserDetails");
					
					if(newJsonData.has("NoData")){
						String NoData = userJsonObject.getString("NoData");

					}
					else{
						
						String Emp_code ,name,Company_name,Department_name,Designation_name,D_o_j , D_o_B ,Address,Email_id,Userphoto,  Company_logo;
					
						
				
							
						
							if(userJsonObject.has("Photo")){
								Userphoto = userJsonObject.getString("Photo");
								
								byte[] image =  Base64.decode(Userphoto, Base64.DEFAULT);
								//ivPhoto = (CircularImageView)findViewById(R.id.ivPhoto);
								IdImage = (ImageView)findViewById(R.id.image);
								Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
								IdImage.setImageBitmap(bm);
								
								TextView idtext = (TextView)findViewById(R.id.textshowbtn);
							
								idtext.setVisibility(View.GONE);
								
							}
							
						}
					}
					
				}
			
				
			
			
		}catch(Exception e){
			
			Utils.log("Error :",""+e);
		}
		
	}

	OnClickListener searchOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			if (TextUtils.isEmpty(searchTxt.getText().toString().trim())) {
				AlertsBoxFactory.showAlert(" Please enter valid data.",context );
				
				return;
			} else {
				searchBtn.setClickable(false);
				getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.execute((String) null);
				//new GetMemberDetailWebService().execute();
			}
		}
	};
	
	OnClickListener todayCollectClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			getTodaysCollectionWebService = new GetTodaysCollectionWebService();
			getTodaysCollectionWebService.execute((String) null);
			
		}
	};
	
	OnClickListener resetOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			searchTxt.setText("");
		}
	};
	private Toast toast;
	private long lastBackPressTime = 0;

	@Override
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
			toast = Toast.makeText(this, "Press back again to close this app",
					4000);
			toast.show();
			this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * Get memeber details WS
	 * 
	 */
	private class GetMemberDetailWebService extends
			AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				SearchVendorActivity.this);
		String AppVersion= Utils.GetAppVersion(SearchVendorActivity.this);
		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			Dialog.setCancelable(false);
			Utils.log("ist Dialog", "ist one");
			
			Message=null;
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			searchBtn.setClickable(true);
			Dialog.dismiss();
			Utils.log("Dismiss Dialog", "ist one dismiss");
			
	 		try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapMemberData != null) {
					Set<String> keys = mapMemberData.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					String selItem = str_keyVal.trim();
					
					isLogout = false;
					
					//finish();
					Intent intent = new Intent(SearchVendorActivity.this,
							ShowDetailsActivity.class);
					MemberData memberData = mapMemberData.get(selItem);
					
					Bundle bundle = new Bundle();
					bundle= BundleHelper.getMemberDataBundel(bundle, memberData);
					//Utils.log("Additional AMount",":"+memberData.getAdditionAmountDetails());
					bundle.putString("username",username);
					bundle.putString("password",password);
					bundle.putString("selItem", selItem);
					
					bundle.putString("message", Message);
					
					intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
					intent.putExtra("com.cnergee.billing.showdetails.screen.INTENT", bundle);
					startActivity(intent);
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){
                AlertsBoxFactory.showAlert(rslt,context );}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {
				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				Authobj.setAppVersion(AppVersion);
				SearchMemberCaller memberCaller = new SearchMemberCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_MEMBER_DETAILS),Authobj);
				Log.e("Authobj","Mobile Number:"+utils.getMobileNumber()+"MobLoginId:"+utils.getMobLoginId()+"MobUserPass:"+utils.getMobUserPass()+"IMEINo:"+utils.getIMEINo()+"CliectAccessId:"+utils.getCliectAccessId()+":"+utils.getMacAddress()+"PhoneUniqueId:"+utils.getPhoneUniqueId()+"Appversion:"+AppVersion);
				memberCaller.username = username;
				memberCaller.password = password;
				memberCaller.searchTxt = searchTxt.getText().toString().trim();
				memberCaller.execteFrom = "search";
				
				memberCaller.join();
				memberCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		 @Override
			protected void onCancelled() {
				Dialog.dismiss();
				Utils.log("onCalcelled Dismiss Dialog", "Diss on Calcelled one");
				
				searchBtn.setClickable(true);
				getMemberDetailWebService = null;
			}
	}
	
	
	/*
	 * 
	 */
	
	private class GetTodaysCollectionWebService extends
	AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(SearchVendorActivity.this);

		
		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			Dialog.setCancelable(false);
			Utils.log("2nd Progress bar dialog", "2nd one");
			
		}
		
		protected void onPostExecute(Void unused) {
			getTodaysCollectionWebService = null;
			collectBtn.setClickable(true);
			Dialog.dismiss();
			Utils.log("Dismiss Dialog", "2nd Dissmiss one");

			Utils.log("Dismiss Dialog", "2nd Dissmiss one"+rslt);
			try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
					AlertsBoxFactory.showAlert("Today's Collection",todayCollection,context );
					
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					AlertsBoxFactory.showAlert("No Data Found !!! ",context );
				}else{
					AlertsBoxFactory.showAlert(rslt,context );
				}
			}catch(Exception e){
                AlertsBoxFactory.showAlert(rslt,context );}
				
		}
		
		@Override
		protected Void doInBackground(String... arg0) {
			try {
				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				TodaysCollectionCaller tcCaller = new TodaysCollectionCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_TODAY_COLLECTION),Authobj);
				tcCaller.setUserLoginName(username);
				tcCaller.join();
				tcCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;	
		}
		
		 @Override
			protected void onCancelled() {
				Dialog.dismiss();
				Utils.log("OnCancelled Dialog", "dilog dismiss one");
				
				collectBtn.setClickable(true);
				getTodaysCollectionWebService = null;
			}
		
	}

	private class GetPaymentRequestWebService extends
	AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(SearchVendorActivity.this);
		
		
		protected void onPreExecute() {
		Dialog.setMessage("Please Wait..");
		Dialog.show();
		Dialog.setCancelable(false);
			Utils.log("3rd Progress bar", "3rd one");
			is_paymentpickup_running=true;
		}
		
		
		
		protected void onPostExecute(Void unused) {
			getPaymentRequestWebService = null;
			 collectBtn.setClickable(true);
			Dialog.dismiss();
//			  Intent finishIntent = new Intent("show_pickupdata");
//              LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
			is_paymentpickup_running=false;
			Utils.log("dialog 3rd dismiss", "3rd dialog");
			try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
					hdPayCount.setText(payCount);
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					AlertsBoxFactory.showAlert("No Data Found !!! ",context );
				}else{
					//AlertsBoxFactory.showAlert(rslt,context );
				}
			}catch(Exception e){
                AlertsBoxFactory.showAlert(rslt,context );
			}
			
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
		protected Void doInBackground(String... arg0) {
			try {
				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				PaymentRequestCaller paymentReqCaller = new PaymentRequestCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_PAYMENT_REQUEST),Authobj);
				paymentReqCaller.setUserLoginName(username);
				paymentReqCaller.setAllData(true);
				paymentReqCaller.join();
				paymentReqCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;	
		}
		
		 @Override
			protected void onCancelled() {
				Dialog.dismiss();
				Utils.log("OnCancelled  dismiss Dialog", " onCancelled dismiss one");
				
				collectBtn.setClickable(true);
				getPaymentRequestWebService = null;
			}
		
	}

	//*************************Bradcast receiver for GPS**************************starts
		private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		       // Utils.log("Service","Message");
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
		    	finish();
		        
		    }
		};
		//*************************Bradcast receiver for GPS**************************ends
		
		 public  void showGPSDisabledAlertToUser(){
			  alertDialogBuilder  = new AlertDialog.Builder(SearchVendorActivity.this);
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
		 
		 public class GetIdCardAsyncTask extends AsyncTask<String, Void, Void>{
			 ProgressDialog prgDialog;
			 String GetIdCardResult="";
			 String GetIdCardResponse="";
			 GetIdCardSOAP getIdCardSOAP;
			 String AppVersion="";
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				prgDialog= new ProgressDialog(SearchVendorActivity.this);
				prgDialog.setMessage("Please wait...");
				prgDialog.show();
				Utils.log("4rth Progress bar", "4rth one");
				
				AppVersion= Utils.GetAppVersion(SearchVendorActivity.this);
			}
			 
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				AuthenticationMobile Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				Authobj.setAppVersion(AppVersion);
				getIdCardSOAP= new GetIdCardSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), utils.getDynamic_Url(), getString(R.string.METHOD_GET_ID_CARD));
				GetIdCardResult=getIdCardSOAP.getIdCardDetails(username, Authobj);
				GetIdCardResponse=getIdCardSOAP.getResponse();
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(GetIdCardResult.length()>0){
					if(GetIdCardResult.equalsIgnoreCase("ok")){
						if(GetIdCardResponse.length()>0){
						//	DatabaseHandler  databaseHandler= new DatabaseHandler(SearchVendorActivity.this);
							//databaseHandler.createIdCardTable();
						}
					}
				}
			}
		 }
}
