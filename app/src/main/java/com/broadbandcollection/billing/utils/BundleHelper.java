package com.broadbandcollection.billing.utils;

import android.content.Intent;
import android.os.Bundle;

import com.broadbandcollection.billing.obj.MemberData;


public class BundleHelper {
	private Bundle backExtras;
	private Bundle currentExtras;
	private Intent intent;
	
	public BundleHelper(Intent intent,String backBundelPackage,String currentBundelPackage){
		
		Bundle backExtras = intent.getBundleExtra(backBundelPackage);
		setBackExtras(backExtras);
		
		Bundle extras = intent.getBundleExtra(currentBundelPackage);
		setCurrentExtras(extras);
	}

	/**
	 * @return the backExtras
	 */
	public Bundle getBackExtras() {
		return backExtras;
	}

	/**
	 * @param backExtras the backExtras to set
	 */
	public void setBackExtras(Bundle backExtras) {
		this.backExtras = backExtras;
	}

	/**
	 * @return the currentExtras
	 */
	public Bundle getCurrentExtras() {
		return currentExtras;
	}

	/**
	 * @param currentExtras the currentExtras to set
	 */
	public void setCurrentExtras(Bundle currentExtras) {
		this.currentExtras = currentExtras;
	}
	
	
	public static Bundle getMemberDataBundel(Bundle bundle, MemberData memberData){
		bundle.putString("MemberId",
				memberData.getMemberId());
		bundle.putString("subscriberID",
				memberData.getSubscriberID());
		bundle.putString("CurrentPlan", memberData.getCurrentPlan());
		// bundle.putString("PackageListCode",
		// memberData.getPackageListCode());
		bundle.putString("AreaCode", memberData.getAreaCode());
		bundle.putString("AreaCodeFilter",
				memberData.getAreaCodeFilter());
		bundle.putString("SubscriberName",
				memberData.getSubscriberName());
		bundle.putString("SubscriberStatus",
				memberData.getSubscriberStatus());
		bundle.putString("expiryDate", memberData.getStrExpiryDate());
		bundle.putDouble("PlanRate", memberData.getPlanRate());
		bundle.putString("PrimaryMobileNo",
				memberData.getPrimaryMobileNo());
		bundle.putString("IsAutoReceipt",
				memberData.getIsAutoReceipt());
		bundle.putString("CheckForRenewal",
				memberData.getCheckForRenewal());
		bundle.putString("AreaName",
				memberData.getAreaName());
		bundle.putString("ISPName",
				memberData.getISPName());
		bundle.putString("PaymentDate",
				memberData.getPaymentDate());
		
		bundle.putString("discountpackrate",memberData.getDiscounPackRate());
		bundle.putString("discountedPack",memberData.getDiscounPackName());
		bundle.putString("ConnectionTypeId",memberData.getConnectionTypeId());
		bundle.putString("AdditionAmountDetails",memberData.getAdditionAmountDetails());
		bundle.putString("MemberAddress",
				memberData.getMemberAddress());
		bundle.putDouble("Outstanding_Amt",
				memberData.getOutstanding_Amt());
		bundle.putString("execteFrom","search");
		
		bundle.putBoolean("IsQos",
				memberData.isIsQos());
		
		bundle.putString("PackageType",
				memberData.getPackageType());
		
	
		
	
		
		return bundle;
	}
	
	/*
	 * Used in ShowDetailActivity
	 */
	
	
	public static Bundle getMemberDataBundelForShowDetailsActivity(Bundle bundle,MemberData memberData){
		
		
		bundle.putString("PrimaryMobileNo",
				memberData.getPrimaryMobileNo());
		bundle.putString("SecondryMobileNo",
				memberData.getSecondryMobileNo());
		bundle.putString("AreaCode", memberData.getAreaCode());
		bundle.putString("AreaCodeFilter",
				memberData.getAreaCodeFilter());
		bundle.putString("IsAutoReceipt",
				memberData.getIsAutoReceipt());
		bundle.putString("PaymentDate",
				memberData.getPaymentDate());
		
		bundle.putString("discountpackrate",
				memberData.getDiscounPackRate());
		bundle.putString("discountedPack",
				memberData.getDiscounPackName());
		
		return bundle;
	}
}
