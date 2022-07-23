package com.broadbandcollection.billing.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.broadbandcollection.billing.utils.AlertsBoxFactory;


public class ShowAlertService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try{
		AlertsBoxFactory.showAlert("Please turn on GPS!!", getApplicationContext());
		}
		catch (Exception e) {
		// TODO: handle exception
			stopSelf();
		}
		stopSelf();
		return START_STICKY;
	}
}
