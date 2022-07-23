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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.SOAP.CashPaymentCaller;
import com.broadbandcollection.billing.SOAP.ValidateReceiptCaller;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastReceiver;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CashActivity extends Activity {

	public static String rslt = "";
	public static String REF_NO;
	Button submit = null;
	EditText receptNo;
	public static Context context;
	public String currentDate;
	TextView payAmount;
	String username;
	String selItem, updatefrom = "", AreaCode, AreaCodeFilter;
	String subscriberID, PackageListCode, CurrentPlan, PrimaryMobileNo,SecondryMobileNo,IsAutoReceipt,PaymentDate;
	double PlanRate;
	Utils utils = new Utils();
	public static boolean isValid = false;
	boolean isPlanchange;
	boolean isAutoReceipt = false;
	public boolean isConfirm = false;
	public String str_dialog_message="";
	CashWebService cashWebService = null;
	ValidateRecNoWebService validateRecNoWebService = null;
	private AuthenticationMobile Authobj = null;
	
	public static final String backBundelPackage = "com.cnergee.billing.renew.screen.INTENT";
	public static final String backBundelPackage0 = "com.cnergee.billing.changepackage.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.showdetails.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.cash.screen.INTENT";
	public static final String backBundelPackage2 = "com.cnergee.billing.search.screen.INTENT";
	
	BundleHelper bundleHelper;
	Bundle extras0,extras1,extras2;
	String str_action;
	LocationManager locationManager;
	AlertDialog  alert ;
	AlertDialog.Builder   alertDialogBuilder;
	String  ConnectionTypeId="";
	Double Oustanding_Add_Amt;
	Double PoolRate=0.0;
	EditText rtNo,comments;

	
	TextView tv_Out_Add_amt,tv_label_out;
	
	public boolean run_cash=true;
	
	String	advance_type_for_volume_pkg="";
	
	private AuthenticationMobile getAuthenticationMobile(){
		return Authobj;
	}
	private AuthenticationMobile setAuthenticationMobile(){
		
		Authobj = new AuthenticationMobile();
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(Utils.GetAppVersion(CashActivity.this));
		return Authobj;
	}
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		run_cash=true;
		setContentView(R.layout.activity_cash);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
			            mMessageReceiver, new IntentFilter("GpsStatus"));
		context = this;
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
		extras2 = this.getIntent().getBundleExtra(backBundelPackage2);
		extras0 = this.getIntent().getBundleExtra(backBundelPackage0);
		Oustanding_Add_Amt=this.getIntent().getDoubleExtra("Oustanding_Add_Amt", 0.0);
		PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);
		Utils.log(""+this.getClass().getSimpleName(),""+Oustanding_Add_Amt);
		Utils.log("PoolRate", ":"+PoolRate);
		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else{
			AppConstants1.GPS_AVAILABLE=false;
			Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);

		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}

		username = bundleHelper.getCurrentExtras().getString("username");
		selItem = bundleHelper.getCurrentExtras().getString("selItem");
		subscriberID = bundleHelper.getCurrentExtras().getString("subscriberID");
		PlanRate = bundleHelper.getCurrentExtras().getDouble("PlanRate");
		PackageListCode = bundleHelper.getCurrentExtras().getString("PackageListCode");
		CurrentPlan = bundleHelper.getCurrentExtras().getString("CurrentPlan");
		PrimaryMobileNo = bundleHelper.getCurrentExtras().getString("PrimaryMobileNo");
		SecondryMobileNo = bundleHelper.getCurrentExtras().getString("SecondryMobileNo");
		updatefrom = bundleHelper.getCurrentExtras().getString("updatefrom");
		AreaCode = bundleHelper.getCurrentExtras().getString("AreaCode");
		AreaCodeFilter = bundleHelper.getCurrentExtras().getString("AreaCodeFilter");
		isPlanchange  = bundleHelper.getCurrentExtras().getBoolean("isPlanchange");
		IsAutoReceipt = bundleHelper.getCurrentExtras().getString("IsAutoReceipt");
		str_action =  bundleHelper.getCurrentExtras().getString("backAction");
		PaymentDate = bundleHelper.getCurrentExtras().getString("PaymentDate");
		//ConnectionTypeId = bundleHelper.getCurrentExtras().getString("ConnectionTypeId");
		ConnectionTypeId=this.getIntent().getStringExtra("ConnectionTypeId");
		advance_type_for_volume_pkg= bundleHelper.getCurrentExtras().getString("advance_type_for_volume_pkg");
		Utils.log("cash activity","from: "+updatefrom);
		Utils.log("advance_type_for_volume_pkg", ":"+advance_type_for_volume_pkg);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat viewDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		currentDate = viewDateFormatter.format(c.getTime());

		final TextView refTxt = (TextView) findViewById(R.id.payDate);
		refTxt.setText(currentDate);

		submit = (Button) findViewById(R.id.submit);
		payAmount = (TextView) findViewById(R.id.payAmount);
		payAmount.setText(Double.toString(PlanRate));
		receptNo = (EditText) findViewById(R.id.receptNo);
		
		tv_Out_Add_amt = (TextView) findViewById(R.id.tv_Out_Add_amt);
		tv_label_out= (TextView) findViewById(R.id.tv_Label_Out_Add_amt);
		
		tv_Out_Add_amt.setText(""+Oustanding_Add_Amt);
		
		
		/*Log.i("SecondryMobileNo ",SecondryMobileNo);*/
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("Payment Mode: <b>Cash</b>");
		sbuf.append("<br>Package Name:<br> <b>"+CurrentPlan+"</b>");
		
		sbuf.append("<br>Amount: <b>"+Double.toString(PlanRate)+"</b>");
		sbuf.append("<br>Mobile No.: <b>"+PrimaryMobileNo+"</b>");
		if(!TextUtils.isEmpty(SecondryMobileNo)){
			sbuf.append("<br>Alternate Mobile No.:<br> <b>"+SecondryMobileNo+"</b>");
		}else{
			sbuf.append("<br>Alternate Mobile No.: -");
		}
		str_dialog_message = sbuf.toString();
		
		
		TextView LabelRec = (TextView)findViewById(R.id.LabelRec);

		 rtNo  =  (EditText)findViewById(R.id.rtNo);
		 comments  = (EditText)findViewById(R.id.comments);

		
		if(IsAutoReceipt.equalsIgnoreCase("true")){
			LabelRec.setVisibility(View.GONE);
			receptNo.setVisibility(View.GONE);
			isAutoReceipt = true;
		}else if(IsAutoReceipt.equalsIgnoreCase("1")){
			LabelRec.setVisibility(View.GONE);
			receptNo.setVisibility(View.GONE);
			isAutoReceipt = true;
		}else{
			LabelRec.setVisibility(View.VISIBLE);
			receptNo.setVisibility(View.VISIBLE);
			isAutoReceipt = false;
		}
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				submit.setClickable(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				
				builder.setMessage(Html.fromHtml("<font color='#20B2AA'>"+str_dialog_message+"</font>"))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(Html.fromHtml("<font color='#20B2AA'><b>Are you sure you want to process?</b></font>"))
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				        	   ((AlertDialog)dialog).getButton(id).setVisibility(View.INVISIBLE);
				        	   if(!isAutoReceipt){
									boolean isOk = true;
									if (TextUtils.isEmpty(receptNo.getText().toString().trim())) {
										isOk = false;
										AlertsBoxFactory.showAlert("Please enter valid receipt no.",context );
										submit.setClickable(true);
										return;
									}
									if (isOk) {
										//new ValidateRecNoWebService().execute();
										validateRecNoWebService = new ValidateRecNoWebService();
										validateRecNoWebService.execute((String)null);
									}
								}else{
									  dialog.cancel();
									cashWebService = new CashWebService();
									cashWebService.execute((String) null);
								}
				        	   Intent i = new Intent(CashActivity.this, AlarmBroadcastReceiver.class);
				        	   if(updatefrom!=null){
									    i.putExtra("activity", "upgrade");
									}
									else{
									    i.putExtra("activity", "renewal");
									}
									//Utils.log("Cheque activity","is: "+updatefrom);
									sendBroadcast(i);
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				                //isConfirm = false;
				                submit.setClickable(true);
				           }
				       });
			
				AlertDialog alert = builder.create();
				alert.show();
				
			}
		});

		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent intent = null;
				
				if(str_action.equals("CP")){
					intent = new Intent(CashActivity.this,
							PackgedetailActivity.class);

				}else{
					intent = new Intent(CashActivity.this,
							RenewActivity.class);
				}
				
				intent.putExtra(backBundelPackage0,extras0 );
				intent.putExtra(backBundelPackage1,extras1 );
				intent.putExtra(backBundelPackage2,extras2 );
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				intent.putExtra("ConnectionTypeId", ConnectionTypeId);
				intent.putExtra("PoolRate", PoolRate);
				
				startActivity(intent);

			}
		});
		setAuthenticationMobile();
	}
	
	@Override
	protected void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
 
	   }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class CashWebService extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(CashActivity.this);
		PaymentObj paymentObj = new PaymentObj();

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			Dialog.setCancelable(false);
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			cashWebService = null;
			submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
			submit.setClickable(true);
			cashWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				run_cash=false;
					finish();
					Intent intent = new Intent(CashActivity.this,PaymentDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("REF_NO", REF_NO);
					bundle.putString("username",username);
					intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
					intent.putExtra(backBundelPackage2,extras2 );
					//intent.putExtra(backBundelPackage,bundleHelper.getBackExtras());
					//intent.putExtra(backBundelPackage1,extras1 );
					intent.putExtra("com.cnergee.billing.payment.screen.INTENT", bundle);
					startActivity(intent);
			} else {
				run_cash=true;
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected synchronized Void doInBackground(String... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(run_cash){
				run_cash=false;
			try {
				paymentObj.setSubscriberID(subscriberID);
				paymentObj.setPlanName(CurrentPlan);
				paymentObj.setPaidAmount(Double.parseDouble(payAmount.getText().toString().trim()));

				Calendar c = Calendar.getInstance();
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"ddMMyyyyHHmmss");
				currentDate = dateFormatter.format(c.getTime());

				paymentObj.setStrPaymentDate(PaymentDate);
				paymentObj.setPaymentMode("CS");
				paymentObj.setStrPaymentModeDate(currentDate);
				paymentObj.setChangePlan(isPlanchange);
				if (updatefrom != null) {
					if (updatefrom.equalsIgnoreCase("I")) {
						paymentObj.setActionType("I");
					} else {
						paymentObj.setActionType("S");
					}
				} else {
					paymentObj.setActionType("S");
				}
				paymentObj.setUserLoginName(username);
				paymentObj.setAltMobileNo(SecondryMobileNo);
				paymentObj.setRecieptNo(receptNo.getText().toString().trim());
				paymentObj.setAdvance_type_for_volume_pkg(advance_type_for_volume_pkg);
				paymentObj.setPoolRate(PoolRate);
				paymentObj.setRtNo(rtNo.getText().toString().length()>0 ? rtNo.getText().toString():"");
				paymentObj.setComment(comments.getText().toString().length()>0 ? comments.getText().toString():"");

				Utils.log("advance_type_for_volume_pkg", ":"+advance_type_for_volume_pkg);
				Utils.log("ACTION_TAKE", ":"+ Utils.ACTION_TAKE);
				CashPaymentCaller caller = null ;
				if(Utils.ACTION_TAKE.equalsIgnoreCase("Renew")){
					caller= new CashPaymentCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_SEND_CASH_MEMBER_DATA),getAuthenticationMobile());
				}
				if(Utils.ACTION_TAKE.equalsIgnoreCase("Reactivate")){
					caller= new CashPaymentCaller(
							getApplicationContext().getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),
									utils.getDynamic_Url(), getApplicationContext()
									.getResources().getString(
											R.string.METHOD_REACTIVATE),getAuthenticationMobile());
				}

				caller.setPaymentObj(paymentObj);
				caller.setUsername(username);
				caller.setOustanding_Add_Amt(Oustanding_Add_Amt);
				caller.join();
				caller.start();
				
				Utils.log("Update From", ":"+paymentObj.getActionType());
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}
			return null;
			
		}
	}

	private class ValidateRecNoWebService extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(CashActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait Validate Receipt No.");
			Dialog.show();
			Dialog.setCancelable(false);
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			validateRecNoWebService = null;
			submit.setClickable(true);
		}
		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
			submit.setClickable(true);
			validateRecNoWebService = null;
			
			//isValid = true;
			if (!isValid) {
				AlertsBoxFactory.showAlert("Invalid Receipt No!!!",context );
				return;
			}
			if (isValid) {
				//new CashWebService().execute();
				cashWebService = new CashWebService();
				cashWebService.execute((String) null);
				
				submit.setClickable(false);
			} else {
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				ValidateReceiptCaller caller = new ValidateReceiptCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), "ValidateReceiptNo",getAuthenticationMobile());

				caller.setUsername(username);
				caller.setAreaCode(AreaCode);
				caller.setAreaCodeFilter(AreaCodeFilter);
				caller.setReceptNo(receptNo.getText().toString().trim());

				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					}catch (Exception ex) {}
				}
			} catch (Exception e) {
				AlertsBoxFactory.showErrorAlert("Error web-service response "+e.toString(),context );
			}
			return null;
		}

	}

	//*************************Bradcast receiver for GPS**************************starts
			private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			      //  Utils.log("Service","Message");
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
				 alertDialogBuilder  = new AlertDialog.Builder(CashActivity.this);
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
