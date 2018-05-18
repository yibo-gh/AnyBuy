package Debugger;

import java.sql.SQLException;

import IntermediateAPI.CoreOperations;
import Object.LinkedList;
import Object.Node;
import Object.Order;

public class functionTester {

	public static void main(String[] args) throws SQLException {
		
		LinkedList l = new LinkedList();
		l.insert("KR");
		l.insert("10");
		Object o = CoreOperations.loadPartialCountryOrder(l);
		
		if (o.getClass().equals("".getClass())) System.out.println((String)o);
		
		l = (LinkedList)o;
		System.out.println(l.getLength());
		Node temp = l.head;
		
		String maxOrder = "";
		String minOrder = "";
		
		while (temp.getNext().getNext() != null) {
			if (temp == l.head) minOrder = ((Order) temp.getObject()).getImage();
			Order od = (Order) temp.getObject();
			System.out.println(od.getImage() + " " + od.getBrand() + " " + od.getProduct() +
				" " + od.getQuantity() + " " + od.getCountry() + " " + od.getTimestamp());
			if (temp.getNext().getNext() == null) maxOrder = ((Order)temp.getObject()).getImage();
			temp = temp.getNext();
		}
		maxOrder = ((Order) temp.getPrev().getObject()).getImage();
		
		String minLine = (String)l.end.getObject();
		String maxLine = (String)l.end.getPrev().getObject();
		
		l = new LinkedList();
		l.insert(maxLine);
		l.insert(minLine);
		l.insert(maxOrder);
		l.insert(minOrder);
		l.insert("0");
		l.insert("15");
		
		o = CoreOperations.loadPartialCountryOrder(l);
		System.out.println(o == null);
		if (o.getClass().equals("".getClass())) System.out.println((String)o);
		
		l = (LinkedList)o;
		System.out.println(l.getLength());
		temp = l.head;
		
		while (temp != null) {
			Order od = (Order) temp.getObject();
			System.out.println(od.getImage() + " " + od.getBrand() + " " + od.getProduct() +
					" " + od.getQuantity() + " " + od.getCountry() + " " + od.getTimestamp());
			temp = temp.getNext();
		}
		
		
		
	}
}
	
	

