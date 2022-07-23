/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.SOAP;



import com.broadbandcollection.billing.activity.SearchVendorActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class TodaysCollectionCaller extends Thread {

	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private AuthenticationMobile Authobj;
	private String UserLoginName;
	
	
	public TodaysCollectionSOAP todaysCollectionSOAP;
	
	
	public TodaysCollectionCaller() {
	}

	public TodaysCollectionCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}
	
	public void run() {

		try {
			todaysCollectionSOAP = new TodaysCollectionSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			todaysCollectionSOAP.setUserLoginName(getUserLoginName());
			SearchVendorActivity.rslt = todaysCollectionSOAP.CallTodaysCollectionSOAP(Authobj);
			SearchVendorActivity.todayCollection = todaysCollectionSOAP.getTodayCollection();
		}catch (SocketException e) {
			e.printStackTrace();
			SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			SearchVendorActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

	/**
	 * @return the userLoginName
	 */
	public String getUserLoginName() {
		return UserLoginName;
	}

	/**
	 * @param userLoginName the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
	}


}
