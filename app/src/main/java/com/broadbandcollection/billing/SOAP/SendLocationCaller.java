package com.broadbandcollection.billing.SOAP;

import android.content.Context;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.services.GpsTrackerService;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SendLocationCaller extends Thread {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	AuthenticationMobile authObj;
	Context ctx;
	
	SendLocationSOAP sendLocationSOAP;
	public String username;

	public SendLocationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL, String METHOD_NAME, AuthenticationMobile Authobj, Context context) {
		// TODO Auto-generated constructor stub
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.authObj=Authobj;
		this.ctx=context;
	}

	public void run(){
		try {
			sendLocationSOAP = new SendLocationSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);

			GpsTrackerService.rslt = sendLocationSOAP.CallSendLocationSOAP(username, authObj,ctx);
			
		}catch (SocketException e) {
			e.printStackTrace();
			GpsTrackerService.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			GpsTrackerService.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			GpsTrackerService.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}
}
