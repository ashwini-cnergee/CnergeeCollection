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
import com.broadbandcollection.billing.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PaymentRequestCaller extends Thread {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private AuthenticationMobile Authobj;
	private String UserLoginName;
	private boolean isAllData;
	
	public PaymentRequestSOAP paymentRequestSOAP;
	
	public PaymentRequestCaller() {
	}

	public PaymentRequestCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			paymentRequestSOAP = new PaymentRequestSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			paymentRequestSOAP.setUserLoginName(getUserLoginName());
			paymentRequestSOAP.setAllData(isAllData());
			
			if(isAllData()){
				SearchVendorActivity.rslt = paymentRequestSOAP.CallPaymentRequestSOAP(Authobj);
				PaymentPickupActivity.rslt=SearchVendorActivity.rslt;
				
				PaymentPickupActivity.paymentPickList = paymentRequestSOAP.getPaymentPickList();
				PaymentPickupActivity.alSubId=paymentRequestSOAP.getAlSubId();
				PaymentPickupActivity.alPickupId=paymentRequestSOAP.getAlPickUpId();
				//PaymentPickupActivity.al_is_postpaid=paymentRequestSOAP.getAl_is_postpaid();
				
				SearchVendorActivity.paymentPickList = paymentRequestSOAP.getPaymentPickList();
				SearchVendorActivity.alSubId=paymentRequestSOAP.getAlSubId();
				SearchVendorActivity.alPickupId=paymentRequestSOAP.getAlPickUpId();
				//SearchVendorActivity.al_is_postpaid=paymentRequestSOAP.getAl_is_postpaid();
				
				SearchVendorActivity.payCount = paymentRequestSOAP.getPayCount();
				
				
				Utils.log("Result is",":"+SearchVendorActivity.rslt);
				Utils.log("Result is",":"+SearchVendorActivity.rslt);
			
			}/*else{
				SearchVendorActivity.rslt = paymentRequestSOAP.CallPaymentRequestSOAP(Authobj);
				SearchVendorActivity.payCount = paymentRequestSOAP.getPayCount();
			
			}*/
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(isAllData())
				PaymentPickupActivity.rslt = "Internet connection not available!!";
			else
				SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(isAllData())
				PaymentPickupActivity.rslt = "Internet connection not available!!";
			else
				SearchVendorActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			if(isAllData())
				PaymentPickupActivity.rslt = "Invalid web-service response.<br>"+e.toString();
			else
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

	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}


}
