package com.broadbandcollection.billing.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	//	Utils.log("Boot Broadcast ","called");
		Intent intentService1 = new Intent(context,AlarmBroadcastReceiver.class);
		intentService1.putExtra("activity", "restart");
		
		PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, PendingIntent.FLAG_IMMUTABLE);
		Calendar cal = Calendar.getInstance();
		AlarmManager alarm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
		// Start every 1mon..
		alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*60*1000, pintent1);
		
		
	}

}
