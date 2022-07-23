package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.broadbandcollection.R;


public class IP_PaymentDetails_Activity extends Activity {
Button btn_search_another_subs,btn_logout;
TextView tv_receipt_confirm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_details);
		initailizeControls();
		 
		btn_search_another_subs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IP_PaymentDetails_Activity.this.finish();
			}
		});
	}
	
	public void initailizeControls(){
		btn_search_another_subs=(Button) findViewById(R.id.search);
		btn_logout=(Button) findViewById(R.id.logout);
		tv_receipt_confirm=(TextView) findViewById(R.id.RefText);
		Intent i= getIntent();
		String details=i.getStringExtra("receipt_no");
		tv_receipt_confirm.setText(details);
	}
}
