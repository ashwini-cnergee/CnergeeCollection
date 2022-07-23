package com.broadbandcollection.billing.SOAP;




import com.broadbandcollection.billing.activity.AppSettingctivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class AppSettingCaller extends Thread {
	AppSettingSoap appSettingSoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	
	public AppSettingCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}
	
	public void run() {

		try {
			appSettingSoap= new AppSettingSoap(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			AppSettingctivity.rslt=appSettingSoap.CallAppSettingSOAP(Authobj);
			
			AppSettingctivity.AccessCodeFlag=appSettingSoap.isValidUser();
			
		}
		catch (SocketException e) {
			e.printStackTrace();
			//Utils.log("1",""+e);
			AppSettingctivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			//Utils.log("2",""+e);
			AppSettingctivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			//Utils.log("3",""+e);
			AppSettingctivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}
}
