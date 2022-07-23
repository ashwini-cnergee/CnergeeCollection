/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.CashActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

public class ValidateReceiptCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	private ValidateReceiptSOAP validateReceiptSOAP;

	String username, AreaCode, AreaCodeFilter, receptNo;

	public ValidateReceiptCaller() {
	}

	public ValidateReceiptCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;

		this.METHOD_NAME = METHOD_NAME;

		this.Authobj = Authobj;

	}

	public void run() {

		try {
			validateReceiptSOAP = new ValidateReceiptSOAP(
					WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			validateReceiptSOAP.setUsername(username);
			validateReceiptSOAP.setAreaCode(AreaCode);
			validateReceiptSOAP.setAreaCodeFilter(AreaCodeFilter);
			validateReceiptSOAP.setReceptNo(getReceptNo());

			CashActivity.isValid = validateReceiptSOAP.ValidateReceiptNo(Authobj);
			CashActivity.rslt = "ok";

		} catch (Exception e) {
			CashActivity.rslt = e.toString();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public String getAreaCodeFilter() {
		return AreaCodeFilter;
	}

	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
	}

	public String getReceptNo() {
		return receptNo;
	}

	public void setReceptNo(String receptNo) {
		this.receptNo = receptNo;
	}
}
