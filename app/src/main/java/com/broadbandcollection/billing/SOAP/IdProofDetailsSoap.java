package com.broadbandcollection.billing.SOAP;

import android.util.Log;

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

public class IdProofDetailsSoap {

	private static String TAG = "IdProofDetailsSoap";
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;
	private String JsonResponse;

	
	public IdProofDetailsSoap(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		
		Utils.log("On IDProof Soap","Soap Executed");
		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
	}
	
	public String GetIdProofDetailsSoap(String UserLoginNam, AuthenticationMobile AuthObj)throws Exception, SocketException,SocketTimeoutException{
		
		
		
		String result = "OK";
		Log.i(" #	#####################  ", " START ");
		
		
		
		Utils.log("On getIdProofWew Soap"," getIdSoap Executed");
		
		Utils.log(" Soap Exceuted ", "Soap Caller Executed");
		
		
		Utils.log(" UserLoginNam ", ""+UserLoginNam);
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
		
		Utils.log(TAG+"URl 45Soap :",""+SOAP_URL);
		Utils.log(" UserLoginNam ", ""+UserLoginNam);
		Utils.log("request", ""+request);
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(UserLoginNam);//159
		pi.setType(String.class);
		request.addProperty(pi);
		
		Utils.log("In Property ","Pro Userid Executed");
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(AuthObj);
		pi.setType(AuthObj.getClass());
		request.addProperty(pi);
		
		Utils.log("In AuthObj ","Pro AuthName Executed");
		Utils.log("In AuthObj ",":"+AuthObj.getAppVersion());
		Utils.log("In AuthObj", ":"+AuthObj.getCliectAccessId());
		Utils.log("In AuthObj",":"+AuthObj.getIMEINo());
		Utils.log("In AuthObj",":"+AuthObj.getMacAddress());
		Utils.log("In AuthObj",":"+AuthObj.getMobileNumber());
		Utils.log("In AuthObj",":"+AuthObj.getMobLoginId());
		Utils.log("In AuthObj",":"+AuthObj.getMobUserPass());
		Utils.log("In AuthObj",":"+AuthObj.getPhoneUniqueId());
		Utils.log("In AuthObj",":"+AuthObj.getPropertyCount());
		
		
		
		
		
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "AuthObj",
				new Authentication().getClass());
		envelope.setOutputSoapObject(request);

		
		Utils.log("In Property Envelope ","Pro envelope Executed");
		
		HttpTransportSE transportSE = new HttpTransportSE(SOAP_URL);
		transportSE.debug = true;
		Utils.log("In Proper Transport ","Pro Transport Executed");
		
		try{
		transportSE.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Utils.log("In Proper ObjResponse ","Pro response Executed");
		
		Utils.log("request ",""+transportSE.requestDump);
		Utils.log("response ",""+transportSE.responseDump);
		
		Utils.log("Soap Url :",""+SOAP_URL);
		
		
		//Object response = envelope.getResponse().toString();
		
		JsonResponse  = envelope.getResponse().toString();
		
	//	Utils.log("In Proper ObjResponse ","Pro response Executed"+response);
		
	
		setIsValidUser((JsonResponse.toString()));
		
		Utils.log(TAG+"response",""+JsonResponse);
	
		/*Utils.log("Response","s"+response);*/
	/*	Utils.log("request ",""+transportSE.requestDump);
		Utils.log("response ",""+transportSE.responseDump);
		Utils.log(TAG+"request",""+request);
		Utils.log("responsePost",""+isValidUser());*/
		
	/*	
		Utils.log(TAG+" New Json","response:"+JsonResponse);*/
		
	}catch(Exception e){
		if (transportSE != null) {
			transportSE.reset();
			transportSE.getServiceConnection().disconnect();
		}
		Utils.log("Error :",""+e);
		
	}
	if (transportSE != null) {
		transportSE.reset();
		transportSE.getServiceConnection().disconnect();
	}
		return result;
		
	}
	
	private String isvalid;

	public void setIsValidUser(String isvalid) {
		this.isvalid = isvalid;
	}

	public String isValidUser() {
		return isvalid;
	}

	
public String getJsonResponse(){
		
		return JsonResponse;
		
	}
	
	}
	
	

