package com.broadbandcollection.billing.SOAP;

import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class AppSettingSoap {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public AppSettingSoap(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallAppSettingSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception{
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		/*Log.i(" #	#####################  ", " START ");

		
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" SIM SR.No ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" Cust ", Authobj.getCliectAccessId());
		Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", SOAP_URL + METHOD_NAME);*/

		PropertyInfo pi = new PropertyInfo();
		

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		// envelope.bodyOut = request;
	
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		// envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj",
				new Authentication().getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
	
		try{	
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE+ METHOD_NAME,envelope);
		Utils.log("Login req",""+androidHttpTransport.requestDump);
		Utils.log("Login response",""+androidHttpTransport.responseDump);
		Object response = envelope.getResponse();
		//Utils.log("Response from server",""+response);
		//setValidUser((Boolean)response);
		setValidUser(Boolean.parseBoolean(response.toString()));
		
		
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			androidHttpTransport.getServiceConnection().disconnect();
		}
		return "OK";
		}
		catch(Exception e){
			//Utils.log("Error in AppSettingSoap",""+e);
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return e.toString();
		}
	}
	
	private Boolean isValidUser=false;
	
	public Boolean isValidUser(){
		return isValidUser;
	}
	 private void setValidUser(Boolean validUser){
		 this.isValidUser=validUser;
	 }
}
