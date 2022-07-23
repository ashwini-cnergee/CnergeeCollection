/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  11 Jan 2013
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.obj;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class AuthenticationMobile implements KvmSerializable {

	public static boolean is_notification_running=false;
	
	private static final long serialVersionUID = -1166006770093411055L;
	private String MobileNumber;
	private String MobLoginId;
	private String MobUserPass;
	private String IMEINo;
	private String CliectAccessId;
	private String MacAddress;
	private String PhoneUniqueId;
	private String AppVersion;
	

	public static String AuthName="AuthObj";
	public static String ClientName="CliectAccessId";

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub

		switch (arg0) {
		case 0:
			return getMobileNumber();
		case 1:
			return getCliectAccessId();					
		case 2:
			return getMobLoginId();			
		case 3:
			return getMobUserPass();			
		case 4:
			return getIMEINo();
		case 5:
			return getMacAddress();
		case 6:
			return getPhoneUniqueId();
		case 7:
			return getAppVersion();
		}
		return null;
	}
	
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MobileNumber";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "CliectAccessId";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MobLoginId";			
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MobUserPass";			
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "IMEINo";
			break;	
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MacAddress";
			break;	
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PhoneUniqueId";
			break;	
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "AppVersion";
			break;		
		default:
			break;
		}

	}
	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			MobileNumber = value.toString();
			break;
		case 1:
			CliectAccessId = value.toString();
			break;		
			
		case 2:
			MobLoginId = value.toString();
			break;
			
		case 3:
			MobUserPass = value.toString();
			break;
			
		case 4:
			IMEINo = value.toString();
			break;
		case 5:
			MacAddress = value.toString();
			break;
		case 6:
			PhoneUniqueId = value.toString();
			break;
		case 7:
			AppVersion = value.toString();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return MobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	/**
	 * @return the mobLoginId
	 */
	public String getMobLoginId() {
		return MobLoginId;
	}
	/**
	 * @param mobLoginId the mobLoginId to set
	 */
	public void setMobLoginId(String mobLoginId) {
		MobLoginId = mobLoginId;
	}
	/**
	 * @return the mobUserPass
	 */
	public String getMobUserPass() {
		return MobUserPass;
	}
	/**
	 * @param mobUserPass the mobUserPass to set
	 */
	public void setMobUserPass(String mobUserPass) {
		MobUserPass = mobUserPass;
	}
	/**
	 * @return the iMEINo
	 */
	public String getIMEINo() {
		return IMEINo;
	}
	/**
	 * @param iMEINo the iMEINo to set
	 */
	public void setIMEINo(String iMEINo) {
		IMEINo = iMEINo;
	}
	
	/**
	 * @return the CustomerId
	 */
	public String getCliectAccessId() {
		return CliectAccessId;
	}
	/**
	 * @param CustomerId the CustomerId to set
	 */
	public void setCliectAccessId(String CustId) {
		CliectAccessId = CustId;
	}
	public String getMacAddress() {
		return MacAddress;
	}


	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}


	public String getPhoneUniqueId() {
		return PhoneUniqueId;
	}


	public void setPhoneUniqueId(String phoneUniqueId) {
		PhoneUniqueId = phoneUniqueId;
	}
	public String getAppVersion() {
		return AppVersion;
	}



	public void setAppVersion(String appVersion) {
		AppVersion = appVersion;
	}

}
