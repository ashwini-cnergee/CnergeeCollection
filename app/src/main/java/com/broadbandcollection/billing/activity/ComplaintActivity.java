package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.SOAP.GetComplaintListSOAP;
import com.broadbandcollection.billing.SOAP.SendComplaintSOAP;
import com.broadbandcollection.billing.broadcast.AlarmBroadcastReceiver;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;
import com.traction.ashok.util.AlertsBoxFactory;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ComplaintActivity extends Activity{
	Spinner spComplaints;
	Button btnSubmit,btnCancel;
	EditText etComments;
	
	TextView tvHeading;
	String AreaCode="", AreaCodeFilter="", SubscriberId="";
	Utils utils= new Utils();
	SharedPreferences sharedPreferences;
	AuthenticationMobile Authobj;
	
	public static ArrayList<String> ComplaintListName;
	public static ArrayList<String> ComplaintListId;
	
	public static String senComplaintResponse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_complaint);
		
		spComplaints=(Spinner) findViewById(R.id.spinnerList);
		btnCancel=(Button)findViewById(R.id.btncancel);
		btnSubmit=(Button)findViewById(R.id.btnsubmit);
		etComments=(EditText) findViewById(R.id.comments);
		tvHeading=(TextView) findViewById(R.id.headerView);
		tvHeading.setText("Launch Complaint");
		Intent i= getIntent();
		
		AreaCode=i.getStringExtra("AreaCode");
		AreaCodeFilter=i.getStringExtra("AreaCodeFilter");
		SubscriberId=i.getStringExtra("SubscriberId");
		
		sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		ComplaintListName= new ArrayList<String>();
		ComplaintListId= new ArrayList<String>();
		
		if(Utils.isOnline(ComplaintActivity.this)){
			new  GetComplaintAsynTask().execute();
		}
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(spComplaints.getSelectedItemPosition()!=0){
					if(TextUtils.isEmpty(etComments.getText().toString().trim()))
					{	
						AlertsBoxFactory.showAlert(" Please enter valid comments.",ComplaintActivity.this );
					}
					else{
						 new SendComplaintAsynTask().execute();
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please Select Valid Complaint", ComplaintActivity.this);
				}
			}
		});
		
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ComplaintActivity.this.finish();
				Intent intent = new Intent(
						ComplaintActivity.this,
						SearchVendorActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("username",utils.getAppUserName());
				bundle.putString("password",utils.getAppPassword());
				intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
				startActivity(intent);
			}
		});
	}
	
	public class SendComplaintAsynTask extends AsyncTask<String, Void, Void>{
		ProgressDialog prgDialog;
		String rslt="";
		String AppVersion= Utils.GetAppVersion(ComplaintActivity.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			prgDialog= new ProgressDialog(ComplaintActivity.this);
			prgDialog.setMessage("Please Wait...");
			prgDialog.setCancelable(false);
			prgDialog.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(Authobj == null){
				Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				Authobj.setAppVersion(AppVersion);
			}
			
			SendComplaintSOAP sendComplaintSOAP = new SendComplaintSOAP(getApplicationContext().getResources().getString(
					R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
							.getResources().getString(
									R.string.METHOD_SEND_COMPLAINT));
			
			try {
				rslt=	sendComplaintSOAP.sendComplaint(AreaCode, AreaCodeFilter, Authobj, utils.getAppUserName(), ComplaintListId.get(spComplaints.getSelectedItemPosition()),SubscriberId,etComments.getText().toString());
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
			prgDialog.dismiss();
			if(rslt.length()>0){
				if(rslt.equalsIgnoreCase("ok")){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ComplaintActivity.this);
					builder.setMessage(senComplaintResponse
							)
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("Confirm",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();
											
											ComplaintActivity.this.finish();
											Intent intent = new Intent(
													ComplaintActivity.this,
													SearchVendorActivity.class);
											Bundle bundle = new Bundle();
											bundle.putString("username",utils.getAppUserName());
											bundle.putString("password",utils.getAppPassword());
											intent.putExtra("com.cnergee.billing.search.screen.INTENT", bundle);
											startActivity(intent);

											Intent i = new Intent(ComplaintActivity.this, AlarmBroadcastReceiver.class);
											i.putExtra("activity", "launch complaint");

												sendBroadcast(i);

										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		}
		
	}
	
	public class GetComplaintAsynTask extends AsyncTask<String, Void, Void>{
		ProgressDialog prgDialog;
		String listRslt="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			prgDialog= new ProgressDialog(ComplaintActivity.this);
			prgDialog.setMessage("Please Wait...");
			prgDialog.setCancelable(false);
			prgDialog.show();
			ComplaintListName.clear();
			ComplaintListId.clear();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			GetComplaintListSOAP getComplaintListSOAP = new GetComplaintListSOAP(getApplicationContext().getResources().getString(
					R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
							.getResources().getString(
									R.string.METHOD_COMPLAINT_CATEGORY_LIST));
			
			
		
			
			try {
				listRslt=	getComplaintListSOAP.getComplaintTypeList(utils.getCliectAccessId());
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
			prgDialog.dismiss();
			if(listRslt.length()>0){
				if(listRslt.equalsIgnoreCase("ok")){
					ArrayAdapter<String> adapter= new ArrayAdapter<String>(ComplaintActivity.this, R.layout.spinner_item,ComplaintListName);
					spComplaints.setAdapter(adapter);
				}
				
			}
		}
		
	}

}
