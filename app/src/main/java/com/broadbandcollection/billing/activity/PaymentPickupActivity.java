/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
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
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.GetCallCustomerSoap;
import com.broadbandcollection.billing.SOAP.PaymentRequestCaller;
import com.broadbandcollection.billing.SOAP.SearchMemberCaller;
import com.broadbandcollection.billing.interface_.ShowTime;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.InformCustomer;
import com.broadbandcollection.billing.obj.InformedCustomerList;
import com.broadbandcollection.billing.obj.InternalStorage;
import com.broadbandcollection.billing.obj.InternalStorage_Inform;
import com.broadbandcollection.billing.obj.MemberData;

import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PaymentPickupActivity extends Activity implements ShowTime {

	public static MemberData memberData;
	private Button backBtn;
	public static final String backBundelPackage = "com.cnergee.billing.search.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.showpaymentpickup.screen.INTENT";
	public static Context context;
	public static String rslt = "";
	public static ArrayList<HashMap<String, String>> paymentPickList;
	public static ArrayList<String> alMessage;
	public static Map<String, MemberData> mapMemberData;
	public static ArrayList<Boolean> al_is_postpaid=new ArrayList<Boolean>();
	boolean isLogout = false;
	
	PaymentPickupAdapter adapter;
	Utils utils = new Utils();
	
	BundleHelper bundleHelper;
	ListView lvPaymentPickup;
	String selMemberId;
	LinearLayout linapaymntpk;
	String username,password;
	String VisitTime="";
	String SubscriberId="";
	String KEY="INFORMCUSTOMER";
	String todays_date="";
	public static ArrayList<String>alSubId= new ArrayList<String>();
	public static ArrayList<String>alPickupId= new ArrayList<String>();
	private GetPaymentRequestWebService getPaymentRequestWebService =null;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	LocationManager locationManager;
	int pos;
	public static String Message=null;
	ArrayList<InformCustomer> alInformedCustomerLists=new ArrayList<InformCustomer>();
	ArrayList<Integer> alPosition= new ArrayList<Integer>();
	String PickUpId="";
AlertDialog  alert ;
AlertDialog.Builder   alertDialogBuilder;
	Boolean inform_cust=false;
	ProgressDialog progressDialog;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
        super.onDestroy();
        System.runFinalizersOnExit(true);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mShowPickupData);

	}
	protected void onPause() {
		super.onPause();
	//Log.i(" >>>> "," IN PAUSE ");
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
		//Log.i(" >>>> PP "," IN RESUME ");
		//setScreenView();
		AppConstants1.APP_OPEN=true;
		if(AppConstants1.GPS_AVAILABLE){
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
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
		setContentView(R.layout.activity_payment_pickup);

	/*	Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});*/

		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else{
			AppConstants1.GPS_AVAILABLE=false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		alMessage=new ArrayList<String>();
		
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
			            mMessageReceiver, new IntentFilter("GpsStatus"));
		
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mShowPickupData, new IntentFilter("show_pickupdata"));


		try {
		InformedCustomerList informedCustomerList=(InformedCustomerList) InternalStorage_Inform.readObject(PaymentPickupActivity.this, KEY);
			
			if(informedCustomerList!=null){
				alInformedCustomerLists=informedCustomerList.getAlInformCustomers();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setScreenView();
	}
	
	private BroadcastReceiver mShowPickupData = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	       // Utils.log("Service","Message");
	        //  ... react to local broadcast message
	    	setPaymentPickUpData();
	    }
	};
	
	private void setScreenView(){
		
		context = this;
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}
		
		inform_cust=sharedPreferences.getBoolean("inform_customer", false);
		
		Utils.log("Inform Customer","is:"+inform_cust);

		username = bundleHelper.getCurrentExtras().getString("username");
		password = bundleHelper.getCurrentExtras().getString("password");
		
		TextView title = (TextView)findViewById(R.id.headerView);
		title.setText(R.string.app_pay_pick_title);
	
		

		lvPaymentPickup = (ListView) findViewById(R.id.listPamtPickup);
		// Click event for single list row
		lvPaymentPickup.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView parent, View view,
                    int position, long id) {
            	//String sel_id = ((TextView) view).getText().toString();
            	selMemberId = ((TextView) view.findViewById(R.id.pmt_pik_name)).getText().toString();
            	
            	//Log.i(" >>> ",selMemberId);
            	pos=position;
            	getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.execute((String) null);
				
            }
        });
		
		if(this.getIntent()!=null){
			if(this.getIntent().getStringExtra("from")!=null){
				getPaymentRequestWebService = new GetPaymentRequestWebService();
				  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
					  getPaymentRequestWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				  }
				  else{			  
					  getPaymentRequestWebService.execute((String) null);
				  }
			}
			else{
				if(SearchVendorActivity.is_paymentpickup_running)
				{
					setPaymentPickUpData();
					PaymentPickupActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressDialog= new ProgressDialog(PaymentPickupActivity.this);
							progressDialog.setMessage("Please wait..");
							progressDialog.show();
						}
					});
				}
				else{
					setPaymentPickUpData();
				}
			}
		}
		else{
			if(SearchVendorActivity.is_paymentpickup_running)
			{
				setPaymentPickUpData();
				PaymentPickupActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressDialog= new ProgressDialog(PaymentPickupActivity.this);
						progressDialog.show();
					}
				});
			}
			else{
				setPaymentPickUpData();
			}
		}
		
	//	setPaymentPickUpData();
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(backOnClickListener);
	
	}

	OnClickListener backOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			PaymentPickupActivity.this.finish();
			/*Intent intent = new Intent(PaymentPickupActivity.this,SearchVendorActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("username",username);
			bundle.putString("password",password);
			intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
			intent.putExtra(backBundelPackage,  bundleHelper.getCurrentExtras());
			startActivity(intent);*/
			
		}
	};

	private class GetPaymentRequestWebService extends
	AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(PaymentPickupActivity.this);

		String AppVersion= Utils.GetAppVersion(PaymentPickupActivity.this);
		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			alMessage.clear();
		}
		
		protected void onPostExecute(Void unused) {
			getPaymentRequestWebService = null;
			Dialog.dismiss();
			
			try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
					if(paymentPickList != null){	
						/*Iterator iter = paymentPickList.iterator();
						int i = 0;
						while(iter.hasNext()){
							HashMap<String, String> map = (HashMap)iter.next();
					
							System.out.println(" >>> "+i+"  >>> "+map.get("MemberLoginID"));
							i++;
						}*/
								
						//System.out.println(" >>> "+paymentPickList.size());
						 
						// Getting adapter
				        adapter = new PaymentPickupAdapter(context, paymentPickList,alMessage,alInformedCustomerLists,alSubId,alPickupId,inform_cust,al_is_postpaid);
				        lvPaymentPickup.setAdapter(adapter);
				       
				        if(alInformedCustomerLists!=null){
				        	if(alInformedCustomerLists.size()>0){
				        		Utils.log("Informed Data","size:"+alInformedCustomerLists.size());
								for(int i=0;i<alInformedCustomerLists.size();i++)
								{
									Utils.log("Old Data",":"+alInformedCustomerLists.get(i).getSubscriberid());
									if(alSubId.contains(alInformedCustomerLists.get(i).getSubscriberid())){
										Utils.log("Id","true");
									}
									else{
										Utils.log("Id","false");
										alPosition.add(i);
									}
								}
								if(alPosition.size()>0){
									for(int i=0;i<alPosition.size();i++){
										alInformedCustomerLists.remove(alPosition.get(i));
									}
								}
								
								
								InformedCustomerList informedCustomerList= new InformedCustomerList();
								informedCustomerList.setAlInformCustomers(alInformedCustomerLists);
								try {
									InternalStorage.writeObject(PaymentPickupActivity.this, KEY, informedCustomerList);
									Utils.log("Object","Write");
									Toast.makeText(PaymentPickupActivity.this, "Payment Pickup not registered successfully.", Toast.LENGTH_LONG).show();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Utils.log("Error Writing","Object");
								}
				        	}
				        }
					}else{
					}
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
				Authobj.setAppVersion(AppVersion);
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
				getPaymentRequestWebService = null;
			}
		
	}
	
	

	/*
	 * Get memeber details WS
	 * 
	 */
	private class GetMemberDetailWebService extends
			AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				PaymentPickupActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			Message=null;
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			Dialog.dismiss();
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
					finish();
					Intent intent = new Intent(PaymentPickupActivity.this,
							ShowDetailsActivity.class);
					MemberData memberData = mapMemberData.get(selItem);
					
					Bundle bundle = new Bundle();
					bundle = BundleHelper.getMemberDataBundel(bundle, memberData);
					bundle.putString("username",username);
					bundle.putString("password",password);
					bundle.putString("selItem", selItem);
					
					bundle.putString("execteFrom","paypickup");
					bundle.putString("message", Message);
					
					intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
					intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
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
				SearchMemberCaller memberCaller = new SearchMemberCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_MEMBER_DETAILS),Authobj);

				memberCaller.username = username;
				memberCaller.password = password;
				memberCaller.searchTxt = selMemberId;
				memberCaller.execteFrom = "paypickup";
				
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
				getMemberDetailWebService = null;
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
			    		// showGPSDisabledAlertToUser();
			    		 }
			        }
			        
			    }
			};
			//*************************Bradcast receiver for GPS**************************ends
			
			 public  void showGPSDisabledAlertToUser(){
				 alertDialogBuilder  = new AlertDialog.Builder(PaymentPickupActivity.this);
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
			
			@Override
			public void CallinformCustomer(String time, String SubscriberId,String todays_date,String PickUpId) {
				VisitTime=time;
				this.SubscriberId=SubscriberId;
				this.todays_date=todays_date;
				this.PickUpId=PickUpId;
				Utils.log("On AsyncTask","AsyncTask Executed");
				new AsyncTaskInformCustomer().execute();
			}


			class AsyncTaskInformCustomer extends AsyncTask<Void , String, String>{
				 
				
				GetCallCustomerSoap getCallCstSoap;
		    String getCustomerSoapResult="";
		    String getCutomerResponse="";    		
		    String AppVersion;
		    String rslt="",response="";
		    private ProgressDialog Dialog = new ProgressDialog(PaymentPickupActivity.this);
		    @Override
		    protected void onPreExecute() {
		    
		    	Utils.log("On Pre","Async ON PRew Executed");
				
		    	super.onPreExecute();
		    	  AppVersion= Utils.GetAppVersion(PaymentPickupActivity.this);
		    	Dialog.setMessage("Please Wait..");
		    	Dialog.setCancelable(false);
					Dialog.show();
		    }


			@Override
			protected String doInBackground(Void... params) {											
				try {
					
					Utils.log("On Asyn Do InBck"," do In Bck Executed");
					
					getCallCstSoap = new GetCallCustomerSoap(getString(R.string.WSDL_TARGET_NAMESPACE),
							utils.getDynamic_Url(),getString(R.string.METHOD_USER_VISIT_INFO));
				//getCustomerSoapResult=getCallCstSoap.CallInformCustomer(UserLoginName,Authobj,SubscriberId,VisitTime);
					
					AuthenticationMobile Authobj = new AuthenticationMobile();
					Authobj.setMobileNumber(utils.getMobileNumber());
					Authobj.setMobLoginId(utils.getMobLoginId());
					Authobj.setMobUserPass(utils.getMobUserPass());
					Authobj.setIMEINo(utils.getIMEINo());
					Authobj.setCliectAccessId(utils.getCliectAccessId());
					Authobj.setMacAddress(utils.getMacAddress());
					Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
					Authobj.setAppVersion(AppVersion);
					Utils.log("On Calling String Of Async", "On CallInformC");
					
					getCustomerSoapResult = getCallCstSoap.CallInformCustomer(Authobj,SubscriberId,VisitTime,username,PickUpId);
					response=getCallCstSoap.getResponse();
				} catch (Exception e) {
					}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Dialog.dismiss();
				if(getCustomerSoapResult.length()>0){
				if(getCustomerSoapResult.equalsIgnoreCase("ok")){
					if(response.length()>0){
						if(response.equalsIgnoreCase("1")){
							InformCustomer informCustomer= new InformCustomer(SubscriberId, todays_date);
							alInformedCustomerLists.add(informCustomer);
							InformedCustomerList informedCustomerList= new InformedCustomerList();
							informedCustomerList.setAlInformCustomers(alInformedCustomerLists);
							try {
								InternalStorage.writeObject(PaymentPickupActivity.this, KEY, informedCustomerList);
								Toast.makeText(PaymentPickupActivity.this, "Customer informed successfully.", Toast.LENGTH_LONG).show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Utils.log("Error Writing","Object");
							}
						}
						else{

							InformCustomer informCustomer= new InformCustomer(SubscriberId, todays_date);
							alInformedCustomerLists.add(informCustomer);
							InformedCustomerList informedCustomerList= new InformedCustomerList();
							informedCustomerList.setAlInformCustomers(alInformedCustomerLists);
							try {
								InternalStorage.writeObject(PaymentPickupActivity.this, KEY, informedCustomerList);
								Toast.makeText(PaymentPickupActivity.this, "Payment Pickup not registered successfully.", Toast.LENGTH_LONG).show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Utils.log("Error Writing","Object");
							}
						
						}
					}
				}
			
			}
			}


			
			}

public void  setPaymentPickUpData(){
	//try{
		Utils.log("Result is",":"+rslt);
		
		if(progressDialog!=null){
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
		if (rslt.trim().equalsIgnoreCase("ok")) {
			//SearchVendorActivity.paymentPickList=paymentPickList;
			if(SearchVendorActivity.paymentPickList != null){	
				/*Iterator iter = paymentPickList.iterator();
				int i = 0;
				while(iter.hasNext()){
					HashMap<String, String> map = (HashMap)iter.next();
			
					System.out.println(" >>> "+i+"  >>> "+map.get("MemberLoginID"));
					i++;
				}*/
						
				//System.out.println(" >>> "+paymentPickList.size());
				 
				// Getting adapter
				
				//SearchVendorActivity.alMessage=alMessage;
				
			//	SearchVendorActivity.alSubId=alSubId;
			//	SearchVendorActivity.alPickupId=alPickupId;
				
				
				
			 if(Integer.valueOf(SearchVendorActivity.payCount)>0){
				 
		        adapter = new PaymentPickupAdapter(context, SearchVendorActivity.paymentPickList,SearchVendorActivity.alMessage,alInformedCustomerLists,SearchVendorActivity.alSubId,SearchVendorActivity.alPickupId,inform_cust,SearchVendorActivity.al_is_postpaid);
		        lvPaymentPickup.setAdapter(adapter);
		       
		        if(alInformedCustomerLists!=null){
		        	if(alInformedCustomerLists.size()>0){
		        		Utils.log("Informed Data","size:"+alInformedCustomerLists.size());
						for(int i=0;i<alInformedCustomerLists.size();i++){
							Utils.log("Old Data",":"+alInformedCustomerLists.get(i).getSubscriberid());
							if(alSubId.contains(alInformedCustomerLists.get(i).getSubscriberid())){
								Utils.log("Id","true");
							}
							else{
								Utils.log("Id","false");
								alPosition.add(i);
							}
						}
						if(alPosition.size()>0){
							for(int i=0;i<alPosition.size();i++){
								alInformedCustomerLists.remove(alPosition.get(i));
							}
							
						}
						
						InformedCustomerList informedCustomerList= new InformedCustomerList();
						informedCustomerList.setAlInformCustomers(alInformedCustomerLists);
						try {
							InternalStorage.writeObject(PaymentPickupActivity.this, KEY, informedCustomerList);
							Utils.log("Object","Write");
							//Toast.makeText(PaymentPickupActivity.this, "Payment Pickup not registered successfully.", Toast.LENGTH_LONG).show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Utils.log("Error Writing","Object");
						}
		        	}
		        }
				}
				else{
					getPaymentRequestWebService = new GetPaymentRequestWebService();
					  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
						  getPaymentRequestWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					  }
					  else{			  
						  getPaymentRequestWebService.execute((String) null);
					  }
				}
		      
			}
			else{
				
			}
		}else if (rslt.trim().equalsIgnoreCase("not")) {
			getPaymentRequestWebService = new GetPaymentRequestWebService();
			  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
				  getPaymentRequestWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			  }
			  else{			  
				  getPaymentRequestWebService.execute((String) null);
			  }
			
			//AlertsBoxFactory.showAlert("No Data Found !!! ",context );
		}else{
			getPaymentRequestWebService = new GetPaymentRequestWebService();
			  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
				  getPaymentRequestWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			  }
			  else{			  
				  getPaymentRequestWebService.execute((String) null);
			  }
			//AlertsBoxFactory.showAlert(rslt,context );
		}
	/*}catch(Exception e){
		Utils.log("Exception","is"+e);
		AlertsBoxFactory.showAlert(rslt,context );}		*/
}
			@Override
			public void callDetailsActivity(String SubscriberId,boolean is_postpaid) {
				// TODO Auto-generated method stub
				if(is_postpaid){
				
					Intent intnet_postpaid=new Intent(PaymentPickupActivity.this, Postpaid_Activity.class);
					intnet_postpaid.putExtra("postpaid_user", SubscriberId);
					startActivity(intnet_postpaid);
				}
				else{
				selMemberId = SubscriberId;
            	
            	//Log.i(" >>> ",selMemberId);
            	//pos=position;
            	getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.execute((String) null);
				}
			}
			
}
