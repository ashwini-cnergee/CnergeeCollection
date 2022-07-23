/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.SOAP;



import com.broadbandcollection.billing.activity.CardActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CardPaymentCaller  extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private CardPaymentSOAP cardPayment;
	private PaymentObj paymentObj;
	private AuthenticationMobile Authobj;
	String username;

	public CardPaymentCaller() {
	}

	public CardPaymentCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			cardPayment = new CardPaymentSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			cardPayment.setUsername(username);
			cardPayment.setPaymentObj(getPaymentObj());

			CardActivity.rslt = cardPayment.CallCardPaymentSOAP(Authobj);
			CardActivity.REF_NO = cardPayment.getRefNo();
		}catch (SocketException e) {
			e.printStackTrace();
			CardActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			CardActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			CardActivity.rslt = "Invalid web-service response.<br>"+e.toString();
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
}
