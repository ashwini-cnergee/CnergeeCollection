/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.ChequeActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;


import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ChequePaymentCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	private ChequePaymentSOAP chequePayment;
	private PaymentObj paymentObj;
	Double Oustanding_Add_Amt;

	public ChequePaymentCaller() {
	}

	public ChequePaymentCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
		}

	public void run() {

		try {
			chequePayment = new ChequePaymentSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);

			chequePayment.setPaymentObj(getPaymentObj());
			chequePayment.setOustanding_Add_Amt(getOustanding_Add_Amt());
			ChequeActivity.rslt = chequePayment.CallChequePaymentSOAP(Authobj);
			ChequeActivity.REF_NO = chequePayment.getRefNo();

		}catch (SocketException e) {
			e.printStackTrace();
			ChequeActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			ChequeActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			ChequeActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

	public void setPaymentObj(PaymentObj paymentObj) {
		this.paymentObj = paymentObj;
	}

	public PaymentObj getPaymentObj() {
		return paymentObj;
	}
	
	public Double getOustanding_Add_Amt() {
		return Oustanding_Add_Amt;
	}

	public void setOustanding_Add_Amt(Double oustanding_Add_Amt) {
		Oustanding_Add_Amt = oustanding_Add_Amt;
	}
}
