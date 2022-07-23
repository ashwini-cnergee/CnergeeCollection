package com.broadbandcollection.billing.SOAP;

import android.util.Log;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.services.PaymentPickupRequestService;
import com.broadbandcollection.billing.utils.Utils;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PaymentPickupNotificationSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	
	public PaymentPickupNotificationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallPaymentPickupRequestSOAP(String UserLoginName, AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
			
		Utils.log("#####################", " START ");
		Utils.log("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Utils.log("SOAP_URL :", SOAP_URL);
		Utils.log("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Utils.log("METHOD_NAME :", METHOD_NAME);
		Utils.log(" IMEI No ", Authobj.getIMEINo());
		Utils.log(" Mobile ", Authobj.getMobileNumber());
		Utils.log(" Mobile User ", Authobj.getMobLoginId());
		Utils.log(" Mobile Password ", Authobj.getMobUserPass());
		
		Log.i("#####################", "");
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
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
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj", new AuthenticationMobile().getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
	try{
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log("PaymentPickupNotificationSOAP "," Request"+androidHttpTransport.requestDump);
			Utils.log("PaymentPickupNotificationSOAP "," Response: "+androidHttpTransport.responseDump);
			
			String response= envelope.getResponse().toString();
			//Utils.log("Check "," Response: "+response);
			if(!response.isEmpty()){
				PaymentPickupRequestService.NotifyId=response;
			}
			
			//Log.i("#####################", " RESPONSE ");
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			
			return "ok";
		}
		 catch (Exception e) {
			 if (androidHttpTransport != null) {
					androidHttpTransport.reset();
					androidHttpTransport.getServiceConnection().disconnect();
				}
			e.printStackTrace();
			return e.toString();
		}
	}

}
