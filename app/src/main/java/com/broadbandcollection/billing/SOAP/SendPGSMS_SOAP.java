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


public class SendPGSMS_SOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	String response="";
	public SendPGSMS_SOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String send_pg_sms(String MemberId, String MemberLoginId, AuthenticationMobile Authobj, String UserLoginName)throws SocketException,SocketTimeoutException,Exception {

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
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("MemberLoginId");
		pi.setValue(MemberLoginId);
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
			Utils.log("SOAP URL",":"+SOAP_URL);
			Utils.log("SendPaymentGatewaySMS","request: "+androidHttpTransport.requestDump);
			Utils.log("SendPaymentGatewaySMS","response: "+androidHttpTransport.responseDump);
			  response = envelope.getResponse().toString();
			// Log.i(" RESPONSE ",response.toString());
			  if (androidHttpTransport != null) {
					androidHttpTransport.reset();
					androidHttpTransport.getServiceConnection().disconnect();
				}
		return str_msg;
	}


	public String getResponse() {
		return response;
	}


	public void setResponse(String response) {
		this.response = response;
	}
	
	

}
