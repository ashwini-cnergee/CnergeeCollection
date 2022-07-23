/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;



import com.broadbandcollection.billing.Marshals.MarshalDouble;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CardPaymentSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private PaymentObj paymentObj;
	private String refNo;

	String username, AreaCode, AreaCodeFilter, receptNo;

	public CardPaymentSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;

		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String CallCardPaymentSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
	/*	Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i("SubscriberID :", getPaymentObj().getSubscriberID());
		Log.i("UserLoginName :", getPaymentObj().getUserLoginName());
		Log.i("AltMobileNo :", getPaymentObj().getAltMobileNo());
		Log.i("PlanName :", getPaymentObj().getPlanName());
		Log.i("PaidAmount :", getPaymentObj().getPaidAmount() + "");
		Log.i("PaymentDate :", getPaymentObj().getStrPaymentDate().toString());
		Log.i("PaymentMode :", getPaymentObj().getPaymentMode());
		Log.i("PaymentModeDate :", getPaymentObj().getStrPaymentModeDate()
				.toString());
		Log.i("BankName :", getPaymentObj().getBankName());
		Log.i("IsChangePlan :", getPaymentObj().getChangePlan() + "");
		Log.i("ActionType :", getPaymentObj().getActionType());
		Log.i("ReceiptNo :", getPaymentObj().getRecieptNo());
		Log.i("TrackID :", getPaymentObj().getTrackID());
		Log.i("PaymentID :", getPaymentObj().getPaymentID());
		Log.i("TransStatus :", getPaymentObj().getTransStatus());
		Log.i("TransID :", getPaymentObj().getTransID());*/
		/*Log.i("#####################", "");*/

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(getPaymentObj().getSubscriberID());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(getPaymentObj().getUserLoginName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AltMobileNo");
		pi.setValue(getPaymentObj().getAltMobileNo());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PlanName");
		pi.setValue(getPaymentObj().getPlanName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaidAmount");
		double payamt = getPaymentObj().getPaidAmount();
		pi.setValue(payamt);
		pi.setType(Double.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaymentDate");
		pi.setValue(getPaymentObj().getStrPaymentDate());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaymentMode");
		pi.setValue(getPaymentObj().getPaymentMode());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaymentModeDate");
		pi.setValue(getPaymentObj().getStrPaymentModeDate());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("BankName");
		pi.setValue(getPaymentObj().getBankName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("IsChangePlan");
		pi.setValue(getPaymentObj().getChangePlan());
		pi.setType(boolean.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("ActionType");
		pi.setValue(getPaymentObj().getActionType());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ReceiptNo");
		pi.setValue(getPaymentObj().getRecieptNo());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("TrackID");
		pi.setValue(getPaymentObj().getTrackID());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaymentID");
		pi.setValue(getPaymentObj().getPaymentID());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("TransStatus");
		pi.setValue(getPaymentObj().getTransStatus());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TransID");
		pi.setValue(getPaymentObj().getTransID());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("TransError");
		pi.setValue(getPaymentObj().getTransError());
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
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "AuthObj",
				new Authentication().getClass());

		MarshalDouble mdouble = new MarshalDouble();
		mdouble.register(envelope);

		// MarshalDate mdate = new MarshalDate();
		// mdate.register(envelope);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

			// String SendMemberDataResponse = "SendMemberDataResponse";

			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);

			//Log.i("#####################", " RESPONSE ");
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				refNo = response.toString();
				setRefNo(refNo);

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				// throw new Exception(soapFault.getMessage());
				return soapFault.getMessage().toString();
			}

			//Log.i("#####################", " DONE ");
			// refNo = result.toString();

			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "ok";

	}
	
	/**
	 * @return the refNo
	 */
	public String getRefNo() {
		return refNo;
	}

	/**
	 * @param refNo
	 *            the refNo to set
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
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
