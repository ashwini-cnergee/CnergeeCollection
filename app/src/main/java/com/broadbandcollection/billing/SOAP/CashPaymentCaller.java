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
import com.broadbandcollection.billing.obj.PaymentObj;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CashPaymentCaller extends Thread {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	private CashPaymentSOAP cashPayment;
	private PaymentObj paymentObj;

	String username;
	Double Oustanding_Add_Amt;

	public CashPaymentCaller() {
	}

	public CashPaymentCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {
		try {
			cashPayment = new CashPaymentSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			cashPayment.setUsername(username);
			cashPayment.setPaymentObj(getPaymentObj());
			cashPayment.setOutstanding_Add_Amount(getOustanding_Add_Amt());
			CashActivity.rslt = cashPayment.CallCahsPaymentSOAP(Authobj);
			CashActivity.REF_NO = cashPayment.getRefNo();
		}catch (SocketException e) {
			e.printStackTrace();
			CashActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			CashActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			CashActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

	public void setPaymentObj(PaymentObj paymentObj) {
		this.paymentObj = paymentObj;
	}

	public PaymentObj getPaymentObj() {
		return paymentObj;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getOustanding_Add_Amt() {
		return Oustanding_Add_Amt;
	}

	public void setOustanding_Add_Amt(Double oustanding_Add_Amt) {
		Oustanding_Add_Amt = oustanding_Add_Amt;
	}

}
