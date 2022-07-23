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

import com.broadbandcollection.R;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PostpaidData;
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


public class Cash_postpaidActivity extends Activity{

	Button			btn_submit,btn_back;
	
	TextView 		dueAmount,heading_dueamount,payment_date,payment_amount,label_add_amount,tv_display_add_amount,tv_Label_Out_Add_amt,tv_Out_Add_amt,date_heading;
	
	EditText		pay_date;
	
	String 			amount,due_amount;
	
	SharedPreferences	sharedPreferences;
	
	ProgressDialog 		prgDialog;
	
	AlertDialog.Builder   alertDialogBuilder;
	
	AlertDialog 	alert;
	
	public String currentDate;
	
	Utils utils = new Utils();
	
	PostpaidData postpaid=new PostpaidData();
	
	AuthenticationMobile Authobj = new AuthenticationMobile();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cash);
		
		InitalizeControls();
		postpaid=(PostpaidData)this.getIntent().getSerializableExtra(Utils.POSTPAID_OBJECT);
		due_amount=getIntent().getExtras().getString("DueAmount");
		Utils.log("DueAmount",":"+due_amount);
		dueAmount.setText(due_amount);
		
		BaseApplication.getEventBus().register(this);

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
				Cash_postpaidActivity.this.finish();
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SEND_DATA();
			}
		});
	}

	public void InitalizeControls() {

		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);

		String AppVersion= Utils.GetAppVersion(Cash_postpaidActivity.this);
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

	  heading_dueamount=(TextView)findViewById(R.id.TextView03);
	  date_heading=(TextView)findViewById(R.id.TextView01);
	  payment_date=(TextView)findViewById(R.id.payDate);
	  dueAmount=(TextView)findViewById(R.id.dueAmount);
      payment_amount=(TextView)findViewById(R.id.payAmount);
      label_add_amount=(TextView)findViewById(R.id.tv_Label_Out_Add_amt);
      tv_display_add_amount=(TextView)findViewById(R.id.tv_Out_Add_amt);
      label_add_amount.setVisibility(View.GONE);
      tv_display_add_amount.setVisibility(View.GONE);
      heading_dueamount.setVisibility(View.VISIBLE);
      dueAmount.setVisibility(View.VISIBLE);

      Calendar c = Calendar.getInstance();
	  SimpleDateFormat viewDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
	  currentDate = viewDateFormatter.format(c.getTime());
      payment_date.setText(currentDate);
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
			pi.setValue(postpaid.getPayNo());
			pi.setType(String.class);
			request.addProperty(pi);

			pi = new PropertyInfo();
			pi.setName("PayDate");
			pi.setValue(postpaid.getPayDate());
			pi.setType(String.class);
			request.addProperty(pi);

			pi = new PropertyInfo();
			pi.setName("BankName");
			pi.setValue(postpaid.getBankName());
			pi.setType(String.class);
			request.addProperty(pi);

			pi = new PropertyInfo();
			pi.setName("ChequeDate");
			pi.setValue(postpaid.getChequeDate());
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

			prgDialog = new ProgressDialog(Cash_postpaidActivity.this);
			prgDialog.setMessage("Please wait..");
			prgDialog.setCancelable(false);
			prgDialog.show();

			new CommonAsyncTask(Cash_postpaidActivity.this) {
				protected void onPostExecute(ArrayList<String> result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					prgDialog.dismiss();

					if(result.get(0).equalsIgnoreCase("OK"))
					{
						if(result.get(1).length()>0){
						if (!result.get(1).equalsIgnoreCase("0")){
							BaseApplication.getEventBus().post(new FinishEvent(Postpaid_Activity.class.getSimpleName()));
							AlertsBoxFactory.showExitAlert("Payment Done Successfully",Cash_postpaidActivity.this);
					    }
					}else {
							Toast.makeText(getApplicationContext(),"Not a valid user", Toast.LENGTH_LONG).show();
						}
					} else {
						AlertsBoxFactory.showAlert("Web Service Not Executed",Cash_postpaidActivity.this);
				}
			}
		}.execute(commonSOAP);
	 }
	
	 public  void SEND_DATA(){
		 alertDialogBuilder  = new AlertDialog.Builder(Cash_postpaidActivity.this);
	        alertDialogBuilder.setMessage("Are you sure you want to submit?")
	        .setCancelable(false)
	        .setPositiveButton("Yes",new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int id)
	            {
	            	CallSavePostpaidData();
	            }
	        });
	        alertDialogBuilder.setNegativeButton
	        (
	        	"NO",new DialogInterface.OnClickListener()
	        {
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

