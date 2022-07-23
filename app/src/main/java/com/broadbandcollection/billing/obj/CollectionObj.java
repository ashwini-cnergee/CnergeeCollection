/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  25 Feb. 2013
 *
 * @Author 
 * Ashok Parmar
 * 
 * Version 1.0
 *
 */
package com.broadbandcollection.billing.obj;

public class CollectionObj {

	private String userId;
	private String memberid;
	private String paypickupid;
	private String typename;
	private String cbdate;
	private String comment;
	
	
	
	
	public String getpaypickid() {
		return paypickupid;
	}

	/**
	 * @param complaintNo the complaintNo to set
	 */
	public void setPayPickId(String paypickupid) {
		this.paypickupid = paypickupid;
	}
	
	public String getMemberLoginId()
	{
		return memberid;
	}
	public void SetMemberLoginId(String memberLoginId)
	{
		this.memberid = memberLoginId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTypeName() {
		return typename;
	}
	public void setTypeName(String typename) {
		this.typename = typename;
	}
	
	public String getCBDate()
	{
		return cbdate;
		
	}
	public void setCBDate(String cbdate) {
		this.cbdate = cbdate;
	}
	
	public String getComment()
	{
		return comment;
		
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
