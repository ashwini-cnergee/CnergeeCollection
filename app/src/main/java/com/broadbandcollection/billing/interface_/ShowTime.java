package com.broadbandcollection.billing.interface_;

public interface ShowTime {
	
	public void CallinformCustomer(String time, String SubscriberId, String todays_date, String PickUpId);
	
	public void callDetailsActivity(String SubscriberId, boolean is_postpaid);
}
