package Object;

public class UserOrderHis implements java.io.Serializable{
	
	private static final long serialVersionUID = 5105837357293850904L;
	
	private Order order;
	private int orderStatus;
	
	public UserOrderHis(){}
	
	public UserOrderHis(Order o, int status){
		this.order = o;
		this.orderStatus = status;
	}
	
	public Order getOrder() {return this.order;}
	public int getOrderStatus() {return this.orderStatus;}

}
