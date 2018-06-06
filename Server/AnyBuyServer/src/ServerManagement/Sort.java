package ServerManagement;

import Object.LinkedList;
import Object.Node;
import Object.UserOrderHis;
import Object.Order;

public class Sort {
	
	public static LinkedList sortOrders (LinkedList l) {
		Order[] uohArray = new Order[l.getLength()];
		Node temp = l.head;
		int i = 0;
		while (temp != null) {
			uohArray[i] = (Order)temp.getObject();
			temp = temp.getNext();
			i++;
		}
		Order[] res = sortOrderArray(uohArray);
		LinkedList ll = new LinkedList();
		for (int j = res.length -1; j >= 0; j--) ll.insert(res[j]);
		
		return ll;
	}
	
	private static Order[] sortOrderArray(Order[] uoh) {
		if (uoh.length == 1) return uoh;
		
		int half = uoh.length/2;
		Order[] uohL = new Order[half];
		Order[] uohR = new Order[uoh.length - half];
		
		for (int i = 0; i < uohL.length; i++) uohL[i] = uoh[i];
		for (int i = 0; i < uohR.length; i++) uohR[i] = uoh[half + i];
		
		Order[] sortL = sortOrderArray(uohL);
		Order[] sortR = sortOrderArray(uohR);
		
		return merge(sortL, sortR);
	}
	
	private static Order[] merge(Order[] l, Order[] r) {
		Order[] res = new Order[l.length + r.length];
		int lPointer = 0, rPointer = 0;
		for (int i = 0; i < res.length; i++) {
			if (lPointer == l.length) {
				res[i] = r[rPointer];
				rPointer++;
			} else if (rPointer == r.length) {
				res[i] = l[lPointer];
				lPointer++;
			} else if (l[lPointer].getTimestamp().getTime() <= r[rPointer].getTimestamp().getTime()) {
				res[i] = l[lPointer];
				lPointer++;
			} else {
				res[i] = r[rPointer];
				rPointer++;
			}
		}
		return res;
	}

	public static LinkedList sortOrdersWithUOH (LinkedList l) {
		UserOrderHis[] uohArray = new UserOrderHis[l.getLength()];
		Node temp = l.head;
		int i = 0;
		while (temp != null) {
			uohArray[i] = (UserOrderHis)temp.getObject();
			temp = temp.getNext();
			i++;
		}
		UserOrderHis[] res = sortUOHArray(uohArray);
		LinkedList ll = new LinkedList();
		for (int j = res.length -1; j >= 0; j--) ll.insert(res[j]);
		
		return ll;
	}
	
	private static UserOrderHis[] sortUOHArray(UserOrderHis[] uoh) {
		if (uoh.length == 1) return uoh;
		
		int half = uoh.length/2;
		UserOrderHis[] uohL = new UserOrderHis[half];
		UserOrderHis[] uohR = new UserOrderHis[uoh.length - half];
		
		for (int i = 0; i < uohL.length; i++) uohL[i] = uoh[i];
		for (int i = 0; i < uohR.length; i++) uohR[i] = uoh[half + i];
		
		UserOrderHis[] sortL = sortUOHArray(uohL);
		UserOrderHis[] sortR = sortUOHArray(uohR);
		
		return merge(sortL, sortR);
	}
	
	private static UserOrderHis[] merge(UserOrderHis[] l, UserOrderHis[] r) {
		UserOrderHis[] res = new UserOrderHis[l.length + r.length];
		int lPointer = 0, rPointer = 0;
		for (int i = 0; i < res.length; i++) {
			if (lPointer == l.length) {
				res[i] = r[rPointer];
				rPointer++;
			} else if (rPointer == r.length) {
				res[i] = l[lPointer];
				lPointer++;
			} else if (l[lPointer].getOrder().getTimestamp().getTime() <= r[rPointer].getOrder().getTimestamp().getTime()) {
				res[i] = l[lPointer];
				lPointer++;
			} else {
				res[i] = r[rPointer];
				rPointer++;
			}
		}
		return res;
	}
	
}
