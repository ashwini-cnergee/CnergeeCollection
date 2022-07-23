package com.broadbandcollection.billing.SOAP;




import com.broadbandcollection.billing.activity.ComplaintActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class GetComplaintListSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	

	public GetComplaintListSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String getComplaintTypeList(String ClientAccessId) throws SocketException,SocketTimeoutException,Exception {

		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" AreaCode ",getAreaCode());*/
		//Log.i("#####################", "");

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName("CliectAccessId");
		pi.setValue(ClientAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<", request.toString());
		
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

/*		try{
			Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			SoapObject soapObject = (SoapObject) envelope.getResponse();
			int tot_count = soapObject.getAttributeCount();
		
			Log.i("##################### tot_count ", " "+tot_count);
			Log.i("#####################", " END ");
			return "ok";
		}catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}*/
		try {
			//Log.i("#####################", " CALL ");
			
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			String ss = androidHttpTransport.responseDump;
			/*Utils.log(">>>>>>>GetComplaintListSOAP <<<<<<", androidHttpTransport.requestDump);
			Utils.log(">>>>>>>GetComplaintListSOAP <<<<<<", ss);*/
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
						
							ComplaintActivity.ComplaintListId.add(tableObj.getPropertyAsString("ComplaintId").toString());
							ComplaintActivity.ComplaintListName.add(tableObj.getPropertyAsString("ComplaintName").toString());
							
											
						}
						
					} else {
					//	ComplaintActivity.ComplaintListId.add("");
					//	ComplaintActivity.ComplaintListName.add("");
					}
				} else {
					//ComplaintActivity.ComplaintListId.add("");
					//ComplaintActivity.ComplaintListName.add("");
				}

			}
				
			/*String ss = androidHttpTransport.responseDump;
			Log.i(">>>>>>>Data Package <<<<<<", ss);
			setXMLData(ss);*/
			//Log.i("#####################", " DONE ");
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
}
