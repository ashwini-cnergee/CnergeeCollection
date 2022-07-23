package com.broadbandcollection.iprenewal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.activity.BaseApplication;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.squareup.otto.Subscribe;

public class SubscriberDetails_Ip_Activity extends Activity{	
	SubscriberDetails_Ip subscriberDetails_Ip;
	TextView tv_ips_name,tv_area_name,tv_package_name,tv_price,tv_status,tv_package_expiry,tv_mobile_number,tv_subs_name,tv_address,tv_ip_address;
	Button btn_renew,btn_change_pack,btn_back;
	EditText et_primary_mob_no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscriber_details_ip);
		subscriberDetails_Ip=(SubscriberDetails_Ip) this.getIntent().getSerializableExtra(Utils.SUBSCRIBER_IP);
		initialzeControls();
		
		setSubscriberData();
		BaseApplication.getEventBus().register(this);
	}
	
	public void initialzeControls(){
		tv_subs_name=(TextView)findViewById(R.id.subscriberdetails_header);
		tv_ips_name=(TextView) findViewById(R.id.tv_isp_name);
		tv_area_name=(TextView) findViewById(R.id.tv_area_name);
		tv_package_name=(TextView) findViewById(R.id.tv_ip_sale_package_name);
		tv_price=(TextView) findViewById(R.id.tv_price);
		tv_status=(TextView) findViewById(R.id.tv_status);
		tv_package_expiry=(TextView) findViewById(R.id.tv_package_expiry);
		tv_mobile_number=(TextView) findViewById(R.id.tv_mobile_number);
		tv_address=(TextView) findViewById(R.id.tv_address);
		tv_ip_address=(TextView) findViewById(R.id.tv_ip_address);
		et_primary_mob_no=(EditText) findViewById(R.id.et_primary_mob_no);
		btn_renew=(Button) findViewById(R.id.btn_renew);
		btn_change_pack=(Button) findViewById(R.id.btn_change_pack);
		btn_back=(Button) findViewById(R.id.btn_back);
		
		btn_renew.setOnClickListener(clickListener);
		btn_change_pack.setOnClickListener(clickListener);
		btn_back.setOnClickListener(clickListener);
		
		if(subscriberDetails_Ip.getIPPoolId().length()>0&&!subscriberDetails_Ip.getIPPoolId().equalsIgnoreCase("0")&&subscriberDetails_Ip.getIPAddress().length()>0&&!subscriberDetails_Ip.getIPAddress().equalsIgnoreCase("0")){
			btn_renew.setVisibility(View.VISIBLE);
		}
		else{
			btn_renew.setVisibility(View.GONE);
		}
		tv_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertsBoxFactory.showAlert("Address", subscriberDetails_Ip.getAddress(), SubscriberDetails_Ip_Activity.this);
			}
		});
	}
	
	public void setSubscriberData(){
		tv_subs_name.setText(subscriberDetails_Ip.getMemberName());
		tv_ips_name.setText(subscriberDetails_Ip.getISP_Name());
		tv_area_name.setText(subscriberDetails_Ip.getAreaName());
		tv_package_name.setText(subscriberDetails_Ip.getIPSalePackageName());
		tv_price.setText(subscriberDetails_Ip.getPrice());
		tv_status.setText(subscriberDetails_Ip.getStatus());
		tv_package_expiry.setText(subscriberDetails_Ip.getIPExpireDate());
		tv_mobile_number.setText(subscriberDetails_Ip.getMobileNoprimary());
		tv_ip_address.setText(subscriberDetails_Ip.getIPAddress());
		
	}

	public OnClickListener clickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btn_renew) {
				if (subscriberDetails_Ip.getMobileNoprimary().length() > 0) {
					
						Intent renew_intent = new Intent(
								SubscriberDetails_Ip_Activity.this,
								Renew_IP_Activity.class);
						renew_intent.putExtra(Utils.SUBSCRIBER_IP,
								subscriberDetails_Ip);
						startActivity(renew_intent);
				}else {
					
				
					if (et_primary_mob_no.getText().length() > 0) {

						if (et_primary_mob_no.getText().length() == 10) {
							subscriberDetails_Ip.setMobileNoprimary(et_primary_mob_no.getText().toString());
							Intent renew_intent = new Intent(
									SubscriberDetails_Ip_Activity.this,
									Renew_IP_Activity.class);
							renew_intent.putExtra(Utils.SUBSCRIBER_IP,
									subscriberDetails_Ip);
							startActivity(renew_intent);
						}
						else{
							AlertsBoxFactory.showAlert("Please enter Valid Alternate Mobile Number.", SubscriberDetails_Ip_Activity.this);
						}
					} else {
						AlertsBoxFactory.showAlert("Please enter Alternate Mobile Number.", SubscriberDetails_Ip_Activity.this);
					}
				} 
			}
			if(v==btn_change_pack){
				Intent change_ip_intent= new Intent(SubscriberDetails_Ip_Activity.this,Change_Ip_Activity.class);
				change_ip_intent.putExtra(Utils.SUBSCRIBER_IP, subscriberDetails_Ip);
				startActivity(change_ip_intent);
			}
			if(v==btn_back){
				SubscriberDetails_Ip_Activity.this.finish();
			}
		}
	};
	
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
	
		if(SubscriberDetails_Ip_Activity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			SubscriberDetails_Ip_Activity.this.finish();
		}
		
	}
}
