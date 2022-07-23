/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;




import com.broadbandcollection.billing.activity.Login;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LoginCaller extends Thread {

	public LoginSOAP login;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	private AuthenticationMobile Authobj;

	public String username;
	public String password;

	public LoginCaller() {
	}

	public LoginCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			login = new LoginSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			Login.rslt = login.CallLoginSOAP(username, password,Authobj);
			Login.isVaildUser = login.isValidUser();
			
		}catch (SocketException e) {
			e.printStackTrace();
			Login.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			Login.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			Login.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

}
