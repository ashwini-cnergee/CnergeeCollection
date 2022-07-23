package com.broadbandcollection.billing.services;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.SOAP.PaymentPickupNotificationSOAP;
import com.broadbandcollection.billing.SOAP.UpdatePickUpNotifySOAP;
import com.broadbandcollection.billing.activity.PaymentPickupActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Random;


public class PaymentPickupRequestService extends Service {

	 private String sharedPreferences_name;
	   private AuthenticationMobile Authobj;
	   public static String NotifyId="0";
	   Context context;
	   Utils utils= new Utils();
	   String AppVersion="";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 context = this;
		 AppVersion= Utils.GetAppVersion(this);
	        //sharedPreferences_name = getString(R.string.shared_preferences_name);
		 sharedPreferences_name = "CNERGEE";
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			utils.setSharedPreferences(sharedPreferences);
		//	Utils.log("onCreate LoginId","is: "+utils.getMobLoginId());
		String	MobLoginId = sharedPreferences.getString("MobLoginId", "");
		//Utils.log("onCreate MobLoginId","is: "+MobLoginId);
			Authobj = new AuthenticationMobile();
			Authobj.setMobileNumber(utils.getMobileNumber());
			Authobj.setMobLoginId(utils.getMobLoginId());
			Authobj.setMobUserPass(utils.getMobUserPass());
			Authobj.setIMEINo(utils.getIMEINo());
			Authobj.setCliectAccessId(utils.getCliectAccessId());
			Authobj.setMacAddress(utils.getMacAddress());
			Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
			Authobj.setAppVersion(AppVersion);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		/*Utils.log("LoginId","is: "+utils.getMobLoginId());
		Utils.log("Mob User Pass","is: "+utils.getAppUserName());
		
		Utils.log("Check","is: "+NotifyId);*/
		
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
	
			        // your logic
				   
				   if(Utils.isOnline(context)){
						/*PaymentPickupNotificationSOAP paymentPickupSoap = new PaymentPickupNotificationSOAP(getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
										.getResources().getString(
												R.string.METHOD_GET_PAYMENT_PICKUP));
						
						
						if(Authobj == null){
							Authobj = new AuthenticationMobile();
							Authobj.setMobileNumber(utils.getMobileNumber());
							Authobj.setMobLoginId(utils.getMobLoginId());
							Authobj.setMobUserPass(utils.getMobUserPass());
							Authobj.setIMEINo(utils.getIMEINo());
							Authobj.setCliectAccessId(utils.getCliectAccessId());
							Authobj.setMacAddress(utils.getMacAddress());
							Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
							Authobj.setAppVersion(AppVersion);
						}
						try {
							String rslt= paymentPickupSoap.CallPaymentPickupRequestSOAP(utils.getAppUserName(), Authobj);
							if(rslt.equalsIgnoreCase("ok")){
								if(NotifyId.equalsIgnoreCase("anyType{}")){
									
								}else{
								if(!NotifyId.equalsIgnoreCase("0")){
									//create notification
									showNotification(NotifyId);
									
									UpdatePickUpNotifySOAP updatePickUpNotifySOAP = new UpdatePickUpNotifySOAP(getApplicationContext().getResources().getString(
											R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
													.getResources().getString(
															R.string.METHOD_UPDATE_PAYMENT_PICKUP));
									String upDateRslt=updatePickUpNotifySOAP.UpdatePaymentPickupNotify(utils.getAppUserName(), Authobj, NotifyId);
									//Utils.log("Update NotifyId"," result: "+upDateRslt);
								}
								else{
									
								}
								}
							}
							else{
								
							}
							stopSelf();
						} catch (SocketTimeoutException e) {
							// TODO Auto-generated catch block
							stopSelf();
							e.printStackTrace();
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							stopSelf();
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							stopSelf();
							e.printStackTrace();
						}
						
					}*/
					   Utils.log("Notification Service","Started");
					   
					   if(!AuthenticationMobile.is_notification_running){
						   
						   Utils.log("Old AsyncTask Notification"," notrunning");
					   
					   if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
						 new  PaymentPickupAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						 }
						 else {
							new PaymentPickupAsync().execute();
						 }
					   }
					   else{
						   Utils.log("Old AsyncTask Notification","running");
					   }

					
			 
