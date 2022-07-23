/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

	public static final String VIEW_DATE_FORMAT = "dd-MMM-yyyy";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public String MobileNumber, MobLoginId, MobUserPass,IMEINo, app_username,
			app_password,CliectAccessId,Dynamic_Url,MacAddress,PhoneUniqueId;
	
	public static boolean ACCEPT_CHEQUE=true;
	public static String ACTION_TAKE="Renew";
	public static boolean SHOW_LOG=true;
	
	public static String POSTPAID_OBJECT="postpaid";
	
	public static String SUBSCRIBER_IP="subscriber_ip_details";
	public static String PaymentMode="PaymentMode";
	public static String PaymentURL="PaymentURL";

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		MobileNumber = sharedPreferences.getString("MobileNumber", "");
		MobLoginId = sharedPreferences.getString("MobLoginId", "");
		MobUserPass = sharedPreferences.getString("MobUserPass", "");
		IMEINo = sharedPreferences.getString("IMEINo", "");
		app_username = sharedPreferences.getString("USER_NAME", "");
		app_password = sharedPreferences.getString("USER_PASSWORD", "");
		CliectAccessId= sharedPreferences.getString("CliectAccessId", "");
		Dynamic_Url= sharedPreferences.getString("Dynamic_Url", "");
		MacAddress= sharedPreferences.getString("MacAddress", "");
		PhoneUniqueId= sharedPreferences.getString("PhoneUniqueId", "");
	}
	
	public void clearSharedPreferences(SharedPreferences sharedPreferences) {
		 
		SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.remove("USER_NAME");
	    editor.remove("USER_PASSWORD");
	    //editor.clear();
	    editor.commit();
	}
	
	/**
	 * @return the app_username
	 */
	public String getAppUserName() {
		return app_username;
	}

	/**
	 * @param app_username
	 *            the app_username to set
	 */
	public void setAppUserName(String app_username) {
		this.app_username = app_username;
	}

	/**
	 * @return the app_password
	 */
	public String getAppPassword() {
		return app_password;
	}

	/**
	 * @param app_password
	 *            the app_password to set
	 */
	public void setAppPassword(String app_password) {
		this.app_password = app_password;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return MobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	/**
	 * @return the mobLoginId
	 */
	public String getMobLoginId() {
		return MobLoginId;
	}

	/**
	 * @param mobLoginId the mobLoginId to set
	 */
	public void setMobLoginId(String mobLoginId) {
		MobLoginId = mobLoginId;
	}
	
	/**
	 * @return the mobUserPass
	 */
	public String getMobUserPass() {
		return MobUserPass;
	}

	/**
	 * @param mobUserPass the mobUserPass to set
	 */
	public void setMobUserPass(String mobUserPass) {
		MobUserPass = mobUserPass;
	}

	/**
	 * @return the iMEINo
	 */
	public String getIMEINo() {
		return IMEINo;
	}

	/**
	 * @param iMEINo the iMEINo to set
	 */
	public void setIMEINo(String iMEINo) {
		IMEINo = iMEINo;
	}
	
	/**
	 * @return the CustomerId
	 */
	public String getCliectAccessId() {
		return CliectAccessId;
	}

	/**
	 * @param CustomerId the CustomerId to set
	 */
	public void setCliectAccessId(String CliectAccessId) {
		this.CliectAccessId = CliectAccessId;
	}

	public String getDynamic_Url() {
		return Dynamic_Url;
	}

	public void setDynamic_Url(String dynamic_Url) {
		Dynamic_Url = dynamic_Url;
	}
	
	public String getMacAddress() {
		return MacAddress;
	}



	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}



	public String getPhoneUniqueId() {
		return PhoneUniqueId;
	}



	public void setPhoneUniqueId(String phoneUniqueID) {
		PhoneUniqueId = phoneUniqueID;
	}
	
	public static boolean isOnline(Context context) {
	    
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	        {
	            if (ni.isConnected())
	            {
	                haveConnectedWifi = true;
	                System.out.println("WIFI CONNECTION AVAILABLE");
	            } else
	            {
	                System.out.println("WIFI CONNECTION NOT AVAILABLE");
	            }
	        }
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	        {
	            if (ni.isConnected())
	            {
	                haveConnectedMobile = true;
	                System.out.println("MOBILE INTERNET CONNECTION AVAILABLE");
	            } else
	            {
	                System.out.println("MOBILE INTERNET CONNECTION NOT AVAILABLE");
	            }
	        }
	    }
	    return haveConnectedWifi || haveConnectedMobile;

	}
public static String GetAppVersion(Context ctx){
PackageInfo pInfo ;
	try {
		pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
		if(pInfo.versionName==null){
			return " ";
		}
		else{
			return pInfo.versionName;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return " ";
	}

 }
	public static void log(String name,String value){
		if(SHOW_LOG){
			Log.e(name,value);
		}
	}
	
	
}
