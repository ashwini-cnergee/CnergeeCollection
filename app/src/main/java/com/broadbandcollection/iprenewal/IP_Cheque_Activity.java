package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.activity.BaseApplication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class IP_Cheque_Activity extends Activity {
	EditText et_cheque_number,et_bank_name,et_cheque_date,et_receipt_no;
	TextView tv_date,tv_amount,tv_Out_Add_amt,tv_Label_Out_Add_amt,tv_rcpt_no;
	Button btn_back,btn_submit;
	
	Utils utils = new Utils();
	SharedPreferences	sharedPreferences;
	AuthenticationMobile Authobj = new AuthenticationMobile();
	SubscriberDetails_Ip subscriberDetails_Ip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		subscriberDetails_Ip=(SubscriberDetails_Ip) this.getIntent().getSerializableExtra(Utils.SUBSCRIBER_IP);
		setContentView(R.layout.activity_cheque_ip);
		initialzeControls();
		BaseApplication.getEventBus().register(this);
		Log.e("IP_Cheque_Activity",":-"+subscriberDetails_Ip);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				valid_cheque_data();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						IP_Cheque_Activity.this.finish();
					}
				});
	}
	
	
	public void initialzeControls(){
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		tv_date=(TextView) findViewById(R.id.payDate);
		tv_amount=(TextView) findViewById(R.id.cheqAmt);
		tv_Out_Add_amt=(TextView) findViewById(R.id.tv_Out_Add_amt);
		tv_Label_Out_Add_amt=(TextView) findViewById(R.id.tv_Label_Out_Add_amt);
		et_cheque_number=(EditText) findViewById(R.id.chequeNo);
		et_bank_name=(EditText) findViewById(R.id.bank);
		et_cheque_date=(EditText) findViewById(R.id.chqDate);		
		tv_amount=(TextView) findViewById(R.id.cheqAmt);		
		et_receipt_no=(EditText) findViewById(R.id.receptNo);
		tv_rcpt_no=(TextView) findViewById(R.id.LabelRec);	
		btn_back=(Button)findViewById(R.id.backBtn);
		btn_submit=(Button)findViewById(R.id.submit);
		tv_Label_Out_Add_amt.setVisibility(View.GONE);
		tv_Label_Out_Add_amt.setVisibility(View.GONE);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat viewDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = viewDateFormatter.format(c.getTime());
		tv_rcpt_no.setVisibility(View.GONE);
		et_receipt_no.setVisibility(View.GONE);
		tv_date.setText(currentDate);
		tv_amount.setText(subscriberDetails_Ip.getPrice());
		tv_Out_Add_amt.setVisibility(View.GONE);
		tv_Label_Out_Add_amt.setVisibility(View.GONE);
		
		String AppVersion=Utils.GetAppVersion(IP_Cheque_Activity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
	}
	
	public void valid_cheque_data(){
		if(et_cheque_number.getText().length()>0){
			if(et_cheque_number.length()==7){
				if(et_bank_name.length()>0){
					if(et_cheque_date.length()>0){
						call_assign_ip_wbservice();
					}
					else{
						AlertsBoxFactory.showAlert("Please enter cheque date.", IP_Cheque_Activity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please enter Bank name.", IP_Cheque_Activity.this);
				}
				
			}
			else{
				AlertsBoxFactory.showAlert("You must have entered 7 digits cheque no.", IP_Cheque_Activity.this);
				
			}
		}
		else{
			AlertsBoxFactory.showAlert("Please enter Cheque number.", IP_Cheque_Activity.this);
		}
	}
	
	public void call_assign_ip_wbservice(){
		CommonSOAP commonSOAP= new CommonSOAP(
				utils.getDynamic_Url(),
getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_ASSIGN_IP_DETAILS));
		
		SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_ASSIGN_IP_DETAILS));
		
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
		pi.setName("SubscriberId");
		pi.setValue(subscriberDetails_Ip.getMemberLoginID());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ISPid");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getISPId()));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("MemberID");
		pi.setValue(Long.valueOf(subscriberDetails_Ip.getMemberID()));
		pi.setType(Long.class);
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
		pi.setName("IpSalePackageId");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getIpSalePackageId()));
		pi.setType(Integer.class);
		request.addProperty(pi);  
		
		pi = new PropertyInfo();
		pi.setName("IPAdd");
		pi.setValue(subscriberDetails_Ip.getIPAddress());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaymentDate");
		pi.setValue(et_cheque_date.getText().toString()+"000000");
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PayMode");
		pi.setValue("CQ");
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Amt");
		pi.setValue(Double.valueOf(subscriberDetails_Ip.getPrice()));
		pi.setType(Double.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ForDays");
		pi.setValue(Integer.valueOf(subscriberDetails_Ip.getForDays()));
		pi.setType(Integer.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("BankName");
		pi.setValue(et_bank_name.getText().toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PayModeNo");
		pi.setValue(et_cheque_number.getText().toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("CIP");
		pi.setValue(subscriberDetails_Ip.isIs_change_ip()?"YES":"NO");
		pi.setType(String.class);
		request.addProperty(pi);
	
		commonSOAP.setRequest(request);
		
		final ProgressDialog prgDialog= new ProgressDialog(IP_Cheque_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new CommonAsyncTask(IP_Cheque_Activity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					IP_Cheque_Activity.this.finish();
					Intent details_intent=new Intent(IP_Cheque_Activity.this, IP_PaymentDetails_Activity.class);
					details_intent.putExtra("receipt_no",result.get(1));
					startActivity(details_intent);

					BaseApplication.getEventBus().post(new FinishEvent(Renew_IP_Activity.class.getSimpleName()));
					BaseApplication.getEventBus().post(new FinishEvent(SubscriberDetails_Ip_Activity.class.getSimpleName()));
					BaseApplication.getEventBus().post(new FinishEvent(Change_Ip_Activity.class.getSimpleName()));
				}
				else{
					AlertsBoxFactory.showAlert("Web Service Not Executed", IP_Cheque_Activity.this);
				}
			}
		}.execute(commonSOAP);
		
	}
}
