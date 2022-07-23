package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.activity.BaseApplication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;

import com.broadbandcollection.widgets.SlidingPanel;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Change_Ip_Activity extends Activity {

	Spinner sp_ip_packages,sp_pool;
	TextView tv_selected_ip;
	RadioButton rb_cash,rb_cheque,rb_edc;
	Button btn_back;
	GridView gv_ip_address;
	SlidingPanel slide_panel_ip;
	RelativeLayout rl_ip_list,rl_close;
	ImageView iv_close;
	
	SharedPreferences	sharedPreferences;
	AuthenticationMobile Authobj = new AuthenticationMobile();
	SubscriberDetails_Ip subscriberDetails_Ip;
	IPSalePackageAdapter ipSalePackageAdapter;
	PoolDetailsAdapter poolDetailsAdapter;
	IPAddresseAdapter ipAddresseAdapter;
	Utils utils = new Utils();
	private Animation animShow, animHide;
	ArrayList<Boolean> alBooleans= new ArrayList<Boolean>();
	private boolean is_panel_visible=false;
	public static int color_position=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_ip);
		initializeControls();
		subscriberDetails_Ip=(SubscriberDetails_Ip) this.getIntent().getSerializableExtra(Utils.SUBSCRIBER_IP);
		BaseApplication.getEventBus().register(this);
		call_get_IP_packages_and_IP_pool();
		
		sp_ip_packages.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				IPSalePackages ipSalePackages=(IPSalePackages)arg0.getItemAtPosition(arg2);
				subscriberDetails_Ip.setIpSalePackageId(ipSalePackages.getIPSalePackageId());
				subscriberDetails_Ip.setForDays(ipSalePackages.getValidity());
				subscriberDetails_Ip.setPrice(ipSalePackages.getTotalAmount());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sp_pool.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				PoolDetails poolDetails=(PoolDetails)arg0.getItemAtPosition(arg2);
			
					subscriberDetails_Ip.setIPPoolId(poolDetails.getIPPoolID());
					subscriberDetails_Ip.setIPPoolName(poolDetails.getIPPoolName());
					if(!poolDetails.getIPPoolID().equalsIgnoreCase("0")){
						call_get_IP_Addresses(poolDetails.getIPPoolID());
						
						
					}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		gv_ip_address.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				IP_Address ip_Address=(IP_Address)arg0.getItemAtPosition(arg2);
				slide_panel_ip.setVisibility(View.GONE);
				slide_panel_ip.startAnimation(animHide);
				tv_selected_ip.setText(ip_Address.getIPADDRESS());
				subscriberDetails_Ip.setIPAddress(ip_Address.getIPADDRESS());
				color_position=arg2;
				TextView tv1=(TextView)arg0.findViewById(R.id.tv_ip_addr);
				tv1.setTextColor(R.color.label_red_color);
				ipAddresseAdapter.notifyDataSetChanged();
				is_panel_visible=false;
				
			}
		});
		
		rl_ip_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!is_panel_visible){
				if(ipAddresseAdapter!=null){
					if(ipAddresseAdapter.getCount()>0){
						slide_panel_ip.setVisibility(View.VISIBLE);
						slide_panel_ip.startAnimation(animShow);
						is_panel_visible=true;
					}
				}
				}
				else{
					is_panel_visible=false;
				}
			}
		});
		
		iv_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
						slide_panel_ip.setVisibility(View.GONE);
						slide_panel_ip.startAnimation(animHide);
						is_panel_visible=false;
				
			}
		});
	}
	
	public void initializeControls(){
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		sp_ip_packages=(Spinner) findViewById(R.id.sp_ip_packages);
		sp_pool=(Spinner) findViewById(R.id.sp_pool_id);
		rb_cash=(RadioButton)findViewById(R.id.rb_cash);
		rb_cheque=(RadioButton)findViewById(R.id.rb_cheque);
		rb_edc=(RadioButton)findViewById(R.id.rb_edc);
		btn_back=(Button)findViewById(R.id.btn_back);
		tv_selected_ip=(TextView)findViewById(R.id.tv_selected_ip);
		gv_ip_address=(GridView) findViewById(R.id.gv_ip_address);
		slide_panel_ip=(SlidingPanel)findViewById(R.id.slide_panel_ip);
		rl_ip_list=(RelativeLayout)findViewById(R.id.rl_show_ip);
		rl_close=(RelativeLayout)findViewById(R.id.rl_close);
		iv_close=(ImageView)findViewById(R.id.iv_close);
		slide_panel_ip.setVisibility(View.GONE);
		animShow = AnimationUtils.loadAnimation( this, R.anim.popup_show);
    	animHide = AnimationUtils.loadAnimation( this, R.anim.popup_hide);
    	    	   	
		btn_back.setOnClickListener(clickListener);
		rb_cash.setOnClickListener(clickListener);
		rb_cheque.setOnClickListener(clickListener);
		rb_edc.setOnClickListener(clickListener);
		
		String AppVersion=Utils.GetAppVersion(Change_Ip_Activity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
	}
	
	
	public void call_get_IP_packages_and_IP_pool(){
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
		pi.setValue(subscriberDetails_Ip.getMemberLoginID());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AreaId");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getAreaID()));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolId");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getIPPoolId()));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Param");
		pi.setValue("PO");
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		commonSOAP.setRequest(request);
		
		final ProgressDialog prgDialog= new ProgressDialog(Change_Ip_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new CommonAsyncTask(Change_Ip_Activity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					parse_IP_packages(result.get(1));
					parse_IP_Pool(result.get(1));
				}
				else{
					AlertsBoxFactory.showAlert("Web Service Not Executed", Change_Ip_Activity.this);
				}
			}
		}.execute(commonSOAP);
		
	}
	
	
	public void parse_IP_packages(String json){
		try {
			
			ArrayList<IPSalePackages> alIpSalePackages= new ArrayList<IPSalePackages>();
			
			IPSalePackages ipSalePackages1= new IPSalePackages();
			
			ipSalePackages1.setBaseAmount("0");
			ipSalePackages1.setIPSalePackageId("0");
			ipSalePackages1.setIPSalePackageName("Select IP Package:");
			ipSalePackages1.setServiceTax("0");
			ipSalePackages1.setTotalAmount("0");
			ipSalePackages1.setValidity("0");
			alIpSalePackages.add(ipSalePackages1);
			
			JSONObject mainJson= new JSONObject(json);
			if(mainJson.has("NewDataSet")){
				if(mainJson.get("NewDataSet") instanceof JSONObject){
					JSONObject newDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(newDataSetJson.has("PackageDetails")){
						
						
						
						if(newDataSetJson.get("PackageDetails") instanceof JSONObject){
							
							IPSalePackages ipSalePackages= new IPSalePackages();
							JSONObject PackagesJsonObject=newDataSetJson.getJSONObject("PackageDetails");
							ipSalePackages.setBaseAmount(PackagesJsonObject.optString("BaseAmount",""));
							ipSalePackages.setIPSalePackageId(PackagesJsonObject.optString("IPSalePackageId",""));
							ipSalePackages.setIPSalePackageName(PackagesJsonObject.optString("IPSalePackageName",""));
							ipSalePackages.setServiceTax(PackagesJsonObject.optString("ServiceTax",""));
							ipSalePackages.setTotalAmount(PackagesJsonObject.optString("TotalAmount",""));
							ipSalePackages.setValidity(PackagesJsonObject.optString("Validity",""));
							
							alIpSalePackages.add(ipSalePackages);
							
					}
						
						if(newDataSetJson.get("PackageDetails") instanceof JSONArray){
							JSONArray PackagesJsonArray=newDataSetJson.getJSONArray("PackageDetails");
							
							for(int i=0;i<PackagesJsonArray.length();i++){
								JSONObject PackagesJsonObject=PackagesJsonArray.getJSONObject(i);
								IPSalePackages ipSalePackages= new IPSalePackages();
								ipSalePackages.setBaseAmount(PackagesJsonObject.optString("BaseAmount",""));
								ipSalePackages.setIPSalePackageId(PackagesJsonObject.optString("IPSalePackageId",""));
								ipSalePackages.setIPSalePackageName(PackagesJsonObject.optString("IPSalePackageName",""));
								ipSalePackages.setServiceTax(PackagesJsonObject.optString("ServiceTax",""));
								ipSalePackages.setTotalAmount(PackagesJsonObject.optString("TotalAmount",""));
								ipSalePackages.setValidity(PackagesJsonObject.optString("Validity",""));
								
								alIpSalePackages.add(ipSalePackages);
								
							}
							
					}
						
						if(alIpSalePackages !=null){
							if(alIpSalePackages.size()>0){
								int position=0;
								ipSalePackageAdapter= new IPSalePackageAdapter(Change_Ip_Activity.this, R.layout.spinner_item , alIpSalePackages);
								sp_ip_packages.setAdapter(ipSalePackageAdapter);
								Utils.log("IpSalePackageId", "is:"+subscriberDetails_Ip.getIpSalePackageId());
								/*if(subscriberDetails_Ip.getIpSalePackageId()!=null){
								if(subscriberDetails_Ip.getIpSalePackageId().length()>0){
									if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
										for(int j=0;j<alIpSalePackages.size();j++){
											IPSalePackages ipSalePackages=alIpSalePackages.get(j);
											if(ipSalePackages.getIPSalePackageId().equalsIgnoreCase(subscriberDetails_Ip.getIpSalePackageId())){
												position=j;
												Utils.log("set position","is:"+j);
											}
										}
										sp_ip_packages.setSelection(position);
									}
								}
							}*/
							}
						}
				}
				else{
					AlertsBoxFactory.showAlert("Pool-Area mapping does not exist!", Change_Ip_Activity.this);
				}
				}
				else{
					AlertsBoxFactory.showAlert("Pool-Area mapping does not exist!", Change_Ip_Activity.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parse_IP_Pool(String json){
		try {
			
			ArrayList<PoolDetails> alPoolDetails= new ArrayList<PoolDetails>();
			
			PoolDetails poolDetails1= new PoolDetails();
			poolDetails1.setIPPoolID("0");
			poolDetails1.setIPPoolName("Select Pool:");
			alPoolDetails.add(poolDetails1);
			
			JSONObject mainJson= new JSONObject(json);
			if(mainJson.has("NewDataSet")){
				if(mainJson.get("NewDataSet") instanceof JSONObject){
					JSONObject newDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(newDataSetJson.has("PoolDetails")){
						
						
						if(newDataSetJson.get("PoolDetails") instanceof JSONObject){
							
							PoolDetails poolDetails = new PoolDetails();
							JSONObject PoolJsonObject=newDataSetJson.getJSONObject("PoolDetails");
							poolDetails.setIPPoolID(PoolJsonObject.optString("IPPoolID",""));
							poolDetails.setIPPoolName(PoolJsonObject.optString("IPPoolName",""));
							
							alPoolDetails.add(poolDetails);						
					}
						
						if(newDataSetJson.get("PoolDetails") instanceof JSONArray){
							JSONArray PoolJsonArray=newDataSetJson.getJSONArray("PoolDetails");
							
							for(int i=0;i<PoolJsonArray.length();i++){
								JSONObject PoolJsonObject=PoolJsonArray.getJSONObject(i);
							
								PoolDetails poolDetails = new PoolDetails();
								poolDetails.setIPPoolID(PoolJsonObject.optString("IPPoolID",""));
								poolDetails.setIPPoolName(PoolJsonObject.optString("IPPoolName",""));
								
								alPoolDetails.add(poolDetails);															
							}
							
					}
						poolDetailsAdapter= new PoolDetailsAdapter(Change_Ip_Activity.this, R.layout.spinner_item, alPoolDetails);
						sp_pool.setAdapter(poolDetailsAdapter);
						
						
				}
				else{
					//AlertsBoxFactory.showAlert("Response is not instance of JSON", Change_Ip_Activity.this);
				}
				}
				else{
					//AlertsBoxFactory.showAlert("Response is not instance of JSON", Change_Ip_Activity.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void call_get_IP_Addresses(String pool_id){
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
		pi.setValue(subscriberDetails_Ip.getMemberLoginID());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AreaId");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getAreaID()));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolId");
		pi.setValue(Integer.valueOf(pool_id));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Param");
		pi.setValue("IP");
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		commonSOAP.setRequest(request);
		
		final ProgressDialog prgDialog= new ProgressDialog(Change_Ip_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new CommonAsyncTask(Change_Ip_Activity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					parse_IP_Address(result.get(1));
					
					
				}
				else{
					AlertsBoxFactory.showAlert("Web Service Not Executed", Change_Ip_Activity.this);
				}
			}
		}.execute(commonSOAP);
		
	}



	public void parse_IP_Address(String json){
		try {
			
			ArrayList<IP_Address> alIp_Addresses= new ArrayList<IP_Address>();
			
			ipAddresseAdapter=null;
			rl_ip_list.setVisibility(View.GONE);
			JSONObject mainJson= new JSONObject(json);
			if(mainJson.has("NewDataSet")){
				if(mainJson.get("NewDataSet") instanceof JSONObject){
					JSONObject newDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(newDataSetJson.has("IPAddress")){
						
						
						if(newDataSetJson.get("IPAddress") instanceof JSONObject){
							
							IP_Address ip_Address= new IP_Address();
							JSONObject IP_Address_JsonObject=newDataSetJson.getJSONObject("IPAddress");
							ip_Address.setIPADDRESS(IP_Address_JsonObject.optString("IPADDRESS",""));
							ip_Address.setIPID(IP_Address_JsonObject.optString("IPID",""));
							ip_Address.setIPPoolID(IP_Address_JsonObject.optString("IPPoolID",""));
							ip_Address.setIsUsed(IP_Address_JsonObject.optBoolean("IsUsed"));
							alIp_Addresses.add(ip_Address);
							
							
					}
						
						if(newDataSetJson.get("IPAddress") instanceof JSONArray){
							JSONArray IP_Address_JsonArray=newDataSetJson.getJSONArray("IPAddress");
							
							for(int i=0;i<IP_Address_JsonArray.length();i++){
								JSONObject IP_Address_JsonObject=IP_Address_JsonArray.getJSONObject(i);
							
								IP_Address ip_Address= new IP_Address();
								
								ip_Address.setIPADDRESS(IP_Address_JsonObject.optString("IPADDRESS",""));
								ip_Address.setIPID(IP_Address_JsonObject.optString("IPID",""));
								ip_Address.setIPPoolID(IP_Address_JsonObject.optString("IPPoolID",""));
								ip_Address.setIsUsed(IP_Address_JsonObject.optBoolean("IsUsed"));
								alIp_Addresses.add(ip_Address);
								
							
							}
							
					}
						
						for(int i=0;i<alIp_Addresses.size();i++){
							alBooleans.add(false);
						}
						
						if(alIp_Addresses!=null){
							if(alIp_Addresses.size()>0){
								ipAddresseAdapter= new IPAddresseAdapter(Change_Ip_Activity.this, R.layout.row_ip_address, alIp_Addresses,alBooleans);
								gv_ip_address.setAdapter(ipAddresseAdapter);
								rl_ip_list.setVisibility(View.VISIBLE);
							}
							else{
								rl_ip_list.setVisibility(View.GONE);
							}
						}
						
						
						slide_panel_ip.setVisibility(View.VISIBLE);
						slide_panel_ip.startAnimation( animShow );
						is_panel_visible=true;
						
				}
				else{
					
					subscriberDetails_Ip.setIPPoolId("0");
					subscriberDetails_Ip.setIPPoolName("0");
					subscriberDetails_Ip.setIPAddress("0");
					tv_selected_ip.setText("");
					AlertsBoxFactory.showAlert("No IP's are in these pool. \n Please select different pool.", Change_Ip_Activity.this);
				}
				}
				else{
					subscriberDetails_Ip.setIPPoolId("0");
					subscriberDetails_Ip.setIPPoolName("0");
					subscriberDetails_Ip.setIPAddress("0");
					tv_selected_ip.setText("");
					AlertsBoxFactory.showAlert("No IP's are in these pool. \n Please select different pool.", Change_Ip_Activity.this);
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(is_panel_visible){
			slide_panel_ip.setVisibility(View.GONE);
			slide_panel_ip.startAnimation( animHide );
			is_panel_visible=false;
		}
		else{
			is_panel_visible=true;
			this.finish();
		}
	}
	
public OnClickListener clickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==btn_back){
				Change_Ip_Activity.this.finish();
			}
			if(v==rb_cash){
				if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
					if(!subscriberDetails_Ip.getIPPoolId().equalsIgnoreCase("0")){
						if(!subscriberDetails_Ip.getIPAddress().equalsIgnoreCase("0")){
						Intent cash_intent= new Intent(Change_Ip_Activity.this, IP_Cash_Activity.class);
						subscriberDetails_Ip.setIs_change_ip(true);
						cash_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
						startActivity(cash_intent);
						}
						else{
							AlertsBoxFactory.showAlert("Please select IP Address", Change_Ip_Activity.this);
						}
					}
					else{
						AlertsBoxFactory.showAlert("Please select IP Pool", Change_Ip_Activity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please select IP Package", Change_Ip_Activity.this);
				}
			}
			if(v==rb_cheque){
				if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
					if(!subscriberDetails_Ip.getIPPoolId().equalsIgnoreCase("0")){
						if(!subscriberDetails_Ip.getIPAddress().equalsIgnoreCase("0")){
						Intent cash_intent= new Intent(Change_Ip_Activity.this, IP_Cheque_Activity.class);
						subscriberDetails_Ip.setIs_change_ip(true);
						cash_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
						startActivity(cash_intent);
						}
						else{
							AlertsBoxFactory.showAlert("Please select IP Address", Change_Ip_Activity.this);
						}
					}
					else{
						AlertsBoxFactory.showAlert("Please select IP Pool", Change_Ip_Activity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please select IP Package", Change_Ip_Activity.this);
				}
			}
			if(v==rb_edc){
				if(sharedPreferences.getBoolean("creditcard", true)){
				
				if(sharedPreferences.getBoolean("user_card_allow", true)){
					
				}
				else{
					AlertsBoxFactory.showAlert("Payment can not be made using this option.", Change_Ip_Activity.this);
				}
			}
			else{
				AlertsBoxFactory.showAlert("Payment can not be made using this option.", Change_Ip_Activity.this);
			}
		}
		
		}
	};
	
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
	
		if(Change_Ip_Activity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			Change_Ip_Activity.this.finish();
		}
		
	}
}
