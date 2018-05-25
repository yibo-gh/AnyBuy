package Object;

public class UserOrderHis implements java.io.Serializable{
	
	private static final long serialVersionUID = 5105837357293850904L;
	
	private String orderID;
	private int orderStatus;
	
	public UserOrderHis(){}
	
	public UserOrderHis(String id, int status){
		this.orderID = id;
		this.orderStatus = status;
	}
	
	public String getOrderID() {return this.orderID;}
	public int getOrderStatus() {return this.orderStatus;}

}
