package Object;

public class UserShippingInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -2294154168924586394L;
	
	private String line1, city, state, zip, card;
	
	public UserShippingInfo(){}
	
	public UserShippingInfo(String l, String c, String s, String z, String cd) {
		this.line1 = l;
		this.city = c;
		this.state = s;
		this.zip = z;
		this.card = cd;
	}
	
	public String getLine1() {return this.line1;}
	public String getCity() {return this.city;}
	public String getState() {return this.state;}
	public String getZip() {return this.zip;}
	public String getCard() {return this.card;}

}
