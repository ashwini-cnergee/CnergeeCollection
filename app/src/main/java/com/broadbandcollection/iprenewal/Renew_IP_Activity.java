package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RadioButton;
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
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Renew_IP_Activity extends Activity {
	TextView tv_date;
	RadioButton rb_cash,rb_cheque,rb_edc;
	Button btn_back;
	Spinner sp_ip_packcges;
	Utils utils = new Utils();
	SharedPreferences	sharedPreferences;
	AuthenticationMobile Authobj = new AuthenticationMobile();
	SubscriberDetails_Ip subscriberDetails_Ip;
	IPSalePackageAdapter ipSalePackageAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_renewal);
		initailzeControls();
		subscriberDetails_Ip=(SubscriberDetails_Ip) this.getIntent().getSerializableExtra(Utils.SUBSCRIBER_IP);
		BaseApplication.getEventBus().register(this);
		call_get_IP_packages();
		sp_ip_packcges.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	}
	
	public void initailzeControls(){
		
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat viewDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = viewDateFormatter.format(c.getTime());

		tv_date=(TextView) findViewById(R.id.tv_date);		
		tv_date.setText(currentDate);
		
		
		rb_cash=(RadioButton)findViewById(R.id.rb_cash);
		rb_cheque=(RadioButton)findViewById(R.id.rb_cheque);
		rb_edc=(RadioButton)findViewById(R.id.rb_edc);
		
		btn_back=(Button)findViewById(R.id.btn_back);
		sp_ip_packcges=(Spinner)findViewById(R.id.sp_ip_packages);
		
		btn_back.setOnClickListener(clickListener);
		rb_cash.setOnClickListener(clickListener);
		rb_cheque.setOnClickListener(clickListener);
		rb_edc.setOnClickListener(clickListener);
		
		String AppVersion=Utils.GetAppVersion(Renew_IP_Activity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
	}
	
	public OnClickListener clickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==btn_back){
				Renew_IP_Activity.this.finish();
			}
			if(v==rb_cash){
				if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
					Intent cash_intent= new Intent(Renew_IP_Activity.this, IP_Cash_Activity.class);
					subscriberDetails_Ip.setIs_change_ip(false);
					cash_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
					startActivity(cash_intent);
				}
				else{
					AlertsBoxFactory.showAlert("Please select IP Package", Renew_IP_Activity.this);
				}
			}
			if(v==rb_cheque){
				if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
					Intent cheque_intent= new Intent(Renew_IP_Activity.this, IP_Cheque_Activity.class);
					subscriberDetails_Ip.setIs_change_ip(false);
					cheque_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
					startActivity(cheque_intent);	
				}
				else{
					AlertsBoxFactory.showAlert("Please select IP Package", Renew_IP_Activity.this);
				}
			}
			if(v==rb_edc){
				if(sharedPreferences.getBoolean("creditcard", true)){
					
					if(sharedPreferences.getBoolean("user_card_allow", true)){
						
					}
					else{
						AlertsBoxFactory.showAlert("Payment can not be made using this option.", Renew_IP_Activity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Payment can not be made using this option.", Renew_IP_Activity.this);
				}
			}
			
		}
	};
	
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
	
		if(Renew_IP_Activity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			Renew_IP_Activity.this.finish();
		}
		
	}
	
	public void call_get_IP_packages(){
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
		pi.setValue(subscriberDetails_Ip.getAreaID());
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolId");
		pi.setValue(subscriberDetails_Ip.getIPPoolId());
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Param");
		pi.setValue("PO");
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		commonSOAP.setRequest(request);
		
		final ProgressDialog prgDialog= new ProgressDialog(Renew_IP_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new CommonAsyncTask(Renew_IP_Activity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					parse_IP_packages(result.get(1));
				}
				else{
					
				}
			}
		}.execute(commonSOAP);
		
	}
	
	public void parse_IP_packages(String json){
		try {
			JSONObject mainJson= new JSONObject(json);
			if(mainJson.has("NewDataSet")){
				if(mainJson.get("NewDataSet") instanceof JSONObject){
					JSONObject newDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(newDataSetJson.has("PackageDetails")){
						
						ArrayList<IPSalePackages> alIpSalePackages= new ArrayList<IPSalePackages>();
						
						IPSalePackages ipSalePackages1= new IPSalePackages();
						
						ipSalePackages1.setBaseAmount("0");
						ipSalePackages1.setIPSalePackageId("0");
						ipSalePackages1.setIPSalePackageName("Select IP Package:");
						ipSalePackages1.setServiceTax("0");
						ipSalePackages1.setTotalAmount("0");
						ipSalePackages1.setValidity("0");
						alIpSalePackages.add(ipSalePackages1);
						
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
								ipSalePackageAdapter= new IPSalePackageAdapter(Renew_IP_Activity.this, R.layout.spinner_item , alIpSalePackages);
								sp_ip_packcges.setAdapter(ipSalePackageAdapter);
								Utils.log("IpSalePackageId", "is:"+subscriberDetails_Ip.getIpSalePackageId());
								if(subscriberDetails_Ip.getIpSalePackageId()!=null){
								if(subscriberDetails_Ip.getIpSalePackageId().length()>0){
									if(!subscriberDetails_Ip.getIpSalePackageId().equalsIgnoreCase("0")){
										for(int j=0;j<alIpSalePackages.size();j++){
											IPSalePackages ipSalePackages=alIpSalePackages.get(j);
											if(ipSalePackages.getIPSalePackageId().equalsIgnoreCase(subscriberDetails_Ip.getIpSalePackageId())){
												position=j;
												Utils.log("set position","is:"+j);
											}
										}
										sp_ip_packcges.setSelection(position);
									}
								}
							}
							}
						}
				}
				else{
					AlertsBoxFactory.showAlert("Response is not instance of JSON", Renew_IP_Activity.this);
				}
				}
				else{
					AlertsBoxFactory.showAlert("Response is not instance of JSON", Renew_IP_Activity.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
