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

import java.io.IOException;

public class AdditionalAmountDetailsSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberData memberData;
	String Price,AddtionalAmount,Disount_amt,Payable_Amt,Fine_Amt,Days_Fine_Amt,Pool_Rate;
	String Check_Bounce_Reason="";

	public AdditionalAmountDetailsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String getAdditionalAmount(AuthenticationMobile Authobj, String SubscriberID, String UserLoginName, String PackageId) {

	String result="OK";
	
		// Get Additional Amount
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
	
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(SubscriberID);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(UserLoginName);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PackageId");
		pi.setValue(PackageId);
		pi.setType(String.class);
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

		try {
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE
					+ METHOD_NAME, envelope);
			Utils.log("Additional AMount Request",":"+androidHttpTransport.requestDump);
			Utils.log("Additional AMount  Response",":"+androidHttpTransport.responseDump);
			//Object response = envelope.getResponse();
			
		
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
						Price=tableObj.getPropertyAsString("PackageRate").toString();
						AddtionalAmount=	tableObj.getPropertyAsString("AdditionalAmount").toString();
						Disount_amt=tableObj.getPropertyAsString("DiscountAmount").toString();
						Fine_Amt=tableObj.getPropertyAsString("FineAmount").toString();
						Payable_Amt=	tableObj.getPropertyAsString("finalcharges").toString();						
						Days_Fine_Amt=	tableObj.getPropertyAsString("DaysFineAmount").toString();
						if(tableObj.hasProperty("PoolRate"))
						Pool_Rate=	tableObj.getPropertyAsString("PoolRate").toString();
						else
						Pool_Rate="0";	
						Check_Bounce_Reason=	tableObj.getPropertyAsString("AdditionalAmountType").toString();
						}
					}
				}
			}
			
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			
			return result;
		
		} catch (Exception e) {
			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				try {
					androidHttpTransport.getServiceConnection().disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return "error";
		}
		
	}

	public String getPice(){
		return Price;
	}
	
	public String getAdditionalAmt(){
		return AddtionalAmount;
	}

	public String getPayableAmt() {
		return Payable_Amt;
	}

	public String getDisount_amt() {
		return Disount_amt;
	}
	public String getFine_amt() {
		return Fine_Amt;
	}

	public String getDays_Fine_Amt() {
		return Days_Fine_Amt;
	}

	public void setDays_Fine_Amt(String days_Fine_Amt) {
		Days_Fine_Amt = days_Fine_Amt;
	}

	public String getCheck_Bounce_Reason() {
		return Check_Bounce_Reason;
	}

	public void setCheck_Bounce_Reason(String check_Bounce_Reason) {
		Check_Bounce_Reason = check_Bounce_Reason;
	}

	public String getPool_Rate() {
		return Pool_Rate;
	}

	public void setPool_Rate(String pool_Rate) {
		Pool_Rate = pool_Rate;
	}
	
	

}
