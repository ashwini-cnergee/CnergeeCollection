package com.broadbandcollection.billing.activity;

import android.app.Application;

public class Controller extends Application{
private String	username;
private String	selItem;
private String	subscriberID;
private double	PlanRate ;
private String	PackageListCode ;
private String	CurrentPlan ;
private String	PrimaryMobileNo ;
private String	SecondryMobileNo ;
private String	updatefrom ;
private String	AreaCode ;
private String	AreaCodeFilter ;
private Boolean	isPlanchange  ;
private String	IsAutoReceipt ;
private String	str_action ;
private String	PaymentDate ;

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getSelItem() {
	return selItem;
}
public void setSelItem(String selItem) {
	this.selItem = selItem;
}
public String getSubscriberID() {
	return subscriberID;
}
public void setSubscriberID(String subscriberID) {
	this.subscriberID = subscriberID;
}
public double getPlanRate() {
	return PlanRate;
}
public void setPlanRate(double planRate) {
	PlanRate = planRate;
}
public String getPackageListCode() {
	return PackageListCode;
}
public void setPackageListCode(String packageListCode) {
	PackageListCode = packageListCode;
}
public String getCurrentPlan() {
	return CurrentPlan;
}
public void setCurrentPlan(String currentPlan) {
	CurrentPlan = currentPlan;
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
public String getUpdatefrom() {
	return updatefrom;
}
public void setUpdatefrom(String updatefrom) {
	this.updatefrom = updatefrom;
}
public String getAreaCode() {
	return AreaCode;
}
public void setAreaCode(String areaCode) {
	AreaCode = areaCode;
}
public String getAreaCodeFilter() {
	return AreaCodeFilter;
}
public void setAreaCodeFilter(String areaCodeFilter) {
	AreaCodeFilter = areaCodeFilter;
}
public Boolean getIsPlanchange() {
	return isPlanchange;
}
public void setIsPlanchange(Boolean isPlanchange) {
	this.isPlanchange = isPlanchange;
}
public String getIsAutoReceipt() {
	return IsAutoReceipt;
}
public void setIsAutoReceipt(String isAutoReceipt) {
	IsAutoReceipt = isAutoReceipt;
}
public String getStr_action() {
	return str_action;
}
public void setStr_action(String str_action) {
	this.str_action = str_action;
}
public String getPaymentDate() {
	return PaymentDate;
}
public void setPaymentDate(String paymentDate) {
	PaymentDate = paymentDate;
}
public void setNull(){
		username=null;
		selItem=null;
		subscriberID=null;
		PlanRate =0.00;
		PackageListCode=null ;
		CurrentPlan =null;
		PrimaryMobileNo=null ;
		SecondryMobileNo =null;
		updatefrom =null;
		AreaCode =null;
		AreaCodeFilter=null ;
		isPlanchange =null ;
		IsAutoReceipt=null ;
		str_action =null;
		PaymentDate =null;
}
}
