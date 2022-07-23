/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */


package com.broadbandcollection.billing.SOAP;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class TodaysCollectionSOAP {

	
	private String UserLoginName;
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String todayCollection;
	
	public TodaysCollectionSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String CallTodaysCollectionSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
		
		
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
		pi.setValue(getUserLoginName());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
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
		/*
		 * anyType{NewDataSet=anyType{Table=anyType{PaymentMode=Cheque;	TotalCount=1; Amount=2250.00; }; 
		 * Table=anyType{PaymentMode=Cash; TotalCount=8; Amount=5600.00; }; }; }
		 */
		
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log("MethodName", ":-"+METHOD_NAME);
			Utils.log("Soap Url", ":-"+SOAP_URL);
			Utils.log("Request", ":-"+androidHttpTransport.requestDump);
			Utils.log("Response", ":-"+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				//Object response = envelope.getResponse();
				SoapObject response = (SoapObject) envelope.getResponse();
				
				if(response != null){
					
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						
						StringBuffer sb = new StringBuffer();
							
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response	.getProperty(i);
							sb.append("<br>");
							sb.append(tableObj.getPropertyAsString("msg").toString());
							/*sb.append("<br>Total ");
							sb.append(tableObj.getPropertyAsString("TotalCount").toString());
							sb.append(" ");
							sb.append(tableObj.getPropertyAsString("PaymentMode").toString());
							sb.append(" ");
							sb.append(tableObj.getPropertyAsString("Amount").toString());
							sb.append(" ");
							*/
						}
						
						setTodayCollection(sb.toString());
						sb = null;
					
					} else {
						setTodayCollection(" No Data Found.");
					}
					
					
				}else{
					setTodayCollection("Nil");
					//Log.i(" >>>> " ," Nill ...");
				}
				
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault
						.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "ok";
		} catch (Exception e) {
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			e.printStackTrace();
			return e.toString();
		}
	}


	/**
	 * @return the userLoginName
	 */
	public String getUserLoginName() {
		return UserLoginName;
	}


	/**
	 * @param userLoginName the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
	}

	public String getTodayCollection() {
		return todayCollection;
	}

	public void setTodayCollection(String todayCollection) {
		this.todayCollection = todayCollection;
	}
	
	
	
}
