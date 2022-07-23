package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.AppSettingctivity;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;
import com.traction.ashok.marshals.MarshalLong;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LogOutButtonSettingSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "CnergeeService"; 

	
	public LogOutButtonSettingSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getButtonSetting(AuthenticationMobile Authobj, String ForType, String TID)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		
		String str_msg = "ok";
		try{
		
		
		PropertyInfo pi = new PropertyInfo();
	
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ForType");
		pi.setValue(ForType);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TID");
		pi.setValue(TID);
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


		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log("LogOutButtonSettingSOAP Soap","URL: "+SOAP_URL);

		Utils.log("LogOutButtonSettingSOAP Soap","request: "+androidHttpTransport.requestDump);

		Utils.log("LogOutButtonSettingSOAP Soap","response: "+androidHttpTransport.responseDump);
		
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			
			SoapObject response = (SoapObject) envelope.getResponse();
			if (response != null) {
				Utils.log("LogOutButtonSettingSOAP Soap","request: "+response.toString());
				/*Log.i(TAG+" >>>> ",response.toString());*/
				response = (SoapObject) response.getProperty("NewDataSet");
				if (response.getPropertyCount() > 0) {

					Utils.log("LogOutButtonSettingSOAP Soap","response: "+response.toString());

					for (int i = 0; i < response.getPropertyCount(); i++) {
						SoapObject tableObj = (SoapObject) response.getProperty(i);
						
						AppSettingctivity.alMobilSettingName.add(tableObj.getPropertyAsString("MobileSettingName").toString());
						AppSettingctivity.alMobilSettingValue.add(tableObj.getPropertyAsString("MobileSettingValue").toString());
						
						//Utils.log("Value","added");
					}
					//Utils.log("Size ","SOAP: "+AppSettingctivity.alMobilSettingName.size());
				} else {
					//complaintList.add(new ComplaintListObj());	
				}
			} else {
				//complaintList.add(new ComplaintListObj());
			}

		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			//complaintList.add(new ComplaintListObj());
		}
		
		//ComplaintListActivity.ShowButtonResult=androidHttpTransport.responseDump;
		
		if (androidHttpTransport != null) {
			androidHttpTransport.reset();
			androidHttpTransport.getServiceConnection().disconnect();
		}
		return str_msg;
	

	
	}catch(Exception e){
		return str_msg="error";
		
	}
}
	
	
}
