package com.broadbandcollection.billing.SOAP;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class GetCallCustomerSoap {

	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String response;
	
	

	public  GetCallCustomerSoap(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	public String CallInformCustomer(AuthenticationMobile Authobj, String SubscriberId, String VisitTime, String UserLoginName, String PickUpId) throws SocketException,SocketTimeoutException,Exception{
		try{
		String result = "OK";
		
		Utils.log("On Call Inform", "Autheticaion Executed");
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("SubscriberId");
		pi.setValue(SubscriberId);
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("VisitTime");
		pi.setValue(VisitTime);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("Type");
		pi.setValue("Collection");
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("PayPickupId");
		pi.setValue(PickUpId);
		pi.setType(String.class);
		request.addProperty(pi);
		
	
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
	/*	envelope.addMapping(WSDL_TARGET_NAMESPACE, "AuthObj",
				new Authentication().getClass());*/
		envelope.setOutputSoapObject(request);

		Utils.log("Soap Url :",""+SOAP_URL);
		Utils.log("Method Name :",""+METHOD_NAME);
		Utils.log("WSDL URL :",""+WSDL_TARGET_NAMESPACE);
		Utils.log("In Property Envelope ","Pro envelope Executed");
		
		HttpTransportSE transportSE = new HttpTransportSE(SOAP_URL);
		transportSE.debug = true;
		Utils.log("In Proper Transport ","Pro Transport Executed");
		transportSE.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Utils.log("In Proper ObjResponse ","Pro response Executed");
		
		Utils.log(this.getClass().getSimpleName()+" request ",""+transportSE.requestDump);
		Utils.log(this.getClass().getSimpleName()+" response ",""+transportSE.responseDump);
		
		response=envelope.getResponse().toString();
		Utils.log("Soap Url :",""+SOAP_URL);
		
		if (transportSE != null) {
			transportSE.reset();
			transportSE.getServiceConnection().disconnect();
		}
		return result;
		}
		catch(Exception e){
			Utils.log("Error :",""+e);
			return "error";
		}
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}	
	
	
}
