
package com.broadbandcollection.billing.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.CardActivitySoap;
import com.broadbandcollection.billing.SOAP.CardPaymentCaller;
import com.broadbandcollection.billing.SOAP.CreditCardFailedSOAP;
import com.broadbandcollection.billing.SOAP.GetStatusSOAP;
import com.broadbandcollection.billing.SOAP.InsertBeforePGSOAP;
import com.broadbandcollection.billing.SOAP.TransactionIDCaller;
import com.broadbandcollection.billing.SOAP.ValidateCardReceiptCaller;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ezetap.ApConfigConstants;
import com.ezetap.sdk.AppConstants;
import com.ezetap.sdk.EzeConstants;
import com.ezetap.sdk.EzetapApiConfig;
import com.ezetap.sdk.EzetapPayApis;
import com.ezetap.sdk.TransactionDetails;

import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CardActivity extends Activity {

	public static String rslt = "";
	public static String strTrackId = "";

	// public static EzetapApiConfig API_CONFIG = new
	// EzetapApiConfig(AppConstants.authMode, ApConfigConstants.appKey,
	// ApConfigConstants.merchantName, ApConfigConstants.currencyCode,
	// ApConfigConstants.appMode, ApConfigConstants.captureSignature,
	// ApConfigConstants.preferredChannel);
	
	public static EzetapApiConfig API_CONFIG = new EzetapApiConfig(
			ApConfigConstants.authMode, ApConfigConstants.productionAppKey,
			ApConfigConstants.merchantName, ApConfigConstants.currencyCode,
			ApConfigConstants.appMode, ApConfigConstants.captureSignature,
			ApConfigConstants.preferredChannel);
	
	
	
	String ActionType="";

	String username, selItem, updatefrom = "", subscriberID, PackageListCode,
			CurrentPlan, SecondryMobileNo, PrimaryMobileNo, AreaCode,
			AreaCodeFilter, IsAutoReceipt, PaymentDate, RenewalType;
	double PlanRate;
	 String currentDate ;
	private final int REQ_CODE_PAY_CARD = 1001;
	
	private static String DEBUG_TAG = "PaymentInputActivity";

	TextView receptNo , text_amount,text_mobile, order_no;
	
	public static Context context;
	Utils utils = new Utils();
	Button next = null;
	public static boolean isValid = false;        
	private PaymentObj paymentObj = new PaymentObj();
	public static String REF_NO,getUpdateDataString="";;
	boolean isPlanchange;
	boolean isAutoReceipt = false;
	public String str_dialog_message = "";
	TransactionDetails aTxn;
	
	String MobileAlno ;
	CardWebService cardWebService = null;
	ValidateRecNoWebService validateRecNoWebService = null;
	GetTransactionIdWebService getTransactionIdWebService = null;

	

	public static final String backBundelPackage = "com.cnergee.billing.renew.screen.INTENT";
	public static final String backBundelPackage0 = "com.cnergee.billing.changepackage.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.showdetails.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.card.screen.INTENT";
	public static final String backBundelPackage2 = "com.cnergee.billing.search.screen.INTENT";
	BundleHelper bundleHelper;
	Bundle extras0, extras1, extras2;
	String str_action;
	LocationManager locationManager;
	AlertDialog alert;
	AlertDialog.Builder alertDialogBuilder;
	final int REQUEST_CODE = 1;
	String ConnectionTypeId = "";
	String MemberId="";
	
	String AppUserName="";
	Double PoolRate=0.0;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.runFinalizersOnExit(true);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
		/* Log.i(" >>>> "," IN PAUSE "); */
		if (alert != null) {
			if (alert.isShowing()) {
				alert.dismiss();
			}
		}
		// finish();
		AppConstants1.APP_OPEN = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Log.i(" >>>>Card Activity "," IN RESUME ");
		AppConstants1.APP_OPEN = true;
		if (AppConstants1.GPS_AVAILABLE) {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				// Toast.makeText(SearchVendorActivity.this,
				// "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
			}/*
			 * else{ showGPSDisabledAlertToUser(); }
			 */
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		context = this;
		
		
		
		RenewalType = Utils.ACTION_TAKE;
		Utils.log("RenewalType", "is:"+RenewalType);
		if (AppConstants1.hasGPSDevice(this)) {
			AppConstants1.GPS_AVAILABLE = true;
			// Toast.makeText(this, "Gps Available Main",
			// Toast.LENGTH_SHORT).show();
		} else {
			AppConstants1.GPS_AVAILABLE = false;
			// Toast.makeText(this, "Gps Not Available Main",
			// Toast.LENGTH_SHORT).show();
		}
		
		//EzetapPayApis.create(CardActivity.API_CONFIG).initializeDevice(CardActivity.this, AppConstants.REQ_CODE_PAY_CARD, AppUserName);
		//EzetapPayApis.create(CardActivity.API_CONFIG).checkForUpdate(CardActivity.this, AppConstants.REQ_CODE_PAY_CARD, AppUserName);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMessageReceiver, new IntentFilter("GpsStatus"));
		bundleHelper = new BundleHelper(this.getIntent(), backBundelPackage,
				currentBundelPackage);
		extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
		extras2 = this.getIntent().getBundleExtra(backBundelPackage2);
		extras0 = this.getIntent().getBundleExtra(backBundelPackage0);

		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		AppUserName=utils.getAppUserName();
		Utils.log("App Username","is"+AppUserName);
		//EzetapPayApis.create(CardActivity.API_CONFIG).checkForIncompleteTransaction(CardActivity.this, AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());
		
		// Bundle extras = getIntent().getExtras();
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}

		/*
		 * if(!isCRDBRunning()) { Intent LaunchIntent = getPackageManager()
		 * .getLaunchIntentForPackage( "com.mrl.crdb");
		 * LaunchIntent.putExtra("VALUE", "START"); startActivity(LaunchIntent);
		 * }
		 */

		username = bundleHelper.getCurrentExtras().getString("username");
		selItem = bundleHelper.getCurrentExtras().getString("selItem");
		MemberId = bundleHelper.getCurrentExtras().getString("MemberId");
		Utils.log("MemberId", "is:" + MemberId);
		subscriberID = bundleHelper.getCurrentExtras()
				.getString("subscriberID");
		PlanRate = bundleHelper.getCurrentExtras().getDouble("PlanRate");
		PackageListCode = bundleHelper.getCurrentExtras().getString(
				"PackageListCode");
		CurrentPlan = bundleHelper.getCurrentExtras().getString("CurrentPlan");
		PrimaryMobileNo = bundleHelper.getCurrentExtras().getString(
				"PrimaryMobileNo");
		SecondryMobileNo = bundleHelper.getCurrentExtras().getString(
				"SecondryMobileNo");
		updatefrom = bundleHelper.getCurrentExtras().getString("updatefrom");
		AreaCode = bundleHelper.getCurrentExtras().getString("AreaCode");
		AreaCodeFilter = bundleHelper.getCurrentExtras().getString(
				"AreaCodeFilter");
		isPlanchange = bundleHelper.getCurrentExtras().getBoolean(
				"isPlanchange");
		IsAutoReceipt = bundleHelper.getCurrentExtras().getString(
				"IsAutoReceipt");
		str_action = bundleHelper.getCurrentExtras().getString("backAction");
		PaymentDate = bundleHelper.getCurrentExtras().getString("PaymentDate");
		ConnectionTypeId = this.getIntent().getStringExtra("ConnectionTypeId");
		PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);
		Utils.log("SubscriberID activity", "from: " + subscriberID);
		Utils.log("PoolRate", ":"+PoolRate);
		text_amount = (TextView) findViewById(R.id.text_amount);
		text_amount.setText(Double.toString(PlanRate));

		text_mobile = (TextView) findViewById(R.id.text_mobile);
		receptNo = (TextView) findViewById(R.id.receptNo);
		order_no = (TextView) findViewById(R.id.order_no);

		if (updatefrom != null) {
			if (updatefrom.equalsIgnoreCase("I")) {
				ActionType="I";
			} else {
				ActionType="S";
			}
		} else {
			ActionType="S";
		}
		
		Utils.log("Order no",""+order_no);
		TextView LabelRec = (TextView) findViewById(R.id.LabelRec);

		if (IsAutoReceipt.equalsIgnoreCase("true")) {
			LabelRec.setVisibility(View.GONE);
			receptNo.setVisibility(View.GONE);
			isAutoReceipt = true;
		} else if (IsAutoReceipt.equalsIgnoreCase("1")) {
			LabelRec.setVisibility(View.GONE);
			receptNo.setVisibility(View.GONE);
			isAutoReceipt = true;
		} else {
			LabelRec.setVisibility(View.VISIBLE);
			receptNo.setVisibility(View.VISIBLE);
			isAutoReceipt = false;
		}

		if (!SecondryMobileNo.equals("")) {
			text_mobile.setText(SecondryMobileNo);
			Utils.log("Mobile no","Alternate :"+text_mobile.getText().toString());
		} else {
			text_mobile.setText(PrimaryMobileNo);
		}

		next = (Button) findViewById(R.id.next);

		
		
		
		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent intent = null;
				if (str_action.equals("CP")) {
					intent = new Intent(CardActivity.this,
							PackgedetailActivity.class);
				} else {
					intent = new Intent(CardActivity.this, RenewActivity.class);
				}
				intent.putExtra(backBundelPackage0, extras0);
				intent.putExtra(backBundelPackage1, extras1);
				intent.putExtra(backBundelPackage2, extras2);
				intent.putExtra(backBundelPackage, bundleHelper.getBackExtras());
				intent.putExtra("ConnectionTypeId", ConnectionTypeId);
				intent.putExtra("PoolRate",PoolRate);
				startActivity(intent);
			}
		});
		
		 if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
	        	new GetPackageAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
	        }
	        else{
	        	new GetPackageAsyncTask().execute();
	        }

		setAuthenticationMobile();
		getTransactionIdWebService = new GetTransactionIdWebService();
		 if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			 getTransactionIdWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
	        }
	        else{
	        	getTransactionIdWebService.execute((String) null);
	        }

		
		
		//EzetapPayApis.create(CardActivity.API_CONFIG).checkForIncompleteTransaction(CardActivity.this, AppConstants.REQ_CODE_PAY_CARD, utils.getMobLoginId());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_card, menu);
		return true;
	}

	
	private void startCardActivity() {
		/*
		 * final EditText amount = (EditText) findViewById(R.id.text_amount);
		 * final EditText mobileNo = (EditText) findViewById(R.id.text_mobile);
		 */

		// ****************** START TESTING *********************************
		/*
		 * PaymentObj paymentObj = new PaymentObj();
		 * paymentObj.setSubscriberID(subscriberID);
		 * paymentObj.setPlanName(CurrentPlan); paymentObj.setPaidAmount(10);
		 * paymentObj.setUserLoginName(username);
		 * paymentObj.setAltMobileNo(SecondryMobileNo);
		 * 
		 * Calendar c = Calendar.getInstance(); SimpleDateFormat dateFormatter =
		 * new SimpleDateFormat( "ddMMyyyyHHmmss"); String currentDate =
		 * dateFormatter.format(c.getTime());
		 * 
		 * paymentObj.setStrPaymentDate(currentDate);
		 * paymentObj.setPaymentMode("EDC");
		 * 
		 * paymentObj.setStrPaymentModeDate(currentDate);
		 * paymentObj.setBankName(" ASHOK PARMAR ");
		 * paymentObj.setTrackID("21343");
		 * paymentObj.setPaymentID("D1356947747184");
		 * paymentObj.setTransStatus("S"); // Error : E
		 * paymentObj.setTransID("21343"); paymentObj.setTransError("");
		 * 
		 * paymentObj.setRecieptNo(receptNo.getText().toString().trim());
		 * paymentObj.setChangePlan(isPlanchange);
		 * paymentObj.setRecieptNo(receptNo.getText().toString().trim());
		 * 
		 * if (updatefrom != null) { if (updatefrom.equalsIgnoreCase("I")) {
		 * paymentObj.setActionType("I"); } else {
		 * paymentObj.setActionType("S"); } } else {
		 * paymentObj.setActionType("S"); } setPaymentObj(paymentObj); new
		 * CardWebService().execute();
		 */
		// ****************** END TESTING *********************************


		/*  PaymentObj paymentObj = new PaymentObj();

		  
		  if (updatefrom != null) { if (updatefrom.equalsIgnoreCase("I")) {
		  paymentObj.setActionType("I"); } else {
		  paymentObj.setActionType("S");
		  }
		
		  }*/
		try {

			/*
			 * Log.i("#####################", " START ");
			 * Log.i("double amount :", text_amount.getText()+"");
			 * Log.i(" double tip :", "0"); Log.i(" orderNumber:",
			 * order_no.getText()+""); Log.i(" mobile:",
			 * text_mobile.getText()+""); Log.i(" USERNAME:",
			 * AppConstants.EZETAP_USERNAME);
			 * 
			 * Log.i("#####################", "");
			 */

			// new ValidateRecNoWebService().execute();
			/*
			 * if(AppConstants.TIP_ENABLED) { tipAmount =
			 * Double.parseDouble(""+tip.getText()); }
			 */
			/*
			 * double amount, double tip, String mobile, Activity context,
			 * String appKey, String username, String orderNumber, int
			 * reqCode,Hashtable<String, Object> appData, boolean
			 * enableCustomLogin
			 */

			// new Ezetap for CardActivity
			Double pay=0.0;
			if(getString(R.string.app_run_mode).equalsIgnoreCase("dev")){
				pay=1.00;
			}
			else{
				pay=PlanRate;
			}

			EzetapPayApis.create(CardActivity.API_CONFIG).startCardPayment(
					this, AppConstants.REQ_CODE_PAY_CARD,AppUserName,
					pay,
					order_no.getText().toString(), 0,
					text_mobile.getText().toString(),
					receptNo.getText().toString(), null, null, null);
			Utils.log("Mobile no","Alternate :"+text_mobile.getText().toString());
			MobileAlno = text_mobile.getText().toString();
			
			Utils.log("Alternate no new ","is:"+MobileAlno);
			Utils.log("Order No ",""+order_no.getText().toString());
			Utils.log("planRAte ","is:"+PlanRate);
			Utils.log("Action type","is:"+ActionType);
			/*
			 * EzetapUtils utils = new EzetapUtils();
			 * utils.startCardPayment(Double.parseDouble("" +
			 * text_amount.getText()), 0, "" + text_mobile.getText(), this,
			 * null, AppConstants.EZETAP_USERNAME, //
			 * "RNO"+System.currentTimeMillis(),//order Number "" +
			 * order_no.getText(), REQ_CODE_PAY_CARD, null, false);
			 */

			// old Ezetap

			
			 /* EzetapUtils utils = new EzetapUtils();
			  utils.startCardPayment(Double.parseDouble("" +
			  text_amount.getText()), 0, "" + text_mobile.getText(), this,
			  null, AppConstants.EZETAP_USERNAME, //
			  "RNO"+System.currentTimeMillis(),//order Number "" +
			  order_no.getText(), REQ_CODE_PAY_CARD, null, false);
			 */

		} catch (Exception e) {
			e.printStackTrace();
			AlertsBoxFactory
					.showAlert(
							"Not able to call Ezetap utils. " + e.getMessage(),
							context);
		}
	}

	private View view;

	public void handleNextCommand(View aView) {
		view = aView;
		// next.setClickable(false);
		// new ValidateRecNoWebService().execute();

		StringBuffer sbuf = new StringBuffer();
		sbuf.append("Payment Mode: <b>Card</b>");
		sbuf.append("<br>Package Name:<br> <b>" + CurrentPlan + "</b>");
		sbuf.append("<br>Order No: <b>" + order_no.getText() + "</b>");
		sbuf.append("<br>Amount: <b>" + Double.toString(PlanRate) + "</b>");
		sbuf.append("<br>Mobile No.: <b>" + PrimaryMobileNo + "</b>");
		if (!TextUtils.isEmpty(SecondryMobileNo)) {
			sbuf.append("<br>Alternate Mobile No.:<br> <b>" + SecondryMobileNo
					+ "</b>");
		} else {
			sbuf.append("<br>Alternate Mobile No.: -");
		}
		str_dialog_message = sbuf.toString();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(
				Html.fromHtml("<font color='#20B2AA'>" + str_dialog_message
						+ "</font>"))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(
						Html.fromHtml("<font color='#20B2AA'><b>Are you sure you want to process?</b></font>"))
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@TargetApi(Build.VERSION_CODES.HONEYCOMB)
							public void onClick(DialogInterface dialog, int id) {

								((AlertDialog) dialog).getButton(id)
										.setVisibility(View.INVISIBLE);

								if (!isAutoReceipt) {
									// Utils.log("","");
									boolean isOk = true;
									if (TextUtils.isEmpty(receptNo.getText()
											.toString().trim())) {
										isOk = false;
										AlertsBoxFactory
												.showAlert(
														"Please enter valid receipt no.",
														context);
										return;
									}
									if (isOk) {
										validateRecNoWebService = new ValidateRecNoWebService();
										validateRecNoWebService
												.equals((String) null);
									}
								} else {
									if(strTrackId!=null){
									if(strTrackId.length()>0){
									
									if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
										  new InsertBeforePGAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
										}
										else {
											 new InsertBeforePGAsyncTask().execute();
										}
									}
									else{
										AlertsBoxFactory.showAlert("Card Payment can not be use. Please use cash or cheque option.\n Please contact Adminstrator.", CardActivity.this);
									}
									}
									else{
										AlertsBoxFactory.showAlert("Card Payment can not be use. Please use cash or cheque option.\n Please contact Adminstrator.", CardActivity.this);
									}
									
									// startmrlActivity();
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// isConfirm = false;
					}
				});

		AlertDialog alert = builder.create();
		alert.show();

	}
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 Log.v(DEBUG_TAG, "ResultCode=" + resultCode); 

		Log.v(DEBUG_TAG, "ResultCode=" + resultCode);
		String msg = "";
		// EzetapResponse serverResponse = null;

		boolean isError = true;

		PaymentObj paymentObj = new PaymentObj();
		paymentObj.setSubscriberID(subscriberID);
		paymentObj.setPlanName(CurrentPlan);
		paymentObj.setUserLoginName(username);
		paymentObj.setAltMobileNo(SecondryMobileNo);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyyHHmmss");
		String currentDate = dateFormatter.format(c.getTime());

		paymentObj.setStrPaymentDate(currentDate);
		paymentObj.setPaymentMode("EDC");

		paymentObj.setStrPaymentModeDate(PaymentDate);
		paymentObj.setRecieptNo(receptNo.getText().toString().trim());
		paymentObj.setChangePlan(isPlanchange);
		paymentObj.setTrackID(order_no.getText().toString());

		if (updatefrom != null) {
			if (updatefrom.equalsIgnoreCase("I")) {
				paymentObj.setActionType("I");
			} else {
				paymentObj.setActionType("S");
			}
		} else {
			paymentObj.setActionType("S");
		}

		switch (resultCode) {

		case EzeConstants.RESULT_SUCCESS:

			try {
				Utils.log("Result ","Successful");
				Toast.makeText(CardActivity.this, "Successful", Toast.LENGTH_SHORT).show();
				Utils.log("Transaction", "" + DEBUG_TAG);
				TransactionDetails aTxn = TransactionDetails
						.getTransactionDetails(new JSONObject(data
								.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));

				msg = "Auth : " + aTxn.getAuthCode();

				msg += "\nAmount : "
						+ String.format("%.2f", aTxn.getTotalAmount());
				// msg += "\nOrderID:"+aTxn.get;

				msg += "\nTxn :" + aTxn.getTransactionId();

				if (aTxn.isSignatureAttached()) {
					msg += "\nSignature attached.";
				} else {
					msg += "\nSignature not captured.";
				}

				
				 * msg = "Auth : " + aTxn.getAuthCode();
				 * 
				 * msg += "\nAmount : " + String.format("%.2f",
				 * aTxn.getTotalAmount()); // msg += "\nOrderID:"+aTxn.get;
				 * 
				 * msg += "\nTxn :" + aTxn.getTransactionId();
				 * 
				 * if (aTxn.isSignatureAttached()) { msg +=
				 * "\nSignature attached."; } else { msg +=
				 * "\nSignature not captured."; }
				 

				paymentObj.setPaidAmount(aTxn.getTotalAmount());

				paymentObj.setBankName(aTxn.getNameOnCard());

				// paymentObj.setPaymentID(aTxn.getLastFourDigits().toString());
				paymentObj.setPaymentID(aTxn.getAuthCode());
				paymentObj.setTransStatus("S");

				// paymentObj.setTransID(Long.toString(aTxn.getTransactionId()));

				paymentObj.setTransID(aTxn.getTransactionId());
				paymentObj.setTransError("");

				isError = false;

			} catch (Exception e) {
				isError = true;
				 Log.v(DEBUG_TAG, "RESULT_SUCCESS=" + e); 
				e.printStackTrace();
			}
			break;
		case EzeConstants.RESULT_FAILED:
			try {
				Utils.log("Result ","FAILED");
				Toast.makeText(CardActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
				TransactionDetails aTxn = TransactionDetails
						.getTransactionDetails(new JSONObject(data
								.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				paymentObj.setPaidAmount(aTxn.getTotalAmount());

				paymentObj.setBankName(aTxn.getNameOnCard());

				// paymentObj.setPaymentID(aTxn.getLastFourDigits().toString());
				paymentObj.setPaymentID(aTxn.getAuthCode());
				paymentObj.setTransStatus("E");
				paymentObj.setTransID(aTxn.getTransactionId());
				paymentObj.setTransError("E");
				isError = true;

			} catch (Exception e) {
				isError = true;
				 Log.v(DEBUG_TAG, "RESULT_SUCCESS=" + e); 
				e.printStackTrace();
			}
			break;
		default:
			Utils.log("Result ","DEAFULT ERROR");
			Toast.makeText(CardActivity.this, "DEAFULT ERROR", Toast.LENGTH_SHORT).show();
			msg = "Transaction failed.";
			isError = true;
			try {

				JSONObject aJson = new JSONObject(
						data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
				msg = "Error:" + aJson.get(EzeConstants.KEY_ERROR_CODE);
				msg += "\n" + aJson.get(EzeConstants.KEY_ERROR_MESSAGE);
			
				
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Error:\n could not complete transation.";
			}

			break;
		}
		if (msg.length() > 0) {
			AlertsBoxFactory.showAlert(msg, context);
		}
		if (!isError) {
			setPaymentObj(paymentObj);
			// new CardWebService().execute();
			cardWebService = new CardWebService();
			cardWebService.execute((String) null);
		}
	}*/

	
	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

  		Log.v(DEBUG_TAG, "ResultCode=" + resultCode);
  		String msg = "";
  		// EzetapResponse serverResponse = null;
  		switch (resultCode) {

  		case EzeConstants.RESULT_SUCCESS:

  			try {
  			
  				Utils.log("Transaction",""+DEBUG_TAG);
  				 aTxn = TransactionDetails.getTransactionDetails(new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));

  				msg = "Auth : " + aTxn.getAuthCode();

  				msg += "\nAmount : " + String.format("%.2f", aTxn.getTotalAmount());
  				// msg += "\nOrderID:"+aTxn.get;

  				msg += "\nTxn :" + aTxn.getTransactionId();

  				
  				Utils.log("Transaction"," transaction Executed :"+ EzeConstants.KEY_RESPONSE_DATA);
  				
  				Utils.log("Transaction Id"," is:"+ aTxn.getTransactionId());
  				Utils.log("Transaction getAmount"," is:"+ aTxn.getAmount());
  				Utils.log("Transaction getAmountFormatted"," is:"+ aTxn.getAmountFormatted());
  				Utils.log("Transaction getAuthCode"," is:"+ aTxn.getAuthCode());
  				Utils.log("Transaction getBatchNumber"," is:"+ aTxn.getBatchNumber());
  				Utils.log("Transaction getCardType"," is:"+ aTxn.getCardType());
  				Utils.log("Transaction getCustomerMobile"," is:"+ aTxn.getCustomerMobile());
  				Utils.log("Transaction getCustomerReceiptUrl"," is:"+ aTxn.getCustomerReceiptUrl());
  				Utils.log("Transaction getExternalRefNum"," is:"+ aTxn.getExternalRefNum());
  				Utils.log("Transaction getInvoiceNumber"," is:"+ aTxn.getInvoiceNumber());
  				Utils.log("Transaction getMerchantName"," is:"+ aTxn.getMerchantName());
  				Utils.log("Transaction getStatus"," is:"+ aTxn.getStatus());
  				Utils.log("Transaction getReverseReferenceNumber"," is:"+ aTxn.getReverseReferenceNumber());
  				Utils.log("Transaction getTimeStamp"," is:"+ aTxn.getTimeStamp());
  				Utils.log("Transaction getTotalAmount"," is:"+ aTxn.getTotalAmount());
  				Utils.log("Transaction getPaymentMode"," is:"+ aTxn.getPaymentMode());
  				Utils.log("","is:"+ aTxn.getNameOnCard());
  				Utils.log("Status","is:"+ aTxn.getStatus());
  				Utils.log("", ""+aTxn.getAuthCode());
  				Utils.log("Subcriberid",""+subscriberID);
  				Utils.log("member_id",""+MemberId);
  				Utils.log("plan Name",""+PlanRate);
  				Utils.log("payment date",""+PaymentDate);
  				Utils.log("reciptno", ""+receptNo);
  				Utils.log("",""+IsAutoReceipt);
  				Utils.log("", ""+isAutoReceipt);
  				Utils.log("",""+isPlanchange);
  				Utils.log("Action Type","is:"+ActionType);
  			
  				Utils.log("Mobile no","Alternate :"+text_mobile.getText().toString());
  			
  				
  				new AsynctaskCardActivity().execute();
  				
  				if (aTxn.isSignatureAttached()) {
  					msg += "\nSignature attached.";
  				
  					Utils.log("Signature done Sucessfully","Asynctask execute");
  					
  					//new AsynctaskCardActivity().execute();
  					
  					Utils.log("After Sig Async task Executed","CardAsynctask executed");
  				//	Toast.makeText(CardActivity.this, "Successful CardAsynctask Executed", Toast.LENGTH_SHORT).show();
  				} else {
  					msg += "\nSignature not captured.";
  				}

  			} catch (Exception e) {
  				Log.v(DEBUG_TAG, "RESULT_SUCCESS=" + e);
  				e.printStackTrace();
  			}

  			break;

  		case EzeConstants.RESULT_FAILED:
  			Utils.log("Transaction Faile","failed");
  			FailedOrCancelTransactionDialog("failed");
  			
  			break;
  		default:
  			FailedOrCancelTransactionDialog("canceled");
  			
  			/*msg = "Transaction failed.";
  			try {
  				this.finish();
  				JSONObject aJson = new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
  				msg = "Error:" + aJson.get(EzeConstants.KEY_ERROR_CODE);
  				msg += "\n" + aJson.get(EzeConstants.KEY_ERROR_MESSAGE);
  				Utils.log("Error Code","is:"+aJson.get(EzeConstants.KEY_ERROR_CODE));
  				Utils.log("Error Message","is:"+aJson.get(EzeConstants.KEY_ERROR_MESSAGE));
  			
  			} catch (Exception e) {
  				e.printStackTrace();
  				msg = "Error:\n Could not complete transation.";
  			}*/

  			break;
  		}
  		
  		
  	

  		if (msg.length() > 0) {
  			showMsgDialog(msg);
  		}
  	}
  	
	
	
  	public class AsynctaskCardActivity extends AsyncTask<String, Void, Void>{

  		ProgressDialog dialog ;
		CardActivitySoap getCardactivtysoap;
		String cardresult = "";
		String response = "";
  		@Override
  		protected void onPreExecute() {
  			Utils.log("Card Async task Executed","AsyanckTask executed");
  		 dialog = new ProgressDialog(CardActivity.this);
  		dialog.setMessage("Please Wait... Card Activity is running ");
  		dialog.show();
  		dialog.setCancelable(false);
  			super.onPreExecute();
  		}
 
  		
  		
		@Override
		protected Void doInBackground(String... params) {
			
			try{
				

				Utils.log("Authentication Async"," Authentication AsyanckTask");
	  			
				
				AuthenticationMobile AuthObj = new AuthenticationMobile();
				AuthObj.setMobileNumber(utils.getMobileNumber());
				AuthObj.setMobLoginId(utils.getMobLoginId());
				AuthObj.setMobUserPass(utils.getMobUserPass());
				AuthObj.setIMEINo(utils.getIMEINo());
				AuthObj.setCliectAccessId(utils.getCliectAccessId());
				AuthObj.setMacAddress(utils.getMacAddress());
		Utils.log("Username", "is:"+AppUserName);
		Utils.log("Mobile Altno",""+MobileAlno);
		Utils.log("SubcriberId","Subcriberid"+subscriberID);
		Utils.log("Curentplan", "Currentplan"+CurrentPlan+cardresult);
		Utils.log("isPlanChanged","is plan Cahanged"+isPlanchange);
		Utils.log("String Type","Type"+updatefrom);
		Utils.log("Current Date","Cu date"+currentDate);
		Utils.log("String Type", "Type"+updatefrom);
		Utils.log("Amount", "is:"+PlanRate);
			
	//	getCardactivtysoap = new CardActivitySoap(getString(R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getString(R.string.METHOD_SEND_MEMBER_DATEMOBILE_PG));
		
		getCardactivtysoap = new CardActivitySoap(getApplicationContext().getResources().getString(R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext().getResources().getString(R.string.METHOD_SEND_MEMBER_DATEMOBILE_PG));
											
	//	cardresult = getCardactivtysoap.GetCardActvityResult(username,text_mobile.getText().toString(),subscriberID,String.valueOf(CurrentPlan),PlanRate,PaymentDate,"NB",order_no.getText().toString(),PaymentDate,"HDFC",order_no.getText().toString(),"0123445","S",order_no.getText().toString(),"Successful",isPlanchange,ActionType,AuthObj);
		
		
		
	//cardresult = getCardactivtysoap.GetCardActvityResult(username,text_mobile.getText().toString(),subscriberID,String.valueOf(CurrentPlan),PlanRate,PaymentDate,"NB",order_no.getText().toString(),PaymentDate,"HDFC",order_no.getText().toString(),aTxn.getAuthCode(),"S",aTxn.getExternalRefNum(),"Successful",isPlanchange,ActionType,RenewalType,AuthObj);
		cardresult = getCardactivtysoap.GetCardActvityResult(username,text_mobile.getText().toString(),subscriberID,String.valueOf(CurrentPlan),PlanRate,PaymentDate,"NB",aTxn.getExternalRefNum(),PaymentDate,"HDFC",aTxn.getExternalRefNum(),aTxn.getAuthCode(),"S",aTxn.getTransactionId(),"Successful",isPlanchange,ActionType,RenewalType, PoolRate,AuthObj);	
														  //(String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName,  double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType, String RenewalType,AuthenticationMobile AuthObj)				
		response = getCardactivtysoap.getResponse();
		}catch(Exception e){
			Utils.log("Error","is:"+e);
			
		}
			return null;
		
		}
		@Override
		protected void onPostExecute(Void result) {
		dialog.dismiss();
			super.onPostExecute(result);
			if (rslt.trim().equalsIgnoreCase("ok")) {
				
				finish();
				Intent intent = new Intent(CardActivity.this,
						PaymentDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("REF_NO", response);
				bundle.putString("username",username);
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra(backBundelPackage2,extras2 );
				//intent.putExtra(backBundelPackage,bundleHelper.getBackExtras());
				//intent.putExtra(backBundelPackage1,extras1 );
				intent.putExtra("com.cnergee.billing.payment.screen.INTENT", bundle);
				startActivity(intent);
		} else {
			AlertsBoxFactory.showAlert(rslt,context );
			return;
		}
		}
		
		
  	}
  	
  	
  	 
  	
  	
  	
  	
	/*protected void showMsgDialog(int msg) {

		AlertDialog.Builder builder;
		
	 builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		
	    
	  }
			
)});
		Utils.log("Message",""+msg);

		Utils.log("Show Message",""+DEBUG_TAG);
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(msg);
		alertDialog.setButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!bPreAuth)
				finish();
				dialog.dismiss();
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.show();
	}
      */
	protected void showMsgDialog(String msg) {
	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setCancelable(true);
		  builder.setPositiveButton("Ok", new OkOnClickListener());
	  
	      AlertDialog dialog = builder.create();
	      dialog.show();
	  }
	
	private final class OkOnClickListener implements
    DialogInterface.OnClickListener {
  public void onClick(DialogInterface dialog, int which) {
	  
	  
	   /* Toast.makeText(getApplicationContext(), "your Transaction is Cancelled",
	            Toast.LENGTH_LONG).show();*/
	  
	  dialog.dismiss();
  }
	
	
	}
	
	
	private class CardWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(CardActivity.this);
		PaymentObj paymentObj = new PaymentObj();

		protected void onPreExecute() {
			Utils.log("Card","webservice executed");
			Toast.makeText(CardActivity.this, "Card Web Service On pre", Toast.LENGTH_SHORT).show();
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			Dialog.setCancelable(false);
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			cardWebService = null;
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			cardWebService = null;
			Toast.makeText(CardActivity.this, "Card Web Service On POST", Toast.LENGTH_SHORT).show();
			if (rslt.trim().equalsIgnoreCase("ok")) {
				finish();
				Intent intent = new Intent(CardActivity.this,
						PaymentDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("REF_NO", REF_NO);
				bundle.putString("username", username);

				intent.putExtra(currentBundelPackage,
						bundleHelper.getCurrentExtras());
				intent.putExtra(backBundelPackage2, extras2);
				// intent.putExtra(backBundelPackage,bundleHelper.getBackExtras());
				// intent.putExtra(backBundelPackage1,extras1 );
				intent.putExtra("com.cnergee.billing.payment.screen.INTENT",
						bundle);
				startActivity(intent);
			} else {
				AlertsBoxFactory.showAlert(rslt, context);
			}
		}

		@Override
		protected Void doInBackground(String... params) {

			try {

				CardPaymentCaller caller = new CardPaymentCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						utils.getDynamic_Url(),

						getApplicationContext().getResources().getString(
								R.string.METHOD_SEND_MOBILE_MEMBER_DATA),
						getAuthenticationMobile());

				caller.setPaymentObj(getPaymentObj());

				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertsBoxFactory.showAlert(rslt, context);
			}
			return null;
		}

	}

	/**
	 * @return the paymentObj
	 */
	public PaymentObj getPaymentObj() {
		return paymentObj;
	}

	/**
	 * @param paymentObj
	 *            the paymentObj to set
	 */
	public void setPaymentObj(PaymentObj paymentObj) {
		this.paymentObj = paymentObj;
	}

	private class ValidateRecNoWebService extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(CardActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait Validate Receipt No.");
			Dialog.show();
			Dialog.setCancelable(false);
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			validateRecNoWebService = null;
			next.setClickable(true);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			validateRecNoWebService = null;
			next.setClickable(true);

			isValid = true;

			if (isValid) {
				if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
					  new InsertBeforePGAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					else {
						 new InsertBeforePGAsyncTask().execute();
					}
				// startCardActivity(view);
				//startmrlActivity();
				next.setClickable(false);
			} else {
				AlertsBoxFactory.showAlert("Invalid Receipt No!!! ", context);
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				ValidateCardReceiptCaller caller = new ValidateCardReceiptCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						utils.getDynamic_Url(), "ValidateReceiptNo",
						getAuthenticationMobile());

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
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);
			}
			return null;
		}

	}

	private class GetTransactionIdWebService extends
			AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(CardActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..  Geting Transaction Id.");
			Dialog.show();
			Dialog.setCancelable(false);
			strTrackId="";
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			getTransactionIdWebService = null;

		}

		protected void onPostExecute(Void unused) {

			
			
			 
			Dialog.dismiss();
			getTransactionIdWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				if (TextUtils.isEmpty(strTrackId)) {
					Toast.makeText(
							CardActivity.this,
							"Transaction / Order No Not Found. Please Try Again!!!",
							Toast.LENGTH_LONG).show();

				}
				order_no.setText(strTrackId);
				
				
				
				
			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				TransactionIDCaller caller = new TransactionIDCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_TRANSACTION_ID),
						getAuthenticationMobile());
				caller.setUsername(username);
				caller.setMemberId(MemberId);
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);
			}
			return null;
		}

	}

	private AuthenticationMobile Authobj = null;

	private AuthenticationMobile getAuthenticationMobile() {
		return Authobj;
	}

	private AuthenticationMobile setAuthenticationMobile() {

		Authobj = new AuthenticationMobile();
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(Utils.GetAppVersion(CardActivity.this));
		return Authobj;
	}

	// *************************Bradcast receiver for
	// GPS**************************starts
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Utils.log("Service","Message");
			// ... react to local broadcast message
			if (AppConstants1.GPS_AVAILABLE) {
				if (locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					if (alert != null)
						alert.dismiss();
				}
				/*
				 * else{ showGPSDisabledAlertToUser();}
				 */
			}

		}
	};

	// *************************Bradcast receiver for
	// GPS**************************ends

	public void showGPSDisabledAlertToUser() {
		alertDialogBuilder = new AlertDialog.Builder(CardActivity.this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		/*
		 * alertDialogBuilder.setNegativeButton("Cancel", new
		 * DialogInterface.OnClickListener(){ public void
		 * onClick(DialogInterface dialog, int id){ dialog.cancel(); } });
		 */
		alert = alertDialogBuilder.create();
		alert.show();

	}

	public void startmrlActivity() {
		/*
		 * final Intent intent = new Intent(Intent.ACTION_MAIN, null); final
		 * ComponentName cn = new
		 * ComponentName("com.mrl.client","com.mrl.client.CRDBActivity.class");
		 * intent.setComponent(cn); intent.setAction(Intent.ACTION_MAIN);
		 * intent.addCategory(Intent.CATEGORY_LAUNCHER);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //
		 * startActivityForResult(intent); startActivityForResult(intent,
		 * REQUEST_CODE);
		 */
		/*
		 * Utils.log("mrl activity","has started"); Intent intent =
		 * getPackageManager().getLaunchIntentForPackage("com.mrl.client");
		 * startActivityForResult(intent, REQUEST_CODE);
		 */
		// if(appInstalledOrNot("com.mrl.crdb")){
		// String
		// stReq="<mrlpayreq><username>demodemo2app</username><password>123456</password><action>LOGIN</action></mrlpayreq>";
		String stReq = "<mrlpayreq><amount>" + text_amount.getText().toString()
				+ "</amount><mobile>" + "" + "</mobile><email>" + ""
				+ "</email><action>SALE</action></mrlpayreq>";
		broadcastIntent(stReq);
		// }
		/*
		 * else{ alertDialogBuilder = new
		 * AlertDialog.Builder(CardActivity.this);
		 * alertDialogBuilder.setMessage(
		 * "We are unable to make payment by Credit card") .setCancelable(false)
		 * .setPositiveButton("OK", new DialogInterface.OnClickListener(){
		 * public void onClick(DialogInterface dialog, int id){
		 * 
		 * } }); }
		 */
	}

	public void broadcastIntent(String strVal) {
		Intent intent = new Intent();
		intent.setAction("com.mrl.crdb.MAIN");
		intent.putExtra("VALUE", strVal);
		sendBroadcast(intent);
	}

	boolean isCRDBRunning() {
		Context context = this.getApplicationContext();
		ActivityManager mgr = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);

		// ConfigurationInfo config = mgr.getDeviceConfigurationInfo();
		List<RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();

		for (int i = 0; i < processes.size(); i++) {
			if (processes.get(i).processName.equals("com.mrl.crdb")) {
				return true;

			}

		}
		return false;
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public void setValue(){
		
		  PaymentObj paymentObj = new PaymentObj();
		  paymentObj.setSubscriberID(subscriberID);
		  paymentObj.setPlanName(CurrentPlan); 
		  paymentObj.setPaidAmount(PlanRate);
		  paymentObj.setUserLoginName(username);
		  paymentObj.setAltMobileNo(SecondryMobileNo);
		 
		 Calendar c = Calendar.getInstance(); SimpleDateFormat dateFormatter =
		  new SimpleDateFormat( "ddMMyyyyHHmmss"); 
		 currentDate =
		  dateFormatter.format(c.getTime());
		  
		  paymentObj.setStrPaymentDate(currentDate);
		  paymentObj.setPaymentMode("EDC");
		  
		  paymentObj.setStrPaymentModeDate(currentDate);
		  paymentObj.setBankName(" ASHOK PARMAR ");
		  paymentObj.setTrackID("21343");
		  paymentObj.setPaymentID("D1356947747184");
		  paymentObj.setTransStatus("S"); // Error : E
		  paymentObj.setTransID("21343");
		  paymentObj.setTransError("");
		  
		  paymentObj.setRecieptNo(receptNo.getText().toString().trim());
		  paymentObj.setChangePlan(isPlanchange);
		//  paymentObj.setRecieptNo(receptNo.getText().toString().trim());
		  
		  if (updatefrom != null) { if (updatefrom.equalsIgnoreCase("I")) {
		  paymentObj.setActionType("I"); } else {
		  paymentObj.setActionType("S"); } } else {
		  paymentObj.setActionType("S"); } setPaymentObj(paymentObj);
		 	}
	
	public class InsertBeforePGAsyncTask extends AsyncTask<String, Void, Void>{
		ProgressDialog progressDialog;
		String rslt="";
		InsertBeforePGSOAP insertBeforePGSOAP;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog=new ProgressDialog(CardActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				insertBeforePGSOAP= new InsertBeforePGSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), utils.getDynamic_Url(), getString(R.string.METHOD_INSERT_BEFORE_PG));
				rslt=insertBeforePGSOAP.insert_beforepg_method(Authobj, MemberId, strTrackId, String.valueOf(PlanRate),CurrentPlan, "0");
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
			progressDialog.dismiss();
			Utils.log("Result","is:"+rslt);
			if(rslt.length()>0){
				if(rslt.equalsIgnoreCase("OK")){
					Utils.log("Insert","successfully");
					startCardActivity();
				}
			}
		}
	}
	
	
	public void FailedOrCancelTransactionDialog(final String error_reason){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(
				Html.fromHtml("<font color='#20B2AA'>" + "Tansaction is failed or canceled."
						+ "</font>"))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(
						Html.fromHtml("<font color='#20B2AA'><b>Alert</b></font>"))
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@TargetApi(Build.VERSION_CODES.HONEYCOMB)
							public void onClick(DialogInterface dialog, int id) {
								if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
									  new SendFailedDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,error_reason);
									}
									else {
										  new SendFailedDetails().execute(error_reason);
									}
							}
						});
		
		builder.show();
			
	}
	
	public class SendFailedDetails extends AsyncTask<String,Void,Void>{
		ProgressDialog prgDialog;
		String rslt="";
		String response="";
		String error_message="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prgDialog=new ProgressDialog(CardActivity.this);
			prgDialog.setMessage("Please wait \n Do not press any key");
			prgDialog.setCancelable(false);
			prgDialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			CreditCardFailedSOAP cardFailedSOAP= new CreditCardFailedSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), utils.getDynamic_Url()	, getString(R.string.METHOD_SEND_FAILED_CANCELED_TRANS));
			try {
				error_message=params[0];
				Utils.log("Error reason",":"+error_message);
				rslt=cardFailedSOAP.SendEztapFailedTrans(strTrackId, params[0], Authobj.getCliectAccessId());
				response=cardFailedSOAP.getResponse();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeFormatException e) {
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
			prgDialog.dismiss();
			goBack();
		
	}
	
}
	
	public void goBack(){
		finish();
		Intent intent = null;
		if (str_action.equals("CP")) {
			intent = new Intent(CardActivity.this,
					ChangePackgeActivity.class);
		} else {
			intent = new Intent(CardActivity.this, RenewActivity.class);
		}
		intent.putExtra(backBundelPackage0, extras0);
		intent.putExtra(backBundelPackage1, extras1);
		intent.putExtra(backBundelPackage2, extras2);
		intent.putExtra(backBundelPackage, bundleHelper.getBackExtras());
		intent.putExtra("ConnectionTypeId", ConnectionTypeId);
		startActivity(intent);
	}
	
	
	 public class GetPackageAsyncTask extends AsyncTask<String, Void, Void>{
			
			String getDataResult="";
		
			 @Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				
				
			}
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				GetStatusSOAP getStatusSOAP= new GetStatusSOAP(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_STATUS)
										); 
				try {
					//Utils.log("UserLoginName","is: "+utils.getAppUserName());
					
					getDataResult=getStatusSOAP.getPackageSOAP(Authobj,utils.getAppUserName(),"S");
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
				
				try{
					Utils.log("getDataResult","is: "+getDataResult);
					if(getDataResult.length()>0){
						if(getDataResult.equalsIgnoreCase("ok")){
							if(getUpdateDataString.length()>0){
								if(getUpdateDataString.equalsIgnoreCase("PackageUpdate")){
									
								//	Utils.log("PackageUpdate","is "+getUpdateDataString);									
									  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
					        			
					        			  
					        			  new UpdatePackageAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					        		  }
					        		  else{
					        			
					        			  new UpdatePackageAsyncTask().execute();
					        		  }
								}
							}
						}
						else{
							//AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
						}
					}
					else{
							//AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
					}
					
					}catch(Exception e){
						
					}
					
					
				}
		 }
	 
	 public class UpdatePackageAsyncTask extends AsyncTask<String, Void, Void>{
			
			String getDataResult="";
			
			 @Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
			
			}
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				GetStatusSOAP getStatusSOAP= new GetStatusSOAP(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_STATUS)
										); 
				try {
					//Utils.log("UserLoginName","is: "+utils.getAppUserName());
					
					getDataResult=getStatusSOAP.getPackageSOAP(Authobj,utils.getAppUserName(),"U");
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
					//prgDialog1.dismiss();
				try{
					if(getDataResult.length()>0){
						if(getDataResult.equalsIgnoreCase("ok")){}
						else{
							//AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
						}
					}
					else{
							//AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
					}
					
					}catch(Exception e){
						
					}
					
					
				}
		 }
}
