package Object;

public class InitialOrder implements java.io.Serializable {

	private static final long serialVersionUID = -7454651918140039974L;
	
	private Order o;
	private UserShippingInfo usi;
	
	public InitialOrder() {}
	
	public InitialOrder(Order order, UserShippingInfo shipping) {
		this.o = order;
		this.usi = shipping;
	}
	
	public Order getOrder() {return this.o;}
	public UserShippingInfo getShippingInfo() {return this.usi;}
}
