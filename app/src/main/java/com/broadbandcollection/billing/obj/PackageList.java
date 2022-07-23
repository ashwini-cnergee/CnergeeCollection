/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.obj;

import java.io.Serializable;
import java.util.Map;

public class PackageList implements Serializable {
	private String planName;
	private String packageRate;
	private String packagevalidity;
	private String ServiceTax;
	private String AreaCode;
	private String AreaCodeFilter;
	private String PackageId;

	private int srno;
	private String packagedesc;
	private String offerdesc;
	private String offervalidityc;
	private String offerpackagedesc;



	private Map<String, PackageList> mapPackageList;

	/**
	 * @return the palanName
	 */
	public Map<String, PackageList> getPalanName() {
		return mapPackageList;
	}

	/**
	 * @param palanName
	 *            the palanName to set
	 */
	public void setPalanName(Map<String, PackageList> mapPackageList) {
		this.mapPackageList = mapPackageList;
	}

	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}

	/**
	 * @param planName
	 *            the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	/**
	 * @return the packageRate
	 */
	public String getPackageRate() {
		return packageRate;
	}

	/**
	 * @param packageRate
	 *            the packageRate to set
	 */
	public void setPackageRate(String packageRate) {
		this.packageRate = packageRate;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return AreaCode;
	}

	/**
	 * @param areaCode
	 *            the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	/**
	 * @return the areaCodeFilter
	 */
	public String getAreaCodeFilter() {
		return AreaCodeFilter;
	}

	/**
	 * @param areaCodeFilter
	 *            the areaCodeFilter to set
	 */
	public void setAreaCodeFilter(String areaCodeFilter) {
		AreaCodeFilter = areaCodeFilter;
	}

	public String getPackagevalidity() {
		return packagevalidity;
	}

	public void setPackagevalidity(String packagevalidity) {
		this.packagevalidity = packagevalidity;
	}

	public String getServiceTax() {
		return ServiceTax;
	}

	public void setServiceTax(String serviceTax) {
		ServiceTax = serviceTax;
	}

	public String getPackageId() {
		return PackageId;
	}

	public void setPackageId(String packageId) {
		PackageId = packageId;
	}


	public int getSrno() {
		return srno;
	}

	public void setSrno(int srno) {
		this.srno = srno;
	}

	public String getPackagedesc() {
		return packagedesc;
	}

	public void setPackagedesc(String packagedesc) {
		this.packagedesc = packagedesc;
	}

	public String getOfferdesc() {
		return offerdesc;
	}

	public void setOfferdesc(String offerdesc) {
		this.offerdesc = offerdesc;
	}

	public String getOffervalidityc() {
		return offervalidityc;
	}

	public void setOffervalidityc(String offervalidityc) {
		this.offervalidityc = offervalidityc;
	}

	public String getOfferpackagedesc() {
		return offerpackagedesc;
	}

	public void setOfferpackagedesc(String offerpackagedesc) {
		this.offerpackagedesc = offerpackagedesc;
	}
	
}
