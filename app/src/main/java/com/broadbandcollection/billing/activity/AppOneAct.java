package com.broadbandcollection.billing.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.broadbandcollection.R;


public class AppOneAct extends Activity {
	TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_app_one);
        tv=(TextView) findViewById(R.id.textView1);
        getParameter();
                
    }

    
    private void getParameter(){
//    	Controller	 controller = (Controller) getApplication();
   
    try {
    	 Intent intent = getIntent();   
    	    Bundle myIntent = intent.getExtras();
    	String msg=myIntent.getString("message");
    	String	billNumber=myIntent.getString("invNumber"); 
    	String	amount=myIntent.getString("amount"); 
    	String	tipAmount=myIntent.getString("tipAmount"); 
    	String	merchantId=myIntent.getString("merchantId"); 
    	String	terminalId=myIntent.getString("tid"); 
    	String	cardNumber=myIntent.getString("cardNumber");
    	
    	String	tid=myIntent.getString("tid"); 
    	String	approvalCode=myIntent.getString("approvalCode"); 
    	String	cardType=myIntent.getString("cardType"); 
    	String	date=myIntent.getString("dateTime"); 
    	String	retrievalReferenceNumber=myIntent.getString("rrn");
    	
    	String string=new String();
    	string +="Msg :"+msg+"\n";
    //	string +="Result :"+result+"\n";
    	string +="Amount :"+amount+"\n";
    	string +="Tip Amount :"+tipAmount+"\n";
    	string +="Merchant Id :"+merchantId+"\n";
    	string +="Terminal Id :"+terminalId+"\n";
    	string +="Card number : xxxx"+cardNumber+"\n";
    	
    	//string	+="Terminal Id :"+tid+"\n";
    	string	+="Invoice Number :"+billNumber+"\n";
    	string	+="Auth Code"+approvalCode+"\n";
    	string	+="Card Type"+cardType+"\n";
    	string	+="Date "+date+"\n";
    	string	+="RRN :"+retrievalReferenceNumber+"\n";
    	intent.putExtra("message", msg);
    	intent.putExtra("invNumber", billNumber);
    	intent.putExtra("amount", amount);
    	intent.putExtra("tipAmount", tipAmount);
    	intent.putExtra("merchantId", merchantId);
    	intent.putExtra("terminalId", terminalId);
    	intent.putExtra("rrn", retrievalReferenceNumber);
    	//intent.putExtra(name, value);
    	//intent.putExtra(name, value);
    	
    	
    	tv.setText(string);
    	Intent mainIntent = new Intent(AppOneAct.this,CardActivity.class);
    	mainIntent.putExtra("message", msg);
    	mainIntent.putExtra("invNumber", billNumber);
    	mainIntent.putExtra("amount", amount);
    	mainIntent.putExtra("tipAmount", tipAmount);
    	mainIntent.putExtra("merchantId", merchantId);
    	mainIntent.putExtra("terminalId", terminalId);
    	mainIntent.putExtra("rrn", retrievalReferenceNumber);
        AppOneAct.this.startActivityForResult(mainIntent, 0);
        AppOneAct.this.finish();
    } catch (Exception e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
   // startActivity(new Intent(AppOneAct.this,MainActivity.class));
    Intent mainIntent = new Intent(AppOneAct.this,MainActivity.class);
    AppOneAct.this.startActivity(mainIntent);
    AppOneAct.this.finish();
    }

    	
    } 
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	this.finish();
    	
    }
}
