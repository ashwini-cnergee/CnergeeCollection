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
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.AdditionalAmountDetailsSOAP;
import com.broadbandcollection.billing.SOAP.AdjustmentCaller;
import com.broadbandcollection.billing.SOAP.GetStatusSOAP;
import com.broadbandcollection.billing.SOAP.PackgeCaller;
import com.broadbandcollection.billing.activity.BaseApplication;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PackageList;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChangePackgeActivity extends Activity {

	RadioButton cashOption, chequeOption, edcOption;
	RadioButton renewOption, nowOption, adjOption;

	Spinner spinnerList;
	TextView price;

	private String updateFrom;

	public static String rslt = "";
	public static Map<String, PackageList> mapPackageList;
	public static String strXML = "";
	public static String adjStringVal = "";

	static String extStorageDirectory = Environment
			.getExternalStorageDirectory().toString();
	final static String xml_folder = "cnergee";
	final static String TARGET_BASE_PATH = extStorageDirectory + "/"
			+ xml_folder + "/";
	
	public String xml_file_postFix = "PackageList.xml";
	public String xml_file;
	public String xml_file_with_path;
	
	//final static String xml_file = TARGET_BASE_PATH + "PackageList.xml";
	public static Context context;
	public static boolean isAdjOptionClick = false;

	String username;
	String selItem, AreaCode, AreaCodeFilter, CurrentPlan;
	String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate, DiscountPackRate,DiscountPackName,ConnectionTypeId;
	double OldPlanRate,NewPlanRate;
	//String previousPlanRate = "";
	 AdditionalAmountDetailsSOAP additionalAmountDetailsSOAP;

	Utils utils = new Utils();
	AdjustmentWebService adjustmentWebService = null;
	PackageListWebService packageListWebService = null;
	
	public static final String backBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.changepackage.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.search.screen.INTENT";
	
	BundleHelper bundleHelper;
	Bundle extras1;
	LocationManager locationManager;
	public static String getUpdateDataString="";
	AlertDialog  alert ;
	AlertDialog.Builder   alertDialogBuilder;
	ProgressDialog prgDialog1;
	TableLayout tableLayoutadditional;
	String sel_package_id="";
	Double Oustanding_Add_Amt;
	TextView tv_Outstanding_Add_Amount;
	TextView tvLabel_outstanding;
	Double NewPlanRate_send=0.0;
	String MemberId="";
	RadioButton rb_sms_pg,rb_time,rb_volume;
	RadioGroup rg_group,rg_time_volume;
	String pg_sms_renewal_type="";
	
	String PackageType="";
	String advance_type_for_volume_pkg="";
	Double PoolRate=0.0;
	@Override
	 public void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFinishSHowDetails);

	   }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
		}
		AppConstants1.APP_OPEN=false;
		
	}
	@SuppressWarnings("static-access")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_packge);
		BaseApplication.getEventBus().register(this);
		
		rb_sms_pg=(RadioButton) findViewById(R.id.rb_sms_pg);
		
		SharedPreferences sharedPreferences12 = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0);
		Utils.log("PG SMS", "is"+sharedPreferences12.getBoolean("sms_pg", false));
		if(!sharedPreferences12.getBoolean("sms_pg", false)){
			rb_sms_pg.setVisibility(View.GONE);
		}
		else{
			if(Utils.ACTION_TAKE.equalsIgnoreCase("Reactivate")){
				rb_sms_pg.setVisibility(View.GONE);
			}
		}
		
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
		
		
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mFinishSHowDetails, new IntentFilter("finish_showdeatils"));
		mapPackageList = new HashMap<String, PackageList>();
		adjStringVal = "";
		isAdjOptionClick = false;
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		String	 AppVersion= Utils.GetAppVersion(ChangePackgeActivity.this);
		
		Authobj=new AuthenticationMobile();
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
		
		tv_Outstanding_Add_Amount=(TextView) findViewById(R.id.tv_Outstanding_Add_Amount);
		tvLabel_outstanding=(TextView)findViewById(R.id.tvLabel_outstanding);
		price = (TextView) findViewById(R.id.price);
		price.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(tableLayoutadditional.getVisibility()==View.VISIBLE){
					tableLayoutadditional.setVisibility(View.GONE);
				}
				else{
					tableLayoutadditional.setVisibility(View.VISIBLE);
				}
			}
		});
		context = this;
		extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}
		tableLayoutadditional=(TableLayout)findViewById(R.id.tableLayoutadditional);
		tableLayoutadditional.setVisibility(View.GONE);
		username = bundleHelper.getCurrentExtras().getString("username");
		selItem = bundleHelper.getCurrentExtras().getString("selItem");
		MemberId= bundleHelper.getCurrentExtras().getString("MemberId");
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
		
		DiscountPackRate= bundleHelper.getCurrentExtras().getString("discountpackrate");
		DiscountPackName= bundleHelper.getCurrentExtras().getString("discountedPack");
		//ConnectionTypeId= bundleHelper.getCurrentExtras().getString("ConnectionTypeId");
		ConnectionTypeId=this.getIntent().getStringExtra("ConnectionTypeId");
		Oustanding_Add_Amt=this.getIntent().getDoubleExtra("Oustanding_Add_Amt", 0.0);
		PackageType= bundleHelper.getCurrentExtras().getString("PackageType");
		PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);
		/*Utils.log("ChangePackgeActivity rate",": "+DiscountPackRate);
		Utils.log("ChangePackgeActivity name ",": "+DiscountPackName);
		Utils.log("ConnectionTypeId name ",": "+ConnectionTypeId);
		Utils.log("MemberLogin ID","is: "+subscriberID);*/
		if(Oustanding_Add_Amt==0.0){
			//tv_Outstanding_Add_Amount.setVisibility(View.GONE);
			//tvLabel_outstanding.setVisibility(View.GONE);
		}
		tv_Outstanding_Add_Amount.setText(""+Oustanding_Add_Amt);
		setAreaCode(AreaCode);
		setAreaCodeFilter(AreaCodeFilter);

		renewOption = (RadioButton) findViewById(R.id.radioRenew);
		nowOption = (RadioButton) findViewById(R.id.radioNow);
		adjOption = (RadioButton) findViewById(R.id.radioAdj);
		
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
		
	/*	renewOption.setChecked(false);
		nowOption.setChecked(false);
		adjOption.setChecked(false);*/
		
		renewOption.setOnClickListener(updateFromOptionOnClickListener);
		nowOption.setOnClickListener(updateFromOptionOnClickListener);
		adjOption.setOnClickListener(updateFromOptionOnClickListener);

		cashOption = (RadioButton) findViewById(R.id.radioCash);
		chequeOption = (RadioButton) findViewById(R.id.radioCheque);
		edcOption = (RadioButton) findViewById(R.id.radioEDC);
		
		RadioGroup rg_group=(RadioGroup)findViewById(R.id.radioPayMode);
		
		/*rg_group.removeAllViews();
		
		rg_group.addView(cashOption);
		rg_group.addView(chequeOption);
		rg_group.addView(edcOption);
		rg_group.addView(rb_sms_pg);*/
		
		if(! Utils.ACCEPT_CHEQUE){
			chequeOption.setVisibility(View.GONE);
		}

		
		cashOption.setOnClickListener(paymentTypeOptionOnClickListener);
		chequeOption.setOnClickListener(paymentTypeOptionOnClickListener);
		edcOption.setOnClickListener(paymentTypeOptionOnClickListener);

		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				
				/*Intent intent = new Intent(ChangePackgeActivity.this,
						ShowDetailsActivity.class);
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				intent.putExtra(backBundelPackage1, extras1);
				//intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				startActivity(intent);*/
			}
		});
		setAuthenticationMobile();
		
		spinnerList = (Spinner) this.findViewById(R.id.planList);
		
		xml_file = ConnectionTypeId+"_"+AreaCode+"_"+xml_file_postFix;
		
		/*Log.i("******* XML FILE ******** ", xml_file);*/
		xml_file_with_path =  TARGET_BASE_PATH + xml_file;
		/*Log.i("******* XML FILE ******** ", xml_file_with_path);*/
		
		if (isXMLFile()) {
			setPackageList();
			if(utils.isOnline(ChangePackgeActivity.this)){
				new GetPackageAsyncTask().execute();
			}
		} else {
			packageListWebService = new PackageListWebService();
			packageListWebService.execute((String)null);		
		}

		spinnerList.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				/*renewOption.setChecked(false);
				nowOption.setChecked(false);
				adjOption.setChecked(false);*/
				RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
				radioGroup.clearCheck();
				
				String selecte_plan = spinnerList.getSelectedItem().toString();
				if(!selecte_plan.equalsIgnoreCase("Select Package")){
					setPlanDetails();
					price.setText(Double.toString(NewPlanRate));
					Log.e("price data",":"+price.getText().toString()+"  "+NewPlanRate);
					new GetAdditionalAmountAsyncTask().execute();
				}else{
					Toast.makeText(ChangePackgeActivity.this, "Please select valid package from the list", Toast.LENGTH_LONG).show();
					price.setText("0");
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				price.setText("0");
			}
		});
		
	rb_sms_pg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences("CNERGEE", 0);
					
					String selecte_plan = spinnerList.getSelectedItem().toString();
					if(selecte_plan.contains("Select Package")){
						Toast.makeText(ChangePackgeActivity.this,
								"Please select valid package from the list",
								Toast.LENGTH_LONG).show();
											
						RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
						radioGroup.clearCheck();
						
					}					
					else{
					boolean isOk = false;
					
					if(renewOption.isChecked()){
						isOk = true;
					}else if(nowOption.isChecked()){
						isOk = true;
					}else if(adjOption.isChecked()){
						isOk = true;
					}else{
						isOk = false;
					}
					
					if(!isOk){
						Toast.makeText(ChangePackgeActivity.this,
								"Please select valid update from option",
								Toast.LENGTH_LONG).show();
						
					}
					else{
					if(sharedPreferences.getBoolean("sms_pg", false)){
						alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
						final EditText et_mob_number=new EditText(ChangePackgeActivity.this);
						if(PrimaryMobileNo!=null)
						et_mob_number.setText(PrimaryMobileNo);
						alertDialogBuilder.setTitle("Confirmation");
				        alertDialogBuilder.setMessage("Please Confirm Mobile Number")
				        //alertDialogBuilder.setMessage("Are you sure?")
				        .setCancelable(false)
				        
				        .setPositiveButton("YES",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				            	/*rb_sms_pg.setChecked(false);
				            	if(et_mob_number.length()>0&&et_mob_number.length()==10)
				            		send_sms_pg_request(et_mob_number.getText().toString());
				            	else
				            		Toast.makeText(ChangePackgeActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();*/
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
				            		Toast.makeText(ChangePackgeActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
				                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
				            }
				        });
					}
					else{
						alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
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

	private boolean isXMLFile() {
	
		File file = new File(TARGET_BASE_PATH,xml_file );
		boolean exists = file.exists();
		
		if (!exists) {
			// It returns false if File or directory does not exist
			if (!file.isFile()) {
				return false;
			} else {
				return true;
			}
		} else {
			// It returns true if File or directory exists
			return true;
		}
	}

	/*
	 * To populate the package list
	 */
	private void setPackageList() {
		// ProgressDialog progressDialog =
		// ProgressDialog.show(ChangePackgeActivity.this,
		// "Loading Package List","Please Wait...");

		try {
			String str_xml = readPackageListXML();
//			PackageListParsing packageList = new PackageListParsing(str_xml,
//					AreaCode, AreaCodeFilter);
//			mapPackageList = packageList.getMapPackageList();
			
			Utils.log("Map Package List","is"+mapPackageList.size());
			//String key = AreaCode + "~" + AreaCodeFilter + "~" + CurrentPlan;
			/*if (!mapPackageList.containsKey(key)) {
				PackageList init_packageList = new PackageList();
				init_packageList.setAreaCode(AreaCode);
				init_packageList.setAreaCodeFilter(AreaCodeFilter);
				init_packageList.setPackageRate("" + PlanRate);
				init_packageList.setPlanName(CurrentPlan);

				mapPackageList.put(key, init_packageList);
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
					context);
		}
		try {
			ArrayAdapter adapterForSpinner = new ArrayAdapter(
					ChangePackgeActivity.this,
					android.R.layout.simple_spinner_item);
			spinnerList.setAdapter(adapterForSpinner);
			Set<String> keys = mapPackageList.keySet();
			Iterator<String> i = keys.iterator();
			List<String> unsortList  = new ArrayList<String>();
			
			Utils.log("Adapter ","size:"+adapterForSpinner.getCount());
			while (i.hasNext()) {
				String key = (String) i.next();
				PackageList pl = (PackageList) mapPackageList.get(key);
				Log.e("Plan Name","is:"+pl.getPlanName());
				Log.e("Plan rate","is:"+pl.getPackageRate());
				if (pl.getAreaCode().equals(AreaCode)&& pl.getAreaCodeFilter().equals(AreaCodeFilter)) {
					if(!pl.getPlanName().equals(CurrentPlan)){
						//Utils.log("Area Code","is:"+AreaCodeFilter);
						//Utils.log("Plan Name","is:"+pl.getPlanName());
						unsortList.add(pl.getPlanName());
					}
					
				}
			}
		
			adapterForSpinner.add("Select Package");
			
			//sort the list
			Collections.sort(unsortList);
			for(String temp: unsortList){
				CharSequence textHolder = "" + temp;
				adapterForSpinner.add(textHolder);
			}
			
			int defaultPosition = adapterForSpinner.getPosition("Select Package");
			// set the default according to value
			spinnerList.setSelection(defaultPosition);

			// android.os.Debug.waitForDebugger();
			// AlertsBoxFactory.showAlert(">>>  "+tmp_val,context );

		} catch (Exception e) {
			e.printStackTrace();
		}
		// progressDialog.dismiss();
	}

	/*
	 * To read the XML file
	 */
	private String readPackageListXML() {

		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		String str_xml = "";

		try {
			Utils.log(">>>XML NAME <<< ", xml_file_with_path);
			br = new BufferedReader(new FileReader(new File(xml_file_with_path)));
			
			// br = new BufferedReader(new
			// InputStreamReader(getAssets().open("xml/PackageList.xml")));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
			str_xml = sb.toString();
			 Utils.log(">>>XML <<< ", str_xml);

		} catch (IOException e) {
			
			Utils.log("Error in Loading", e.toString());
			e.printStackTrace();
			Toast.makeText(ChangePackgeActivity.this,
					"PackageList File Not Found.", Toast.LENGTH_LONG).show();
			Toast.makeText(ChangePackgeActivity.this,
					"Create file cnergee/PackageList.xml", Toast.LENGTH_LONG)
					.show();

		} finally {
			try {
				if(br != null)
					br.close(); // stop reading
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return str_xml;
	}

	OnClickListener updateFromOptionOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			String selecte_plan = spinnerList.getSelectedItem().toString();
			if(selecte_plan.equalsIgnoreCase("Select Package")){
				Toast.makeText(ChangePackgeActivity.this,
						"Please select valid package from the list",
						Toast.LENGTH_LONG).show();
				renewOption.setChecked(false);
				nowOption.setChecked(false);
				adjOption.setChecked(false);
				return;
			}
			
			if (renewOption.isChecked()) {
				updateFrom = "S";
				isAdjOptionClick = false;
				setPlanDetails();
				//price.setText(Double.toString(NewPlanRate));
				price.setText(additionalAmountDetailsSOAP.getPayableAmt());
				pg_sms_renewal_type="R";
				// new UpdatePrice().execute();

			} else if (nowOption.isChecked()) {
				updateFrom = "I";
				isAdjOptionClick = false;
				setPlanDetails();
				//price.setText(Double.toString(NewPlanRate));
				price.setText(additionalAmountDetailsSOAP.getPayableAmt());
				pg_sms_renewal_type="I";
				// new UpdatePrice().execute();
			} else if (adjOption.isChecked()) {
				updateFrom = "I";
				//new AdjustmentWebService().execute();
				adjustmentWebService = new AdjustmentWebService();
				adjustmentWebService.execute((String)null);
				pg_sms_renewal_type="A";
			}
		}
	};

	OnClickListener paymentTypeOptionOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String selecte_plan = spinnerList.getSelectedItem().toString();
			if(selecte_plan.contains("Select Package")){
				Toast.makeText(ChangePackgeActivity.this,
						"Please select valid package from the list",
						Toast.LENGTH_LONG).show();
				
			/*	cashOption.setChecked(false);
				chequeOption.setChecked(false);
				edcOption.setChecked(false);*/
				RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
				radioGroup.clearCheck();
				
			/*	renewOption.setChecked(false);
				nowOption.setChecked(false);
				adjOption.setChecked(false);*/
				
				return;
			}
			boolean isOk = false;
			/*
			RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
			int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
			String radioButtonSelected = "";
			 
			switch (checkedRadioButton) {
			  case R.id.renewOption : radioButtonSelected = "radiobutton1";
			                   	              break;
			  case R.id.nowOption : radioButtonSelected = "radiobutton2";
					                      break;
			  case R.id.radiobutton3 : radioButtonSelected = "radiobutton3";
					                      break;
			}*/
			
			if(renewOption.isChecked()){
				isOk = true;
			}else if(nowOption.isChecked()){
				isOk = true;
			}else if(adjOption.isChecked()){
				isOk = true;
			}else{
				isOk = false;
			}
			
			if(!isOk){
				Toast.makeText(ChangePackgeActivity.this,
						"Please select valid update from option",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (chequeOption.isChecked()) {
				if(is_package_type_valid()){
				if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
				if(!isAdjOptionClick)
					setPlanDetails();
				
				if(NewPlanRate == 0){
					AlertsBoxFactory.showAlert("Package price is zero.",context );
					return;
				}
			    Intent finishIntent = new Intent("finish_showdetails");	
                LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
				
				finish();
				Intent intent = new Intent(ChangePackgeActivity.this,
						ChequeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("username", "" + username);
				bundle.putString("updatefrom", "" + updateFrom);
				bundle.putString("MemberId",  MemberId);
				bundle.putString("subscriberID", subscriberID);
				bundle.putDouble("PlanRate",NewPlanRate_send);
				bundle.putString("CurrentPlan", str_planname);
				bundle.putString("PackageListCode", PackageListCode);
				bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
				bundle.putString("SecondryMobileNo", SecondryMobileNo);
				bundle.putString("AreaCode", AreaCode);
				bundle.putString("AreaCodeFilter", AreaCodeFilter);
				bundle.putBoolean("isPlanchange", true);
				bundle.putString("IsAutoReceipt", IsAutoReceipt);
				bundle.putString("backAction", "CP");
				bundle.putString("PaymentDate",PaymentDate);
				bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
				
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
			} else if (edcOption.isChecked()) {
				if(is_package_type_valid()){
				if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
				if(!isAdjOptionClick)
					setPlanDetails();
				
				if(NewPlanRate == 0){
					AlertsBoxFactory.showAlert("Package price is zero.",context );
					return;
				}
				SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CNERGEE", 0);
				//if(sharedPreferences.getBoolean("creditcard", true)){
					
					//if(sharedPreferences.getBoolean("user_card_allow", true)){
						
						Intent finishIntent = new Intent("finish_showdetails");
			                LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
					finish();
				Intent intent = new Intent(ChangePackgeActivity.this, CardActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("username", "" + username);
				bundle.putString("updatefrom", "" + updateFrom);
				bundle.putString("MemberId",  MemberId);
				bundle.putString("subscriberID", subscriberID);
				bundle.putDouble("PlanRate",NewPlanRate_send);
				bundle.putString("CurrentPlan", str_planname);
				bundle.putString("PackageListCode", PackageListCode);
				bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
				bundle.putString("SecondryMobileNo", SecondryMobileNo);
				bundle.putString("AreaCode", AreaCode);
				bundle.putString("AreaCodeFilter", AreaCodeFilter);
				bundle.putBoolean("isPlanchange", true);
				bundle.putString("IsAutoReceipt", IsAutoReceipt);
				bundle.putString("backAction", "CP");
				bundle.putString("PaymentDate",PaymentDate);
				bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
				
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra("com.cnergee.billing.card.screen.INTENT", bundle);
				intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
				intent.putExtra(backBundelPackage1, extras1);				
				intent.putExtra("ConnectionTypeId", ConnectionTypeId);
				intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
				intent.putExtra("PoolRate", PoolRate);


				startActivity(intent);
					/*}
					else{
						
						 alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
					        alertDialogBuilder.setMessage("Payment can not be made using this option.")
					        .setCancelable(false)
					        .setPositiveButton("OK",
					                new DialogInterface.OnClickListener(){
					            public void onClick(DialogInterface dialog, int id){
					               
					            }
					        });
					       
					        alert  = alertDialogBuilder.create();
					        alert.show();
					}*/
				/*}
				else{
					
					 alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
				        alertDialogBuilder.setMessage("Payment can not be made using this option.")
				        .setCancelable(false)
				        .setPositiveButton("OK",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				               
				            }
				        });
				       
				        alert  = alertDialogBuilder.create();
				        alert.show();
				}*/
				}
				else{
					AlertsBoxFactory.showAlert("Package price is zero.",context );
					return;	
				}
			}
			} else if (cashOption.isChecked()) {
				if(is_package_type_valid()){
				if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
				if(!isAdjOptionClick)
					setPlanDetails();
				
				if(NewPlanRate == 0){
					AlertsBoxFactory.showAlert("Package price is zero.",context );
					return;
				}
				
				  Intent finishIntent = new Intent("finish_showdetails");	
	                LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
				finish();
				Intent intent = new Intent(ChangePackgeActivity.this,
						CashActivity.class);
				
				Bundle bundle = new Bundle();
				bundle.putString("username", "" + username);
				bundle.putString("updatefrom", "" + updateFrom);
				bundle.putString("MemberId",  MemberId);
				bundle.putString("subscriberID", subscriberID);
				bundle.putDouble("PlanRate", NewPlanRate_send);
				bundle.putString("CurrentPlan", str_planname);
				bundle.putString("PackageListCode", PackageListCode);
				bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
				bundle.putString("SecondryMobileNo", SecondryMobileNo);
				bundle.putString("AreaCode", AreaCode);
				bundle.putString("AreaCodeFilter", AreaCodeFilter);
				bundle.putBoolean("isPlanchange", true);
				bundle.putString("IsAutoReceipt", IsAutoReceipt);
				bundle.putString("backAction", "CP");
				bundle.putString("PaymentDate",PaymentDate);
				bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
				
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra("com.cnergee.billing.cash.screen.INTENT", bundle);
				intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
				intent.putExtra(backBundelPackage1, extras1);
				intent.putExtra("ConnectionTypeId", ConnectionTypeId);
				intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
				intent.putExtra("PoolRate", PoolRate);
				
				startActivity(intent);
				}
				}
				else{
					AlertsBoxFactory.showAlert("Package price is zero.",context );
					return;	
				}
			}
		}
	};

	/*
	 * Write the XML file
	 */
	private void writeXMLFile() {
		// Log.i(">>>XML <<< ", strXML);
		
		try {
			File direct = new File(extStorageDirectory + "/" + xml_folder);
			 if(direct.exists())
			 {
				 direct.delete();
				 Toast.makeText(ChangePackgeActivity.this, "File Deleted", Toast.LENGTH_LONG);
				// Utils.log("File","Deleted");
			 }
			if (!direct.exists()) {
				direct.mkdirs(); // directory is created;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		FileOutputStream fOut = null;
		OutputStreamWriter xmlOutWriter = null;

		try {
			File xmlFile = new File(xml_file_with_path);
			xmlFile.createNewFile();
			fOut = new FileOutputStream(xmlFile);
			xmlOutWriter = new OutputStreamWriter(fOut);
			xmlOutWriter.append(strXML);
			//Utils.log("String in","XML is "+strXML);
			Toast.makeText(getBaseContext(),
					"Done writing '/cnergee/'"+xml_file+" file.",
					Toast.LENGTH_LONG).show();
			// new ReadPackageListXML().execute();

		}catch(IOException io){
			io.printStackTrace();
			Toast.makeText(getBaseContext(), io.getMessage(), Toast.LENGTH_LONG)
			.show();
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		} finally {
			try {
				if(xmlOutWriter != null){
					xmlOutWriter.flush();
					xmlOutWriter.close();
				}
				if(xmlOutWriter != null){
					fOut.flush();
					fOut.close();
				}

			} catch (IOException io) {
			}
			strXML = null;
		}
		setPackageList();
	}

	/*
	 * private class UpdatePrice extends AsyncTask<String, Void, Void>{ private
	 * ProgressDialog Dialog = new ProgressDialog( ChangePackgeActivity.this);
	 * 
	 * protected void onPreExecute() {
	 * Dialog.setMessage("Please Wait.. Set Price."); Dialog.show(); } protected
	 * void onPostExecute(Void unused) { Dialog.dismiss(); }
	 * 
	 * @Override protected Void doInBackground(String... arg0) {
	 * price.setText(str_planrate); return null; } }
	 */
	private class AdjustmentWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				ChangePackgeActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait.. Recalulate Price.");
			Dialog.show();
			Dialog.setCancelable(false);
		}
		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			adjustmentWebService = null;
			
		}
		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
			adjustmentWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					NewPlanRate_send = Double.parseDouble(adjStringVal);
					price.setText(Double.toString(NewPlanRate_send));
					isAdjOptionClick = true;
				} catch (NumberFormatException nue) {
					RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
					radioGroup.clearCheck();
					if(adjStringVal.equalsIgnoreCase("anyType{}")){
						AlertsBoxFactory.showAlert("Conversion is not possible.", context);
					}else{
						AlertsBoxFactory.showAlert(adjStringVal, context);
					}
				}

			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				AdjustmentCaller adjCaller = new AdjustmentCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(),
						getApplicationContext().getResources().getString(
								R.string.METHOD_GET_PACKAGE_ADJUSTMENT_AMOUNT),getAuthenticationMobile());
				adjCaller.setUsername(username);
				setPlanDetails();
				adjCaller.setNewPlan(str_planname);
				adjCaller.setAreaCode(AreaCode);
				adjCaller.setAreaCodeFilter(AreaCodeFilter);
				adjCaller.setSubscriberID(subscriberID);

				adjCaller.join();
				adjCaller.start();
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

	/*
	 * private class ReadPackageListXML extends AsyncTask<String, Void, Void> {
	 * private ProgressDialog Dialog = new ProgressDialog(
	 * ChangePackgeActivity.this);
	 * 
	 * protected void onPreExecute() {
	 * Dialog.setMessage("Please Wait.. Populating Package List.");
	 * Dialog.show(); }
	 * 
	 * protected void onPostExecute(Void unused) { if (mapPackageList != null) {
	 * try{ ArrayAdapter adapterForSpinner = new ArrayAdapter(
	 * ChangePackgeActivity.this, android.R.layout.simple_spinner_item);
	 * spinnerList.setAdapter(adapterForSpinner);
	 * 
	 * // Log.i(">>> mapPackageList.size() <<< ", // ""+mapPackageList.size());
	 * Set<String> keys = mapPackageList.keySet(); String tmp_val = "";
	 * Iterator<String> i = keys.iterator();
	 * 
	 * while(i.hasNext()) { String key = (String) i.next(); PackageList pl =
	 * (PackageList) mapPackageList.get(key);
	 * 
	 * Log.i(" >>>>KEY ",""+pl.getPlanName());
	 * Log.i(" >>>>KEY ",""+pl.getAreaCode());
	 * Log.i(" >>>>KEY ",""+pl.getAreaCodeFilter());
	 * Log.i(" >>>>KEY ",""+pl.getPackageRate());
	 * 
	 * if (pl.getAreaCode().equals(AreaCode) &&
	 * pl.getAreaCodeFilter().equals(AreaCodeFilter)) { CharSequence textHolder
	 * = "" + key; adapterForSpinner.add(textHolder); tmp_val =
	 * tmp_val.concat(textHolder.toString()); } if (isCancelled()) break; }
	 * AlertsBoxFactory.showAlert(">>>  "+tmp_val,context );
	 * 
	 * 
	 * }catch(Exception e){ e.printStackTrace(); } } Dialog.dismiss();
	 * this.cancel(true);
	 * 
	 * }
	 * 
	 * @Override protected Void doInBackground(String... arg0) { try { String
	 * str_xml = readPackageListXML(); PackageListParsing packageList = new
	 * PackageListParsing(str_xml); mapPackageList =
	 * packageList.getMapPackageList(); } catch (Exception e) {
	 * e.printStackTrace();
	 * AlertsBoxFactory.showErrorAlert("Error web-service response "
	 * +e.toString(),context ); } return null; } }
	 */

	private class PackageListWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				ChangePackgeActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Please Wait.. Loading XML file from server.");
			Dialog.show();
		}
		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			packageListWebService = null;
		}
		protected void onPostExecute(Void unused) {
			//Utils.log("onPostExecute"," executed");
			
			Dialog.dismiss();
			packageListWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				writeXMLFile();
			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				/*PackgeCaller caller = new PackgeCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_DISCOUNT),getAuthenticationMobile());*/
				PackgeCaller caller = new PackgeCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								utils.getDynamic_Url(), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_CONNCETION_TYPE_PACKAGE_LIST),getAuthenticationMobile());
				caller.setSubscriberId(subscriberID);
				caller.setAreaCode(getAreaCode());
				caller.setAreaCodeFilter(getAreaCodeFilter());
				caller.setConnectionTypeId(ConnectionTypeId);
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

	private String str_planname,str_planrate;
	private void setPlanDetails() {
		
		String selecte_plan = spinnerList.getSelectedItem().toString();
		String map_kye = AreaCode + "~" + AreaCodeFilter + "~" + selecte_plan;
		
		PackageList packageList = (PackageList) mapPackageList.get(map_kye);
		if (packageList != null) {
			
			str_planname = packageList.getPlanName();
			//str_planrate = packageList.getPackageRate();
			str_planrate = packageList.getPackageRate();
			sel_package_id=packageList.getPackageId();

			Log.e("setPlanDetails",":"+sel_package_id+" "+str_planname+" "+str_planrate);

			try{
				if(selecte_plan.equalsIgnoreCase(DiscountPackName))
				{
					NewPlanRate = Double.parseDouble(DiscountPackRate);
				/*	Utils.log("Same","Package: "+NewPlanRate);
					Utils.log("Selected","Package: "+selecte_plan);
					Utils.log("DiscountPackName","Package: "+DiscountPackName);*/
				}
				else
				{
					NewPlanRate = Double.parseDouble(packageList.getPackageRate());
					/*Utils.log("Different","Package: "+NewPlanRate);
					Utils.log("Selected","Package: "+selecte_plan);
					Utils.log("DiscountPackName","Package: "+DiscountPackName);*/
				}

				Log.e("NewPlanRate setPlanDet",":"+NewPlanRate);
			}catch(NumberFormatException nu){
				NewPlanRate = 0;
			}
		
		} else {
			str_planname = CurrentPlan;
			//str_planrate = PlanRate + "";
			NewPlanRate = 0;
		}
		//Utils.log("New Plan","Rate: "+NewPlanRate);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
/*	private boolean isPlanchange(){
		boolean isChange = false;
		
		if(CurrentPlan.equals(str_planname)){
			isChange = true;
		}else{
			isChange = false;
		}
		return isChange;
	}
*/	
	private AuthenticationMobile Authobj = null;
	
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
		Authobj.setAppVersion(Utils.GetAppVersion(ChangePackgeActivity.this));
		return Authobj;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return AreaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	/**
	 * @return the areaCodeFilter
	 */
	public String getAreaCodeFilter() {
		return AreaCodeFilter;
	}

	/**
	 * @param areaCodeFilter the areaCodeFilter to set
	 */
	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
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
	
	
	private BroadcastReceiver mFinishSHowDetails = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	      //  Utils.log("Service","Message");
	        //  ... react to local broadcast message
	      finish();
	        
	        
	    }
	};
	
	 public  void showGPSDisabledAlertToUser(){
		 alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
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
	 
	 public class GetPackageAsyncTask extends AsyncTask<String, Void, Void>{
			
		String getDataResult="";
		
		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			 prgDialog1= new ProgressDialog(ChangePackgeActivity.this);
			 prgDialog1.setMessage("Please wait...");
			 prgDialog1.setCancelable(false);
			 prgDialog1.show();
			 getUpdateDataString="";
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Utils.log("Update ","Package");
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
				prgDialog1.dismiss();
			try{
				//Utils.log("getDataResult","is: "+getDataResult);
				if(getDataResult.length()>0){
					if(getDataResult.equalsIgnoreCase("ok")){
						if(getUpdateDataString.length()>0){
							if(getUpdateDataString.equalsIgnoreCase("PackageUpdate")){

								new PackageListWebService().execute();
								new UpdatePackageAsyncTask().execute();		
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
						if(getDataResult.equalsIgnoreCase("ok")){
							if(getUpdateDataString.length()>0){
								if(!getUpdateDataString.equalsIgnoreCase("PackageUpdate")){
									
									//new PackageListWebService().execute();
											
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
	 
	 public class GetAdditionalAmountAsyncTask extends AsyncTask<String, Void, Void>{
		
		 String additionalAmtResult;
		 String additionalAmtResponse;
		 String AppVersion;
		 ProgressDialog progressDialog;
		
		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog=new ProgressDialog(ChangePackgeActivity.this);
			progressDialog.show();
			  AppVersion= Utils.GetAppVersion(ChangePackgeActivity.this);
			 additionalAmountDetailsSOAP= new AdditionalAmountDetailsSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), utils.getDynamic_Url(), getString(R.string.METHOD_ADDITONAL_AMOUNT));
			
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
								
			additionalAmtResult=additionalAmountDetailsSOAP.getAdditionalAmount(Authobj, subscriberID, utils.getAppUserName(),sel_package_id);
			return null;
		}
		 
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(additionalAmtResult.length()>0){
				if(additionalAmtResult.equalsIgnoreCase("OK")){
					// final Dialog dialog= new Dialog(ShowDetailsActivity.this);
					// LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					// View v=inflater.inflate(R.layout.additional_amount_xml, null);
				
					 TextView tvPackRate=(TextView)findViewById(R.id.tvPackrate);
					 TextView tvAddAmtDetail=(TextView)findViewById(R.id.AddAmount);
					 TextView tvDiscount=(TextView)findViewById(R.id.DiscAmt);
					 TextView tvPayable=(TextView)findViewById(R.id.TotalPayAmt);
					 TextView tvFineAmt=(TextView)findViewById(R.id.FineAmt);
					 TextView tvDaysFineAmt=(TextView)findViewById(R.id.DaysFineAmt);
					 TableRow tableRowPack=(TableRow)findViewById(R.id.tableRowPackage);
					 TableRow tableRowAddtAmount=(TableRow)findViewById(R.id.tableRowAddtAmount);
					 TableRow tableRowDiscount=(TableRow)findViewById(R.id.tableRowDiscount);
					 TableRow tableFine=(TableRow)findViewById(R.id.tableFine);
					 TableRow tableTotal=(TableRow)findViewById(R.id.tableTotal);
					 TableRow tableDaysFineAmt=(TableRow)findViewById(R.id.tableDaysFineAmt);
					 if(additionalAmountDetailsSOAP.getPayableAmt()!=null)

					 NewPlanRate=Double.valueOf(additionalAmountDetailsSOAP.getPayableAmt());
					 if(additionalAmountDetailsSOAP.getPayableAmt()!=null)
					 NewPlanRate_send=Double.valueOf(additionalAmountDetailsSOAP.getPayableAmt());
					// price.setPaintFlags(price.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
					// rate.setText(Html.fromHtml("<u>"+additionalAmountDetailsSOAP.getPayableAmt())+" (Click here for details)</u>");
					 
					 price.setText(additionalAmountDetailsSOAP.getPayableAmt()+ " (Click Here for Details)");

					Log.e("additionalAmountDesSOAP",":"+NewPlanRate+" "+price.getText().toString());

					 // dialog.setTitle("Amount Details");
					 if(additionalAmountDetailsSOAP.getPice()!=null){
						 if(additionalAmountDetailsSOAP.getPice().equalsIgnoreCase("0")){
							 tableRowPack.setVisibility(View.GONE);
						 }
						 else{
							 tvPackRate.setText(additionalAmountDetailsSOAP.getPice());
						 }
					 }
					 if(additionalAmountDetailsSOAP.getAdditionalAmt()!=null){
						 if(additionalAmountDetailsSOAP.getAdditionalAmt().equalsIgnoreCase("0")){
							 tableRowAddtAmount.setVisibility(View.GONE);
						 }
						 else{
							 tvAddAmtDetail.setText(additionalAmountDetailsSOAP.getAdditionalAmt());
						 }
					 }
					 if(additionalAmountDetailsSOAP.getDisount_amt()!=null){
						 if(additionalAmountDetailsSOAP.getDisount_amt().equalsIgnoreCase("0")){
							 tableRowDiscount.setVisibility(View.GONE);
						 }
						 else{
							 tvDiscount.setText(additionalAmountDetailsSOAP.getDisount_amt());
						 }
					 }
					 if(additionalAmountDetailsSOAP.getFine_amt()!=null){
						 if(additionalAmountDetailsSOAP.getFine_amt().equalsIgnoreCase("0")){
							 tableFine.setVisibility(View.GONE);
						 }
						 else{
							 tvFineAmt.setText(additionalAmountDetailsSOAP.getFine_amt());
						 }
					 }
					 
					 if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
						 if(additionalAmountDetailsSOAP.getPayableAmt().equalsIgnoreCase("0")){
							 tableTotal.setVisibility(View.GONE);
						 }
						 else{
							 tvPayable.setText(additionalAmountDetailsSOAP.getPayableAmt());
						 }
					 }
					 
					 if(additionalAmountDetailsSOAP.getDays_Fine_Amt()!=null){
						 if(additionalAmountDetailsSOAP.getDays_Fine_Amt().equalsIgnoreCase("0")){
							 tableDaysFineAmt.setVisibility(View.GONE);
						 }
						 else{
							 tvDaysFineAmt.setText(additionalAmountDetailsSOAP.getDays_Fine_Amt());
						 }
					 }
					// dialog.setContentView(v);
					
					// dialog.show();
				}
			}
		}
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
			commonSOAP.setRequest(request);
							
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
			pi.setValue(str_planname);
			pi.setType(String.class);
			request.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.setName("RenewalType");
			pi.setValue(pg_sms_renewal_type);
			pi.setType(String.class);
			request.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.setName("Amount");
			pi.setValue(NewPlanRate_send);
			pi.setType(String.class);
			request.addProperty(pi);
			
			
		
			commonSOAP.setRequest(request);
			
			final ProgressDialog prgDialog= new ProgressDialog(ChangePackgeActivity.this);
			prgDialog.setMessage("Please wait..");
			prgDialog.setCancelable(false);
			prgDialog.show();
			new CommonAsyncTask(ChangePackgeActivity.this){
				
				
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
										
										alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
								        alertDialogBuilder.setMessage(res_array[0])
								        .setCancelable(false)
								        .setPositiveButton("OK",
								                new DialogInterface.OnClickListener(){
								            public void onClick(DialogInterface dialog, int id){
								            	ChangePackgeActivity.this.finish();
												BaseApplication.getEventBus().post(new FinishEvent(RenewActivity.class.getSimpleName()));
												BaseApplication.getEventBus().post(new FinishEvent(ShowDetailsActivity.class.getSimpleName()));
								            }
								        });
								       
								        alert  = alertDialogBuilder.create();
								        alert.show();
										 	
									}
									else{
										rb_sms_pg.setChecked(false);
										alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
								        alertDialogBuilder.setMessage(res_array[0])
								        .setCancelable(false)
								        .setPositiveButton("OK",
								                new DialogInterface.OnClickListener(){
								            public void onClick(DialogInterface dialog, int id){
								            	ChangePackgeActivity.this.finish();
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
									alertDialogBuilder  = new AlertDialog.Builder(ChangePackgeActivity.this);
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
						AlertsBoxFactory.showAlert("Web Service Not Executed", ChangePackgeActivity.this);
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
