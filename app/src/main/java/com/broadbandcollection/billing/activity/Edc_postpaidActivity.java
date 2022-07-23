package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.broadbandcollection.R;


public class Edc_postpaidActivity extends Activity{

	Button			btn_submit,btn_back;
	
	TextView 		payment_amount,label_add_amount,tv_display_add_amount;
	
	EditText		payment_date,dd_no,bank_name;
	
	String          amount;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_postpaid_edc);
		
		InitalizeControls();
		
		amount = getIntent().getExtras().getString("Amount");
		payment_amount.setText(amount);
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Edc_postpaidActivity.this.finish();
				Intent edc_intent=new Intent(Edc_postpaidActivity.this, Postpaid_Activity.class);
				startActivity(edc_intent);
			}
		});
	}
	
	public void InitalizeControls() {
	 
	  btn_submit=(Button)findViewById(R.id.submit);
	  btn_back=(Button)findViewById(R.id.backBtn);
	
      payment_date=(EditText)findViewById(R.id.edc_date);
      dd_no=(EditText)findViewById(R.id.edc_no);
      bank_name=(EditText)findViewById(R.id.edc_bank_name);
      
      payment_amount=(TextView)findViewById(R.id.edc_amount);
  }
}

