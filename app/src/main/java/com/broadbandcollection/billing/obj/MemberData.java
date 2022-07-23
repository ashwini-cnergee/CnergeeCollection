/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.obj;

import java.util.Date;
import java.util.Map;

public class MemberData /* implements Parcelable */{

	private String MemberId;
	private String SubscriberID;
	private String SubscriberName;
	private Date expiryDate;
	private String strExpiryDate;
	private String CurrentPlan;
	private double PlanRate;
	private String SubscriberStatus;
	private String PackageListCode;
	private Map<String, MemberData> mapMemberData;
	private String PrimaryMobileNo;
	private String SecondryMobileNo;
	private String AreaCode;
	private String AreaCodeFilter;
	private String IsAutoReceipt;
	private String CheckForRenewal;
	private String AreaName;
	private String ISPName;
	private String PaymentDate;
	private String MemberAddress;
	private String DiscounPackRate;
	private String DiscounPackName;
	private String AdditionAmountDetails;
	private String ConnectionTypeId;
	private Double Outstanding_Amt;
	
	private boolean IsQos;
	private String PackageType;
	
	public Double getOutstanding_Amt() {
		return Outstanding_Amt;
	}

	public void setOutstanding_Amt(Double outstanding_Amt) {
		Outstanding_Amt = outstanding_Amt;
	}

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}

	public String getAdditionAmountDetails() {
		return AdditionAmountDetails;
	}

	public void setAdditionAmountDetails(String additionAmountDetails) {
		AdditionAmountDetails = additionAmountDetails;
	}

	public String getDiscounPackRate() {
		return DiscounPackRate;
	}

	public void setDiscounPackRate(String discounPackRate) {
		DiscounPackRate = discounPackRate;
	}

	public String getDiscounPackName() {
		return DiscounPackName;
	}

	public void setDiscounPackName(String discounPackName) {
		DiscounPackName = discounPackName;
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

	public String getPrimaryMobileNo() {
		return PrimaryMobileNo;
	}

	public void setPrimaryMobileNo(String primaryMobileNo) {
		PrimaryMobileNo = primaryMobileNo;
	}

	public String getSecondryMobileNo() {
		return SecondryMobileNo;
	}

	public void setSecondryMobileNo(String secondryMobileNo) {
		SecondryMobileNo = secondryMobileNo;
	}

	/**
	 * @return the mapMemberData
	 */
	public Map<String, MemberData> getMapMemberData() {
		return mapMemberData;
	}

	/**
	 * @param mapMemberData
	 *            the mapMemberData to set
	 */
	public void setMapMemberData(Map<String, MemberData> mapMemberData) {
		this.mapMemberData = mapMemberData;
	}

	/**
	 * @return the strExpiryDate
	 */
	public String getStrExpiryDate() {
		return strExpiryDate;
	}

	/**
	 * @param strExpiryDate
	 *            the strExpiryDate to set
	 */
	public void setStrExpiryDate(String strExpiryDate) {
		this.strExpiryDate = strExpiryDate;
	}

	/**
	 * @return the subscriberID
	 */
	public String getSubscriberID() {
		return SubscriberID;
	}

	/**
	 * @param subscriberID
	 *            the subscriberID to set
	 */
	public void setSubscriberID(String subscriberID) {
		SubscriberID = subscriberID;
	}

	/**
	 * @return the subscriberName
	 */
	public String getSubscriberName() {
		return SubscriberName;
	}

	/**
	 * @param subscriberName
	 *            the subscriberName to set
	 */
	public void setSubscriberName(String subscriberName) {
		SubscriberName = subscriberName;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the currentPlan
	 */
	public String getCurrentPlan() {
		return CurrentPlan;
	}

	/**
	 * @param currentPlan
	 *            the currentPlan to set
	 */
	public void setCurrentPlan(String currentPlan) {
		CurrentPlan = currentPlan;
	}

	/**
	 * @return the planRate
	 */
	public double getPlanRate() {
		return PlanRate;
	}

	/**
	 * @param planRate
	 *            the planRate to set
	 */
	public void setPlanRate(double planRate) {
		PlanRate = planRate;
	}

	/**
	 * @return the subscriberStatus
	 */
	public String getSubscriberStatus() {
		return SubscriberStatus;
	}

	/**
	 * @param subscriberStatus
	 *            the subscriberStatus to set
	 */
	public void setSubscriberStatus(String subscriberStatus) {
		SubscriberStatus = subscriberStatus;
	}

	/**
	 * @return the packageListCode
	 */
	public String getPackageListCode() {
		return PackageListCode;
	}

	/**
	 * @param packageListCode
	 *            the packageListCode to set
	 */
	public void setPackageListCode(String packageListCode) {
		PackageListCode = packageListCode;
	}

	/**
	 * @return the isAutoReceipt
	 */
	public String getIsAutoReceipt() {
		return IsAutoReceipt;
	}

	/**
	 * @param isAutoReceipt the isAutoReceipt to set
	 */
	public void setIsAutoReceipt(String isAutoReceipt) {
		IsAutoReceipt = isAutoReceipt;
	}
	
	public String getCheckForRenewal() {
		return CheckForRenewal;
	}

	public void setCheckForRenewal(String checkForRenewal) {
		CheckForRenewal = checkForRenewal;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	/**
	 * @return the iSPName
	 */
	public String getISPName() {
		return ISPName;
	}

	/**
	 * @param iSPName the iSPName to set
	 */
	public void setISPName(String iSPName) {
		ISPName = iSPName;
	}

	/**
	 * @return the paymentDate
	 */
	public String getPaymentDate() {
		return PaymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(String paymentDate) {
		PaymentDate = paymentDate;
	}

	public String getMemberAddress() {
		return MemberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		MemberAddress = memberAddress;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public boolean isIsQos() {
		return IsQos;
	}

	public void setIsQos(boolean isQos) {
		IsQos = isQos;
	}

	public String getPackageType() {
		return PackageType;
	}

	public void setPackageType(String packageType) {
		PackageType = packageType;
	}

	
	
	
	/*
	 * @Override public int describeContents() { // TODO Auto-generated method
	 * stub return 0; }
	 * 
	 * @Override public void writeToParcel(Parcel dest, int flags) { // TODO
	 * Auto-generated method stub dest.writeString(getSubscriberID());
	 * dest.writeString(getCurrentPlan());
	 * dest.writeString(getPackageListCode());
	 * dest.writeString(getSubscriberName());
	 * dest.writeString(getSubscriberStatus());
	 * dest.writeValue(getExpiryDate().toString());
	 * 
	 * dest.writeDouble(getPlanRate()); }
	 * 
	 * public MemberData() {
	 * 
	 * } public MemberData(Parcel in) { readFromParcel(in); } private void
	 * readFromParcel(Parcel in) { // We just need to read back each // field in
	 * the order that it was // written to the parcel SubscriberID =
	 * in.readString(); CurrentPlan = in.readString(); PackageListCode =
	 * in.readString(); SubscriberName = in.readString(); SubscriberStatus =
	 * in.readString(); strExpiryDate = in.readString(); PlanRate =
	 * in.readDouble(); }
	 * 
	 * @SuppressWarnings("unchecked") public static final Parcelable.Creator
	 * CREATOR = new Parcelable.Creator() { public MemberData
	 * createFromParcel(Parcel in){ return new MemberData(in); } public
	 * MemberData[] newArray(int size){ return new MemberData[size]; } };
	 */
}
