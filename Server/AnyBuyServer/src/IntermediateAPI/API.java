package IntermediateAPI;

import java.io.IOException;
import java.sql.SQLException;

import Object.LinkedList;
import ServerManagement.Server;

public class API {
	
	static Server s;
	static int failCounter = 0;
	
	public static void main (String[] args) {
		try {
			CoreOperations.writeLog("Starting Server");
			s = new Server();
		} catch (IOException e) {
			failCounter++;
			CoreOperations.writeLog("Server failed.");
			if (failCounter < 5) main(null);
			else System.exit(0);
		}
	}
	
	public static Object getCommand (LinkedList ll) throws SQLException {
		if (ll == null || ll.head == null) return CoreOperations.illegalInput();
		switch ((String)ll.head.getObject()) {
			case "reg": return CoreOperations.register(voidHead(ll));
			case "lgi": return CoreOperations.login(voidHead(ll));
			case "plo": return CoreOperations.placeOrder(voidHead(ll));
			case "ldo": return CoreOperations.loadCountryOrder(voidHead(ll));
			case "cco": return CoreOperations.cancelOrder(voidHead(ll));
			case "ldl": return CoreOperations.loadPersonalOrder(voidHead(ll));
			case "gvr": return CoreOperations.giveRate(voidHead(ll));
			case "art": return CoreOperations.acceptRate(voidHead(ll));
			case "adc": return CoreOperations.addCard(voidHead(ll));
			case "ldc": return CoreOperations.loadCard(voidHead(ll));
			case "dtc": return CoreOperations.deleteCard(voidHead(ll));
			case "ada": return CoreOperations.addAddress(voidHead(ll));
			case "lda": return CoreOperations.loadAddress(voidHead(ll));
			case "dta": return CoreOperations.deleteAddress(voidHead(ll));
			case "lop": return CoreOperations.loadPartialCountryOrder(voidHead(ll));
			default: return CoreOperations.illegalInput();
		}
	}
	

	static LinkedList voidHead (LinkedList ll) {
		ll.delete(1);
		return ll;
	}
}
