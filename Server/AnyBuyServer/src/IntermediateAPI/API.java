package IntermediateAPI;

import java.io.IOException;
import java.sql.Connection;

public class API {
	
	static Server s;
	static int failCounter = 0;
	
	public static void main (String[] args) {
		try {
			writeLog("Starting Server");
			s = new Server();
		} catch (IOException e) {
			failCounter++;
			writeLog("Server failed.");
			if (failCounter < 5) main(null);
			else System.exit(0);
		}
	}
	
	public static int getCommand (String str) {
		String[] strArr;
		if (str.length() >= 4) strArr = str.split("\\&");
		else return illegalInput();
		if (strArr[0].equalsIgnoreCase("reg")) return register(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("lgi")) return login(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("plo")) return placeOrder(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("gvr")) return giveRate(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("cco")) return cancelOrder(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("art")) return acceptRate(voidHead(strArr));
		else if (strArr[0].equalsIgnoreCase("adc")) return addCard(voidHead(strArr));
		else return illegalInput();
		
	}
	
	static int register (String[] str) {
		writeLog("Register");
		String[] str2 = str[0].split("\\?");
		String[] uInfo = str2[0].split("\\@");
		if (str2.length != 2 || uInfo.length != 2) return 0x1A01;
		Connection c = SQLControl.SQLOperation.getConnect("userInfo", "anybuy", "CMPS115.");
		String emailDomainCode = SQLControl.SQLOperation.readDatabase(c, "select code from domainCode"
				+ " where emailDomain='" + uInfo[1] + "'");
		if (emailDomainCode == null) emailDomainCode = UserManage.createDomainCode(c, uInfo[1]);
		System.out.println(emailDomainCode);
		return -0xFF;
	}
	
	static int login (String[] str) {
		writeLog("Login");
		return -0xFF;
	}
	
	static int placeOrder (String[] str) {
		writeLog("Place Order");
		return -0xFF;
	}
	
	static int giveRate (String[] str) {
		writeLog("Give Rate");
		return -0xFF;
	}
	
	static int cancelOrder (String[] str) {
		writeLog("Cancel Order");
		return -0xFF;
	}
	
	static int acceptRate (String[] str) {
		writeLog("Accept Rate");
		return -0xFF;
	}
	
	static int addCard (String[] str) {
		writeLog("Add Payment Method");
		return -0xFF;
	}
	
	static int illegalInput() {
		writeLog("Illegal Input.");
		return -0xFF;
	}
	
	static void writeLog (String str) {
		System.out.println(str);
	}
	
	static String[] voidHead (String [] str) {
		String[] res = new String[str.length - 1];
		for (int i = 1; i < str.length; i++) res[i - 1] = str[i];
		return res;
	}
}
