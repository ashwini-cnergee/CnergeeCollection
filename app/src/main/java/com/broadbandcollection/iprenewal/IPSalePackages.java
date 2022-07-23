package com.broadbandcollection.iprenewal;

import java.io.Serializable;

public class IPSalePackages implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String IPSalePackageName;

    private String Validity;

    private String TotalAmount;

    private String BaseAmount;

    private String ServiceTax;

    private String IPSalePackageId;

    public String getIPSalePackageName ()
    {
        return IPSalePackageName;
    }

    public void setIPSalePackageName (String IPSalePackageName)
    {
        this.IPSalePackageName = IPSalePackageName;
    }

    public String getValidity ()
    {
        return Validity;
    }

    public void setValidity (String Validity)
    {
        this.Validity = Validity;
    }

    public String getTotalAmount ()
    {
        return TotalAmount;
    }

    public void setTotalAmount (String TotalAmount)
    {
        this.TotalAmount = TotalAmount;
    }

    public String getBaseAmount ()
    {
        return BaseAmount;
    }

    public void setBaseAmount (String BaseAmount)
    {
        this.BaseAmount = BaseAmount;
    }

    public String getServiceTax ()
    {
        return ServiceTax;
    }

    public void setServiceTax (String ServiceTax)
    {
        this.ServiceTax = ServiceTax;
    }

    public String getIPSalePackageId ()
    {
        return IPSalePackageId;
    }

    public void setIPSalePackageId (String IPSalePackageId)
    {
        this.IPSalePackageId = IPSalePackageId;
    }
}
