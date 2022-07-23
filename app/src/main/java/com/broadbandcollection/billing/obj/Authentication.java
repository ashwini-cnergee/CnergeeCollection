/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.obj;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Authentication implements KvmSerializable /*
														 * implements
														 * Serializable
														 */{
	private static final long serialVersionUID = -1166006770093411055L;
	public String VendorCode;
	public String UserName;
	public String Password;

	/**
	 * @return the vendorCode
	 */
	public String getVendorCode() {
		return VendorCode;
	}

	/**
	 * @param vendorCode
	 *            the vendorCode to set
	 */
	public void setVendorCode(String vendorCode) {
		VendorCode = vendorCode;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub

		switch (arg0) {
		case 0:
			return getUserName();
		case 1:
			return getPassword();
		case 2:
			return getVendorCode();

		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "UserName";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Password";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "VendorCode";
			break;
		default:
			break;
		}

	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			UserName = value.toString();
			break;
		case 1:
			Password = value.toString();
			break;
		case 2:
			VendorCode = value.toString();
			break;
		default:
			break;
		}
	}
}
