package Object;

public class Offer implements java.io.Serializable{
	private static final long serialVersionUID = 9140053761916518599L;

	private String orderID, sellerID, remark;
	private double rate, expressCost;
	private int shippingMethod;
	private boolean acceptance;
	
	public Offer() {}
	
	public Offer (String OID, String SID, double RA, double EC, int SM, boolean A, String RM) {
		// Read-only once created.
		this.orderID = OID;
		this.sellerID = SID;
		this.rate = RA;
		this.expressCost = EC;
		this.shippingMethod = SM;
		this.acceptance = A;
		this.remark = RM;
	}
	
	public String getOrderID() {return this.orderID;}
	public String getSellerID() {return this.sellerID;}
	public double getRate() {return this.rate;}
	public double getExpressCost() {return this.expressCost;}
	public int getShippingMethod() {return this.shippingMethod;}
	public boolean getAcceptance() {return this.acceptance;}
	public String getRemark() {return this.remark;}
}
