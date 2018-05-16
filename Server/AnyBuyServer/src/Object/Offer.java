package Object;

public class Offer implements java.io.Serializable{
		private static final long serialVersionUID = 9140053761916518599L;
	
		private String sellerID, remark;
		private double rate, expressCost;
		private int shippingMethod;
		private boolean acceptance;
		
		public Offer() {}
		
		public Offer (String ID, double RA, double EC, int SM, boolean A, String RM) {
			// Read-only once created.
			this.sellerID = ID;
			this.rate = RA;
			this.expressCost = EC;
			this.shippingMethod = SM;
			this.acceptance = A;
			this.remark = RM;
		}
		
		public String getSellerID() {return this.sellerID;}
		public double getRate() {return this.rate;}
		public double getExpressCost() {return this.expressCost;}
		public int getShippingMethod() {return this.shippingMethod;}
		public boolean getAcceptance() {return this.acceptance;}
		public String getRemark() {return this.remark;}
}
