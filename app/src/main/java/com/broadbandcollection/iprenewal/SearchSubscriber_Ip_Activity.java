package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.broadbandcollection.R;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class SearchSubscriber_Ip_Activity extends Activity {
	
EditText et_search;
Button btn_search,btn_reset,btn_back;
Utils utils = new Utils();
SharedPreferences	sharedPreferences;
AuthenticationMobile Authobj = new AuthenticationMobile();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_subscriber_ip);
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		initializeControls();
		

		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_search.getText().length()>0)
				call_searchsubscriber();
				else
				Toast.makeText(SearchSubscriber_Ip_Activity.this, "Please enter Subscriber Id", Toast.LENGTH_LONG).show();
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchSubscriber_Ip_Activity.this.finish();
			}
		});
	}
	
	@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			et_search.setText("");
		}
	
	public void initializeControls(){
		et_search=(EditText) findViewById(R.id.et_search);
		
		btn_search=(Button)findViewById(R.id.btn_search);
		btn_reset=(Button)findViewById(R.id.btn_reset);
		btn_back=(Button)findViewById(R.id.btn_back);
		
		String AppVersion=Utils.GetAppVersion(SearchSubscriber_Ip_Activity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
	}
	
	public void call_searchsubscriber(){
		CommonSOAP commonSOAP= new CommonSOAP(
				utils.getDynamic_Url(),
getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_GET_IP_SALE_MEMBER_DETAILS));
		
		SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_GET_IP_SALE_MEMBER_DETAILS));
		
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
		pi.setName("SubscriberID");
		pi.setValue(et_search.getText().toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AreaId");
		pi.setValue(0);
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolId");
		pi.setValue(0);
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Param");
		pi.setValue("S");
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		commonSOAP.setRequest(request);
		
		final ProgressDialog prgDialog= new ProgressDialog(SearchSubscriber_Ip_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new CommonAsyncTask(SearchSubscriber_Ip_Activity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					parse_subscriber_details(result.get(1));
				}
				else{
					AlertsBoxFactory.showAlert("Web Service Not Executed", SearchSubscriber_Ip_Activity.this);
				}
			}
		}.execute(commonSOAP);
		
	}

	public void parse_subscriber_details(String json){
		try {
			JSONObject mainJson= new JSONObject(json);
			if(mainJson.has("NewDataSet")){
				if(mainJson.get("NewDataSet") instanceof JSONObject){
					JSONObject newDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(newDataSetJson.has("MemberIPSaleDetails")){
						if(newDataSetJson.get("MemberIPSaleDetails") instanceof JSONObject){
							JSONObject memberJson=newDataSetJson.getJSONObject("MemberIPSaleDetails");
							SubscriberDetails_Ip subscriberDetails_Ip= new SubscriberDetails_Ip();
							
							subscriberDetails_Ip.setAreaID(memberJson.optString("AreaID",""));
							subscriberDetails_Ip.setAreaName(memberJson.optString("AreaName"));
							subscriberDetails_Ip.setCheckRenewal(memberJson.optString("CheckRenewal"));
							subscriberDetails_Ip.setCompanyID(memberJson.optString("CompanyID"));
							subscriberDetails_Ip.setConnectionTypeId(memberJson.optString("ConnectionTypeId"));
							subscriberDetails_Ip.setCreatedOn(memberJson.optString("CreatedOn"));
							subscriberDetails_Ip.setCurrentRenewalType(memberJson.optString("CurrentRenewalType"));
							subscriberDetails_Ip.setIPAddress(memberJson.optString("IPAddress"));
							subscriberDetails_Ip.setIPExpireDate(memberJson.optString("IPExpireDate"));
							subscriberDetails_Ip.setIPNodeType(memberJson.optString("IPNodeType"));
							subscriberDetails_Ip.setIPPoolId(memberJson.optString("IPPoolId"));
							subscriberDetails_Ip.setIPPoolName(memberJson.optString("IPPoolName"));
							subscriberDetails_Ip.setIsCC(memberJson.optString("IsCC"));
							subscriberDetails_Ip.setIsCreation(memberJson.optString("IsCreation"));
							subscriberDetails_Ip.setISPId(memberJson.optString("ISPId"));
							subscriberDetails_Ip.setIsRenewal(memberJson.optString("IsRenewal"));
							subscriberDetails_Ip.setIsWebservice(memberJson.optString("IsWebservice"));
							subscriberDetails_Ip.setMACAddress(memberJson.optString("MACAddress"));
							subscriberDetails_Ip.setMemberID(memberJson.optString("MemberID"));
							subscriberDetails_Ip.setMemberLoginID(memberJson.optString("MemberLoginID"));
							subscriberDetails_Ip.setMemberName(memberJson.optString("MemberName"));
							subscriberDetails_Ip.setMobileNoprimary(memberJson.optString("MobileNoprimary"));
							subscriberDetails_Ip.setMobileNosecondry(memberJson.optString("MobileNosecondry"));
							subscriberDetails_Ip.setReceiptNo(memberJson.optString("ReceiptNo"));
							subscriberDetails_Ip.setServerID(memberJson.optString("ServerID"));
							subscriberDetails_Ip.setServerIPorName(memberJson.optString("ServerIPorName"));
							subscriberDetails_Ip.setServerType(memberJson.optString("ServerType"));
							subscriberDetails_Ip.setServerType1(memberJson.optString("ServerType1"));
							subscriberDetails_Ip.setStatus(memberJson.optString("Status"));
							subscriberDetails_Ip.setPrice(memberJson.optString("TotalAmount"));
							subscriberDetails_Ip.setAddress(memberJson.optString("BillAddress"));
							subscriberDetails_Ip.setIPSalePackageName(memberJson.optString("IPSalePackageName"));
							subscriberDetails_Ip.setIpSalePackageId(memberJson.optString("IpSalePackageId"));
							subscriberDetails_Ip.setForDays(memberJson.optString("ForDays"));
							subscriberDetails_Ip.setISP_Name(memberJson.optString("ISPName"));
							Utils.log("IpSalePackageId","is:"+memberJson.optString("IpSalePackageId"));
							
							if(subscriberDetails_Ip.getCheckRenewal().equalsIgnoreCase("GO")){
								Intent subscriber_ip_details_intent= new Intent(SearchSubscriber_Ip_Activity.this, SubscriberDetails_Ip_Activity.class);
								subscriber_ip_details_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
								startActivity(subscriber_ip_details_intent);
							}
							else{								
								AlertsBoxFactory.showAlert(subscriberDetails_Ip.getCheckRenewal(), SearchSubscriber_Ip_Activity.this);
							}
					}
				}
				else{
					AlertsBoxFactory.showAlert("Subsciber Not Found", SearchSubscriber_Ip_Activity.this);
				}
				}
				else{
					AlertsBoxFactory.showAlert("Subsciber Not Found", SearchSubscriber_Ip_Activity.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
