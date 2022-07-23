/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Map;

public class SearchMemberSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;
	private Map<String, MemberData> mapMemberData;
	private String message;
	public SearchMemberSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallSearchMemberSOAP(String username, String password,
									   String searchTxt, AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" searchTxt ", searchTxt);
		//Log.i(" #	#####################  ", " START ");

		//Log.i(" username ", username);
		//Log.i(" password ", password);
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i(" searchTxt ", searchTxt);*/
		//Log.i("#####################", "");
	
		PropertyInfo pi = new PropertyInfo();
		pi.setName("SearchString");
		pi.setValue(searchTxt.toString());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(username.toString());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("LoginPassword");
		pi.setValue(password.toString());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj",
				new Authentication().getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		String str_msg = "ok";
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log("SearchMemberSOAP","request: "+androidHttpTransport.requestDump);
			Utils.log("SearchMemberSOAP","response: "+androidHttpTransport.responseDump);
			// Object response = envelope.getResponse();
			// Log.i(" RESPONSE ",response.toString());
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, MemberData> mapMemberData = new Hashtable<String, MemberData>();
				SoapObject response = (SoapObject) envelope.getResponse();
				Utils.log("SearchMemberSOAP","response: "+response.toString());
				if (response != null) {
					//Log.i(" RESPONSE ", response.toString());
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response
									.getProperty(i);
							setMemberData(tableObj, mapMemberData);
					
						}
						setMapMemberData(mapMemberData);
						str_msg = "ok";
					} else {
						str_msg = "not";
					}
				} else {
					str_msg = "not";
				}

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
		return str_msg;
	}
 
	public void setMemberData(SoapObject response,
			Map<String, MemberData> mapMemberData) {

		//Log.i("#####################",
		//		response.getPropertyAsString("SubscriberID").toString() + "");

		MemberData memberData = new MemberData();
		
		Utils.log("MemberId","is:"+response.getPropertyAsString("MemberId")
				.toString());
		if(response.hasProperty("MemberId"))		
		memberData.setMemberId(response.getPropertyAsString("MemberId")
				.toString());
		if(response.hasProperty("SubscriberID"))		
		memberData.setSubscriberID(response.getPropertyAsString("SubscriberID")
				.toString());
		if(response.hasProperty("SubscriberName"))
		memberData.setSubscriberName(response.getPropertyAsString(
				"SubscriberName").toString());
		if(response.hasProperty("SubscriberStatus"))
		memberData.setSubscriberStatus(response.getPropertyAsString(
				"SubscriberStatus").toString());
		if(response.hasProperty("expiryDate"))
		memberData.setStrExpiryDate(response.getPropertyAsString("expiryDate")
				.toString());

		if(response.hasProperty("PlanRate"))
		memberData.setPlanRate(Double.parseDouble(response.getPropertyAsString("PlanRate").toString()));
		
		if(response.hasProperty("CurrentPlan"))
		memberData.setCurrentPlan(response.getPropertyAsString("CurrentPlan")
				.toString());
		if(response.hasProperty("AreaCode"))
		memberData.setAreaCode(response.getPropertyAsString("AreaCode")
				.toString());
		if(response.hasProperty("AreaCodeFilter"))
		memberData.setAreaCodeFilter(response.getPropertyAsString(
				"AreaCodeFilter").toString());

		if(response.hasProperty("PrimaryMobileNo"))
		memberData.setPrimaryMobileNo(response.getPropertyAsString(
				"PrimaryMobileNo").toString());
		
		if(response.hasProperty("IsAutoReceipt"))
		memberData.setIsAutoReceipt(response.getPropertyAsString(
				"IsAutoReceipt").toString());
		
		if(response.hasProperty("CheckForRenewal"))
		memberData.setCheckForRenewal(response.getPropertyAsString(
				"CheckForRenewal").toString());
		
		if(response.hasProperty("AreaName"))
		memberData.setAreaName(response.getPropertyAsString(
				"AreaName").toString());
		
		if(response.hasProperty("ISPName"))
		memberData.setISPName(response.getPropertyAsString(
				"ISPName").toString());
		if(response.hasProperty("PaymentDate"))
		memberData.setPaymentDate(response.getPropertyAsString(
				"PaymentDate").toString());
		
		if(response.hasProperty("MemAddress"))
		memberData.setMemberAddress(response.getPropertyAsString(
				"MemAddress").toString());
		
		if(response.hasProperty("discountpackrate"))
		memberData.setDiscounPackRate(response.getPropertyAsString(
				"discountpackrate").toString());
		
		if(response.hasProperty("discountedPack"))
		memberData.setDiscounPackName(response.getPropertyAsString(
				"discountedPack").toString());
		
		if(response.hasProperty("ConnectionTypeId"))
		memberData.setConnectionTypeId(response.getPropertyAsString(
				"ConnectionTypeId").toString());
		
		if(response.hasProperty("AdditionAmountDetails"))
		memberData.setAdditionAmountDetails(response.getPropertyAsString("AdditionAmountDetails").toString());
		message=response.getPropertyAsString(
				"PaymentPickupmessage").toString();
		if(response.hasProperty("BalanceAmount")){
			memberData.setOutstanding_Amt(Double.valueOf(response.getPropertyAsString("BalanceAmount")));
		}
		else{
			memberData.setOutstanding_Amt(0.0);
		}
		
		if(response.hasProperty("IsQos")){
			memberData.setIsQos(Boolean.valueOf(response.getPropertyAsString("IsQos")));
		}
		else{
			memberData.setIsQos(false);
		}
		
		if(response.hasProperty("PackageType")){
			memberData.setPackageType(response.getPropertyAsString("PackageType"));
		}
		else{
			memberData.setPackageType("");
		}
		
		
		mapMemberData.put(memberData.getSubscriberID(), memberData);
	//	Utils.log(" Mobile User ", Authobj.getMobLoginId());
	}

	/**
	 * @param mapMemberData
	 *            the mapMemberData to set
	 */
	public void setMapMemberData(Map<String, MemberData> mapMemberData) {
		this.mapMemberData = mapMemberData;
	}

	public Map<String, MemberData> getMapMemberData() {
		return this.mapMemberData;
	}

	public String getMessage(){
		return message;
	}
}
