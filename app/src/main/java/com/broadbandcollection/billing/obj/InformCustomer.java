package com.broadbandcollection.billing.obj;

import java.io.Serializable;

public class InformCustomer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String subscriberid="";
	String date="";
	
	public InformCustomer(String subscriberid, String date) {
	//	super();
		this.subscriberid = subscriberid;
		this.date = date;
	}
	public String getSubscriberid() {
		return subscriberid;
	}
	public void setSubscriberid(String subscriberid) {
		this.subscriberid = subscriberid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
