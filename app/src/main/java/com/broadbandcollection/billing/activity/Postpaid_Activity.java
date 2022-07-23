package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PostpaidData;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;

import com.broadbandcollection.R;

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
import java.util.Date;


public class Postpaid_Activity extends Activity{

	AutoCompleteTextView 	act_subscriber;
	
	Button 					btn_back,btn_reset,btn_search,btn_update_status;
	
	TextView 				fullname,mem_address,mb_number,invoice_no,invoice_period,invoice_duedate,unpaid_amount,invoice_amount,fine_amount,due_date,paynality,tv_show_details,
							invoice_heading,payment_mode,Paid_amount,adjust_amount,payment_date;
	
	RadioGroup				rg_payment_mode;
	
	RadioButton				rb_cash,rb_cheque,rb_dd,rb_edc;
	
	LinearLayout			ll_amount,postpaid_details,pay_summary,payment_option,postpaid_whole_details,ll_unpaid_amount,ll_fine_amount
							,ll_paid_amount,ll_adjust_amount,heading_payment;
	
	ProgressDialog 			prgDialog;
	
	String 					amount,postpaid_user,area_id,billing_type_id,connection_id,renewal_type,expiry_date,invoice_date,invoice_number,
							is_payment_completed,isp_id,last_invoice_id,member_idmember_login_id,member_id,member_login_id,
							pakage_id,server_id,
							
							additional_amount,additional_by,additional_date,additional_type,adjustment_amount,area_name,balance_after_payment
							,bill_amount_fine,bill_cycle,bill_type,billing_from,bill_month,bill_paynality,cheque_date,current_billamount
							,current_paynality,discount_amount,discount_by,discount_date,discount_type,payment_due_date,full_name,invoice_id
							,is_additional,IsCollAppPartialPayment,is_discount,is_payment_closed,is_tds_deducted,is_write_off,address,package_id,paid_amount,
							pay_date,pending_bill,pre_balance_amount,pre_invoice_id,mobile_no,status,tds_amount,tds_date,tds_given_by,
							write_off_amount,write_off_by,write_off_comment,write_off_date,current_date;
	
	Utils utils = new Utils();
	
	String 					total_amount,payment_type,ExcessAmtAfterBillPaid,RemaningAmtAfterBillPaid;
	
	String 					selected_payment_mode="";
	
	SharedPreferences		sharedPreferences;
	
	AuthenticationMobile Authobj = new AuthenticationMobile();

	EditText     			paying_amount;
	
	PostpaidData postpaid=new PostpaidData();
	
