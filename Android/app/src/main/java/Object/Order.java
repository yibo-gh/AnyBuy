package Object;

import java.sql.Timestamp;

public class Order implements java.io.Serializable{

	// Object Hash for validation use.
	private static final long serialVersionUID = -3998657393394608240L;
	
	private String product, brand, country, image;
	private int quantity;
	private Timestamp orderTime;
	
	public Order() {}
	
	public Order (String p, String b, int q, String c, String img, Timestamp ts) {
		// Read-only once created.
		this.product = p;
		this.brand = b;
		this.quantity = q;
		this.image = img;
		this.orderTime = ts;
		this.country = c;
	}
	
	public String getProduct() {return this.product;}
	public String getBrand() {return this.brand;}
	public int getQuantity() {return this.quantity;}
	public String getImage() {return this.image;}
	public Timestamp getTimestamp() {return this.orderTime;}
	public String getCountry() {return this.country;}
	
}
