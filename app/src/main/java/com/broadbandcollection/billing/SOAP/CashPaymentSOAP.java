/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.SOAP;



import com.broadbandcollection.billing.Marshals.MarshalDouble;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PaymentObj;
import com.broadbandcollection.billing.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CashPaymentSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;

	private PaymentObj paymentObj;
	private String refNo;

	String username, AreaCode, AreaCodeFilter, receptNo;
	Double Outstanding_Add_Amount;

	public CashPaymentSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;

		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallCahsPaymentSOAP(AuthenticationMobile Authobj)throws SocketException,SocketTimeoutException,Exception {

		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i("SubscriberID :", getPaymentObj().getSubscriberID());
		Log.i("UserLoginName :", getPaymentObj().getUserLoginName());
		Log.i("AltMobileNo :", getPaymentObj().getAltMobileNo());
		Log.i("PlanName :", getPaymentObj().getPlanName());
		Log.i("PaidAmount :", getPaymentObj().getPaidAmount() + "");
		Log.i("PaymentDate :", getPaymentObj().getStrPaymentDate().toString());
		Log.i("PaymentMode :", getPaymentObj().getPaymentMode());
		Log.i("IsChangePlan :", getPaymentObj().getChangePlan() + "");
		Log.i("ActionType :", getPaymentObj().getActionType());
		Log.i("ReceiptNo :", getPaymentObj().getRecieptNo());*/
		/*Log.i("#####################", "");*/

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(getPaymentObj().getSubscriberID());
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(getPaymentObj().getUserLoginName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AltMobileNo");
		pi.setValue(getPaymentObj().getAltMobileNo());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PlanName");
		pi.setValue(getPaymentObj().getPlanName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaidAmount");
		double payamt = getPaymentObj().getPaidAmount();
		pi.setValue(payamt);
		pi.setType(Double.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaymentDate");
		pi.setValue(getPaymentObj().getStrPaymentDate());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("PaymentMode");
		pi.setValue(getPaymentObj().getPaymentMode());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("IsChangePlan");
		pi.setValue(getPaymentObj().getChangePlan());
		pi.setType(boolean.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("ActionType");
		pi.setValue(getPaymentObj().getActionType());
		pi.setType(String.class);
		request.addProperty(pi);


		pi = new PropertyInfo();
		pi.setName("ReceiptNo");
		pi.setValue(getPaymentObj().getRecieptNo());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("BalanceAmount");
		pi.setValue(getOutstanding_Add_Amount());
		pi.setType(Double.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AdvanceType");
		pi.setValue(getPaymentObj().getAdvance_type_for_volume_pkg());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("ReferenceNumber");
		pi.setValue(getPaymentObj().getRtNo());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("Comment");
		pi.setValue(getPaymentObj().getComment());
		pi.setType(String.class);
		request.addProperty(pi);


		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.AuthName);
		pi.setValue(Authobj);
		pi.setType(Authobj.getClass());
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PoolRate");
		pi.setValue(getPaymentObj().getPoolRate());
		pi.setType(Double.class);
		request.addProperty(pi);
		
		

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

		// MarshalDate mdate = new MarshalDate();
		// mdate.register(envelope);
		// new org.ksoap2.serialization.MarshalDate().register(envelope);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

			// String SendMemberDataResponse = "SendMemberDataResponse";

			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log("#########CASH############", " REQUEST "+androidHttpTransport.requestDump);

			Utils.log("#########CASH############", " RESPONSE "+androidHttpTransport.responseDump);

			// SoapObject response = (SoapObject) envelope.getResponse();
			

			// SoapObject result;

			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				refNo = response.toString();

				// SoapObject soapObject = (SoapObject) envelope.bodyIn;
				// Log.i("@@@@@@@@", soapObject.toString());
				// SoapObject response = (SoapObject) envelope.getResponse();
				// response = (SoapObject) response.getProperty("NewDataSet");
				// response = (SoapObject) response.getProperty("Table");
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				// throw new Exception(soapFault.getMessage());
				return soapFault.getMessage().toString();
			}

			/*
			 * Object result = null; if (!isResultVector){ result =
			 * envelope.getResponse(); }else{ result = envelope.bodyIn; }
			 */
			//Log.i("#####################", " DONE ");
			// refNo = result.toString();

			if (androidHttpTransport != null) {
				androidHttpTransport.reset();
				androidHttpTransport.getServiceConnection().disconnect();
			}
			
			return "ok";
	
	}

	public void setPaymentObj(PaymentObj paymentObj) {
		this.paymentObj = paymentObj;
	}

	public PaymentObj getPaymentObj() {
		return paymentObj;
	}

	/**
	 * @return the refNo
	 */
	public String getRefNo() {
		return refNo;
	}

	/**
	 * @param refNo
	 *            the refNo to set
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getOutstanding_Add_Amount() {
		return Outstanding_Add_Amount;
	}

	public void setOutstanding_Add_Amount(Double outstanding_Add_Amount) {
		Outstanding_Add_Amount = outstanding_Add_Amount;
	}

}
