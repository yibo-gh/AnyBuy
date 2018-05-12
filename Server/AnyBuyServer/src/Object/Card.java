package Object;

public class Card implements java.io.Serializable{
	
	private String FN, LN, issuer, cardNum, zip, exp;
	
	public Card() {}
	
	public Card(String f, String l, String i, String c, String e, String z) {
		this.FN = f;
		this.LN = l;
		this.issuer = i;
		this.cardNum = c;
		this.zip = z;
		this.exp = e;
	}
	
	public String getFN() {return this.FN;}
	public String getLN() {return this.LN;}
	public String getIssuser() {return this.issuer;}
	public String getCardNum() {return this.cardNum;}
	public String getZip() {return this.zip;}
	public String getExp() {return this.exp;}
	
}
