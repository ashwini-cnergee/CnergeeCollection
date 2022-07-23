/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.ChangePackage_NewActivity;
import com.broadbandcollection.billing.activity.ChangePackgeActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

public class AdjustmentCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private AuthenticationMobile Authobj;
	private AdjustmentSOAP adjustmentSOAP;
	private String username, newPlan, subscriberID, AreaCode, AreaCodeFilter;

	public AdjustmentCaller() {
	}

	public AdjustmentCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			adjustmentSOAP = new AdjustmentSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			adjustmentSOAP.setUsername(getUsername());
			adjustmentSOAP.setAreaCode(getAreaCode());
			adjustmentSOAP.setAreaCodeFilter(getAreaCodeFilter());
			adjustmentSOAP.setNewPlan(getNewPlan());
			adjustmentSOAP.setSubscriberID(getSubscriberID());

			ChangePackage_NewActivity.rslt = adjustmentSOAP
					.CallAdjustmentAmountSOAP(Authobj);
			ChangePackage_NewActivity.adjStringVal = adjustmentSOAP
					.getServerMessage();

		} catch (Exception e) {
			ChangePackgeActivity.rslt = e.toString();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the newPlan
	 */
	public String getNewPlan() {
		return newPlan;
	}

	/**
	 * @param newPlan
	 *            the newPlan to set
	 */
	public void setNewPlan(String newPlan) {
		this.newPlan = newPlan;
	}

	/**
	 * @return the subscriberID
	 */
	public String getSubscriberID() {
		return subscriberID;
	}

	/**
	 * @param subscriberID
	 *            the subscriberID to set
	 */
	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return AreaCode;
	}

	/**
	 * @param areaCode
	 *            the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	/**
	 * @return the areaCodeFilter
	 */
	public String getAreaCodeFilter() {
		return AreaCodeFilter;
	}

	/**
	 * @param areaCodeFilter
	 *            the areaCodeFilter to set
	 */
	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
	}

}
