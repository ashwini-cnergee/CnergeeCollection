package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.R;
import com.broadbandcollection.billing.obj.PostpaidData;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;


import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;


public class Cheque_postpaidActivity extends Activity{

	Button			btn_submit,btn_back;
	
	TextView 		payment_amount,heading_dueamount,dueAmount,payDate;
	
	EditText		payment_date,cheque_no,bank_name;
	
	String 			amount,cheque_number,cheque_bank,cheque_date,date_to_send,due_amount;
	
    PostpaidData postpaid=new PostpaidData();
	
	Utils utils = new Utils();
	
	AuthenticationMobile Authobj = new AuthenticationMobile();
	
	SharedPreferences		sharedPreferences;
	
	ProgressDialog 			prgDialog;
	
    AlertDialog.Builder   alertDialogBuilder;
	
	AlertDialog 		alert ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cheque);
		
		InitalizeControls();
		postpaid=(PostpaidData)this.getIntent().getSerializableExtra(Utils.POSTPAID_OBJECT);
		BaseApplication.getEventBus().register(this);
		
		due_amount=getIntent().getExtras().getString("DueAmount");
		Utils.log("DueAmount",":"+due_amount);
		dueAmount.setText(due_amount);
		
		amount = postpaid.getAmount();
		payment_amount.setText(amount);
		
		Utils.log("User Name",":"+postpaid.getUserName());
		Utils.log("MemberLogin_Id",":"+postpaid.getMemberLogin_Id());
		Utils.log("invoice_id",":"+postpaid.getInvoiceId());
		Utils.log("Payment Mode",":"+postpaid.getPaymentMode());
		Utils.log("Paynality",":"+postpaid.getPenaltyAmount());
		Utils.log("Bill_Amount",":"+postpaid.getBillAmount());
		Utils.log("Amount",":"+postpaid.getAmount());
		Utils.log("Client Access Id",":"+Authobj.getCliectAccessId());
		
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cheque_postpaidActivity.this.finish();
			}
		});
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((cheque_number.equalsIgnoreCase(""))&&((cheque_no.getText().toString().trim().length()==6)||(cheque_no.getText().toString().trim().length()==7))){
					if((!cheque_bank.equalsIgnoreCase(""))||(bank_name.getText().toString().trim().length()>0)){
					  if((!cheque_date.equalsIgnoreCase(""))||(payment_date.getText().toString().length()==8)){
						  cheque_date=payment_date.getText().toString().trim();
						  date_to_send=cheque_date+"000000";
						  SEND_DATA();
					  }else{
						  Toast.makeText(getApplicationContext(), "Please Enter Cheque Date", Toast.LENGTH_LONG).show();
					  }
		        }else{
		        	Toast.makeText(getApplicationContext(), "Please Enter Bank Name", Toast.LENGTH_LONG).show();
		        }
				}else{
					Toast.makeText(getApplicationContext(), "Please Enter Valid Cheque Number", Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
	
	public void InitalizeControls() {
		
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
	 
		String AppVersion= Utils.GetAppVersion(Cheque_postpaidActivity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
		
	  btn_submit=(Button)findViewById(R.id.submit);
	  btn_back=(Button)findViewById(R.id.backBtn);
	
	  payDate=(TextView)findViewById(R.id.payDate);
      payment_amount=(TextView)findViewById(R.id.cheqAmt);
      heading_dueamount=(TextView)findViewById(R.id.TextView05);
      dueAmount=(TextView)findViewById(R.id.dueAmount);
      
      cheque_no=(EditText)findViewById(R.id.chequeNo);
      bank_name=(EditText)findViewById(R.id.bank);
      payment_date=(EditText)findViewById(R.id.chqDate);
      
      cheque_number=cheque_no.getText().toString().trim();
      cheque_bank=bank_name.getText().toString().trim();
      cheque_date=payment_date.getText().toString().trim();
     
      cheque_date=payment_date.getText().toString();
      heading_dueamount.setVisibility(View.VISIBLE);
      dueAmount.setVisibility(View.VISIBLE);
      payDate.setVisibility(View.GONE);
      payment_date.setVisibility(View.VISIBLE);
  }
	

	public void CallSavePostpaidData(){

		// TODO Auto-generated method stub
		CommonSOAP commonSOAP = new CommonSOAP(utils.getDynamic_Url(),
				getApplicationContext().getResources().getString(
						R.string.WSDL_TARGET_NAMESPACE),
				getApplicationContext().getResources().getString(
						R.string.METHOD_SAVE_POSTPAID_DATA));

		SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.METHOD_SAVE_POSTPAID_DATA));

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(postpaid.getUserName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("MemberLoginId");
		pi.setValue(postpaid.getMemberLogin_Id());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(postpaid.getMember_Id());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("InvoiceId");
		pi.setValue(postpaid.getInvoiceId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaymentMode");
		pi.setValue(postpaid.getPaymentMode());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Amount");
		pi.setValue(postpaid.getAmount());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PayNo");
		pi.setValue(cheque_no.getText().toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PayDate");
		pi.setValue(postpaid.getPayDate());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("BankName");
		pi.setValue(bank_name.getText().toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ChequeDate");
		pi.setValue(date_to_send);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PenaltyAmount");
		pi.setValue(postpaid.getPenaltyAmount());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("BillAmount");
		pi.setValue(postpaid.getBillAmount());
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		commonSOAP.setRequest(request);

		prgDialog = new ProgressDialog(Cheque_postpaidActivity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();

		new CommonAsyncTask(Cheque_postpaidActivity.this) {
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();

				if (result.get(0).equalsIgnoreCase("OK")) 
				{
					if(result.get(1).length()>0){
					if (!result.get(1).equalsIgnoreCase("0")){
						
							BaseApplication.getEventBus().post(new FinishEvent(Postpaid_Activity.class.getSimpleName()));
							AlertsBoxFactory.showExitAlert("Payment Done Successfully",Cheque_postpaidActivity.this);
					}
				}else {
						Toast.makeText(getApplicationContext(),"Not a valid user", Toast.LENGTH_LONG).show();
					}
				} else {
					AlertsBoxFactory.showAlert("Web Service Not Executed",Cheque_postpaidActivity.this);
				}
			}
		}.execute(commonSOAP);
	
 }
	
	 public  void SEND_DATA(){
		 alertDialogBuilder  = new AlertDialog.Builder(Cheque_postpaidActivity.this);
	        alertDialogBuilder.setMessage("Are you sure you want to submit?")
	        .setCancelable(false)
	        .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	            	CallSavePostpaidData();
	            }
	        });
	        alertDialogBuilder.setNegativeButton("NO",
	                new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	            }
	        });
	       alert  = alertDialogBuilder.create();
	       alert.show();
	       
	     }
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

