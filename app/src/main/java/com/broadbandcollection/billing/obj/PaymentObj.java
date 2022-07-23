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

public class PaymentObj {

	private String SubscriberID;
	private String PlanName;
	private double PaidAmount;
	private Date PaymentDate;
	private String StrPaymentDate;
	private String PaymentMode;
	private Date PaymentModeDate;
	private String StrPaymentModeDate;
	private String ChequeDDEDCNo;
	private String BankName;
	private String RecieptNo;
	private boolean ChangePlan;
	private String ActionType;
	private String UserLoginName;
	private String AltMobileNo;
	private String TrackID;
	private String PaymentID;
	private String TransStatus;
	private String TransID;
	private String TransError;
	private String 	advance_type_for_volume_pkg;
	Double PoolRate=0.0;
	private String rtNo;
	private String comment;
	
	public String getUserLoginName() {
		return UserLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
	}

	public String getAltMobileNo() {
		return AltMobileNo;
	}

	public void setAltMobileNo(String altMobileNo) {
		AltMobileNo = altMobileNo;
	}

	/**
	 * @return the strPaymentDate
	 */
	public String getStrPaymentDate() {
		return StrPaymentDate;
	}

	/**
	 * @param strPaymentDate
	 *            the strPaymentDate to set
	 */
	public void setStrPaymentDate(String strPaymentDate) {
		StrPaymentDate = strPaymentDate;
	}

	/**
	 * @return the strPaymentModeDate
	 */
	public String getStrPaymentModeDate() {
		return StrPaymentModeDate;
	}

	/**
	 * @param strPaymentModeDate
	 *            the strPaymentModeDate to set
	 */
	public void setStrPaymentModeDate(String strPaymentModeDate) {
		StrPaymentModeDate = strPaymentModeDate;
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
	 * @return the planName
	 */
	public String getPlanName() {
		return PlanName;
	}

	/**
	 * @param planName
	 *            the planName to set
	 */
	public void setPlanName(String planName) {
		PlanName = planName;
	}

	/**
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return PaidAmount;
	}

	/**
	 * @param paidAmount
	 *            the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		PaidAmount = paidAmount;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return PaymentDate;
	}

	/**
	 * @param paymentDate
	 *            the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		PaymentDate = paymentDate;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return PaymentMode;
	}

	/**
	 * @param paymentMode
	 *            the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		PaymentMode = paymentMode;
	}

	/**
	 * @return the paymentModeDate
	 */
	public Date getPaymentModeDate() {
		return PaymentModeDate;
	}

	/**
	 * @param paymentModeDate
	 *            the paymentModeDate to set
	 */
	public void setPaymentModeDate(Date paymentModeDate) {
		PaymentModeDate = paymentModeDate;
	}

	/**
	 * @return the chequeDDEDCNo
	 */
	public String getChequeDDEDCNo() {
		return ChequeDDEDCNo;
	}

	/**
	 * @param chequeDDEDCNo
	 *            the chequeDDEDCNo to set
	 */
	public void setChequeDDEDCNo(String chequeDDEDCNo) {
		ChequeDDEDCNo = chequeDDEDCNo;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return BankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		BankName = bankName;
	}

	/**
	 * @return the recieptNo
	 */
	public String getRecieptNo() {
		return RecieptNo;
	}

	/**
	 * @param recieptNo
	 *            the recieptNo to set
	 */
	public void setRecieptNo(String recieptNo) {
		RecieptNo = recieptNo;
	}

	/**
	 * @return the changePlan
	 */
	public boolean getChangePlan() {
		return ChangePlan;
	}

	/**
	 * @param changePlan
	 *            the changePlan to set
	 */
	public void setChangePlan(boolean changePlan) {
		ChangePlan = changePlan;
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return ActionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(String actionType) {
		ActionType = actionType;
	}

	/**
	 * @return the trackID
	 */
	public String getTrackID() {
		return TrackID;
	}

	/**
	 * @param trackID the trackID to set
	 */
	public void setTrackID(String trackID) {
		TrackID = trackID;
	}

	/**
	 * @return the paymentID
	 */
	public String getPaymentID() {
		return PaymentID;
	}

	/**
	 * @param paymentID the paymentID to set
	 */
	public void setPaymentID(String paymentID) {
		PaymentID = paymentID;
	}

	/**
	 * @return the transStatus
	 */
	public String getTransStatus() {
		return TransStatus;
	}

	/**
	 * @param transStatus the transStatus to set
	 */
	public void setTransStatus(String transStatus) {
		TransStatus = transStatus;
	}

	/**
	 * @return the transID
	 */
	public String getTransID() {
		return TransID;
	}

	/**
	 * @param transID the transID to set
	 */
	public void setTransID(String transID) {
		TransID = transID;
	}

	/**
	 * @return the transError
	 */
	public String getTransError() {
		return TransError;
	}

	/**
	 * @param transError the transError to set
	 */
	public void setTransError(String transError) {
		TransError = transError;
	}

	public String getAdvance_type_for_volume_pkg() {
		return advance_type_for_volume_pkg;
	}

	public void setAdvance_type_for_volume_pkg(String advance_type_for_volume_pkg) {
		this.advance_type_for_volume_pkg = advance_type_for_volume_pkg;
	}

	public Double getPoolRate() {
		return PoolRate;
	}

	public void setPoolRate(Double poolRate) {
		PoolRate = poolRate;
	}

	public String getRtNo() {
		return rtNo;
	}

	public void setRtNo(String rtNo) {
		this.rtNo = rtNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
