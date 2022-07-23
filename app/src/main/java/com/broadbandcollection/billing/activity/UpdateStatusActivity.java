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
import android.app.Dialog;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.CollectionCaller;
import com.broadbandcollection.billing.activity.Postpaid_Activity;
import com.broadbandcollection.billing.activity.SearchVendorActivity;
import com.broadbandcollection.billing.activity.ShowDetailsActivity;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastReceiver;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.CollectionObj;
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.widgets.DateTimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UpdateStatusActivity extends Activity {
	public static String rslt = "";
	public static String responseMsg ="";
	public static String CallType = "";
	 
	private AuthenticationMobile Authobj = null;
	 
    CollectionWebService collectionWebService = null;
 
    private static final String TAG = "DialogActivity";
    private static final int TEXT_ID = 0;
    
	public static MemberData memberData;
	public boolean isReqAltMobNo = false;
	public static Context context;
	public String selDateTime = null;
	
	Button btnUpdate;
	CheckBox callback = null;
	CheckBox visitagain = null;
	CheckBox doorlock = null;
	CheckBox shifted = null;
	CheckBox service = null;
	CheckBox outstation = null;

	String username,password;
	String selItem, AreaCode, AreaCodeFilter, CurrentPlan;
	String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate;
	double OldPlanRate,NewPlanRate;
	
	Utils utils = new Utils();
	public static final String backBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.updatestatus.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.search.screen.INTENT";
	
	BundleHelper bundleHelper;
	Bundle extras1;
	boolean is_postpaid=false;
	LocationManager locationManager;

AlertDialog  alert ;
AlertDialog.Builder   alertDialogBuilder;
	private String comment;
	private String currDateTime;
	public boolean isSelectOption = false;
	
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
		Authobj.setAppVersion(Utils.GetAppVersion(UpdateStatusActivity.this));
		return Authobj;
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
 
        switch (id) {
            case 0:
                return shiftDialog();
            case 1:
                return serviceDialog();
            default:
                return null;
        }
    }
 
    /**
     * If a dialog has already been created,
     * this is called to reset the dialog
     * before showing it a 2nd time. Optional.
     */
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
 
        // Clear the input box.
        EditText text = (EditText) dialog.findViewById(TEXT_ID);
        text.setText("");

    }
 
     private Dialog shiftDialog() {
 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
       	builder.setTitle("Enter New Shifted Address");
        	//builder.setMessage("What is New Address:");
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(this);
         input.setId(TEXT_ID);
         builder.setView(input);
 
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                setComment(value);
                collectionWebService = new CollectionWebService();
                collectionWebService.execute((String) null);
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
 
        return builder.create();
    }
	
     private Dialog serviceDialog() {
    	 
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         
    		builder.setTitle("Enter Service Issue");
           // Use an EditText view to get user input.
          final EditText input = new EditText(this);
          input.setId(TEXT_ID);
          builder.setView(input);
  
         builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
  
             @Override
             public void onClick(DialogInterface dialog, int whichButton) {
                 String value = input.getText().toString();
                 setComment(value);
                 collectionWebService = new CollectionWebService();
                 collectionWebService.execute((String) null);
                 return;
             }
         });
  
         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 return;
             }
         });
  
         return builder.create();
     }
	
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
	        	//  showGPSDisabledAlertToUser();
	        }
 		}
 	}	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update_status);
		context = this;
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
		selDateTime = null;
		TextView title = (TextView)findViewById(R.id.headerView);
		title.setText(R.string.title_activity_updatestatus);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		is_postpaid=this.getIntent().getBooleanExtra("is_postpaid", false);
		if(!is_postpaid){
		extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
		
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		//Bundle extras = getIntent().getExtras();
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}
			
		username = bundleHelper.getCurrentExtras().getString("username");
		try{
		password = bundleHelper.getCurrentExtras().getString("password");
		}catch(Exception e){}
		
		selItem = bundleHelper.getCurrentExtras().getString("selItem");
		subscriberID = bundleHelper.getCurrentExtras().getString("subscriberID");
		OldPlanRate = bundleHelper.getCurrentExtras().getDouble("PlanRate");
		PackageListCode = bundleHelper.getCurrentExtras().getString("PackageListCode");
		PrimaryMobileNo = bundleHelper.getCurrentExtras().getString("PrimaryMobileNo");
		SecondryMobileNo = bundleHelper.getCurrentExtras().getString("SecondryMobileNo");
		AreaCode = bundleHelper.getCurrentExtras().getString("AreaCode");
		AreaCodeFilter = bundleHelper.getCurrentExtras().getString("AreaCodeFilter");
		CurrentPlan = bundleHelper.getCurrentExtras().getString("CurrentPlan");
		IsAutoReceipt = bundleHelper.getCurrentExtras().getString("IsAutoReceipt");
		PaymentDate = bundleHelper.getCurrentExtras().getString("PaymentDate");
		}
		else{
			subscriberID=this.getIntent().getStringExtra("subscriber_id");
			username = this.getIntent().getStringExtra("username");
		}
		
		
		final TextView subscriberid = (TextView) findViewById(R.id.subscriberid);
		subscriberid.setText(subscriberID);
		
		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				finish();
				if(!is_postpaid){
				Intent intent = null;
					intent = new Intent(UpdateStatusActivity.this,
							ShowDetailsActivity.class);
				
				intent.putExtra(backBundelPackage1,extras1 );
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				startActivity(intent);
				}
				else{
					
				}
	
			}
		});
		
		btnUpdate = (Button) findViewById(R.id.updateBtn);
		btnUpdate.setEnabled(false);
		btnUpdate.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				
				 //setComment("");
                 collectionWebService = new CollectionWebService();
                 collectionWebService.execute((String) null);
               
                 
               
			}
		});
		
		
		final OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			 
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			isSelectOption = isChecked;
			if(isSelectOption){
				btnUpdate.setEnabled(true);
			}else{
				btnUpdate.setEnabled(false);
			}
			
			if(isChecked){
				
				//EditText compComment = (EditText)findViewById(R.id.compComment);
			switch(arg0.getId())
			  {
			
			    case R.id.callback:
			    	callback.setChecked(true);
			    	visitagain.setChecked(false);
			    	doorlock.setChecked(false);
			    	shifted.setChecked(false);
			    	service.setChecked(false);
			    	outstation.setChecked(false);
			    	CallType = "CallBack";
			    	setComment("Customer is requested for call back.");
			    	
			    	showDateTimeDialog();    	
			    	/*Intent i = new Intent(UpdateStatusActivity.this, AndroidDateTimeActivity.class);
			    	i.putExtra("Header", "CB");
			    	startActivity(i);*/
			    	
			    	
			         break;
			    case R.id.visitagain:
			    	visitagain.setChecked(true);
			    	callback.setChecked(false);
			    	doorlock.setChecked(false);
			    	shifted.setChecked(false);
			    	service.setChecked(false);
			    	outstation.setChecked(false);
			    	CallType = "VisitAgain";
			    	setComment("Customer is requested for Visit Again");
			    	
			    	showDateTimeDialog();
			    	/*Intent j = new Intent(UpdateStatusActivity.this, AndroidDateTimeActivity.class);
			    	j.putExtra("Header", "VisitAgain");
			    	startActivity(j);*/
			    	
			         break;
			   case R.id.doorlock:
				   doorlock.setChecked(true);
				   callback.setChecked(false);
				   	visitagain.setChecked(false);
			    	shifted.setChecked(false);
			    	service.setChecked(false);
			    	outstation.setChecked(false);
			    	CallType = "DoorLock";
			    	setComment("Customer Door is Locked");
				   //compComment.setText("");
			        break;
			   case R.id.shifted:
				   	shifted.setChecked(true);
				   	callback.setChecked(false);
				   	visitagain.setChecked(false);
			    	doorlock.setChecked(false);
			    	service.setChecked(false);
			    	outstation.setChecked(false);
			    	CallType = "Shifted";
				   //compComment.setText("");
			    	showDialog(0);
				   break;
			   case R.id.serviceissue:
				   service.setChecked(true);
				   callback.setChecked(false);
				   visitagain.setChecked(false);
			    	doorlock.setChecked(false);
			    	shifted.setChecked(false);
			    	outstation.setChecked(false);
			    	CallType = "ServiceIssue";
				   //compComment.setText("");
			    	showDialog(1);
				   break;
				   
			   case R.id.outstation:
				   outstation.setChecked(true);
				   callback.setChecked(false);
				   visitagain.setChecked(false);
			    	doorlock.setChecked(false);
			    	shifted.setChecked(false);
			    	service.setChecked(false);
			    	CallType = "OutStation";
			       //compComment.setText("");
			    	
			    	showDateTimeDialog();
			    	
			    	/*Intent k = new Intent(UpdateStatusActivity.this, AndroidDateTimeActivity.class);
			    	k.putExtra("Header", "OutStation");
			    	startActivity(k);
				   */
			  }
			}
			 
			}
			};
		
			callback = (CheckBox)findViewById(R.id.callback);
			callback.setOnCheckedChangeListener(listener);
			 
			visitagain = (CheckBox)findViewById(R.id.visitagain);
			visitagain.setOnCheckedChangeListener(listener);
			 
			doorlock = (CheckBox)findViewById(R.id.doorlock);
			doorlock.setOnCheckedChangeListener(listener);
			
			shifted = (CheckBox)findViewById(R.id.shifted);
			shifted.setOnCheckedChangeListener(listener);
			
			service = (CheckBox)findViewById(R.id.serviceissue);
			service.setOnCheckedChangeListener(listener);
			
			outstation = (CheckBox)findViewById(R.id.outstation);
			outstation.setOnCheckedChangeListener(listener);
		
			setAuthenticationMobile();
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
	
	private class CollectionWebService extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(UpdateStatusActivity.this);
		CollectionObj collectinosObj = new CollectionObj();

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
			 
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			collectionWebService = null;
			//submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
			//submit.setClickable(true);
			collectionWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(responseMsg)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Web Service Respone")
				       .setCancelable(false)
				       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				    	   public void onClick(DialogInterface dialog, int id) {
				    	
				    			finish();
				    			if(!is_postpaid){
								Intent intent = new Intent(UpdateStatusActivity.this, SearchVendorActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("username",username);
								bundle.putString("password",password);
								intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
								intent.putExtra(backBundelPackage1, extras1);
								startActivity(intent);
				    			}
				    			else{
				    				((Activity) Postpaid_Activity.context).finish();
				    			}
				    	   }     
				       });
				builder.show();
				
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
			
			 Intent i = new Intent(UpdateStatusActivity.this, AlarmBroadcastReceiver.class);
	       	   	if(CallType!=null){
					i.putExtra("activity", CallType);
						sendBroadcast(i);
	       	   	}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//setCurrDateTime();
				//Log.i(" >>>>> ",getCurrDateTime());
				Calendar c = Calendar.getInstance();

				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"ddMMyyyyHHmmss");
				
				if(selDateTime == null)
					selDateTime = dateFormatter.format(c.getTime());
				//selDateTime
				
				collectinosObj.SetMemberLoginId(subscriberID);
				collectinosObj.setPayPickId("");
				collectinosObj.setTypeName(CallType);
				collectinosObj.setCBDate(selDateTime);
				
				if(CallType.equals("CallBack") || CallType.equals("VisitAgain") || CallType.equals("OutStation"))
				{
					//DateFormat df = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");  
					//String s = df.format(selDateTime);
					collectinosObj.setComment(getComment());
					
				}
				else if (CallType.equals("DoorLock")){
					String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
					collectinosObj.setComment(getComment() + " at " + currentDateTimeString);
				}
				else
				{
					collectinosObj.setComment(getComment());
				}
				collectinosObj.setUserId(username);
				

				CollectionCaller caller = new CollectionCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_SEND_COLLECTION),getAuthenticationMobile());

				caller.setCollectionObj(collectinosObj);
				caller.setUsername(username);
	
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
				AlertsBoxFactory.showAlert(rslt,context );
			}
			return null;
		}
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		
		this.comment = comment;
	}
	/**
	 * @return the currDateTime
	 */
	public String getCurrDateTime() {
		
		return currDateTime;
	}
	public void setCurrDateTime() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"ddMMyyyyHHmmss");
		String currentDate = dateFormatter.format(c.getTime());

		this.currDateTime = currDateTime;
	}
	
	

	private void showDateTimeDialog() {
		
		// Create the dialog
				final Dialog mDateTimeDialog = new Dialog(this);
				// Inflate the root layout
				final LinearLayout mDateTimeDialogView = (LinearLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
				// Grab widget instance
				final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
				// Check is system is set to use 24h time (this doesn't seem to work as expected though)
				final String timeS = android.provider.Settings.System.getString(getContentResolver(), android.provider.Settings.System.TIME_12_24);
				final boolean is24h = !(timeS == null || timeS.equals("12"));
				
				// Update demo TextViews when the "OK" button is clicked 
				((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();
						// TODO Auto-generated method stub
						((TextView) findViewById(R.id.Date)).setText(mDateTimePicker.get(Calendar.DAY_OF_MONTH) + "-" + (mDateTimePicker.get(Calendar.MONTH)+1) + "-"
								+ mDateTimePicker.get(Calendar.YEAR));
						
						/*((TextView) findViewById(R.id.Date)).setText(mDateTimePicker.get(Calendar.YEAR) + "/" + (mDateTimePicker.get(Calendar.MONTH)+1) + "/"
								+ mDateTimePicker.get(Calendar.DAY_OF_MONTH));*/
						
						if (mDateTimePicker.is24HourView()) {
							((TextView) findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY) + ":" + mDateTimePicker.get(Calendar.MINUTE));
						} else {
							((TextView) findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR) + ":" + mDateTimePicker.get(Calendar.MINUTE) + " "
									+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));
						}
						int month = mDateTimePicker.get(Calendar.MONTH)+1;
						//ddMMyyyyHHmmss
						Calendar c = Calendar.getInstance();
						SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyyHHmmss");
						c.set(mDateTimePicker.get(Calendar.YEAR), mDateTimePicker.get(Calendar.MONTH), mDateTimePicker.get(Calendar.DAY_OF_MONTH), mDateTimePicker.get(Calendar.HOUR_OF_DAY), mDateTimePicker.get(Calendar.MINUTE), 0);
						
						selDateTime = dateFormatter.format(c.getTime());
						//selDateTime = ""+mDateTimePicker.get(Calendar.DAY_OF_MONTH)+""+Integer.toString(month)+""+mDateTimePicker.get(Calendar.YEAR)+""+mDateTimePicker.get(Calendar.HOUR)+""+ mDateTimePicker.get(Calendar.MINUTE)+"00";
						//Log.i(" >>> ",selDateTime);
						mDateTimeDialog.dismiss();
					}
				});

				// Cancel the dialog when the "Cancel" button is clicked
				((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

				// Reset Date and Time pickers when the "Reset" button is clicked
				((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});
				
				// Setup TimePicker
				mDateTimePicker.setIs24HourView(is24h);
				// No title on the dialog window
				mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				// Set the dialog content view
				mDateTimeDialog.setContentView(mDateTimeDialogView);
				// Display the dialog
				mDateTimeDialog.show();
	}
	
	@Override
	protected void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

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
				 alertDialogBuilder  = new AlertDialog.Builder(UpdateStatusActivity.this);
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
