import Debugger.objectClientTester;
import Object.*;

public class generalExperiment {

	public static void main(String[] args) throws Exception {

		LinkedList ll = new LinkedList();
		ll.insert("lgi");
		User u = new User("yoona", "snsd.or.kr", "loveYOONA!");
		ll.insert(u);
		String sessionID = (String)objectClientTester.run(ll);
		
		ll = new LinkedList();
		ll.insert("ldo");
		ll.insert(sessionID);
		ll.insert("KOR");
		Object obj = objectClientTester.run(ll);
		LinkedList res = (LinkedList) obj;
		Node temp = res.head;
		if (temp != null && temp.getObject().getClass().equals("".getClass())) {
			System.out.println(temp.getObject());
			return;
		}
		while (temp != null) {
			Order o = (Order) temp.getObject();
			System.out.println(o.getImage() + " " + o.getBrand() + " " + o.getProduct() +
					" " + o.getQuantity() + " " + o.getCountry() + " " + o.getTimestamp());
			temp = temp.getNext();
		}
	}
}
