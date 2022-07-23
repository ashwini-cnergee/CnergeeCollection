package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.ChangePackgeActivity;
import com.broadbandcollection.billing.activity.IdCardActivity;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;
import com.traction.ashok.marshals.MarshalLong;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class GetStatusSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "CnergeeService"; 

	
	public GetStatusSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getPackageSOAP(AuthenticationMobile Authobj, String UserLoginName, String action)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		
		String str_msg = "ok";
		
		
		PropertyInfo pi = new PropertyInfo();
	
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Parameter");
		pi.setValue(action);
		pi.setType(String.class);
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
		Utils.log("GetStatusSOAP Soap","request: "+androidHttpTransport.requestDump);
		Utils.log("GetStatusSOAP Soap","response: "+androidHttpTransport.responseDump);
		Utils.log("Response ","is: "+envelope.getResponse().toString());
		//if(action.equalsIgnoreCase("S")){
			ChangePackgeActivity.getUpdateDataString=envelope.getResponse().toString();
			
			IdCardActivity.getUpdateDataString=envelope.getResponse().toString();
		//}
			
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			
		return str_msg;
	

	
	}catch(Exception e){
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			androidHttpTransport.getServiceConnection().disconnect();
		}
		return str_msg="error";
		
	}
}
	
	
}
