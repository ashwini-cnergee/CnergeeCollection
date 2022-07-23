package com.broadbandcollection.billing.obj;

import com.broadbandcollection.billing.obj.InformCustomer;

import java.io.Serializable;
import java.util.ArrayList;

public class InformedCustomerList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<InformCustomer> alInformCustomers;

	public ArrayList<InformCustomer> getAlInformCustomers() {
	return alInformCustomers;
	}

	public void setAlInformCustomers(ArrayList<InformCustomer> alInformCustomers) {
	this.alInformCustomers = alInformCustomers;
	}

}
