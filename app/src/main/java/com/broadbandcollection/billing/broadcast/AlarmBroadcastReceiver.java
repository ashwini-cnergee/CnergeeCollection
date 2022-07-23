package com.broadbandcollection.billing.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.broadbandcollection.billing.services.GpsTrackerService;
import com.broadbandcollection.billing.services.PaymentPickupRequestService;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Utils.log("Alarm broadcast","started");
		String activity= intent.getStringExtra("activity");
		if(activity==null){
			activity="null";
		}
		if(activity.equalsIgnoreCase("alarm")){
			
			Intent i= new  Intent(context, PaymentPickupRequestService.class);
			context.startService(i);		
		}
		else
		{
			Intent i= new  Intent(context, GpsTrackerService.class);
			i.putExtra("activity", activity);
			context.startService(i);	
		}
		
	}

}
