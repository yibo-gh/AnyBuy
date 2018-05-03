package IntermediateAPI;

import java.io.IOException;
import java.sql.SQLException;

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
	
	public static String getCommand (String str) throws SQLException {
		String[] strArr;
		if (str.length() >= 4) strArr = str.split("\\&");
		else return CoreOperations.illegalInput();
		switch (strArr[0]) {
			case "reg": return CoreOperations.register(voidHead(strArr));
			case "lgi": return CoreOperations.login(voidHead(strArr));
			case "plo": return CoreOperations.placeOrder(voidHead(strArr));
			case "ldo": return CoreOperations.loadOrder(voidHead(strArr));
			case "gvr": return CoreOperations.giveRate(voidHead(strArr));
			case "cco": return CoreOperations.cancelOrder(voidHead(strArr));
			case "art": return CoreOperations.acceptRate(voidHead(strArr));
			case "adc": return CoreOperations.addCard(voidHead(strArr));
			case "ldc": return CoreOperations.loadCard(voidHead(strArr));
			case "dtc": return CoreOperations.deleteCard(voidHead(strArr));
			case "ada": return CoreOperations.addAddress(voidHead(strArr));
			case "lda": return CoreOperations.loadAddress(voidHead(strArr));
			case "dta": return CoreOperations.deleteAddress(voidHead(strArr));
			default: return CoreOperations.illegalInput();
		}
	}
	

	static String[] voidHead (String [] str) {
		String[] res = new String[str.length - 1];
		for (int i = 1; i < str.length; i++) res[i - 1] = str[i];
		return res;
	}
}
