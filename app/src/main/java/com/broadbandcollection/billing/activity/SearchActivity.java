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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.broadbandcollection.R;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SearchActivity extends Activity {

	private ListView mainListView;
	private ArrayAdapter<String> listAdapter;
	private EditText searchText;
	int textlength = 0;

	Utils utils = new Utils();
	public static String rslt = "";
	String username, password;
	
	public static Map<String, MemberData> mapMemberData;

	private ArrayList<String> array_sort = new ArrayList<String>();
	private ArrayList<String> valuesList = new ArrayList<String>();
	LocationManager locationManager;

	AlertDialog alert = null;
AlertDialog.Builder   alertDialogBuilder;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
		}

		AppConstants1.APP_OPEN=false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);
		
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
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		alert = new AlertDialog.Builder(this).create();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

		username = extras.getString("username");
		password = extras.getString("password");

		new MemberListWebService().execute();;

		/*
		 * memberData = new MemberData();
		 * 
		 * memberData.setSubscriberID(extras.getString("subscriberID"));
		 * memberData.setCurrentPlan(extras.getString("CurrentPlan"));
		 * memberData.setPackageListCode(extras.getString("PackageListCode"));
		 * memberData.setSubscriberName(extras.getString("SubscriberName"));
		 * memberData.setSubscriberStatus(extras.getString("SubscriberStatus"));
		 * memberData.setStrExpiryDate(extras.getString("expiryDate"));
		 * memberData.setPlanRate(extras.getDouble("PlanRate"));
		 */

		// ArrayList<String> dataList = new ArrayList<String>();
		// dataList.addAll( Arrays.asList(values) );

		// Find the ListView resource.
		mainListView = (ListView) findViewById(R.id.searchListData);

		// Create ArrayAdapter using the data list.
		listAdapter = new ArrayAdapter<String>(this, R.layout.searchrow,
				valuesList);
		mainListView.setAdapter(listAdapter);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// selected item
				String selItem = ((TextView) view).getText().toString();

				Intent intent = new Intent(SearchActivity.this, ShowDetailsActivity.class);
				MemberData memberData = mapMemberData.get(selItem);

				intent.putExtra("username", "" + username);
				intent.putExtra("selItem", "" + selItem);
				intent.putExtra("subscriberID", memberData.getSubscriberID());
				intent.putExtra("CurrentPlan", memberData.getCurrentPlan());
				intent.putExtra("PackageListCode", memberData.getPackageListCode());
				intent.putExtra("SubscriberName", memberData.getSubscriberName());
				intent.putExtra("SubscriberStatus", memberData.getSubscriberStatus());
				intent.putExtra("expiryDate", memberData.getStrExpiryDate());
				intent.putExtra("PlanRate", memberData.getPlanRate());
				intent.putExtra("discountpackrate", memberData.getDiscounPackRate());
				intent.putExtra("discountedPack", memberData.getDiscounPackName());
				
				startActivityForResult(intent, 1);
			}
		});

		searchText = (EditText) findViewById(R.id.searchText);

		searchText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// Abstract Method of TextWatcher Interface.
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Abstract Method of TextWatcher Interface.
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textlength = searchText.getText().length();
				array_sort.clear();

				for (int i = 0; i < valuesList.size(); i++) {
					if (textlength <= valuesList.get(i).length()) {
						if (searchText
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) valuesList.get(i).subSequence(
												0, textlength))) {
							array_sort.add(valuesList.get(i));
						}
					}
				}
				mainListView.setAdapter(new ArrayAdapter<String>(
						SearchActivity.this,
						android.R.layout.simple_list_item_1, array_sort));
			}
		});
	}

	private class MemberListWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait..");
			Dialog.show();
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapMemberData != null) {
					Set<String> keys = mapMemberData.keySet();
					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						String key = (String) i.next();
						valuesList.add(key);
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Member not found!!!", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SearchActivity.this, Login.class);
					startActivityForResult(intent, 0);
				}
			} else {
				alert.setTitle("WebService Response!!!");
				alert.setMessage(rslt);
				alert.show();
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			/*
			 * try{ LoginCaller loginCaller = new
			 * LoginCaller(getApplicationContext
			 * ().getResources().getString(R.string.WSDL_TARGET_NAMESPACE),
			 * getApplicationContext
			 * ().getResources().getString(R.string.SOAP_URL),
			 * getApplicationContext
			 * ().getResources().getString(R.string.SOAP_ACTION_GET_MEMBER_LIST
			 * ), getApplicationContext().getResources().getString(R.string.
			 * METHOD_GET_MEMBER_LIST),
			 * utils.getVendorCode(),utils.getWsUsername
			 * (),utils.getWsPassword());
			 * 
			 * loginCaller.username = username; loginCaller.password = password;
			 * 
			 * loginCaller.join(); loginCaller.start(); rslt="START";
			 * 
			 * while(rslt == "START"){ try{ Thread.sleep(10); }catch(Exception
			 * ex){} }
			 * 
			 * }catch(Exception e){ alert.setTitle("Error!");
			 * alert.setMessage(e.toString()); alert.show(); }
			 */
			return null;
		}
	}
	
	//*************************Bradcast receiver for GPS**************************starts
			private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			      //  Utils.log("Service","Message");
			        //  ... react to local broadcast message
			        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			    	{
			    		if(alert!=null)
			    			alert.dismiss();
			    	}
			    	else{
			    		// showGPSDisabledAlertToUser();
			    		 }
			        
			    }
			};
			//*************************Bradcast receiver for GPS**************************ends
			
			 public  void showGPSDisabledAlertToUser(){
				 alertDialogBuilder  = new AlertDialog.Builder(SearchActivity.this);
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
