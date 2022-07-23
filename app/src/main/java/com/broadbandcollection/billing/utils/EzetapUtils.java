package com.broadbandcollection.billing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class EzetapUtils {
	// for dev server
//		public static final String BASE_PACKAGE = "com.ezetap.service.dev";
//		public static final String APK_URL 	   = "http://d.eze.cc/portal/app/android/serviceApp";
		
		//For DEMO Server
		public static final String BASE_PACKAGE = "com.ezetap.service.demo";
		public static final String APK_URL 	   = "http://d.eze.cc/demo/app/android/serviceApp";
		
		public static final String EZETAP_PACKAGE_ACTION = ".EZESERV";
		private static final String DEBUG_TAG = "EzeUtils";
		
		public static final String KEY_JSON_REQ_DATA 			= "jsonReqData";
		public static final String KEY_ACTION 					= "action";
		public static final String ACTION_PAYCARD				= "paycard";
		public static final String KEY_ENABLE_CUSTOM_LOGIN			= "enableCustomLogin";
		public static final String KEY_AMOUNT 					= "amount";
		public static final String KEY_TIP_AMOUNT				= "additionalAmount";
		public static final String KEY_USERNAME 				= "username";
		public static final String KEY_ORDERID 					= "orderNumber";
		public static final String KEY_CUSTOMER_MOBILE 			= "customerMobileNumber";
		
		
		public void startCardPayment(double amount, double tip, String mobile,
				Activity context, 
				String appKey, 
				String username, 
				String orderNumber,
				int reqCode,Hashtable<String, Object> appData, boolean enableCustomLogin) {
			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					/*Log.v(DEBUG_TAG, "Ezetap app not found.");*/
					showDownloadDialog(context);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				
				JSONObject reqData = new JSONObject();
				
				reqData.put(KEY_ACTION, ACTION_PAYCARD);

				reqData.put(KEY_ENABLE_CUSTOM_LOGIN,enableCustomLogin);
				reqData.put(KEY_AMOUNT, new Double(amount));
				if (tip > 0.0) {
					reqData.put(KEY_TIP_AMOUNT, new Double(tip));
				}
				reqData.put(KEY_USERNAME, username);
//				reqData.put(EzeConstants.KEY_APPKEY, appKey);
				reqData.put(KEY_ORDERID, orderNumber);
				reqData.put(KEY_CUSTOMER_MOBILE, mobile);
				
				if(appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if(aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[])aVal;

							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							
							reqData.put(aKey, jsonArr);
						} else if(aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
					
				}
				/*Log.v(DEBUG_TAG, "MAP>>"+appData);
				Log.v(DEBUG_TAG, ">>"+reqData.toString(0));*/
				intent.putExtra(KEY_JSON_REQ_DATA, reqData.toString());
				
				
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private String findTargetAppPackage(Intent intent, Activity context) {
			PackageManager pm = context.getPackageManager();
			List<ResolveInfo> availableApps = pm.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
			if (availableApps != null) {
				for (ResolveInfo availableApp : availableApps) {
					String packageName = availableApp.activityInfo.packageName;
					if (BASE_PACKAGE.equals(packageName)) {
						return packageName;
					}
				}
			}
			return null;
		}
		private AlertDialog showDownloadDialog(final Activity context) {
			AlertDialog.Builder downloadDialog = new AlertDialog.Builder(context);
			downloadDialog.setTitle("Install Ezetap");
			downloadDialog
					.setMessage("This application requires Ezetap. Would you like to install it?");
			downloadDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							//TODO: Jayesh : need to define multiple links
//							Uri uri = Uri
//									.parse(APK_URL);
//							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							try {
//								context.startActivity(intent);
								EzetapDownloadUtils utils = new EzetapDownloadUtils(APK_URL, context);
								utils.start();
								
							} catch (ActivityNotFoundException anfe) {
								/*Log.v(DEBUG_TAG,
										"Could not install Ezetap application.");*/
							}
						}
					});
			downloadDialog.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Toast toast = Toast.makeText(context,
									"Operation aborted.", Toast.LENGTH_LONG);
							toast.show();
						}
					});
			return downloadDialog.show();
		}
}
