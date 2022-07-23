/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.PaymentPickupActivity;
import com.broadbandcollection.billing.activity.SearchVendorActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;


import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SearchMemberCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	private SearchMemberSOAP searchMember;

	public String username;
	public String password, searchTxt;
	public String execteFrom = null;
	
	public SearchMemberCaller() {
	}

	public SearchMemberCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			searchMember = new SearchMemberSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			if(execteFrom.equalsIgnoreCase("paypickup")){
				PaymentPickupActivity.rslt = searchMember.CallSearchMemberSOAP(
						username, password, searchTxt,Authobj);
				PaymentPickupActivity.mapMemberData = searchMember
						.getMapMemberData();
				PaymentPickupActivity.Message=searchMember.getMessage();
				
			}else{
				SearchVendorActivity.rslt = searchMember.CallSearchMemberSOAP(
					username, password, searchTxt,Authobj);
				SearchVendorActivity.mapMemberData = searchMember
					.getMapMemberData();
				SearchVendorActivity.Message=searchMember.getMessage();
			}
			
		}catch (SocketException e) {
			e.printStackTrace();
			SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			e.printStackTrace();
			SearchVendorActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

}
