/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  11 Jan. 2012
 *
 * Version 1.2
 *
 */

package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.CardActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;

public class TransactionIDCaller extends Thread  {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private AuthenticationMobile Authobj;
	private TransactionIDSOAP trasactionIdSOAP;
	private String username,MemberId;
	
	public TransactionIDCaller(String WSDL_TARGET_NAMESPACE,
			String SOAP_URL, String METHOD_NAME, AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;

		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	
	}

	public void run() {

		try {
			trasactionIdSOAP = new TransactionIDSOAP(
					WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			trasactionIdSOAP.setUsername(getUsername());
			CardActivity.rslt = trasactionIdSOAP
					.GetTrasactionId(Authobj,getMemberId());
			CardActivity.strTrackId = trasactionIdSOAP
					.getServerMessage();			

		} catch (Exception e) {
			CardActivity.rslt = e.toString();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

}
