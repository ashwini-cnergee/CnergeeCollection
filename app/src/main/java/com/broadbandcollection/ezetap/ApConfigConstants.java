package com.broadbandcollection.ezetap;

import com.ezetap.sdk.EzeConstants.AppMode;
import com.ezetap.sdk.EzeConstants.CommunicationChannel;
import com.ezetap.sdk.EzeConstants.LoginAuthMode;


public interface ApConfigConstants {

	//public static final LoginAuthMode authMode = LoginAuthMode.EZETAP_LOGIN_BYPASS;
	
	public static final LoginAuthMode authMode = LoginAuthMode.EZETAP_LOGIN_BYPASS;
	
	
	//You appKey comes here. This is provided by Ezetap. Please contact Ezetap support if you do not have one.
	
	//public static final String appKey = "858d2bba-d9e4-4bcb-ba56-15aa7d042403";
	

	
	public static final String productionAppKey = "0be92449-cf82-4297-ba99-00b8e04ec199";
	
	//The name of your organization	
	public static final String merchantName = "DVOISBROADBAND";
	
	//Defaulted to INR. Set to appropriate currency code your application uses.
	
	public static final String currencyCode = "INR";

	public static final AppMode appMode = AppMode.EZETAP_PROD;
	// Set to TRUE if you wish Ezetap to capture signature
	public static final boolean captureSignature = true; 
	// Specify which CommunicationChannel you wish set as preferred

	public static final CommunicationChannel preferredChannel = CommunicationChannel.NONE;
}
