/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.activity;



import static com.broadbandcollection.billing.utils.Utils.PaymentMode;
import static com.broadbandcollection.billing.utils.Utils.PaymentURL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.R;

import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RenewActivity extends Activity {

	RadioButton cashOption,rb_dd_pg,rb_upi_pg;
	RadioButton chequeOption;
	RadioButton edcOption;

	String username;
	String selItem, AreaCodeFilter, AreaCode;
	String subscriberID, PackageListCode, CurrentPlan, PrimaryMobileNo,
			SecondryMobileNo,IsAutoReceipt,PaymentDate,ConnectionTypeId;
	double PlanRate;
	public static final String backBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.renew.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.search.screen.INTENT";
	String pg_sms_renewal_type="";
	BundleHelper bundleHelper;
	Bundle extras1;
	LocationManager locationManager;
	AlertDialog  alert ;
	AlertDialog.Builder   alertDialogBuilder;
	Double Oustanding_Add_Amt;
	TextView tv_Out_Add_amt,tv_label_out;
	String MemberId="";
	RadioButton rb_sms_pg,rb_time,rb_volume;
	RadioGroup rg_group,rg_time_volume;
	Utils utils = new Utils();
	AuthenticationMobile Authobj;
	String PackageType="",str_PaymentMode = "";
	String advance_type_for_volume_pkg="";
	Double PoolRate=0.0;
	//;
	public static Context context;
	protected void onPause() {
		super.onPause();
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
		}
	/*Log.i(" >>>> "," IN PAUSE ");*/
		finish();
		AppConstants1.APP_OPEN=false;
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_renew);
		BaseApplication.getEventBus().register(this);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		context = this;
		String	 AppVersion= Utils.GetAppVersion(RenewActivity.this);

		Authobj = new AuthenticationMobile();
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);

		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else{
			AppConstants1.GPS_AVAILABLE=false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("GpsStatus"));
		Calendar c = Calendar.getInstance();
		SimpleDateFormat viewDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = viewDateFormatter.format(c.getTime());

		final TextView refTxt = (TextView) findViewById(R.id.payDate);
		refTxt.setText(currentDate);

		rb_dd_pg = findViewById(R.id.rb_dd_pg);
		rb_upi_pg = findViewById(R.id.rb_upi_pg);
		rb_sms_pg=(RadioButton) findViewById(R.id.rb_sms_pg);
		SharedPreferences sharedPreferences12 = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0);
		if(!sharedPreferences12.getBoolean("sms_pg", false)){
			rb_sms_pg.setVisibility(View.GONE);
		}
		else{
			if(Utils.ACTION_TAKE.equalsIgnoreCase("Reactivate")){
				rb_sms_pg.setVisibility(View.GONE);
			}
		}

		extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}

	Utils.log("MemberId","is:"+bundleHelper.getCurrentExtras().getString("MemberId"));
		username = bundleHelper.getCurrentExtras().getString("username");
		selItem = bundleHelper.getCurrentExtras().getString("selItem");
		MemberId= bundleHelper.getCurrentExtras().getString("MemberId");
		subscriberID = bundleHelper.getCurrentExtras().getString("subscriberID");
		PlanRate = bundleHelper.getCurrentExtras().getDouble("PlanRate");
		PackageListCode = bundleHelper.getCurrentExtras().getString("PackageListCode");
		CurrentPlan = bundleHelper.getCurrentExtras().getString("CurrentPlan");
		PrimaryMobileNo = bundleHelper.getCurrentExtras().getString("PrimaryMobileNo");
		SecondryMobileNo = bundleHelper.getCurrentExtras().getString("SecondryMobileNo");
		AreaCode = bundleHelper.getCurrentExtras().getString("AreaCode");
		AreaCodeFilter = bundleHelper.getCurrentExtras().getString("AreaCodeFilter");
		IsAutoReceipt = bundleHelper.getCurrentExtras().getString("IsAutoReceipt");
		PaymentDate = bundleHelper.getCurrentExtras().getString("PaymentDate");
		ConnectionTypeId= bundleHelper.getCurrentExtras().getString("ConnectionTypeId");
		Oustanding_Add_Amt=this.getIntent().getDoubleExtra("Oustanding_Add_Amt", 0.0);
		PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);
		PackageType= bundleHelper.getCurrentExtras().getString("PackageType");
		Utils.log("Outstanding Add Amount","is:"+Oustanding_Add_Amt);


		cashOption = (RadioButton) findViewById(R.id.radioCash);
		chequeOption = (RadioButton) findViewById(R.id.radioCheque);
		edcOption = (RadioButton) findViewById(R.id.radioEDC);

		rg_group=(RadioGroup)findViewById(R.id.radioPayMode);

		rg_time_volume=(RadioGroup) findViewById(R.id.rg_time_volume);

		rb_time=(RadioButton) findViewById(R.id.rb_time);
		rb_volume=(RadioButton) findViewById(R.id.rb_volume);


		if(PackageType!=null &&PackageType.length()>0){

			if(PackageType.equalsIgnoreCase("Volume")){
				rg_time_volume.setVisibility(View.VISIBLE);
			}
			else{
				rg_time_volume.setVisibility(View.GONE);
			}

		}
		else{
			rg_time_volume.setVisibility(View.GONE);
		}

		/*rg_group.removeAllViews();

		rg_group.addView(cashOption);
		rg_group.addView(chequeOption);
		rg_group.addView(edcOption);
		rg_group.addView(rb_sms_pg);*/

		tv_Out_Add_amt = (TextView) findViewById(R.id.tv_Out_Add_amt);
		tv_label_out= (TextView) findViewById(R.id.tv_Label_Out_Add_amt);

		tv_Out_Add_amt.setText(""+Oustanding_Add_Amt);

		cashOption.setOnClickListener(paymentTypeOptionOnClickListener);
		chequeOption.setOnClickListener(paymentTypeOptionOnClickListener);
		edcOption.setOnClickListener(paymentTypeOptionOnClickListener);
		rb_dd_pg.setOnClickListener(paymentTypeOptionOnClickListener);
		rb_upi_pg.setOnClickListener(paymentTypeOptionOnClickListener);
		// cashOption.setChecked(true);

		if(! Utils.ACCEPT_CHEQUE){
			chequeOption.setVisibility(View.GONE);
		}

		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				/*Intent intent = new Intent(RenewActivity.this,
						ShowDetailsActivity.class);
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				intent.putExtra(backBundelPackage1, extras1);
				//intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				startActivity(intent);*/
			}
		});

		rb_sms_pg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){

					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences("CNERGEE", 0);
					final EditText et_mob_number=new EditText(RenewActivity.this);
					if(PrimaryMobileNo!=null)
					et_mob_number.setText(PrimaryMobileNo);
					if(sharedPreferences.getBoolean("sms_pg", false)){
						alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
						alertDialogBuilder.setTitle("Confirmation");
				        alertDialogBuilder.setMessage("Please Confirm Mobile Number")


				        .setCancelable(false)

				        .setPositiveButton("YES",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				            	/*rb_sms_pg.setChecked(false);
				            	if(et_mob_number.length()>0&&et_mob_number.length()==10)
				            		send_sms_pg_request(et_mob_number.getText().toString());
				            	else
				            		Toast.makeText(RenewActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();*/
				            	//new Send_PG_SMS_AsyncTask().execute();
				            }
				        })
				        .setNegativeButton("NO",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				            	rb_sms_pg.setChecked(false);
				            }
				        });
				        alertDialogBuilder.setView(et_mob_number);
				        alert  = alertDialogBuilder.create();
				        alert.show();

				        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener()
				        {
				            @Override
				            public void onClick(View v)
				            {

				                //Do stuff, possibly set wantToCloseDialog to true then...
				            	rb_sms_pg.setChecked(false);
				            	if(et_mob_number.length()>0&&et_mob_number.length()==10){
				            		alert.dismiss();
				            		send_sms_pg_request(et_mob_number.getText().toString());
				            	}
				            	else
				            		Toast.makeText(RenewActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
				                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
				            }
				        });

					}
					else{
						alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
				        alertDialogBuilder.setMessage("Payment can not be made using this option.")
				        .setCancelable(false)
				        .setPositiveButton("OK",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){

				            }
				        });

				        alert  = alertDialogBuilder.create();
				        alert.show();
					}
				}
				else{

				}
			}
		});

		rb_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					advance_type_for_volume_pkg="dt";
				}
			}
		});

		rb_volume.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					advance_type_for_volume_pkg="v";
				}
			}
		});
	}

	OnClickListener paymentTypeOptionOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (chequeOption.isChecked()) {
				str_PaymentMode = "CQ";
				if(is_package_type_valid()){
				finish();
				Intent intent = new Intent(RenewActivity.this,
						ChequeActivity.class);

				  Intent finishIntent = new Intent("finish_showdetails");
	                LocalBroadcastManager.getInstance(RenewActivity.this).sendBroadcast(finishIntent);
				Bundle bundle = new Bundle();

				bundle.putString("username", "" + username);
				bundle.putString("selItem", "" + selItem);
				bundle.putString("MemberId",  MemberId);
				bundle.putString("subscriberID", subscriberID);
				bundle.putDouble("PlanRate", PlanRate);
				bundle.putString("PackageListCode", PackageListCode);
				bundle.putString("CurrentPlan", CurrentPlan);
				bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
				bundle.putString("SecondryMobileNo", SecondryMobileNo);
				bundle.putString("AreaCode", AreaCode);
				bundle.putString("AreaCodeFilter", AreaCodeFilter);
				bundle.putBoolean("isPlanchange", false);
				bundle.putString("IsAutoReceipt", IsAutoReceipt);
				bundle.putString("backAction", "R");
				bundle.putString("PaymentDate", PaymentDate);
				bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

					intent.putExtra(PaymentURL, getApplicationContext().getResources().getString(R.string.METHOD_SEND_CHEQUE_MEMBER_DATA));
					intent.putExtra(PaymentMode, str_PaymentMode);
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
				intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
				intent.putExtra(backBundelPackage1, extras1);
				intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
				intent.putExtra("PoolRate", PoolRate);

				startActivity(intent);
				}

			} else if (edcOption.isChecked()) {
				if(is_package_type_valid()){
				SharedPreferences sharedPreferences = getApplicationContext()
						.getSharedPreferences("CNERGEE", 0);
				if(sharedPreferences.getBoolean("creditcard", true)){

					if(sharedPreferences.getBoolean("user_card_allow", true)){
						finish();
						Intent intent = new Intent(RenewActivity.this,
								CardActivity.class);
						 Intent finishIntent = new Intent("finish_showdetails");
			                LocalBroadcastManager.getInstance(RenewActivity.this).sendBroadcast(finishIntent);
						Bundle bundle = new Bundle();
						bundle.putString("username", "" + username);
						intent.putExtra("selItem", "" + selItem);
						bundle.putString("MemberId",  MemberId);
						bundle.putString("subscriberID", subscriberID);
						bundle.putDouble("PlanRate", PlanRate);
						bundle.putString("PackageListCode", PackageListCode);
						bundle.putString("CurrentPlan", CurrentPlan);
						bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
						bundle.putString("SecondryMobileNo", SecondryMobileNo);
						bundle.putString("AreaCode", AreaCode);
						bundle.putString("AreaCodeFilter", AreaCodeFilter);
						bundle.putBoolean("isPlanchange", false);
						bundle.putString("IsAutoReceipt", IsAutoReceipt);
						bundle.putString("backAction", "R");
						bundle.putString("PaymentDate", PaymentDate);
						bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);


						intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
						intent.putExtra("com.cnergee.billing.card.screen.INTENT", bundle);
						intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
						intent.putExtra(backBundelPackage1, extras1);
						intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
						intent.putExtra("PoolRate", PoolRate);
						startActivity(intent);
					}
					else{
						 alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
					        alertDialogBuilder.setMessage("Payment can not be made using this option.")
					        .setCancelable(false)
					        .setPositiveButton("OK",
					                new DialogInterface.OnClickListener(){
					            public void onClick(DialogInterface dialog, int id){

					            }
					        });

					        alert  = alertDialogBuilder.create();
					        alert.show();
					}
				}
				else{
					 alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
				        alertDialogBuilder.setMessage("Payment can not be made using this option.")
				        .setCancelable(false)
				        .setPositiveButton("OK",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){

				            }
				        });

				        alert  = alertDialogBuilder.create();
				        alert.show();
				}
				}

			} else if (cashOption.isChecked()) {
					if(is_package_type_valid()){
						finish();
						Intent intent = new Intent(RenewActivity.this,
								CashActivity.class);

						 Intent finishIntent = new Intent("finish_showdetails");
			                LocalBroadcastManager.getInstance(RenewActivity.this).sendBroadcast(finishIntent);
						Bundle bundle = new Bundle();
						bundle.putString("username", "" + username);
						bundle.putString("selItem", "" + selItem);
						bundle.putString("MemberId",  MemberId);
						bundle.putString("subscriberID", subscriberID);
						bundle.putDouble("PlanRate", PlanRate);
						bundle.putString("PackageListCode", PackageListCode);
						bundle.putString("CurrentPlan", CurrentPlan);
						bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
						bundle.putString("SecondryMobileNo", SecondryMobileNo);
						bundle.putString("AreaCode", AreaCode);
						bundle.putString("AreaCodeFilter", AreaCodeFilter);
						bundle.putBoolean("isPlanchange", false);
						bundle.putString("IsAutoReceipt", IsAutoReceipt);
						bundle.putString("backAction", "R");
						bundle.putString("PaymentDate", PaymentDate);
						bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

						intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
						intent.putExtra("com.cnergee.billing.cash.screen.INTENT", bundle);
						intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
						intent.putExtra(backBundelPackage1, extras1);
						intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
						intent.putExtra("PoolRate", PoolRate);
						startActivity(intent);
					}

			}
			else if (rb_dd_pg.isChecked()) {
				str_PaymentMode = "DD";
				if(is_package_type_valid()){
					finish();
						Intent intent = new Intent(RenewActivity.this,
								ChequeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("username", "" + username);
						bundle.putString("selItem", "" + selItem);
						bundle.putString("MemberId",  MemberId);
						bundle.putString("subscriberID", subscriberID);
						bundle.putDouble("PlanRate", PlanRate);
						bundle.putString("PackageListCode", PackageListCode);
						bundle.putString("CurrentPlan", CurrentPlan);
						bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
						bundle.putString("SecondryMobileNo", SecondryMobileNo);
						bundle.putString("AreaCode", AreaCode);
						bundle.putString("AreaCodeFilter", AreaCodeFilter);
						bundle.putBoolean("isPlanchange", false);
						bundle.putString("IsAutoReceipt", IsAutoReceipt);
						bundle.putString("backAction", "R");
						bundle.putString("PaymentDate", PaymentDate);
						bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

						intent.putExtra(PaymentMode, str_PaymentMode);
						intent.putExtra(PaymentURL,"SendMemberDataCheque");
						intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
						intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
						intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
						intent.putExtra(backBundelPackage1, extras1);
						intent.putExtra("ConnectionTypeId", ConnectionTypeId);
						intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
						intent.putExtra("PoolRate", PoolRate);
						Utils.log(""+this.getClass().getSimpleName(),""+Oustanding_Add_Amt);


						startActivity(intent);
					}
					else{
						AlertsBoxFactory.showAlert("Package price is zero.",context );
						return;
					}

			}
			else if (rb_upi_pg.isChecked()) {
				str_PaymentMode = "UPI";
				if (is_package_type_valid()) {

						Intent finishIntent = new Intent("finish_showdetails");
						LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);

						finish();

						Intent intent = new Intent(RenewActivity.this,
								ChequeActivity.class);

						Bundle bundle = new Bundle();
						bundle.putString("username", "" + username);
						bundle.putString("selItem", "" + selItem);
						bundle.putString("MemberId",  MemberId);
						bundle.putString("subscriberID", subscriberID);
						bundle.putDouble("PlanRate", PlanRate);
						bundle.putString("PackageListCode", PackageListCode);
						bundle.putString("CurrentPlan", CurrentPlan);
						bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
						bundle.putString("SecondryMobileNo", SecondryMobileNo);
						bundle.putString("AreaCode", AreaCode);
						bundle.putString("AreaCodeFilter", AreaCodeFilter);
						bundle.putBoolean("isPlanchange", false);
						bundle.putString("IsAutoReceipt", IsAutoReceipt);
						bundle.putString("backAction", "R");
						bundle.putString("PaymentDate", PaymentDate);
						bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

						intent.putExtra(PaymentMode, str_PaymentMode);
						intent.putExtra(PaymentURL,"SendMemberDataUPI");
						intent.putExtra(currentBundelPackage, bundleHelper.getCurrentExtras());
						intent.putExtra(backBundelPackage, bundleHelper.getBackExtras());
						intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
						intent.putExtra(backBundelPackage1, extras1);
						intent.putExtra("ConnectionTypeId", ConnectionTypeId);
						intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
						intent.putExtra("PoolRate", PoolRate);
						Utils.log("" + this.getClass().getSimpleName(), "" + Oustanding_Add_Amt);


						startActivity(intent);
					} else {
						AlertsBoxFactory.showAlert("Package price is zero.", context);
						return;
					}
				}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_renew, menu);
		return true;
	}
	@Override
	 public void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

	   }
	//*************************Bradcast receiver for GPS**************************starts
			private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			        //Utils.log("Service","Message");
			        //  ... react to local broadcast message
			        if(AppConstants1.GPS_AVAILABLE){
			        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			    	{
			    		if(alert!=null)
			    			alert.dismiss();
			    	}
			    	else{
			    		// showGPSDisabledAlertToUser();
			    		 }
			        }

			    }
			};
	//*************************Bradcast receiver for GPS**************************ends

			 public  void showGPSDisabledAlertToUser(){
				 alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
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

			 public void send_sms_pg_request(String MobileNumber){

					CommonSOAP commonSOAP= new CommonSOAP(
							utils.getDynamic_Url(),
			getApplicationContext().getResources().getString(
											R.string.WSDL_TARGET_NAMESPACE), getApplicationContext()
									.getResources().getString(
											R.string.METHOD_SEND_PG_SMS));

					SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_SEND_PG_SMS));

					PropertyInfo pi = new PropertyInfo();
					pi.setName("UserLoginName");
					pi.setValue(utils.getAppUserName());
					pi.setType(String.class);
					request.addProperty(pi);


					pi = new PropertyInfo();
					pi.setName(AuthenticationMobile.AuthName);
					pi.setValue(Authobj);
					pi.setType(Authobj.getClass());
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("MemberId");
					pi.setValue(MemberId);
					pi.setType(String.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("MemberLoginId");
					pi.setValue(subscriberID);
					pi.setType(String.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("MobileNumber");
					pi.setValue(MobileNumber);
					pi.setType(String.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("PackageName");
					pi.setValue(CurrentPlan);
					pi.setType(String.class);
					request.addProperty(pi);

					pg_sms_renewal_type="R";
					pi = new PropertyInfo();
					pi.setName("RenewalType");
					pi.setValue(pg_sms_renewal_type);
					pi.setType(String.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("Amount");
					pi.setValue(String.valueOf(PlanRate));
					pi.setType(String.class);
					request.addProperty(pi);

					commonSOAP.setRequest(request);

					final ProgressDialog prgDialog= new ProgressDialog(RenewActivity.this);
					prgDialog.setMessage("Please wait..");
					prgDialog.setCancelable(false);
					prgDialog.show();
					new CommonAsyncTask(RenewActivity.this){


						@Override
						protected void onPostExecute(ArrayList<String> result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							prgDialog.dismiss();
							if(result.get(0).equalsIgnoreCase("OK")){
								String response_sms=result.get(1);
								if(response_sms.length()>0){
									if(response_sms.contains("#")){
										String[] res_array=response_sms.split("#");
										if(res_array.length>0){
											if(res_array[1].equalsIgnoreCase("1")){

												alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
										        alertDialogBuilder.setMessage(res_array[0])
										        .setCancelable(false)
										        .setPositiveButton("OK",
										                new DialogInterface.OnClickListener(){
										            public void onClick(DialogInterface dialog, int id){
										            	RenewActivity.this.finish();
														BaseApplication.getEventBus().post(new FinishEvent(RenewActivity.class.getSimpleName()));
														BaseApplication.getEventBus().post(new FinishEvent(ShowDetailsActivity.class.getSimpleName()));
										            }
										        });

										        alert  = alertDialogBuilder.create();
										        alert.show();

											}
											else{
												rb_sms_pg.setChecked(false);
												alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
										        alertDialogBuilder.setMessage(res_array[0])
										        .setCancelable(false)
										        .setPositiveButton("OK",
										                new DialogInterface.OnClickListener(){
										            public void onClick(DialogInterface dialog, int id){
										            	RenewActivity.this.finish();
														BaseApplication.getEventBus().post(new FinishEvent(RenewActivity.class.getSimpleName()));
														BaseApplication.getEventBus().post(new FinishEvent(ShowDetailsActivity.class.getSimpleName()));
										            }
										        });
										       
										        alert  = alertDialogBuilder.create();
										        alert.show();
											}
										}
										else{
											rb_sms_pg.setChecked(false);
											alertDialogBuilder  = new AlertDialog.Builder(RenewActivity.this);
									        alertDialogBuilder.setMessage(response_sms)
									        .setCancelable(false)
									        .setPositiveButton("OK",
									                new DialogInterface.OnClickListener(){
									            public void onClick(DialogInterface dialog, int id){
									            	
									            }
									        });
									       
									        alert  = alertDialogBuilder.create();
									        alert.show();
										}
									}
									else{
										rb_sms_pg.setChecked(false);
									}
								}
								else{
									rb_sms_pg.setChecked(false);
								}
							}
							else{
								rb_sms_pg.setChecked(false);
								AlertsBoxFactory.showAlert("Web Service Not Executed", RenewActivity.this);
							}
						}
					}.execute(commonSOAP);
					
				
			 }
			
			 @Subscribe
				public void	onFinishEvent(FinishEvent event){
				
					if(this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
						this.finish();
					}
					
				}
			 
			
			 public boolean is_package_type_valid(){
				 if(PackageType!=null&&PackageType.length()>0){
					 if(PackageType.equalsIgnoreCase("VOLUME")){
						 if(rb_time.isChecked()||rb_volume.isChecked()){
							 return true;
						 }
						 else{
							 AlertsBoxFactory.showAlert("Please select Advance type.", this);
							 return false; 
							
						 }
					 }
					 else{
						 return true; 
					 }
				 }
				 else{
					 return true;
				 }
			 }
}
