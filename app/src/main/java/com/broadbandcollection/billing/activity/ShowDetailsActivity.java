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
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.AdditionalAmountDetailsSOAP;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.MemberData;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.squareup.otto.Subscribe;

public class ShowDetailsActivity extends Activity {

	public static MemberData memberData;
	public boolean isReqAltMobNo = false;
	public static Context context;
	BundleHelper bundleHelper;
	public String execteFrom;
	
	public static final String backBundelPackage = "com.cnergee.billing.search.screen.INTENT";
	public static final String backBundelPackage1 = "com.cnergee.billing.showpaymentpickup.screen.INTENT";
	public static final String currentBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
	
	String password;
	LocationManager locationManager;
	AlertDialog  alert ;
	AlertDialog.Builder   alertDialogBuilder;
	Button btnLaunchComplaint;
	TableRow tableRow;
	//TextView tvAddtionalAmt;
	 TextView rate;
	SharedPreferences sharedPreferences;
	Utils utils;
	String connectionTypeId="";
	TableLayout tableLayoutadditional;
	Double Outstanding_Amt=0.0;
	TextView tvOutstanding_Bal_Amt,tv_outstanding_add_label,tv_outstanding_bal_label;
	EditText et_Outstanding_Add_Amt;
	Double Outstanding_Add_Amt=0.0;
	String Get_Renew_Status="";
	 Button btnrenew,btnchangepackage;
	 AdditionalAmountDetailsSOAP additionalAmountDetailsSOAP;
	 Double PoolRate=0.0;
	protected void onPause() {
		super.onPause();
		if(alert!=null){
			if(alert.isShowing()){
			alert.dismiss();
		}
		}
	//Log.i(" >>>> "," IN PAUSE ");
		
		//finish();
		
		AppConstants1.APP_OPEN=false;
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		//Log.i(" >>>> "," IN RESUME ");
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
		BaseApplication.getEventBus().register(this);
		setContentView(R.layout.activity_show_details);
		context = this;
		tableLayoutadditional=(TableLayout)findViewById(R.id.tableLayoutadditional);
		//tableLayoutadditional.setVisibility(View.GONE);
		tvOutstanding_Bal_Amt=(TextView)findViewById(R.id.tv_outstanding_bal);
		et_Outstanding_Add_Amt=(EditText)findViewById(R.id.et_outstanding_add);
		tv_outstanding_add_label=(TextView) findViewById(R.id.tv_outstanding_add_label);
		tv_outstanding_bal_label=(TextView) findViewById(R.id.tv_outstanding_bal_label);
		et_Outstanding_Add_Amt.setText("0.0");
		//tvAddtionalAmt=(TextView) findViewById(R.id.AdditionalRate);
		if(AppConstants1.hasGPSDevice(this)){
			AppConstants1.GPS_AVAILABLE=true;
			//Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
		}
		else{
			AppConstants1.GPS_AVAILABLE=false;
			//Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
		}
		
		sharedPreferences = getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils= new Utils();
		utils.setSharedPreferences(sharedPreferences);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("GpsStatus"));
		
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mFinishShowDetails, new IntentFilter("finish_showdetails"));
		bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
		//Bundle extras = getIntent().getExtras();
		if (bundleHelper.getCurrentExtras() == null) {
			return;
		}
	/*	Log.i(" INT ",""+bundleHelper.getCurrentExtras().getString("username"));
		Log.i(" INT ",""+bundleHelper.getCurrentExtras().getString("subscriberID"));
*/		
		final String username = bundleHelper.getCurrentExtras().getString("username");
		final String selItem = bundleHelper.getCurrentExtras().getString("selItem");
		execteFrom = bundleHelper.getCurrentExtras().getString("execteFrom");
		String message=bundleHelper.getCurrentExtras().getString("message");
		//Utils.log("Message","is"+message);
		if(execteFrom.equalsIgnoreCase("paypickup")){
			password = bundleHelper.getCurrentExtras().getString("password");
		}
		
		Utils.log("MemberId","is:"+bundleHelper.getCurrentExtras().getString("MemberId"));
		memberData = new MemberData();
		
		memberData.setMemberId(bundleHelper.getCurrentExtras().getString("MemberId"));
		memberData.setSubscriberID(bundleHelper.getCurrentExtras().getString("subscriberID"));
		memberData.setCurrentPlan(bundleHelper.getCurrentExtras().getString("CurrentPlan"));
		// memberData.setPackageListCode(extras.getString("PackageListCode"));
		memberData.setSubscriberName(bundleHelper.getCurrentExtras().getString("SubscriberName"));
		memberData.setSubscriberStatus(bundleHelper.getCurrentExtras().getString("SubscriberStatus"));
		memberData.setStrExpiryDate(bundleHelper.getCurrentExtras().getString("expiryDate"));
		memberData.setPlanRate(bundleHelper.getCurrentExtras().getDouble("PlanRate"));
		memberData.setPrimaryMobileNo(bundleHelper.getCurrentExtras().getString("PrimaryMobileNo"));
		memberData.setAreaCode(bundleHelper.getCurrentExtras().getString("AreaCode"));
		memberData.setAreaCodeFilter(bundleHelper.getCurrentExtras().getString("AreaCodeFilter"));
		memberData.setIsAutoReceipt(bundleHelper.getCurrentExtras().getString("IsAutoReceipt"));
		memberData.setCheckForRenewal(bundleHelper.getCurrentExtras().getString("CheckForRenewal"));
		memberData.setAreaName(bundleHelper.getCurrentExtras().getString("AreaName"));
		memberData.setISPName(bundleHelper.getCurrentExtras().getString("ISPName"));
		memberData.setPaymentDate(bundleHelper.getCurrentExtras().getString("PaymentDate"));
		memberData.setMemberAddress(bundleHelper.getCurrentExtras().getString("MemberAddress"));
		memberData.setAdditionAmountDetails(bundleHelper.getCurrentExtras().getString("AdditionAmountDetails"));
		memberData.setDiscounPackRate(bundleHelper.getCurrentExtras().getString("discountpackrate"));
		memberData.setDiscounPackName(bundleHelper.getCurrentExtras().getString("discountedPack"));
		memberData.setConnectionTypeId(bundleHelper.getCurrentExtras().getString("ConnectionTypeId"));
		memberData.setOutstanding_Amt(bundleHelper.getCurrentExtras().getDouble("Outstanding_Amt"));
		memberData.setIsQos(bundleHelper.getCurrentExtras().getBoolean("IsQos"));
		memberData.setPackageType(bundleHelper.getCurrentExtras().getString("PackageType"));
		Utils.log("Outstanding Amt ",": "+bundleHelper.getCurrentExtras().getDouble("Outstanding_Amt"));
		Outstanding_Amt=bundleHelper.getCurrentExtras().getDouble("Outstanding_Amt");
		/*Utils.log("ChangePackgeActivity rate",": "+bundleHelper.getCurrentExtras().getString("discountpackrate"));
		Utils.log("ShowDetailsActivity",": "+bundleHelper.getCurrentExtras().getString("discountedPack"));
		Utils.log("Connection Type Id",": "+bundleHelper.getCurrentExtras().getString("ConnectionTypeId"));*/
		
		tableRow=(TableRow) findViewById(R.id.tableRowMessage);
		TextView tvMessage = (TextView)findViewById(R.id.tvMessage);
		
		if(message!=null){
			tableRow.setVisibility(View.VISIBLE);
			
			if(message.length()>0){
				if (message.equalsIgnoreCase("anyType{}")) {

					tableRow.setVisibility(View.GONE);
				} else {
					tvMessage.setText(message);
				}
			}
			
		}
		else{
			tableRow.setVisibility(View.GONE);	
		}
		if(Outstanding_Amt==0.0){
			tvOutstanding_Bal_Amt.setVisibility(View.GONE);
			et_Outstanding_Add_Amt.setVisibility(View.GONE);
			tv_outstanding_add_label.setVisibility(View.GONE);
			tv_outstanding_bal_label.setVisibility(View.GONE);
		}
		tvOutstanding_Bal_Amt.setText(""+Outstanding_Amt);
		et_Outstanding_Add_Amt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					if(!(Outstanding_Amt<0.0)){
				if(Double.valueOf(s.toString())>Outstanding_Amt){
					
					AlertsBoxFactory.showAlert("Outstanding Add Amount can not be greater than Outstanding Balance Amount", ShowDetailsActivity.this);
					et_Outstanding_Add_Amt.setText("0.0");
				}
				else{
					
				}
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TextView title = (TextView)findViewById(R.id.headerView);
		title.setText(R.string.app_sub_details_title);
		
		final TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.setText(memberData.getSubscriberName());

		rate = (TextView) findViewById(R.id.Rate);
		rate.setText(Double.toString(memberData.getPlanRate()));
		
		rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(Utils.isOnline(ShowDetailsActivity.this)){
					new GetAdditionalAmountAsyncTask().execute();
				}*/
				if(tableLayoutadditional.getVisibility()==View.VISIBLE){
					tableLayoutadditional.setVisibility(View.GONE);
				}
				else{
					tableLayoutadditional.setVisibility(View.VISIBLE);
				}
			}
		});
		
		final TextView packageName = (TextView) findViewById(R.id.packageName);
		packageName.setText(memberData.getCurrentPlan());

		final TextView Status = (TextView) findViewById(R.id.Status);
		Status.setText(memberData.getSubscriberStatus());
		
		final TextView AreaName = (TextView) findViewById(R.id.AreaName);
		AreaName.setText(memberData.getAreaName());
		
		final TextView ISPName = (TextView) findViewById(R.id.ISPName);
		ISPName.setText(memberData.getISPName());
		
	//	tvAddtionalAmt.setText(Html.fromHtml(memberData.getAdditionAmountDetails()));
		
		//final TextView address = (TextView) findViewById(R.id.address);
		//address.setText(memberData.getMemberAddress());
		if(Utils.isOnline(ShowDetailsActivity.this)){
			new GetAdditionalAmountAsyncTask().execute();
		}
		btnLaunchComplaint=(Button) findViewById(R.id.btnLaunchComplaint);
		btnLaunchComplaint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(ShowDetailsActivity.this,ComplaintActivity.class);
				i.putExtra("SubscriberId",memberData.getSubscriberID());
				i.putExtra("AreaCode",memberData.getAreaCode());
				i.putExtra("AreaCodeFilter",memberData.getAreaCodeFilter());
				
				startActivity(i);
			}
		});
		
		final TextView view = (TextView) findViewById(R.id.addText);
		view.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
				/*Toast.makeText(ShowDetailsActivity.this,
						memberData.getMemberAddress(),
						150000).show();*/
			  AlertsBoxFactory.showAlert("Address",memberData.getMemberAddress(),context );
		  }

		});
		
		final TextView Expiry = (TextView) findViewById(R.id.Expiry);
		/*
		 * SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		 * Date dateExp = null; try { dateExp = new
		 * SimpleDateFormat("dd-MM-yyyy").parse(memberData.getStrExpiryDate());
		 * 
		 * } catch (ParseException e) { Utils.log(" ERROR ",
		 * "Exception i Date-Formatering"); e.printStackTrace(); }
		 * Expiry.setText(dateExp.parse(string));
		 */

		String str_exp_date = memberData.getStrExpiryDate();
		if (str_exp_date != null) {
			String[] str_split = str_exp_date.split("T");
			String[] str_split_date = str_split[0].split("-");
			String str_date = str_split_date[2]+"-"+str_split_date[1]+"-"+str_split_date[0];
			
			Expiry.setText(str_date);
		}

		final TextView Mobile = (TextView) findViewById(R.id.Mobile);
		Mobile.setText(memberData.getPrimaryMobileNo());
		
		if(memberData.getPrimaryMobileNo().equals("0")){
			isReqAltMobNo = true;
		}else{
			isReqAltMobNo = false;
		}
		final EditText altMobile = (EditText) findViewById(R.id.altMobile);
		
		/*Log.i(" ALT MOB...",""+altMobile.getText().toString());
		
		if (!TextUtils.isEmpty(altMobile.getText())) {
			memberData.setSecondryMobileNo(altMobile.getText().toString()
					.trim());
		} else {
			memberData.setSecondryMobileNo("");
		}*/
		
	btnrenew = (Button) findViewById(R.id.renew);
	btnchangepackage = (Button) findViewById(R.id.changepackage);
		final Button updatestatus = (Button)findViewById(R.id.updatestatus);
		
		
		
		if(getString(R.string.app_run_mode).equalsIgnoreCase("dev")){
			memberData.setCheckForRenewal("GO");
		}
		
		Utils.log("CHECK FOR RENEWAL",":"+memberData.getCheckForRenewal());
		
		if(!memberData.getCheckForRenewal().equalsIgnoreCase("GO")){
			/*btnrenew.setVisibility(View.VISIBLE);
			btnchangepackage.setVisibility(View.VISIBLE);
		}else{*/
			btnrenew.setVisibility(View.GONE);
			btnchangepackage.setVisibility(View.GONE);
			//Log.i("**** RED",""+getString(R.color.label_red_color));
			AlertsBoxFactory.showAlertColorTxt("#FF0000",""+memberData.getCheckForRenewal(),context );
		}
		
		

		//memberData.getPlanRate()
	
		if(memberData.getPlanRate() == 0){
			btnrenew.setVisibility(View.GONE);
		/*}else{
			btnrenew.setVisibility(View.VISIBLE);*/
		}
		
		btnrenew.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean isOky = true;
				//if(Get_Renew_Status.length()>0){
					//if(Get_Renew_Status.equalsIgnoreCase("Reactivate")){
						
					//}
					//else{
						
						/*if(isReqAltMobNo){
							
							if (TextUtils.isEmpty(altMobile.getText().toString().trim())) {
								isOky = false;
								AlertsBoxFactory.showAlert("Please enter alternate mobile no.",context );
								return;
							}
						}
						memberData.setSecondryMobileNo(altMobile.getText().toString().trim());
						
						if(isOky){
							finish();
							Intent intent = new Intent(ShowDetailsActivity.this,
									RenewActivity.class);
							Bundle bundle = new Bundle();
							bundle = BundleHelper.getMemberDataBundelForShowDetailsActivity(bundle, memberData);
							
							bundle.putString("username", "" + username);
							bundle.putString("selItem", "" + selItem);
							bundle =getBundleMemberData(bundle);
							
							
							intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
							intent.putExtra("com.cnergee.billing.renew.screen.INTENT", bundle);
							intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
							intent.putExtra("Oustanding_Add_Amt", et_Outstanding_Add_Amt.length()>0?Double.valueOf(et_Outstanding_Add_Amt.getText().toString()):"0.0");
							startActivity(intent);
						}*/
					//	}
					
				//}else{
				
				if(isReqAltMobNo){
					
					if (TextUtils.isEmpty(altMobile.getText().toString().trim())) {
						isOky = false;
						AlertsBoxFactory.showAlert("Please enter alternate mobile no.",context );
						return;
					}
				}
				memberData.setSecondryMobileNo(altMobile.getText().toString().trim());
				
				if(isOky){
				//	finish();
					if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
					
						Intent intent = new Intent(ShowDetailsActivity.this,
								RenewActivity.class);
						Bundle bundle = new Bundle();
						bundle = BundleHelper.getMemberDataBundelForShowDetailsActivity(bundle, memberData);

						bundle.putString("username", "" + username);
						bundle.putString("selItem", "" + selItem);
						bundle =getBundleMemberData(bundle);

						intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
						intent.putExtra("com.cnergee.billing.renew.screen.INTENT", bundle);
						intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
						intent.putExtra("Oustanding_Add_Amt", et_Outstanding_Add_Amt.length()>0?Double.valueOf(et_Outstanding_Add_Amt.getText().toString()):"0.0");
						intent.putExtra("PoolRate", PoolRate);

						startActivity(intent);
					}
					else{
						AlertsBoxFactory.showAlert("Package price is zero.",context );
						return;
					}
				}
				//}
			}
		});

		btnchangepackage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				boolean isOky = true;
				
				if(isReqAltMobNo){
					
					if (TextUtils.isEmpty(altMobile.getText().toString().trim())) {
						isOky = false;
						AlertsBoxFactory.showAlert("Please enter alternate mobile no.",context );
						return;
					}
				}
				
				memberData.setSecondryMobileNo(altMobile.getText().toString().trim());
				
				//finish();
				Intent intent = new Intent(ShowDetailsActivity.this,
						PackgedetailActivity.class);
				
				Bundle bundle = new Bundle();
				
				bundle = BundleHelper.getMemberDataBundelForShowDetailsActivity(bundle, memberData);
				
				
				bundle.putString("username", "" + username);
				bundle.putString("selItem", "" + selItem);
				bundle =getBundleMemberData(bundle);
				
				
				
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra("com.cnergee.billing.changepackage.screen.INTENT", bundle);
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				
				intent.putExtra("ConnectionTypeId", bundleHelper.getCurrentExtras().getString("ConnectionTypeId"));
				//Utils.log("Sending Intent",":"+bundleHelper.getCurrentExtras().getString("ConnectionTypeId"));
				intent.putExtra("Oustanding_Add_Amt", et_Outstanding_Add_Amt.length()>0?Double.valueOf(et_Outstanding_Add_Amt.getText().toString()):0.0);
				intent.putExtra("PoolRate", PoolRate);
				startActivity(intent);
			}
		});

		final Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				
				if(execteFrom.equalsIgnoreCase("paypickup")){
					/*Log.i(" PAYMENT ... "," IN ");*/
					finish();
					/*Intent intent = new Intent(ShowDetailsActivity.this,
							PaymentPickupActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username",username);
					bundle.putString("password",password);
					//intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
					intent.putExtra("com.cnergee.billing.showpaymentpickup.screen.INTENT", bundle);
					startActivity(intent);*/
					
				}else{
					finish();
					/*Intent intent = new Intent(ShowDetailsActivity.this,
							SearchVendorActivity.class);
					intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
					startActivity(intent);*/
				}
			}
		});
		
		
		updatestatus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				boolean isOky = true;
				
				if(isReqAltMobNo){
					
					if (TextUtils.isEmpty(altMobile.getText().toString().trim())) {
						isOky = false;
						AlertsBoxFactory.showAlert("Please enter alternate mobile no.",context );
						return;
					}
				}
				
				memberData.setSecondryMobileNo(altMobile.getText().toString().trim());
				
				finish();
				
				Intent intent = new Intent(ShowDetailsActivity.this,
						UpdateStatusActivity.class);
				Bundle bundle = new Bundle();
				
				bundle = BundleHelper.getMemberDataBundelForShowDetailsActivity(bundle, memberData);
				bundle =getBundleMemberData(bundle);
				
				bundle.putString("username", "" + username);
				bundle.putString("password",password);
				
				bundle.putString("selItem", "" + selItem);
				
				intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
				intent.putExtra("com.cnergee.billing.updatestatus.screen.INTENT", bundle);
				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
				intent.putExtra("is_postpaid", false);
				startActivity(intent);
				
			}
		});
		
	}

	private Bundle getBundleMemberData(Bundle bundle){
		
		bundle.putString("MemberId", getMemberData()
				.getMemberId());
		bundle.putString("subscriberID", getMemberData()
				.getSubscriberID());
		bundle.putString("PackageListCode", getMemberData()
				.getPackageListCode());
		bundle.putString("CurrentPlan", getMemberData().getCurrentPlan());
		bundle.putDouble("PlanRate", getMemberData().getPlanRate());
		bundle.putString("PackageType", getMemberData().getPackageType());
		
		 
		return bundle;
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
	
	@Override
	protected void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFinishShowDetails);

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
			
			
			private BroadcastReceiver mFinishShowDetails = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			       // Utils.log("Service","Message");
			        //  ... react to local broadcast message
			       finish();
			    }
			};
			
			 public  void showGPSDisabledAlertToUser(){
				  alertDialogBuilder  = new AlertDialog.Builder(ShowDetailsActivity.this);
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
			 
		public class GetAdditionalAmountAsyncTask extends AsyncTask<String, Void, Void>{
				
				 String additionalAmtResult;
				 String additionalAmtResponse;
				 String AppVersion;
				 ProgressDialog progressDialog;
				
				 @Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					progressDialog=new ProgressDialog(ShowDetailsActivity.this);
					progressDialog.show();
					  AppVersion= Utils.GetAppVersion(ShowDetailsActivity.this);
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
										
					additionalAmtResult=additionalAmountDetailsSOAP.getAdditionalAmount(Authobj, memberData.getSubscriberID(), utils.getAppUserName(),"0");
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
							 TextView tvPoolRate=(TextView)findViewById(R.id.PoolRate);
							 TextView tvPayable=(TextView)findViewById(R.id.TotalPayAmt);
							 TextView tvFineAmt=(TextView)findViewById(R.id.FineAmt);
							 TextView tvDaysFineAmt=(TextView)findViewById(R.id.DaysFineAmt);
							 TableRow tableRowPack=(TableRow)findViewById(R.id.tableRowPackage);
							 TableRow tableRowAddtAmount=(TableRow)findViewById(R.id.tableRowAddtAmount);
							 TableRow tableRowDiscount=(TableRow)findViewById(R.id.tableRowDiscount);
							 TableRow tableRowPoolRate=(TableRow)findViewById(R.id.tableRowPoolRate);
							 TableRow tableFine=(TableRow)findViewById(R.id.tableFine);
							 TableRow tableTotal=(TableRow)findViewById(R.id.tableTotal);
							 TableRow tableDaysFineAmt=(TableRow)findViewById(R.id.tableDaysFineAmt);
							 
							/* Button btnOK=(Button)v.findViewById(R.id.btnOk);
							 btnOK.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});*/
							 
							 rate.setPaintFlags(rate.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
							 if(additionalAmountDetailsSOAP.getPayableAmt()!=null)
							 memberData.setPlanRate(Double.valueOf(additionalAmountDetailsSOAP.getPayableAmt()));
							// rate.setText(Html.fromHtml("<u>"+additionalAmountDetailsSOAP.getPayableAmt())+" (Click here for details)</u>");
							 rate.setText(additionalAmountDetailsSOAP.getPayableAmt()+ " (Click Here for Details)");
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
									 Utils.ACCEPT_CHEQUE=true;
									 
								 }
								 else{
									 tvFineAmt.setText(additionalAmountDetailsSOAP.getFine_amt());
									 Utils.ACCEPT_CHEQUE=false;
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
							 
							 if(additionalAmountDetailsSOAP.getPool_Rate()!=null){
								 if(additionalAmountDetailsSOAP.getPool_Rate().equalsIgnoreCase("0")){
									 tableRowPoolRate.setVisibility(View.GONE);
									 PoolRate=0.0;
								 }
								 else{
									 PoolRate=Double.valueOf(additionalAmountDetailsSOAP.getPool_Rate());
									 tvPoolRate.setText(additionalAmountDetailsSOAP.getPool_Rate());
								 }
							 }
							 
							 if(additionalAmountDetailsSOAP.getCheck_Bounce_Reason()!=null){
								
								 if(additionalAmountDetailsSOAP.getCheck_Bounce_Reason().length()>0){
									 if(additionalAmountDetailsSOAP.getCheck_Bounce_Reason().contains("#")){
										 String[] split_type=additionalAmountDetailsSOAP.getCheck_Bounce_Reason().split("#");
										 Get_Renew_Status=split_type[1];
										if(split_type[0].equalsIgnoreCase("CQDD")&&split_type[1].equalsIgnoreCase("Renew")){
											Utils.log("Reason",":"+split_type[0]);
											Utils.log("status",":"+split_type[1]);
											btnchangepackage.setVisibility(View.VISIBLE);
											 btnrenew.setText(split_type[1]);
											 Utils.ACTION_TAKE="Renew";
										 }
										 else{
											 if(split_type[0].equalsIgnoreCase("CQDD")&&split_type[1].equalsIgnoreCase("Reactivate")){
												 
												 Utils.log("Reason",":"+split_type[0]);
													Utils.log("status",":"+split_type[1]);
													btnchangepackage.setVisibility(View.GONE);
													btnrenew.setText(split_type[1]);
													Utils.ACTION_TAKE="Reactivate";
												 }
										 }
									 }
									 else{
								 if(additionalAmountDetailsSOAP.getCheck_Bounce_Reason().equalsIgnoreCase("CQDD")){
									 Utils.ACTION_TAKE="Renew";
									 findViewById(R.id.changepackage).setVisibility(View.GONE);
								 }
								 else{
									 Utils.ACTION_TAKE="Renew";
									 findViewById(R.id.changepackage).setVisibility(View.VISIBLE);
								 }}
								 }
								
							 }
							 
							 
							 if(memberData.isIsQos()){
									btnchangepackage.setVisibility(View.GONE);
								}
							 
							// dialog.setContentView(v);
							
							// dialog.show();
						}
					}
				}
			 }
			 
			 @Subscribe
				public void	onFinishEvent(FinishEvent event){
				
					if(this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
						this.finish();
					}					
				}
}
