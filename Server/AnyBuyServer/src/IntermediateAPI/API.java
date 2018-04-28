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
		if (strArr[0].equalsIgnoreCase("reg")) return CoreOperations.register(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("lgi")) return CoreOperations.login(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("plo")) return CoreOperations.placeOrder(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("gvr")) return CoreOperations.giveRate(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("cco")) return CoreOperations.cancelOrder(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("art")) return CoreOperations.acceptRate(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("adc")) return CoreOperations.addCard(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("ldc")) return CoreOperations.loadCard(voidHead(strArr));
//		else if (strArr[0].equalsIgnoreCase("ada")) return CoreOperations.addAddress(voidHead(strArr));
		else return CoreOperations.illegalInput();
		
	}
	

	static String[] voidHead (String [] str) {
		String[] res = new String[str.length - 1];
		for (int i = 1; i < str.length; i++) res[i - 1] = str[i];
		return res;
	}
}
