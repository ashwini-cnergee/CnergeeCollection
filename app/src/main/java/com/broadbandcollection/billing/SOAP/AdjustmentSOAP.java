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


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;


public class AdjustmentSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String username, newPlan, subscriberID, AreaCode, AreaCodeFilter;

	private String serverMessage;

	public AdjustmentSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;

		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallAdjustmentAmountSOAP(AuthenticationMobile Authobj) {
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i("SubscriberID :", getSubscriberID());
		Log.i("UserLoginName :", getUsername());
		Log.i("NewPlanName :", getNewPlan());
		Log.i("AreaCode :", getAreaCode());
		Log.i("AreaCodeFilter :", getAreaCodeFilter());*/

		/*Log.i("#####################", "");*/

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(getSubscriberID());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(getUsername());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("NewPlanName");
		pi.setValue(getNewPlan());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AreaCode");
		pi.setValue(getAreaCode());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AreaCodeFilter");
		pi.setValue(getAreaCodeFilter());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "AuthObj",new Authentication().getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				setServerMessage(response.toString());
				
				/*String ss = androidHttpTransport.responseDump;
				Log.i("#####################", ss);
				*/
				
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
			//Log.i("#####################", getServerMessage());
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "ok";
		} catch (Exception e) {
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				try {
					androidHttpTransport.getServiceConnection().disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return e.toString();
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

	/**
	 * @return the serverMessage
	 */
	public String getServerMessage() {
		return serverMessage;
	}

	/**
	 * @param serverMessage
	 *            the serverMessage to set
	 */
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

}
