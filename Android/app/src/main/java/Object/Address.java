package Object;

public class Address implements java.io.Serializable {

	private static final long serialVersionUID = 4187513628479291175L;
	
	private String FN, LN, com, line1, line2, city, state, zip;
	
	public Address() {}
	
	public Address (String f, String l, String co, String l1, String l2, String c, String s, String z) {
		this.FN = f;
		this.LN = l;
		this.com = co;
		this.line1 = l1;
		this.line2 = l2;
		this.city = c;
		this.state = s;
		this.zip = z;
	}
	
	public String getFN() { return this.FN;}
	public String getLN() { return this.LN;}
	public String getCom() {return this.com;}
	public String getL1() {return this.line1;}
	public String getL2() {return this.line2;}
	public String getCity() {return this.city;}
	public String getState() {return this.state;}
	public String getZip() {return this.zip;}
	
}
