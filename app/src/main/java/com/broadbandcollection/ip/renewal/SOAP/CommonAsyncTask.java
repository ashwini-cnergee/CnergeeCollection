package com.broadbandcollection.ip.renewal.SOAP;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class CommonAsyncTask extends AsyncTask<Object, Void	, ArrayList<String>> implements OnCancelListener {

	Context ctx;
	
	public CommonAsyncTask(Context ctx){
		this.ctx=ctx;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Log.d("OnPrexcute","execute");
		
	}
	
	@Override
	protected ArrayList<String> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		
		Log.d("doInBackground","execute");
		CommonSOAP comSoap=(CommonSOAP)params[0];
		ArrayList<String>alResponses= new ArrayList<String>(); 
		String result="";
		String response="";
		try {
			result=comSoap.executeMethod();
			response=comSoap.getResponse();
			
			alResponses.add(result);
			alResponses.add(response);
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alResponses.add(result);
			alResponses.add(response);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alResponses.add(result);
			alResponses.add(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alResponses.add(result);
			alResponses.add(response);
		}
		return alResponses;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.d("onPostExecute","execute");
		//mProgressHUD.dismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		//mProgressHUD.dismiss();
	}
}
