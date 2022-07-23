package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.traction.ashok.marshals.MarshalLong;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class GetIdCardSOAP {
	
	String WSDL_TARGET_NAMESPACE;
	String SOAP_URL;
	String METHOD_NAME;
	String response;
	
	public GetIdCardSOAP(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		this.WSDL_TARGET_NAMESPACE=WSDL_TARGET_NAMESPACE;
		this.SOAP_URL=SOAP_URL;
		this.METHOD_NAME=METHOD_NAME;
	}

	public String getIdCardDetails(String userLoginName, AuthenticationMobile Authobj){
		String str_msg="OK";
	
		SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		 
		PropertyInfo pi = new PropertyInfo();
		pi.setName("Userid");
		pi.setValue(userLoginName);
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

		MarshalLong mlong = new MarshalLong();
		mlong.register(envelope);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		try{
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		/*Utils.log("GetIdCardSOAP Soap","request: "+androidHttpTransport.requestDump);
		Utils.log("GetIdCardSOAP Soap","response: "+androidHttpTransport.responseDump);
		Utils.log("Response ","is: "+envelope.getResponse().toString());*/
		//if(action.equalsIgnoreCase("S")){
			response=envelope.getResponse().toString();
		//}
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
		return str_msg;
	

	
	}catch(Exception e){
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			try {
				androidHttpTransport.getServiceConnection().disconnect();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return str_msg="error";
		
	}
		
	}
	public String getResponse(){
		return response;
	}
}
