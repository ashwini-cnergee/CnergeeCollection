package com.broadbandcollection.billing.SOAP;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.services.PaymentPickupRequestService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UpdatePickUpNotifySOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	
	public UpdatePickUpNotifySOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String UpdatePaymentPickupNotify(String UserLoginName, AuthenticationMobile Authobj, String NotiyId)throws SocketException,SocketTimeoutException,Exception {
			
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" UserLoginName ", getUserLoginName());*/
	

		/*Log.i("#####################", "");*/
		
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
		
		pi = new PropertyInfo();
		pi.setName("PaypickupId");
		pi.setValue(NotiyId);
		pi.setType(String.class);
		request.addProperty(pi);
	
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj",
				new AuthenticationMobile().getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		try{
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			/*Utils.log("UpdatePickUpNotifySOAP "," Request"+androidHttpTransport.requestDump);
			Utils.log("UpdatePickUpNotifySOAP "," Response: "+androidHttpTransport.responseDump);*/
			String response= envelope.getResponse().toString();
			
				PaymentPickupRequestService.NotifyId=response;
			
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
