package com.broadbandcollection.billing.SOAP;

import android.content.Context;
import android.database.Cursor;

import com.broadbandcollection.billing.database.DatabaseHandler;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.MemberData;
import com.broadbandcollection.billing.utils.Utils;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SendLocationSOAP {


	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;

	public SendLocationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallSendLocationSOAP(String username,
									   AuthenticationMobile Authobj, Context ctx)throws SocketException,SocketTimeoutException,Exception {
		try{
			String row_id;
			DatabaseHandler db= new DatabaseHandler(ctx);
			Cursor mCur=db.getLocation();
			if(mCur.getCount()>0){
				while(mCur.moveToNext()){
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

		row_id=mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.ROW_ID));

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		
		
		pi= new PropertyInfo();
		pi.setName("Latitude");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.LATITUDE)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("Longitude");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.LONGITUDE)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("LocationDate");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.DATE)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("LocationProvider");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.PROVIDER)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("GPSStatus");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.GPS_STATUS)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		//Utils.log("sendlocation","soap: "+mCur.getString(mCur
		//		.getColumnIndex(DatabaseHandler.ACTIVITY)));
		
		
		pi= new PropertyInfo();
		pi.setName("Activity");
		pi.setValue(mCur.getString(mCur
				.getColumnIndex(DatabaseHandler.ACTIVITY)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("ForService");
		pi.setValue("Collection");
		pi.setType(String.class);
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
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Utils.log("LocationSOAP request",": "+androidHttpTransport.requestDump);
		Utils.log("LocationSOAP response",": "+androidHttpTransport.responseDump);
		Utils.log("Envelope response","is: "+envelope.getResponse().toString());
		if(envelope.getResponse().toString().equalsIgnoreCase("1")){
			db.DeleteRow(row_id);
		}else{
			
		}
		
		//Object response = envelope.getResponse();
		//setIsValidUser(Boolean.parseBoolean(response.toString()));
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
		
		}catch(SoapFault e){
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			///Utils.log("inner try","soap error"+e);
			return "error";
		}
		catch(Exception e){
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "error";
		}
		catch(Error e){
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			return "error";
		}
		
			}
		   		
		}
		
			return "OK";
		}
		catch(Exception e){
			
			Utils.log("Main try","error"+e);
			return "error";
		}

	}

	private boolean isvalid;

	public void setIsValidUser(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public boolean isValidUser() {
		return isvalid;
	}


}
