package com.broadbandcollection.billing.SOAP;

import android.util.TimeFormatException;


import com.broadbandcollection.billing.Marshals.MarshalDouble;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;

public class CardActivitySoap {

	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static String TAG = "CardActivitySoap";
	private String JsonResponse;
	
	public CardActivitySoap(String WSDL_TARGET_NAMESPACE, String SOAP_URL,String METHOD_NAME){
		
		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		
		Utils.log("Card Actvty", " CARD actvity Executed :"+TAG);
	}
	
//	public String GetCardActvityResult(AuthenticationMobile AuthObj, String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName, double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType)throws Exception,SocketException,TimeFormatException{
		
	
	public String GetCardActvityResult(String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName,  double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType, String RenewalType,Double PoolRate,AuthenticationMobile AuthObj)throws Exception,SocketException,TimeFormatException{
		
	//	public String GetCardActvityResult(String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName,  double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType,AuthenticationMobile AuthObj)throws Exception,SocketException,TimeFormatException{
			
				
		String result = "OK";
		//try{
			
		
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
			
		PropertyInfo pi = new PropertyInfo();
		Utils.log("property Info ", "Property info Executed :"+TAG);
		
		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
		pi.setType(String.class);
		request.addProperty(pi);
	
		
		pi = new PropertyInfo();
		pi.setName("AltMobileNo");
		pi.setValue(AltMobileNo);
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(SubscriberID);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PlanName");
		pi.setValue(PlanName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("PaidAmount");
		pi.setValue(PaidAmount);
		pi.setType(Double.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("PaymentDate");
		pi.setValue(PaymentDate);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("PaymentMode");
		pi.setValue(PaymentMode);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("ReceiptNo");
		pi.setValue(ReceiptNo);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		
		pi = new PropertyInfo();
		pi.setName("PaymentModeDate");
		pi.setValue(PaymentModeDate);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("BankName");
		pi.setValue(BankName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("TrackID");
		pi.setValue(TrackID);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("PaymentID");
		pi.setValue(PaymentID);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("TransStatus");
		pi.setValue(TransStatus);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		
		pi = new PropertyInfo();
		pi.setName("TransID");
		pi.setValue(TransID);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("TransError");
		pi.setValue(TransError);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("IsChangePlan");
		pi.setValue(IsChangePlan);
		pi.setType(boolean.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("ActionType");
		pi.setValue(ActionType);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("RenewalType");
		pi.setValue(RenewalType);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolRate");
		pi.setValue(PoolRate);
		pi.setType(Double.class);
		request.addProperty(pi);
		

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(AuthObj);
		pi.setType(AuthObj.getClass());
		request.addProperty(pi);
		
		
	
		
	
		Utils.log("UserLoginName",":"+UserLoginName);
		Utils.log("AltMobileNo",":"+AltMobileNo);
		Utils.log("SubscriberID",":"+SubscriberID);
		Utils.log("PaidAmount",":"+PaidAmount);
		Utils.log("PaymentDate",":"+PaymentDate);
		Utils.log("PaymentMode",":"+PaymentMode);
		Utils.log("ReceiptNo",":"+ReceiptNo);
		Utils.log("PaymentModeDate",":"+PaymentModeDate);
		
		Utils.log("BankName",":"+BankName);
		Utils.log("TrackID",":"+TrackID);
		Utils.log("PaymentID",":"+PaymentID);
		
		Utils.log("TransStatus",":"+TransStatus);
		Utils.log("TransID",":"+TransID);
		Utils.log("TransError",":"+TransError);
		
		Utils.log("IsChangePlan",":"+IsChangePlan);
		Utils.log("ActionType",":"+ActionType);
		Utils.log("PlanName",":"+PlanName);
		Utils.log("RenewalType","is:"+RenewalType);
		
		
		
		Utils.log("property Auth ", "Property Auth Executed :"+TAG);
		
	
		
		Utils.log("property Info ", "Property info Executed");
		/*SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		
		Utils.log("envelope Executed ", "Envelope Executed");
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj",
				new Authentication().getClass());

		MarshalLong mlong = new MarshalLong();
		mlong.register(envelope);*/
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "AuthObj",
				new Authentication().getClass());

		MarshalDouble mdouble = new MarshalDouble();
		mdouble.register(envelope);
		
		
		Utils.log("hhttp taransport exexuted ",""+TAG);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		/*Utils.log("GetIdCardSOAP Soap","request: "+androidHttpTransport.requestDump);
		Utils.log("GetIdCardSOAP Soap","response: "+androidHttpTransport.responseDump);
		Utils.log("Response ","is: "+envelope.getResponse().toString());*/
		//if(action.equalsIgnoreCase("S")){
	
		
		  Utils.log(TAG+"","URl :"+SOAP_URL);
		  Utils.log(TAG+"","METHOD_NAME :"+METHOD_NAME);
		  Utils.log(TAG+":","request"+androidHttpTransport.requestDump);
		  Utils.log(TAG+":","response"+androidHttpTransport.responseDump);
		  JsonResponse = envelope.getResponse().toString();
		
		
		Utils.log("Card Activity request ","request"+androidHttpTransport.requestDump);
		Utils.log("Card Activity response ","response"+androidHttpTransport.responseDump);
		
	
	
		
		
        Utils.log("Soapurl","Soap_Url"+SOAP_URL);
        Utils.log("Subcriber Id", "Id"+SubscriberID);
        Utils.log("Method", "Method"+METHOD_NAME);
        Utils.log("currentDate","CurrentDate"+PaymentDate);
        Utils.log("ActionType","ActionType"+ActionType);
        Utils.log("PaidAmount","PaidAmount"+PaidAmount);
        Utils.log("PaidAmount","PaidAmount"+BankName);
        Utils.log("PaidAmount","PaidAmount"+IsChangePlan);
        Utils.log("PaidAmount","PaidAmount"+PaymentID);
        Utils.log("PaidAmount","PaidAmount"+PaymentModeDate);
        Utils.log("PaidAmount","PaidAmount"+PlanName);
        Utils.log("PaidAmount","PaidAmount"+ReceiptNo);
        Utils.log("PaidAmount","PaidAmount"+result);
        Utils.log("Action Type","Action Type"+ActionType);
        Utils.log("Recip No","Recno:"+JsonResponse);
        
			JsonResponse=envelope.getResponse().toString();
		//}
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}	
		return result;
	

	
	/*}catch(Exception e){
	Utils.log("Error","Error"+e);
	Utils.log("ErrorException","Exception"+e);
		
	}*/
		//return result;
		
	}
	public String getResponse(){
		return JsonResponse;
	}

}