		stopSelf();
		
				   }
				   return START_STICKY;
	}
	
	class PaymentPickupAsync extends AsyncTask<String, Void, Void>{
		String rslt="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			AuthenticationMobile.is_notification_running=true;
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Utils.log("AsyncTask","Notification started");
			
			PaymentPickupNotificationSOAP paymentPickupSoap = new PaymentPickupNotificationSOAP(getApplicationContext().getResources().getString(
					R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_PAYMENT_PICKUP));
			if(Authobj == null){
				Authobj = new AuthenticationMobile();
				Authobj.setMobileNumber(utils.getMobileNumber());
				Authobj.setMobLoginId(utils.getMobLoginId());
				Authobj.setMobUserPass(utils.getMobUserPass());
				Authobj.setIMEINo(utils.getIMEINo());
				Authobj.setCliectAccessId(utils.getCliectAccessId());
				Authobj.setMacAddress(utils.getMacAddress());
				Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
				Authobj.setAppVersion(AppVersion);
			}
				try {
					rslt= paymentPickupSoap.CallPaymentPickupRequestSOAP(utils.getAppUserName(), Authobj);
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					Utils.log("Error in Notification","is"+e);
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					Utils.log("Error in Notification","is"+e);
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Utils.log("Error in Notification","is"+e);
					e.printStackTrace();
				}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Utils.log("AsyncTask Notification","on PostExecute");
			
			AuthenticationMobile.is_notification_running=false;
			if(rslt.equalsIgnoreCase("ok")){
				if(NotifyId.equalsIgnoreCase("anyType{}")){
					
				}else{
				if(!NotifyId.equalsIgnoreCase("0")){
					//create notification
					//showNotification(NotifyId);

					createNotification(NotifyId);
					
					new AsyncTask<String, Void, Void>(){

						@Override
						protected Void doInBackground(String... params) {
							// TODO Auto-generated method stub
							
							UpdatePickUpNotifySOAP updatePickUpNotifySOAP = new UpdatePickUpNotifySOAP(getApplicationContext().getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),utils.getDynamic_Url(),getApplicationContext()
											.getResources().getString(
													R.string.METHOD_UPDATE_PAYMENT_PICKUP));
							try {
								String upDateRslt=updatePickUpNotifySOAP.UpdatePaymentPickupNotify(utils.getAppUserName(), Authobj, NotifyId);
							} catch (SocketTimeoutException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}
						
					}.execute();
					
					
					//Utils.log("Update NotifyId"," result: "+upDateRslt);
				}
				else{
					
				}
				}
			}
			else{
				
			}
		}
	}
		

	
	
	
	public void showNotification(String PickUpId){
   
	        Random rand = new Random();
	        int notifID = rand.nextInt(100);
					
				 Intent intent = new Intent(context,
							PaymentPickupActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username",utils.getAppUserName());
					bundle.putString("password",utils.getAppPassword());
					intent.putExtra("from", "notification");
					intent.putExtra("com.cnergee.billing.showpaymentpickup.screen.INTENT", bundle);
					final Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
					NotificationManager notificationManager = (NotificationManager) systemService;
					
			        
					PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
					//startActivity(intent);intent
					  Notification notifyObj = new Notification(
				                R.drawable.pickup_button,
				                "Cnergee Billing",
				                System.currentTimeMillis());
				        
				        CharSequence from = "Cnergee Billing";
				        CharSequence message = "Payment Pickup Request from: "+PickUpId;        
				      //  notifyObj.setLatestEventInfo(this, from, message, contentIntent);
				     
				      //Value indicates the current number of events represented by the notification
				     //   notifyObj.number=++count;
				        //Set default vibration
				        notifyObj.defaults |= Notification.DEFAULT_VIBRATE;
				        //Set default notification sound
				        notifyObj.defaults |= Notification.DEFAULT_SOUND;
				        //Clear the status notification when the user selects it
				        notifyObj.flags|=Notification.FLAG_AUTO_CANCEL;   
				        //Send notification
				        notificationManager.notify(notifID, notifyObj);

	}

	private void createNotification(String PickUpId) {
		Intent intent = new Intent( this , PaymentPickupActivity. class );
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
				PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);

		Log.e("MessageBody",":"+PickUpId);

		//CharSequence message = "Payment Pickup Request from: "+PickUpId;
		Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
				.setSmallIcon( R.drawable.pickup_button)
				.setContentTitle("Cnergee Billing Notification")
				.setContentText(PickUpId)
				.setAutoCancel( true )
				.setSound(notificationSoundURI)
				.setContentIntent(resultIntent);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, mNotificationBuilder.build());
	}
	
	
}
