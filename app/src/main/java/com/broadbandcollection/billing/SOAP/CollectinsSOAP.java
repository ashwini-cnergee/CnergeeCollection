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
import com.broadbandcollection.billing.obj.CollectionObj;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CollectinsSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private CollectionObj collectionobj;
	private String refNo;
	private String username;
	private String responseMsg;
	
	//String MemberId,pickupid,typename,cbdate,comment,username;
	
	public CollectinsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallCollectionSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
		
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" MemberId ", getCollectionObj().getMemberLoginId());
		Log.i(" PayPickUpId ", getCollectionObj().getpaypickid());
		Log.i(" TypeName ", getCollectionObj().getTypeName());
		Log.i(" CallBackDate ", getCollectionObj().getCBDate());
		Log.i(" Comment ", getCollectionObj().getComment());
		Log.i(" UserName ",getUsername() );*/

		/*Log.i("#####################", "");*/
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("UserLoginName");
		pi.setValue(getUsername());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(getCollectionObj().getMemberLoginId());
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("pickupid");
		pi.setValue(getCollectionObj().getpaypickid());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("typename");
		pi.setValue(getCollectionObj().getTypeName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("cbdate");
		pi.setValue(getCollectionObj().getCBDate());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("comment");
		pi.setValue(getCollectionObj().getComment());
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
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log("Collection Followup Soap","request: "+androidHttpTransport.requestDump);

			Utils.log("Collection Followup Soap","response: "+androidHttpTransport.responseDump);


			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				setResponseMsg(response.toString());
				//Log.i(" >>>> " ,response.toString());
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
		 
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	public void setCollectionObj(CollectionObj collectionObj) {
		this.collectionobj = collectionObj;
	}

	public CollectionObj getCollectionObj() {
		return collectionobj;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
}
