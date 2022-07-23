/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.ChangePackgeActivity;
import com.broadbandcollection.billing.activity.PackgedetailActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

public class PackgeCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	private AuthenticationMobile Authobj;
	private String  AreaCode, AreaCodeFilter,ConnectionTypeId;
	public String username;
	public String password;
	PackgeSOAP packageSOAP;
	private String SubscriberID;

	public PackgeCaller() {
	}

	public PackgeCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			packageSOAP = new PackgeSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			packageSOAP.setAreaCode(getAreaCode());
			packageSOAP.setAreaCodeFilter(getAreaCodeFilter());
			packageSOAP.setConnectionTypeId(getConnectionTypeId());
			//ChangePackgeActivity.rslt = packageSOAP.CallPackageSOAP(Authobj,SubscriberID);
			//ChangePackgeActivity.strXML = packageSOAP.getXMLData();

			PackgedetailActivity.rslt = packageSOAP.CallPackageSOAP(Authobj,SubscriberID);
			PackgedetailActivity.strXML = packageSOAP.getXMLData();

		} catch (Exception e) {
			ChangePackgeActivity.rslt = e.toString();
		}
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return AreaCode;
	}

	/**
	 * @param areaCode the areaCode to set
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
	 * @param areaCodeFilter the areaCodeFilter to set
	 */
	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
	}
	
	public String getSubscriberId(){
		return SubscriberID;
	}
	
	public void setSubscriberId(String SubscriberID){
		this.SubscriberID=SubscriberID;
	}
	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}
}
