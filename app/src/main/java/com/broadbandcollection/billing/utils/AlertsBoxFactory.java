
/*
*
* Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
* Date of code generation:  22 Dec. 2012
*
* Version 1.1
*
*/

package com.broadbandcollection.billing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Html;


public class AlertsBoxFactory {
		
		public static void showAlert(String message, Context ctx)
		{
			showAlert(null,message,ctx);
		}
	   public static void showAlert(String strTitle,String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	      builder.setIcon(android.R.drawable.ic_dialog_alert);
	      
	      if(strTitle != null)
	    	  builder.setTitle(strTitle);
	      else
	    	  builder.setTitle("Alert");
	      
	      builder.setMessage(Html.fromHtml(message));
	      
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }
	   public static void showAlertColorTxt(String str_color,String message, Context ctx)
	   {
		   showAlertColorTxt(null,str_color,message,ctx);
	   }
	   public static void showAlertColorTxt(String strTitle,String str_color,String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	      builder.setIcon(android.R.drawable.ic_dialog_alert);
	      if(strTitle != null)
	    	  builder.setTitle(strTitle);
	      else
	    	  builder.setTitle("Alert");
	      
	      if(str_color != null || str_color != ""){
	    	  builder.setMessage(Html.fromHtml("<font color='"+str_color+"'>"+message+"</font>"));
	      }
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }

	   public static void showErrorAlert(String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	      builder.setIcon(android.R.drawable.ic_dialog_info);
	      builder.setTitle("Exception ");
	      builder.setMessage(message);
	      
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }
	   
	   public static void showGPSDisabledAlertToUser(final Context context){
		   AlertDialog.Builder   alertDialogBuilder   = new AlertDialog.Builder(context);
	        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
	        .setCancelable(false)
	        .setPositiveButton("Goto Settings Page To Enable GPS",
	                new OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                Intent callGPSSettingIntent = new Intent(
	                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                context.startActivity(callGPSSettingIntent);
	            }
	        });
	        /*alertDialogBuilder.setNegativeButton("Cancel",
	                new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	            }
	        });*/
	        AlertDialog  alert   = alertDialogBuilder.create();
	        alert.show();
	       	        
	    }
	
	   public static void showExitAlert(String message,final Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	      builder.setIcon(android.R.drawable.ic_dialog_alert);
	      
	     
	    	  builder.setTitle("Alert");
	      
	      builder.setMessage(Html.fromHtml(message));
	      
	      //add buttons and listener
	      
	      builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				((Activity)ctx).finish();
			}
		});
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }
	 

	   
	   
}
