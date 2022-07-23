package com.broadbandcollection.billing.SOAP;

import android.util.TimeFormatException;

import com.broadbandcollection.billing.Marshals.MarshalDouble;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;

public class CreditCardFailedSOAP {

	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static String TAG ;
	private String JsonResponse;
	
	public CreditCardFailedSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,String METHOD_NAME){
		
		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		
		Utils.log("Card Actvty", " CARD actvity Executed :"+TAG);
	}
	
//	public String GetCardActvityResult(AuthenticationMobile AuthObj, String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName, double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType)throws Exception,SocketException,TimeFormatException{
		
	
	public String SendEztapFailedTrans(String TrackId, String status,String CliectAccessId)throws Exception,SocketException,TimeFormatException{
		
	//public String GetCardActvityResult(String UserLoginName, String AltMobileNo, String SubscriberID, String PlanName,  double PaidAmount, String PaymentDate, String PaymentMode, String ReceiptNo, String PaymentModeDate, String BankName, String TrackID, String PaymentID, String TransStatus, String TransID, String TransError, boolean IsChangePlan, String ActionType,AuthenticationMobile AuthObj)throws Exception,SocketException,TimeFormatException{
		TAG = this.getClass().getSimpleName();
				
		String result = "OK";
		//try{
			
		
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
			
		PropertyInfo pi = new PropertyInfo();
		Utils.log("property Info ", "Property info Executed :"+TAG);
		
		pi = new PropertyInfo();
		pi.setName("TrackId");
		pi.setValue(TrackId);
		pi.setType(String.class);
		request.addProperty(pi);
	
		
		pi = new PropertyInfo();
		pi.setName("TransStatus");
		pi.setValue(status);
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("CliectAccessId");
		pi.setValue(CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
	
		Utils.log("TrackId",":"+TrackId);
		Utils.log("TransStatus",":"+status);
		Utils.log("CliectAccessId","is:"+CliectAccessId);
		
		
		
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
		
		
	
		
	
	
		
		
        Utils.log("Soapurl","Soap_Url"+SOAP_URL);
      
        Utils.log("Method", "Method"+METHOD_NAME);
      
        
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
