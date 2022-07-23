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
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class LoginSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;

	public LoginSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallLoginSOAP(String username, String password,
			AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		/*Log.i(" #	#####################  ", " START ");*/

		/*Log.i(" username ", username);
		Log.i(" password ", password);
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());*/

		/*Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);*/

		/*Authentication aut = new Authentication();
		aut.setVendorCode(Authenticat_VendorCode);
		aut.setUserName(Authenticat_user);
		aut.setPassword(Authenticat_pass);*/

		PropertyInfo pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(username);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("LoginPassword");
		pi.setValue(password);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		Utils.log("LoginSOAP URL ","s"+SOAP_URL);
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

		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log("LoginSOAP URL ","s"+SOAP_URL);
		Utils.log("LoginSOAP request ","s"+androidHttpTransport.requestDump);
		Utils.log("LoginSOAP response ","s"+androidHttpTransport.responseDump);
		Object response = envelope.getResponse();
		
		setIsValidUser(Boolean.parseBoolean(response.toString()));
		/*Utils.log("Response","s"+response);*/
		/*
			 * if (envelope.bodyIn instanceof SoapObject) { // SoapObject =
			 * SUCCESS Object response = envelope.getResponse();
			 * Log.i(" RESPONSE ",response.toString());
			 * 
			 * } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
			 * FAILURE SoapFault soapFault = (SoapFault) envelope.bodyIn; return
			 * soapFault.getMessage().toString(); }
			 */
		
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			androidHttpTransport.getServiceConnection().disconnect();
		}
		return "OK";

	}

	private boolean isvalid;

	public void setIsValidUser(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public boolean isValidUser() {
		return isvalid;
	}

}
