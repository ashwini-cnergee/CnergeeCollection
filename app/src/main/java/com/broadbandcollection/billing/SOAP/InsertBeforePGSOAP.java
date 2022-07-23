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

public class InsertBeforePGSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "CnergeeService"; 

	
	public InsertBeforePGSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String insert_beforepg_method(AuthenticationMobile Authobj, String MemberId, String TrackId, String Amount, String PackageName, String ServiceTax)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		
		String str_msg = "ok";
		
			Utils.log(this.getClass().getSimpleName(),"TrackId: "+TrackId);
			
		PropertyInfo pi = new PropertyInfo();
	
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.ClientName);
		pi.setValue(Authobj.getCliectAccessId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TrackId");
		pi.setValue(TrackId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Amount");
		pi.setValue(Amount);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PackageName");
		pi.setValue(PackageName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ServiceTax");
		pi.setValue(ServiceTax);
		pi.setType(String.class);
		request.addProperty(pi);
	
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
	

	
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		try{		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log(this.getClass().getSimpleName(),"request: "+androidHttpTransport.requestDump);
		Utils.log(this.getClass().getSimpleName(),"response: "+androidHttpTransport.responseDump);
		
		
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
		Utils.log(this.getClass().getSimpleName(),"Exception: "+e);
		return str_msg="error";
		
	}
}
	
	
}