	public static Context context;
	Dialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_postpaid);
		context = this;
		dialog = new Dialog(Postpaid_Activity.this);
		InitalizeControls();
		
		
		if(getIntent().getStringExtra("postpaid_user").length()>0){
			act_subscriber.setText(getIntent().getStringExtra("postpaid_user"));
			
			invoice_heading.setVisibility(View.VISIBLE);
			postpaid_details.setVisibility(View.VISIBLE);
			tv_show_details.setVisibility(View.VISIBLE);
			
			payment_mode.setVisibility(View.VISIBLE);
			payment_option.setVisibility(View.VISIBLE);
			postpaid_user=act_subscriber.getText().toString().trim();
			Utils.log("Postpaid User", ":"+postpaid_user);
			CallPostpaid_Details();
		}
		BaseApplication.getEventBus().register(this);
		
		//ll_amount.setVisibility(View.GONE);
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Postpaid_Activity.this.finish();
			}
		});
		
		btn_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act_subscriber.setText("");
			}
		});
		
		
		btn_search.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(act_subscriber.length()>0){
				    invoice_heading.setVisibility(View.VISIBLE);
					postpaid_details.setVisibility(View.VISIBLE);
					tv_show_details.setVisibility(View.VISIBLE);
					
					payment_mode.setVisibility(View.VISIBLE);
					payment_option.setVisibility(View.VISIBLE);
					postpaid_user=act_subscriber.getText().toString().trim();
					Utils.log("Postpaid User", ":"+postpaid_user);
					CallPostpaid_Details();
				}
				else
				{
				  AlertsBoxFactory.showAlert("Please enter valid data.",context );
				}
			}
		});
		
		tv_show_details.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*if(postpaid_whole_details.getVisibility()==View.GONE){
					postpaid_whole_details.setVisibility(View.VISIBLE);
				}else{
					postpaid_whole_details.setVisibility(View.GONE);
			  }*/
				
				showCustomDialog();
			}
		});
		
		
		mem_address.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
				/*Toast.makeText(ShowDetailsActivity.this,
						memberData.getMemberAddress(),
						150000).show();*/
			  AlertsBoxFactory.showAlert("Address",postpaid.getAddress(),context );
		  }
		  
		  
		 

	});
		
		btn_update_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Postpaid_Activity.this, UpdateStatusActivity.class);
				intent.putExtra("subscriber_id", member_login_id);
				intent.putExtra("username",utils.getAppUserName());
				intent.putExtra("is_postpaid",true);
				
				startActivity(intent);
			}
		});
		
		
		rg_payment_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId)
				{
				  case R.id.rb_cash:
				
				 if(ll_amount.getVisibility()==View.VISIBLE)
				  {
					//Utils.log("Value", ":"+(Double.valueOf(paying_amount.getText().toString())));
					  if(((Double.valueOf(paying_amount.getText().length()>0?paying_amount.getText().toString():"0.0"))<=0))
					  {
						 rg_payment_mode.clearCheck();
						 Toast.makeText(getApplicationContext(),"Please Enter Valid Paying Amount", Toast.LENGTH_LONG).show();
					  }
					  else
					{
					  selected_payment_mode="CS";
					  postpaid.setPaymentMode(selected_payment_mode);
					  postpaid.setAmount(paying_amount.getText().toString());
					  postpaid.setBankName("");
					  postpaid.setPayNo("");
					  postpaid.setChequeDate("");
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					 
					  //Utils.log("Payment Mode",":"+ selected_payment_mode);
					  Intent cash_intent=new Intent(Postpaid_Activity.this, Cash_postpaidActivity.class);
					  cash_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
					  cash_intent.putExtra("DueAmount",total_amount );
					  //Utils.log("Amount",":"+paying_amount.getText().toString());
					  startActivity(cash_intent);
					  rg_payment_mode.clearCheck();
					}
				  }
				 else{
					  selected_payment_mode="CS";
					  postpaid.setPaymentMode(selected_payment_mode);
					  postpaid.setAmount(total_amount);
					  postpaid.setBankName("");
					  postpaid.setPayNo("");
					  postpaid.setChequeDate("");
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					 
					  //Utils.log("Payment Mode",":"+ selected_payment_mode);
					  Intent cash_intent=new Intent(Postpaid_Activity.this, Cash_postpaidActivity.class);
					  cash_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
					  cash_intent.putExtra("DueAmount",total_amount );
					  //Utils.log("Amount",":"+paying_amount.getText().toString());
					  startActivity(cash_intent);
					  rg_payment_mode.clearCheck();
				  }
				   break;
				   
				   case R.id.rb_cheque:
					  if(ll_amount.getVisibility()==View.VISIBLE)
					  {
					    if((Double.valueOf(paying_amount.getText().length()>0?paying_amount.getText().toString():"0.0"))<=0)
						{
						  rg_payment_mode.clearCheck();
						  Toast.makeText(getApplicationContext(),"Please Enter Valid Paying Amount", Toast.LENGTH_LONG).show();
					 }else{
					  selected_payment_mode="CQ";
					  postpaid.setPaymentMode(selected_payment_mode);
					  postpaid.setAmount(paying_amount.getText().toString());
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					  rg_payment_mode.clearCheck();
					  Utils.log("Payment Mode",":"+selected_payment_mode);
					  Intent cheque_intent=new Intent(Postpaid_Activity.this, Cheque_postpaidActivity.class);
					  cheque_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
					  cheque_intent.putExtra("DueAmount",total_amount );
					  startActivity(cheque_intent);
					}
				  }else{
					  selected_payment_mode="CQ";
					  postpaid.setPaymentMode(selected_payment_mode);
					  postpaid.setAmount(total_amount);
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					  rg_payment_mode.clearCheck();
					  Utils.log("Payment Mode",":"+selected_payment_mode);
					  Intent cheque_intent=new Intent(Postpaid_Activity.this, Cheque_postpaidActivity.class);
					  cheque_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
					  cheque_intent.putExtra("DueAmount",total_amount );
					  startActivity(cheque_intent);
				  }
				   break; 
				   
				  case R.id.rb_dd:
					  if(ll_amount.getVisibility()==View.VISIBLE)
					  {
						  if(((Double.valueOf((paying_amount.getText().length()>0?paying_amount.getText().toString():"0.0")))<=0))
						  {
						    rg_payment_mode.clearCheck();
						    Toast.makeText(getApplicationContext(),"Please Enter Valid Paying Amount", Toast.LENGTH_LONG).show();
						  }
						  else{
					  selected_payment_mode="DD";
					  Utils.log("Payment Mode",":"+paying_amount.getText().toString());
					  postpaid.setPaymentMode(selected_payment_mode);
					  postpaid.setAmount(paying_amount.getText().toString());
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					  rg_payment_mode.clearCheck();
					
					  Utils.log("Payment Mode",":"+postpaid.getAmount());
					  Intent dd_intent=new Intent(Postpaid_Activity.this, DD_postpaidActivity.class);
					  dd_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
					  dd_intent.putExtra("DueAmount",total_amount );
					  startActivity(dd_intent);
					 } 
						  
					/*if(paying_amount.getText().toString().equalsIgnoreCase("")){
						rg_payment_mode.clearCheck();
						Toast.makeText(getApplicationContext(),"Please Enter Paying Amount", Toast.LENGTH_LONG).show();
					}else if(Double.valueOf(paying_amount.getText().toString())<=0){
						rg_payment_mode.clearCheck();
						Toast.makeText(getApplicationContext(),"Please Enter Valid Paying Amount", Toast.LENGTH_LONG).show();
					}else if(Double.valueOf(paying_amount.getText().toString())>0){
						 selected_payment_mode="DD";
						  Utils.log("Payment Mode",":"+paying_amount.getText().toString());
						  postpaid.setPaymentMode(selected_payment_mode);
						  postpaid.setAmount(paying_amount.getText().toString());
						  postpaid.setPayDate("");
						  paying_amount.setText("");
						  rg_payment_mode.clearCheck();
						
						  Utils.log("Payment Mode",":"+postpaid.getAmount());
						  Intent dd_intent=new Intent(Postpaid_Activity.this, DD_postpaidActivity.class);
						  dd_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
						  dd_intent.putExtra("DueAmount",total_amount );
						  startActivity(dd_intent);
					}*/
						  
				}else{
						 selected_payment_mode="DD";
						  Utils.log("Payment Mode",":"+paying_amount.getText().toString());
						  postpaid.setPaymentMode(selected_payment_mode);
						  postpaid.setAmount(total_amount);
						  postpaid.setPayDate("");
						  paying_amount.setText("");
						  rg_payment_mode.clearCheck();
						
						  Utils.log("Payment Mode",":"+postpaid.getAmount());
						  Intent dd_intent=new Intent(Postpaid_Activity.this, DD_postpaidActivity.class);
						  dd_intent.putExtra(Utils.POSTPAID_OBJECT,postpaid);
						  dd_intent.putExtra("DueAmount",total_amount);
						  startActivity(dd_intent);
					 }
				  break; 
				  case R.id.rb_edc:
					  selected_payment_mode="EDC";
					  Utils.log("Payment Mode",":"+selected_payment_mode);
					  postpaid.setPayDate("");
					  paying_amount.setText("");
					  rg_payment_mode.clearCheck();
					  Intent edc_intent=new Intent(Postpaid_Activity.this, Edc_postpaidActivity.class);
					  edc_intent.putExtra("Amount",paying_amount.getText().toString());
					  startActivity(edc_intent);
				   break; 
				}
			}
		});
	}

	public void InitalizeControls() {
		// TODO Auto-generated method stub
		act_subscriber=(AutoCompleteTextView)findViewById(R.id.searchUser);
		
		sharedPreferences= getApplicationContext()
				.getSharedPreferences("CNERGEE", 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		
		String AppVersion= Utils.GetAppVersion(Postpaid_Activity.this);
		Authobj.setMobileNumber(utils.getMobileNumber());
		Authobj.setMobLoginId(utils.getMobLoginId());
		Authobj.setMobUserPass(utils.getMobUserPass());
		Authobj.setIMEINo(utils.getIMEINo());
		Authobj.setCliectAccessId(utils.getCliectAccessId());
		Authobj.setMacAddress(utils.getMacAddress());
		Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
		Authobj.setAppVersion(AppVersion);
		
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_reset=(Button)findViewById(R.id.btn_reset);
		btn_search=(Button)findViewById(R.id.btn_search);
		btn_update_status=(Button)findViewById(R.id.btn_update_status);
		btn_update_status.setVisibility(View.GONE);
		fullname=(TextView)findViewById(R.id.tv_disp_full_name);
		mem_address=(TextView)findViewById(R.id.tv_disp_address);
		mb_number=(TextView)findViewById(R.id.tv_disp_mobile_no);
		invoice_no=(TextView)findViewById(R.id.tv_disp_invoiceno);
		invoice_period=(TextView)findViewById(R.id.tv_disp_invoice_period);
		invoice_duedate=(TextView)findViewById(R.id.tv_disp_invoice_duedate);
		tv_show_details=(TextView)findViewById(R.id.tv_show_details);
		invoice_heading=(TextView)findViewById(R.id.invoice_heading);
		payment_mode=(TextView)findViewById(R.id.tv_payment_mode);
		
		/*unpaid_amount=(TextView)dialog.findViewById(R.id.tv_disp_unpaidAmount);
		invoice_amount=(TextView)dialog.findViewById(R.id.tv_disp_invoice_amount);
		fine_amount=(TextView)dialog.findViewById(R.id.tv_disp_fine_amount);
		due_date=(TextView)dialog.findViewById(R.id.tv_disp_due_date);
		paynality=(TextView)dialog.findViewById(R.id.tv_disp_paynality);
		Paid_amount=(TextView)dialog.findViewById(R.id.tv_disp_paid_amount);
		adjust_amount=(TextView)dialog.findViewById(R.id.tv_disp_adjust_amount);
		payment_date=(TextView)dialog.findViewById(R.id.tv_disp_payment_date);*/
		
		rg_payment_mode=(RadioGroup)findViewById(R.id.rg_payment_mode);
		rb_cash=(RadioButton)findViewById(R.id.rb_cash);
		rb_cheque=(RadioButton)findViewById(R.id.rb_cheque);
		rb_dd=(RadioButton)findViewById(R.id.rb_dd);
		rb_edc=(RadioButton)findViewById(R.id.rb_Edc);
		
		paying_amount=(EditText)findViewById(R.id.et_amount);
		amount=paying_amount.getText().toString();
		
		ll_amount=(LinearLayout)findViewById(R.id.ll_amount);
		postpaid_details=(LinearLayout)findViewById(R.id.ll_postpaid_details);
		pay_summary=(LinearLayout)findViewById(R.id.ll_pay_summary);
		heading_payment=(LinearLayout)findViewById(R.id.ll_heading_payment);
		payment_option=(LinearLayout)findViewById(R.id.ll_payment_option);
		/*postpaid_whole_details=(LinearLayout)findViewById(R.id.ll_postpaid2_details);
		ll_unpaid_amount=(LinearLayout)findViewById(R.id.ll_unpaid_amount);
		ll_fine_amount=(LinearLayout)findViewById(R.id.ll_fine_amount);
		ll_paid_amount=(LinearLayout)findViewById(R.id.ll_paid_amount);
		ll_adjust_amount=(LinearLayout)findViewById(R.id.ll_adjust_amount);*/
		
		invoice_heading.setVisibility(View.GONE);
		postpaid_details.setVisibility(View.GONE);
		tv_show_details.setVisibility(View.GONE);
		//postpaid_whole_details.setVisibility(View.GONE);
		payment_mode.setVisibility(View.GONE);
		payment_option.setVisibility(View.GONE);
		
	
		/*payment_type="";
		ExcessAmtAfterBillPaid="0";
		RemaningAmtAfterBillPaid="0";
		is_payment_completed="true";
		if(Integer.parseInt(paying_amount.getText().toString())<Integer.parseInt(invoice_date)){
			payment_type="LESS";
			ExcessAmtAfterBillPaid="0";
			RemaningAmtAfterBillPaid=String.valueOf(Integer.parseInt(invoice_date)-Integer.parseInt(paying_amount.getText().toString()));
		}
		
		if(Integer.parseInt(paying_amount.getText().toString())>Integer.parseInt(invoice_date)){
			payment_type="MORE";
			ExcessAmtAfterBillPaid=String.valueOf(Integer.parseInt(paying_amount.getText().toString())-Integer.parseInt(invoice_date));
			RemaningAmtAfterBillPaid="0";
		}*/
	}
	
	public void CallPostpaid_Details() {
		// TODO Auto-generated method stub
		CommonSOAP commonSOAP = new CommonSOAP(getApplicationContext()
				.getResources().getString(R.string.GLOBAL_SOAP_URL),
				getApplicationContext().getResources().getString(
						R.string.WSDL_TARGET_NAMESPACE),
				getApplicationContext().getResources().getString(
						R.string.METHOD_GET_POSTPAID_DATA));

		SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.METHOD_GET_POSTPAID_DATA));

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("MemberLoginId");
		pi.setValue(postpaid_user);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(utils.getAppUserName());
		pi.setType(String.class);
		request.addProperty(pi);

		commonSOAP.setRequest(request);

		prgDialog = new ProgressDialog(Postpaid_Activity.this);
		prgDialog.setMessage("Please wait..");
		prgDialog.setCancelable(false);
		prgDialog.show();

		new CommonAsyncTask(Postpaid_Activity.this) {
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prgDialog.dismiss();

				if (result.get(0).equalsIgnoreCase("OK")) 
				{
					if (!result.get(1).equalsIgnoreCase("0")){
						btn_update_status.setVisibility(View.VISIBLE);
						ParsePostResponse(result.get(1));
					    setPostpaidData(postpaid);
					}else {
						Toast.makeText(getApplicationContext(),"Not a valid user", Toast.LENGTH_LONG).show();
					}
				} else {
					AlertsBoxFactory.showAlert("Web Service Not Executed",Postpaid_Activity.this);
				}
			}
		}.execute(commonSOAP);
	}
	
   public void ParsePostResponse(String response){
	   //Utils.log("Post_paid_response", ":"+response);
	   try
	   {
		   JSONObject postpaid_obj= new JSONObject(response);
		   if(postpaid_obj.has("NewDataSet"))
			{
			   JSONObject new_postpaid_obj= postpaid_obj.getJSONObject("NewDataSet");
				
				if(new_postpaid_obj.has("Table"))
				{
					if(new_postpaid_obj.get("Table") instanceof JSONObject)
					{
						JSONObject postpaid_result_obj=new_postpaid_obj.getJSONObject("Table");
					
							area_id=postpaid_result_obj.optString("Areaid");
							billing_type_id=postpaid_result_obj.optString("BillingCycleTypeId");
							connection_id=postpaid_result_obj.optString("Connectiontypeid");
							renewal_type=postpaid_result_obj.optString("CurrentRenewalType");
							expiry_date=postpaid_result_obj.optString("ExpiryDate24OL");
							invoice_date=postpaid_result_obj.optString("InvoiceDate");
							invoice_number=postpaid_result_obj.optString("InvoiceNo");
							is_payment_completed=postpaid_result_obj.optString("IsPaymentCompleted");
							isp_id=postpaid_result_obj.optString("ispid");
							last_invoice_id=postpaid_result_obj.optString("LastInvoiceId");
							member_id=postpaid_result_obj.optString("Memberid");
							member_login_id=postpaid_result_obj.optString("MemberloginId");
							pakage_id=postpaid_result_obj.optString("Packageid24Ol");
							server_id=postpaid_result_obj.optString("Serverid");
					}
				if(new_postpaid_obj.get("Table") instanceof JSONArray)
				{
					JSONArray postpaid_data_array=new_postpaid_obj.getJSONArray("Table");
					for(int i=0;i<postpaid_data_array.length();i++)
					 {
						 JSONObject postpaid_result_obj=postpaid_data_array.getJSONObject(i);
						
						 area_id=postpaid_result_obj.optString("Areaid");
						 billing_type_id=postpaid_result_obj.optString("BillingCycleTypeId");
						 connection_id=postpaid_result_obj.optString("Connectiontypeid");
						 renewal_type=postpaid_result_obj.optString("CurrentRenewalType");
						 expiry_date=postpaid_result_obj.optString("ExpiryDate24OL");
						 invoice_date=postpaid_result_obj.optString("InvoiceDate");
						 invoice_number=postpaid_result_obj.optString("InvoiceNo");
						 is_payment_completed=postpaid_result_obj.optString("IsPaymentCompleted");
						 isp_id=postpaid_result_obj.optString("ispid");
						 last_invoice_id=postpaid_result_obj.optString("LastInvoiceId");
						 member_id=postpaid_result_obj.optString("Memberid");
						 member_login_id=postpaid_result_obj.optString("MemberloginId");
						 pakage_id=postpaid_result_obj.optString("Packageid24Ol");
						 server_id=postpaid_result_obj.optString("Serverid");
				     }
				  }
				  invoice_no.setText(invoice_number);
			    }
				
				
				if(new_postpaid_obj.has("Table1"))
				{
					if(new_postpaid_obj.get("Table1") instanceof JSONObject)
					{
						JSONObject postpaid_result_obj=new_postpaid_obj.getJSONObject("Table1");
						if(!postpaid_result_obj.has("Message"))
						{
						additional_amount=postpaid_result_obj.optString("AdditionalAmount");
						additional_by=postpaid_result_obj.optString("AdditionalBy");
						additional_date=postpaid_result_obj.optString("AdditionalDate");
						additional_type=postpaid_result_obj.optString("AdditionalType");
						adjustment_amount=postpaid_result_obj.optString("AdjustmentAmt");
						area_id=postpaid_result_obj.optString("Areaid");
						area_name=postpaid_result_obj.optString("AreaName");
						balance_after_payment=postpaid_result_obj.optString("BalanceAfterPayment");
						bill_amount_fine=postpaid_result_obj.optString("billAmtFine");
						bill_cycle=postpaid_result_obj.optString("BillCycle");
						bill_type=postpaid_result_obj.optString("billingCycleType");
						billing_from=postpaid_result_obj.optString("BillingFrom");
						bill_month=postpaid_result_obj.optString("BillMonth");
						bill_paynality=postpaid_result_obj.optString("BillwithPenalty");
						cheque_date=postpaid_result_obj.optString("chequeDate");
						connection_id=postpaid_result_obj.optString("Connectiontypeid");
						current_billamount=postpaid_result_obj.optString("CurrentBillAmt");
						current_paynality=postpaid_result_obj.optString("CurrentPenalty");
						current_date=postpaid_result_obj.optString("CurrentTime");
						discount_amount=postpaid_result_obj.optString("DiscountAmount");
						discount_by=postpaid_result_obj.optString("DiscountBy");
						discount_date=postpaid_result_obj.optString("DiscountDate");
						discount_type=postpaid_result_obj.optString("DiscountType");
						payment_due_date=postpaid_result_obj.optString("DueDate");
						full_name=postpaid_result_obj.optString("FullName");
						invoice_date=postpaid_result_obj.optString("InvoiceDate");
						invoice_id=postpaid_result_obj.optString("InvoiceId");
						invoice_number=postpaid_result_obj.optString("InvoiceNo");
						is_additional=postpaid_result_obj.optString("IsAdditional");
						IsCollAppPartialPayment=postpaid_result_obj.optString("IsCollAppPartialPayment");
						is_discount=postpaid_result_obj.optString("IsDiscount");
						is_payment_closed=postpaid_result_obj.optString("IsPaymentClosed");
						is_payment_completed=postpaid_result_obj.optString("IsPaymentCompleted");
						is_tds_deducted=postpaid_result_obj.optString("IsTDSDedcuted");
						is_write_off=postpaid_result_obj.optString("IsWriteoff");
						address=postpaid_result_obj.optString("MemAddress");
						member_id=postpaid_result_obj.optString("Memberid");
						member_login_id=postpaid_result_obj.optString("MemberloginId");
						package_id=postpaid_result_obj.optString("Packageid24Ol");
						paid_amount=postpaid_result_obj.optString("PaidAmount");
						pay_date=postpaid_result_obj.optString("PayDate");
						pending_bill=postpaid_result_obj.optString("PendingBill");
						pre_balance_amount=postpaid_result_obj.optString("PrevBalanceAmt");
						pre_invoice_id=postpaid_result_obj.optString("PrevInvoiceId");
						mobile_no=postpaid_result_obj.optString("PrimaryMobileNo");
						server_id=postpaid_result_obj.optString("Serverid");
						status=postpaid_result_obj.optString("Status");
						tds_amount=postpaid_result_obj.optString("TDSAmount");
						tds_date=postpaid_result_obj.optString("TDSDate");
						tds_given_by=postpaid_result_obj.optString("TDSGivenBy");
						write_off_amount=postpaid_result_obj.optString("WriteoffAmount");
						write_off_by=postpaid_result_obj.optString("WriteoffBy");
						write_off_comment=postpaid_result_obj.optString("WriteoffComments");
						write_off_date=postpaid_result_obj.optString("WriteoffDate");
						rg_payment_mode.setVisibility(View.VISIBLE);
						heading_payment.setVisibility(View.VISIBLE);
						//IsCollAppPartialPayment="false";
						
						if(IsCollAppPartialPayment.equalsIgnoreCase("true")){
							ll_amount.setVisibility(View.VISIBLE);
						}else{
							ll_amount.setVisibility(View.GONE);
						}
				  }	
				else
				  {
					String message=postpaid_result_obj.optString("Message");
					AlertsBoxFactory.showExitAlert(message, this);
					rg_payment_mode.setVisibility(View.GONE);
					heading_payment.setVisibility(View.GONE);
				  }
				fullname.setText(full_name);
				mb_number.setText(mobile_no);
				getCompare();
			 }
					
				if(new_postpaid_obj.get("Table1") instanceof JSONArray)
				{
					JSONArray postpaid_data_array=new_postpaid_obj.getJSONArray("Table1");
					for(int i=0;i<postpaid_data_array.length();i++)
						{
						  JSONObject postpaid_result_obj=postpaid_data_array.getJSONObject(i);
							if(!postpaid_result_obj.has("Message"))
							  {
								additional_amount=postpaid_result_obj.optString("AdditionalAmount");
								additional_by=postpaid_result_obj.optString("AdditionalBy");
								additional_date=postpaid_result_obj.optString("AdditionalDate");
								additional_type=postpaid_result_obj.optString("AdditionalType");
								adjustment_amount=postpaid_result_obj.optString("AdjustmentAmt");
								area_id=postpaid_result_obj.optString("Areaid");
								area_name=postpaid_result_obj.optString("AreaName");
								balance_after_payment=postpaid_result_obj.optString("BalanceAfterPayment");
								bill_amount_fine=postpaid_result_obj.optString("billAmtFine");
								bill_cycle=postpaid_result_obj.optString("BillCycle");
								bill_type=postpaid_result_obj.optString("billingCycleType");
								billing_from=postpaid_result_obj.optString("BillingFrom");
								bill_month=postpaid_result_obj.optString("BillMonth");
								bill_paynality=postpaid_result_obj.optString("BillwithPenalty");
								cheque_date=postpaid_result_obj.optString("chequeDate");
								connection_id=postpaid_result_obj.optString("Connectiontypeid");
								current_billamount=postpaid_result_obj.optString("CurrentBillAmt");
								current_paynality=postpaid_result_obj.optString("CurrentPenalty");
								current_date=postpaid_result_obj.optString("CurrentTime");
								discount_amount=postpaid_result_obj.optString("DiscountAmount");
								discount_by=postpaid_result_obj.optString("DiscountBy");
								discount_date=postpaid_result_obj.optString("DiscountDate");
								discount_type=postpaid_result_obj.optString("DiscountType");
								payment_due_date=postpaid_result_obj.optString("DueDate");
								full_name=postpaid_result_obj.optString("FullName");
								invoice_date=postpaid_result_obj.optString("InvoiceDate");
								invoice_id=postpaid_result_obj.optString("InvoiceId");
								invoice_number=postpaid_result_obj.optString("InvoiceNo");
								is_additional=postpaid_result_obj.optString("IsAdditional");
								IsCollAppPartialPayment=postpaid_result_obj.optString("IsCollAppPartialPayment");
								is_discount=postpaid_result_obj.optString("IsDiscount");
								is_payment_closed=postpaid_result_obj.optString("IsPaymentClosed");
								is_payment_completed=postpaid_result_obj.optString("IsPaymentCompleted");
								is_tds_deducted=postpaid_result_obj.optString("IsTDSDedcuted");
								is_write_off=postpaid_result_obj.optString("IsWriteoff");
								address=postpaid_result_obj.optString("MemAddress");
								member_id=postpaid_result_obj.optString("Memberid");
								member_login_id=postpaid_result_obj.optString("MemberloginId");
								package_id=postpaid_result_obj.optString("Packageid24Ol");
								paid_amount=postpaid_result_obj.optString("PaidAmount");
								pay_date=postpaid_result_obj.optString("PayDate");
								pending_bill=postpaid_result_obj.optString("PendingBill");
								pre_balance_amount=postpaid_result_obj.optString("PrevBalanceAmt");
								pre_invoice_id=postpaid_result_obj.optString("PrevInvoiceId");
								mobile_no=postpaid_result_obj.optString("PrimaryMobileNo");
								server_id=postpaid_result_obj.optString("Serverid");
								status=postpaid_result_obj.optString("Status");
								tds_amount=postpaid_result_obj.optString("TDSAmount");
								tds_date=postpaid_result_obj.optString("TDSDate");
								tds_given_by=postpaid_result_obj.optString("TDSGivenBy");
								write_off_amount=postpaid_result_obj.optString("WriteoffAmount");
								write_off_by=postpaid_result_obj.optString("WriteoffBy");
								write_off_comment=postpaid_result_obj.optString("WriteoffComments");
								write_off_date=postpaid_result_obj.optString("WriteoffDate");
								rg_payment_mode.setVisibility(View.VISIBLE);
								heading_payment.setVisibility(View.VISIBLE);
								//IsCollAppPartialPayment="false";
								
								if(IsCollAppPartialPayment.equalsIgnoreCase("true")){
									ll_amount.setVisibility(View.VISIBLE);
								}else{
									ll_amount.setVisibility(View.GONE);
								}
						  }	
						else
						 {
							String message=postpaid_result_obj.optString("Message");
							AlertsBoxFactory.showExitAlert(message, this);
							rg_payment_mode.setVisibility(View.GONE);
							heading_payment.setVisibility(View.GONE);
						 } 
					   fullname.setText(full_name);
					   mb_number.setText(mobile_no);
					   getCompare();
					}
				}
			     invoice_period.setText(billing_from);
			     /*invoice_amount.setText(current_billamount);
			     
			     if(Double.valueOf(pre_balance_amount)>0.0){
			    	 ll_unpaid_amount.setVisibility(View.VISIBLE);
				     unpaid_amount.setText(pre_balance_amount);
			     }else{
			    	 ll_unpaid_amount.setVisibility(View.GONE);
			     }

				 
				 if(Double.valueOf(bill_amount_fine)>0.0){
					 ll_fine_amount.setVisibility(View.VISIBLE);
					 fine_amount.setText(bill_amount_fine);
				 }else{
					 ll_fine_amount.setVisibility(View.GONE);
				 }
				
				 if(Double.valueOf(paid_amount)>0.0){
						ll_paid_amount.setVisibility(View.VISIBLE);
						Paid_amount.setText(paid_amount);
					}else
					{
						ll_paid_amount.setVisibility(View.GONE);
					}
				 
				 if(Double.valueOf(adjustment_amount)>0.0){
						ll_adjust_amount.setVisibility(View.VISIBLE);
						adjust_amount.setText(adjustment_amount);
				}
				 else{
					  ll_adjust_amount.setVisibility(View.GONE);
					}
				 
				 fullname.setText(full_name);
				 
				 mb_number.setText(mobile_no);
				 paynality.setText(current_paynality);
				 payment_date.setText(getDate(current_date));*/
				 
			  }
			}
		 }catch(JSONException e){
			 Utils.log("Error", "is:"+e.toString());
	   }
   }
   
   public  String getDate(String due_date)
	  {
	       Utils.log("Due Date", ":"+due_date);
	       String formattedDate="";
	     try
	       {
	    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    	 SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");//,h:mm a"
	    	 Date d = sdf.parse(due_date);
	    	 formattedDate = output.format(d);
	       }
	    catch(java.text.ParseException e){
	    	//MyUtils.log("Date ", ":"+e);
	    	e.printStackTrace();
	     }
	    catch(NullPointerException e){
	    	//MyUtils.log("Date ", ":"+e);
	    	e.printStackTrace();
	     }
	    return formattedDate;
	 }
	
    public void getCompare() {
    try
     {
    	String pay_due_date=getDate(payment_due_date);
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
    	Date pay_Date = sdf.parse(pay_due_date);
    	Utils.log("pay_due_date", ":"+pay_due_date);
    	int year = pay_Date.getYear(); // this is deprecated
    	int month = pay_Date.getMonth(); // this is deprecated
    	int day = pay_Date.getDay(); // this is deprecated       
    	Calendar cal_pay_date = Calendar.getInstance();
    	cal_pay_date.set(year, month, day);
    	
    	
    	String currentDate=getDate(current_date);
    	Utils.log("Current_Date", ":"+currentDate);
    	Date curr_date = sdf.parse(currentDate);
    	int curr_year = curr_date.getYear(); // this is deprecated
    	int curr_month = curr_date.getMonth(); // this is deprecated
    	int curr_day = curr_date.getDay(); // this is deprecated       
    	Calendar cal_curr_date = Calendar.getInstance();
    	cal_curr_date.set(curr_year, curr_month, curr_day);

    	
    	if (cal_curr_date.after(cal_pay_date)) {
    		Utils.log("Current Date", "After");
    		Double val=Double.valueOf(bill_paynality);
    		Utils.log("Double val", ":"+val);
    		total_amount=String.valueOf(val);
			invoice_duedate.setText(total_amount+"");
    	}else{
    		Utils.log("Current Date", "Before");
    		total_amount=String.valueOf(balance_after_payment);
    		invoice_duedate.setText(balance_after_payment);
    	}
    	
    	if(Double.parseDouble(invoice_duedate.getText().toString())>0.0){
    		heading_payment.setVisibility(View.VISIBLE);
    		payment_option.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		heading_payment.setVisibility(View.GONE);
    		payment_option.setVisibility(View.GONE);
    	}
     } 
    catch(Exception e)
     {
    	Utils.log("Get Compare ", ":"+e);
    	e.printStackTrace();
     }
   }

    public void setPostpaidData(PostpaidData postpaid){
    	postpaid.setUserName(utils.getAppUserName());
    	postpaid.setMemberLogin_Id(member_login_id);
    	postpaid.setInvoiceId(invoice_id);
    	postpaid.setPaymentMode(selected_payment_mode);
    	postpaid.setPenaltyAmount(current_paynality);
    	postpaid.setBillAmount(invoice_duedate.getText().toString());
    	postpaid.setMember_Id("0");
    	postpaid.setAddress(address);
    }
  
    protected void showCustomDialog() {
        // TODO Auto-generated method stub
      
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	  AlertDialog.Builder dialog = new AlertDialog.Builder(this).setInverseBackgroundForced(true);
    	  LayoutInflater _inflater = LayoutInflater.from(Postpaid_Activity.this);
          View view = _inflater.inflate(R.layout.custom_layout,null);
        //dialog.setContentView(R.layout.custom_layout);
          dialog.setView(view);
    
      //  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        unpaid_amount=(TextView)view.findViewById(R.id.tv_disp_unpaidAmount);
		invoice_amount=(TextView)view.findViewById(R.id.tv_disp_invoice_amount);
		fine_amount=(TextView)view.findViewById(R.id.tv_disp_fine_amount);
		due_date=(TextView)view.findViewById(R.id.tv_disp_due_date);
		paynality=(TextView)view.findViewById(R.id.tv_disp_penalty);
		Paid_amount=(TextView)view.findViewById(R.id.tv_disp_paid_amount);
		adjust_amount=(TextView)view.findViewById(R.id.tv_disp_adjust_amount);
		payment_date=(TextView)view.findViewById(R.id.tv_disp_payment_date);
		
		postpaid_whole_details=(LinearLayout)view.findViewById(R.id.ll_postpaid2_details);
		ll_unpaid_amount=(LinearLayout)view.findViewById(R.id.ll_unpaid_amount);
		ll_fine_amount=(LinearLayout)view.findViewById(R.id.ll_fine_amount);
		ll_paid_amount=(LinearLayout)view.findViewById(R.id.ll_paid_amount);
		ll_adjust_amount=(LinearLayout)view.findViewById(R.id.ll_adjust_amount);
		
        setDialogData();
        
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
       
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width-120),
                LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
        
        postpaid_whole_details.setLayoutParams(lp);
        AlertDialog alert = dialog.create();
       
        alert.show();
 
    }
   
    public void setDialogData(){
    	invoice_period.setText(billing_from);
	      invoice_amount.setText(current_billamount);
			     
		  if(Double.valueOf(pre_balance_amount)>0.0){
			    	 ll_unpaid_amount.setVisibility(View.VISIBLE);
				     unpaid_amount.setText(pre_balance_amount);
			     }else{
			    	 ll_unpaid_amount.setVisibility(View.GONE);
			     }

				 
		  if(Double.valueOf(bill_amount_fine)>0.0){
					 ll_fine_amount.setVisibility(View.VISIBLE);
					 fine_amount.setText(bill_amount_fine);
				 }else{
					 ll_fine_amount.setVisibility(View.GONE);
				 }
				
		  if(Double.valueOf(paid_amount)>0.0){
						ll_paid_amount.setVisibility(View.VISIBLE);
						Paid_amount.setText(paid_amount);
					}else
					{
						ll_paid_amount.setVisibility(View.GONE);
					}
				 
		  if(Double.valueOf(adjustment_amount)>0.0){
				ll_adjust_amount.setVisibility(View.VISIBLE);
				adjust_amount.setText(adjustment_amount);
			}
		else{
				ll_adjust_amount.setVisibility(View.GONE);
			}
			
		 
		 due_date.setText(getDate(payment_due_date));
		 paynality.setText(current_paynality);
		 payment_date.setText(getDate(current_date));
		 getCompare();
    }
    
    @Subscribe
    public void	onFinishEvent(FinishEvent event){
    	
    	Utils.log("Pospaid", "Finish");
		if(Postpaid_Activity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			Postpaid_Activity.this.finish();
		}
	}
    
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
    	Utils.log("Total", "Amount:"+total_amount);
    }
    
}
    