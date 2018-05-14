package Object;

public class imageRequest implements java.io.Serializable {
	
	private static final long serialVersionUID = 718731112186734542L;
	
	private String imageName, orderID;
	
	public imageRequest() {}
	
	public imageRequest(String img, String id) {
		this.imageName = img;
		this.orderID = id;
	}
	
	public String getImg() {return this.imageName;}
	public String getID() {return this.orderID;}
}
