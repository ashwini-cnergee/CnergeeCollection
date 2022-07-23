package com.broadbandcollection.iprenewal;
                 
public class IP_Address
{
    private String IPID;

    private Boolean IsUsed;

    private String IPPoolID;

    private String IPADDRESS;

    public String getIPID ()
    {
        return IPID;
    }

    public void setIPID (String IPID)
    {
        this.IPID = IPID;
    }

   

    public Boolean getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		IsUsed = isUsed;
	}

	public String getIPPoolID ()
    {
        return IPPoolID;
    }

    public void setIPPoolID (String IPPoolID)
    {
        this.IPPoolID = IPPoolID;
    }

    public String getIPADDRESS ()
    {
        return IPADDRESS;
    }

    public void setIPADDRESS (String IPADDRESS)
    {
        this.IPADDRESS = IPADDRESS;
    }
}
	