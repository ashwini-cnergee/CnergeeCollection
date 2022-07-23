package com.broadbandcollection.billing.SOAP;




import com.broadbandcollection.billing.activity.UpdateStatusActivity;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.CollectionObj;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CollectionCaller  extends Thread {
	
	public CollectinsSOAP collections;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private CollectinsSOAP collectionpayment;
	
	private CollectionObj collectionObj;

	private AuthenticationMobile Authobj;
	private String  MemberId,PayPickUpId,TypeName,CBDate,CreatedDate,Comment,MobUserId;
	public String username;
	public String password;
	//PackgeSOAP packageSOAP;
	//private String SubscriberID;

	public CollectionCaller() {
	}

	public CollectionCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,AuthenticationMobile Authobj) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.Authobj = Authobj;
	}

	public void run() {

		try {
			collectionpayment = new CollectinsSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			collectionpayment.setUsername(username);
			collectionpayment.setCollectionObj(getCollectionObj());

			UpdateStatusActivity.rslt = collectionpayment.CallCollectionSOAP(Authobj);
			UpdateStatusActivity.responseMsg = collectionpayment.getResponseMsg();
			
		}catch (SocketException e) {
			e.printStackTrace();
			UpdateStatusActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			UpdateStatusActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			UpdateStatusActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}
	
	public void setCollectionObj(CollectionObj collectionObj) {
		this.collectionObj = collectionObj;
	}

	public CollectionObj getCollectionObj() {
		return collectionObj;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}