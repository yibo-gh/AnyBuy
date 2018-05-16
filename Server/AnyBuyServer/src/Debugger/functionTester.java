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
		
		minOrder = ((Order) temp.getObject()).getImage();
		
		while (temp.getNext().getNext() != null) {
			temp = temp.getNext();
		}
		maxOrder = ((Order) temp.getPrev().getObject()).getImage();
		
		String minLine = (String)l.end.getObject();
		String maxLine = (String)l.end.getPrev().getObject();
		
		l = new LinkedList();
		l.insert("18");
		l.insert("7");
		l.insert("KR91008");
		l.insert("KR61000");
		l.insert("0");
		l.insert("10");
		
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
	
	

