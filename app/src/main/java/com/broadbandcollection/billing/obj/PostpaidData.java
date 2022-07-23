package com.broadbandcollection.billing.obj;

import java.io.Serializable;

public class PostpaidData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
        private String Address;  
	
        private String UserName;
        
        private String MemberLogin_Id;
        
        private String Member_Id;
        
	    private String PaymentMode;

	    private String PayNo;

	    private String Amount;

	    private String PaymentType;

	    private String ChequeDate;

	    private String InvoiceId;

	    private String IsPaymentCompleted;

	    private String BankName;

	    private String PenaltyAmount;

	    private String PayDate;

	    private String BillAmount;

	
	    
	    public String getAddress() {
			return Address;
		}


		public void setAddress(String address) {
			Address = address;
		}


		public int getPropertyCount() {
			// TODO Auto-generated method stub
			return 13;
		}
	    
	
		public String getUserName() {
			return UserName;
		}

		public void setUserName(String userName) {
			UserName = userName;
		}

		public String getMemberLogin_Id() {
			return MemberLogin_Id;
		}

		public void setMemberLogin_Id(String memberLogin_Id) {
			MemberLogin_Id = memberLogin_Id;
		}

		public String getMember_Id() {
			return Member_Id;
		}

		public void setMember_Id(String member_Id) {
			Member_Id = member_Id;
		}

		public String getPaymentMode ()
	    {
	        return PaymentMode;
	    }

	    public void setPaymentMode (String PaymentMode)
	    {
	        this.PaymentMode = PaymentMode;
	    }

	    public String getPayNo ()
	    {
	        return PayNo;
	    }

	    public void setPayNo (String PayNo)
	    {
	        this.PayNo = PayNo;
	    }

	    public String getAmount ()
	    {
	        return Amount;
	    }

	    public void setAmount (String Amount)
	    {
	        this.Amount = Amount;
	    }

	    public String getPaymentType ()
	    {
	        return PaymentType;
	    }

	    public void setPaymentType (String PaymentType)
	    {
	        this.PaymentType = PaymentType;
	    }

	    public String getChequeDate ()
	    {
	        return ChequeDate;
	    }

	    public void setChequeDate (String ChequeDate)
	    {
	        this.ChequeDate = ChequeDate;
	    }

	    public String getInvoiceId ()
	    {
	        return InvoiceId;
	    }

	    public void setInvoiceId (String InvoiceId)
	    {
	        this.InvoiceId = InvoiceId;
	    }

	    public String getIsPaymentCompleted ()
	    {
	        return IsPaymentCompleted;
	    }

	    public void setIsPaymentCompleted (String IsPaymentCompleted)
	    {
	        this.IsPaymentCompleted = IsPaymentCompleted;
	    }

	    public String getBankName ()
	    {
	        return BankName;
	    }

	    public void setBankName (String BankName)
	    {
	        this.BankName = BankName;
	    }

	    public String getPenaltyAmount ()
	    {
	        return PenaltyAmount;
	    }

	    public void setPenaltyAmount (String PenaltyAmount)
	    {
	        this.PenaltyAmount = PenaltyAmount;
	    }

	    public String getPayDate ()
	    {
	        return PayDate;
	    }

	    public void setPayDate (String PayDate)
	    {
	        this.PayDate = PayDate;
	    }

	    public String getBillAmount() {
			return BillAmount;
		}


		public void setBillAmount(String billAmount) {
			BillAmount = billAmount;
		}


		@Override
	    public String toString()
	    {
	        return "ClassPojo [PaymentMode = "+PaymentMode+", PayNo = "+PayNo+", Amount = "+Amount+", PaymentType = "+PaymentType+", ChequeDate = "+ChequeDate+", InvoiceId = "+InvoiceId+", IsPaymentCompleted = "+IsPaymentCompleted+", BankName = "+BankName+", PenaltyAmount = "+PenaltyAmount+", PayDate = "+PayDate+"]";
	    }

}
