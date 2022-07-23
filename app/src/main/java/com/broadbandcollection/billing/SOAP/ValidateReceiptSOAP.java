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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class ValidateReceiptSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;
	String username, AreaCode, AreaCodeFilter, receptNo;

	public ValidateReceiptSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public boolean ValidateReceiptNo(AuthenticationMobile Authobj) {

		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i("UserLoginName :", getUsername());
		Log.i("AreaCodeFilter :", getAreaCodeFilter());
		Log.i("ReceiptNo :", getReceptNo());*/
		/*Log.i("#####################", "");*/

		// ValidateReceiptNo
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
	
		PropertyInfo pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(getUsername());
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("AreaCodeFilter");
		pi.setValue(getAreaCodeFilter());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("ReceiptNo");
		pi.setValue(getReceptNo());
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

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		boolean isValid = false;
		try {
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE
					+ "ValidateReceiptNo", envelope);
			Object response = envelope.getResponse();
			//Log.i(" RESPONSE ", response.toString());
			isValid = (Boolean.parseBoolean(response.toString()));
			//Log.i("#####################", " END ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			try {
				androidHttpTransport.getServiceConnection().disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isValid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public String getAreaCodeFilter() {
		return AreaCodeFilter;
	}

	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
	}

	public String getReceptNo() {
		return receptNo;
	}

	public void setReceptNo(String receptNo) {
		this.receptNo = receptNo;
	}

}
