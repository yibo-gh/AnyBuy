package Debugger;
import java.sql.Timestamp;

import ExperimentalUse.objectClientTester;
import Object.*;

public class Simulator {

	public static void main(String[] args) throws Exception {

		
		
		LinkedList ll = new LinkedList();

		ll.insert("reg");
		User u = new User("yoona", "snsd.or.kr", "loveYOONA!");
		ll.insert(u);
		System.out.println((String)objectClientTester.run(ll));
		
		ll = new LinkedList();
		ll.insert("lgi");
		u = new User("yoona", "snsd.or.kr", "loveYOONA!");
		ll.insert(u);
		String sessionID = (String)objectClientTester.run(ll);
		
		ll = new LinkedList();
		ll.insert("plo");
		ll.insert(sessionID);
		String p, b, c, img;
		int q;
		Timestamp ts;
		p = "Yoona\\'s Choice";
		b = "Innisfree";
		q = 1;
		c = "KOR";
		img = "";
		ts = new Timestamp(System.currentTimeMillis());
		Order o = new Order(p, b, q, c, img, ts);
		ll.insert(o);
		Object obj = objectClientTester.run(ll);
		if (obj.getClass().equals(new LinkedList().getClass())) {
			
			ExperimentalUse.imageTester.main(null);
		}
		
		ll = new LinkedList();
		ll.insert("ldo");
		ll.insert(sessionID);
		ll.insert("KOR");
		obj = objectClientTester.run(ll);
		LinkedList res = (LinkedList) obj;
		Node temp = res.head;
		if (temp != null && temp.getObject().getClass().equals("".getClass())) {
			System.out.println(temp.getObject());
			return;
		}
		while (temp != null) {
			o = (Order) temp.getObject();
			System.out.println(o.getImage() + " " + o.getBrand() + " " + o.getProduct() +
					" " + o.getQuantity() + " " + o.getCountry() + " " + o.getTimestamp());
			temp = temp.getNext();
		}
	}
}
