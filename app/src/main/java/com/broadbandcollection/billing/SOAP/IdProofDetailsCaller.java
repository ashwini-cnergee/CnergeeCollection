package com.broadbandcollection.billing.SOAP;


import com.broadbandcollection.billing.activity.IdCardActivity;
import com.broadbandcollection.billing.activity.Login;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class IdProofDetailsCaller extends Thread {
	
	public IdProofDetailsSoap IDProofSoap;
	Utils utils = new Utils();
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String JsonResponse;
	
	private AuthenticationMobile AuthObj;
	
	public String UserLoginNam;
	
	public IdProofDetailsCaller() {
	
	}
	
	
	public IdProofDetailsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,String METHOD_NAME,AuthenticationMobile AuthObj){

	
		Utils.log("UserName  Calle",""+UserLoginNam);
	
		Utils.log(" do in Caller Id PrrofWebCallerExceuted ", "WebSer Data Caller Executed");
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.AuthObj = AuthObj;
	}
	



	public void run(){
		
		Utils.log("UserName  Calle",""+UserLoginNam);
		Utils.log("On Caller", " Caller Run Executed");
		try{
			
			Utils.log("In Id Proof Caller","CallerSoap executed");
			IDProofSoap = new IdProofDetailsSoap(WSDL_TARGET_NAMESPACE,SOAP_URL,METHOD_NAME);
			
			Utils.log("In Id Proof Caller","CallerSoap executed");
			IdCardActivity.rslt = IDProofSoap.GetIdProofDetailsSoap(UserLoginNam,AuthObj);
			IdCardActivity.isVaildUser = IDProofSoap.isValidUser();
			
		
		
			
		}catch (SocketException e) {
			e.printStackTrace();
			Login.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			Login.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			Login.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}


	public String getJsonResponse() {
		// TODO Auto-generated method stub
		return JsonResponse;
	}
	}


