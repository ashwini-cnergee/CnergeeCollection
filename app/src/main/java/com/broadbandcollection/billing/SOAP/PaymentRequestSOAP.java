/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.PaymentPickupActivity;
import com.broadbandcollection.billing.activity.SearchVendorActivity;
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
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentRequestSOAP {

	
	private String UserLoginName;
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private boolean isAllData;
	private String payCount;
	private ArrayList<HashMap<String, String>> paymentPickList;
	 
	private ArrayList<String> alSubId= new ArrayList<String>();
	private ArrayList<String> alPickUpId= new ArrayList<String>();
	public ArrayList<Boolean> al_is_postpaid=new ArrayList<Boolean>();
	

	public PaymentRequestSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String CallPaymentRequestSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {
		
		
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
		
		pi.setName("Username");
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
		
		
		
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log(""+this.getClass().getSimpleName(),"request:"+androidHttpTransport.requestDump);
			Utils.log(""+this.getClass().getSimpleName(),"response:"+androidHttpTransport.responseDump);
			Utils.log(""+this.getClass().getSimpleName(),"url:"+SOAP_URL);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				//Object response = envelope.getResponse();
				SoapObject response = (SoapObject) envelope.getResponse();
				
				if(response != null){
					//Log.i(" >>> ",response.toString());
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						PaymentPickupActivity.alMessage=new ArrayList<String>();
						SearchVendorActivity.alMessage=new ArrayList<String>();
						PaymentPickupActivity.al_is_postpaid=new ArrayList<Boolean>();
						SearchVendorActivity.al_is_postpaid=new ArrayList<Boolean>();
						
						if(isAllData()){
							paymentPickList = new ArrayList<HashMap<String, String>>();
								 
							for (int i = 0; i < response.getPropertyCount(); i++) {
								SoapObject tableObj = (SoapObject) response	.getProperty(i);
								HashMap<String, String> map = new HashMap<String, String>();
									
								map.put("MemberLoginID",tableObj.getPropertyAsString("MemberLoginID").toString());
								map.put("VisitDate", tableObj.getPropertyAsString("VisitDate").toString());
								
								if(tableObj.hasProperty("Message")){
									PaymentPickupActivity.alMessage.add(tableObj.getPropertyAsString("Message").toString());
									SearchVendorActivity.alMessage.add(tableObj.getPropertyAsString("Message").toString());
								}
								else{
									PaymentPickupActivity.alMessage.add("Payment Pickup");
									SearchVendorActivity.alMessage.add("Payment Pickup");
								}
								
								if(tableObj.hasProperty("IsPostPaid")){
									PaymentPickupActivity.al_is_postpaid.add(Boolean.valueOf(tableObj.getPropertyAsString("IsPostPaid").toString()));
									SearchVendorActivity.al_is_postpaid.add(Boolean.valueOf(tableObj.getPropertyAsString("IsPostPaid").toString()));
									Utils.log("Test","if");
								}else{
									Utils.log("Test","else");
									PaymentPickupActivity.al_is_postpaid.add(false);
									SearchVendorActivity.al_is_postpaid.add(false);
								}
								
								Utils.log("Size", "PP Activity:"+PaymentPickupActivity.al_is_postpaid.size());
								Utils.log("Size", "Search Activity:"+SearchVendorActivity.al_is_postpaid.size());
								
								alSubId.add(tableObj.getPropertyAsString("MemberLoginID").toString());
								alPickUpId.add(tableObj.getPropertyAsString("PayPickupId").toString());
								
								//Utils.log("Postpiad", "Value; "+tableObj.getPropertyAsString("IsPostPaid").toString());
								
								//Log.i(" >>> ",tableObj.getPropertyAsString("MemberLoginID").toString());
								paymentPickList.add(map);
							}
							setPaymentPickList(paymentPickList);
							setPayCount(Integer.toString(response.getPropertyCount()));
						
						}else{
							setPayCount(Integer.toString(response.getPropertyCount()));
							//Log.i(" >>> ",""+response.getPropertyCount());
						}
											
					} else {
						setPayCount("0");
					}
					
					
				}else{
					setPayCount("0");
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


/*anyType{NewDataSet=anyType{Table=anyType{MemberLoginID=vineethkumar; VisitDate=2013-06-17T20:00:00+05:30; }; 
 * Table=anyType{MemberLoginID=aswani_1170; VisitDate=2013-06-17T00:00:00+05:30; }; Table=anyType{MemberLoginID=amo
		l.kadam123; VisitDate=2013-06-16T00:00:00+05:30; }; Table=anyType{MemberLoginID=
		sandeep_bs; VisitDate=2013-06-15T00:00:00+05:30; }; Table=anyType{MemberLoginID=
		somendar; VisitDate=2013-03-28T11:00:00+05:30; }; Table=anyType{MemberLoginID=vi
		jesh0701; VisitDate=2013-02-04T19:00:00+05:30; }; Table=anyType{MemberLoginID=tu
		shar0454; VisitDate=2013-01-29T10:00:00+05:30; }; }; }*/
	
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

	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}

	public String getPayCount() {
		return payCount;
	}

	public void setPayCount(String payCount) {
		this.payCount = payCount;
	}

	public ArrayList<HashMap<String, String>> getPaymentPickList() {
		return paymentPickList;
	}

	public void setPaymentPickList(
			ArrayList<HashMap<String, String>> paymentPickList) {
		this.paymentPickList = paymentPickList;
	}
	public ArrayList<String> getAlSubId() {
		return alSubId;
	}

	public void setAlSubId(ArrayList<String> alSubId) {
		this.alSubId = alSubId;
	}

	public ArrayList<String> getAlPickUpId() {
		return alPickUpId;
	}

	public void setAlPickUpId(ArrayList<String> alPickUpId) {
		this.alPickUpId = alPickUpId;
	}

	public ArrayList<Boolean> getAl_is_postpaid() {
		return al_is_postpaid;
	}

	public void setAl_is_postpaid(ArrayList<Boolean> al_is_postpaid) {
		this.al_is_postpaid = al_is_postpaid;
	}
	

}
