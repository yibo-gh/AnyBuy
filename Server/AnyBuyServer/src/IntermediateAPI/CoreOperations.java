package IntermediateAPI;

import java.sql.Connection;
import java.sql.SQLException;

import SQLControl.SQLOperation;

public class CoreOperations {

	static String register (String[] str) throws SQLException {
		writeLog("Register");
		if (str.length < 1) return "0x1A06";
		String[] str2 = str[0].split("\\?");
		String[] uInfo = str2[0].split("\\@");
		if (str2.length != 2 || uInfo.length != 2) return "0x1A01";
		if (uInfo[0].charAt(0) == '0' && uInfo[0].charAt(1) == 'x' && uInfo[0].length() == 6) return "0x1A01";
		Connection c = SQLControl.SQLOperation.getConnect("userInfo", "anybuy", "CMPS115.");
		String emailDomainCode = SQLControl.SQLOperation.readDatabase(c, "select code from domainCode"
				+ " where emailDomain='" + uInfo[1] + "'");
		if (emailDomainCode == null) {
			emailDomainCode = UserManage.createDomainCode(c, uInfo[1]);
		}
		if (emailDomainCode == "0x1A07") return "0x1A07";
		emailDomainCode = SQLControl.SQLOperation.readDatabase(c, "select code from domainCode"
				+ " where emailDomain='" + uInfo[1] + "'");
		String usr = SQLControl.SQLOperation.readDatabase(c, "select psc from " + emailDomainCode + " where name='" + uInfo[0] + "'");
		if (usr != null) return "0x1A08";
		int uid = SQLOperation.countLine(c, emailDomainCode) + 10000;
		String sql = "INSERT INTO " + emailDomainCode + "(name,psc,id) VALUES('" + uInfo[0] + "','" + str2[1] + "','" + uid + "');";
		SQLControl.SQLOperation.writeData(c, sql);
		SQLOperation.creatProfile(c, emailDomainCode + "" + uid);
		c.close();
		return "0x01";
	}
	
	static String login (String[] str) throws SQLException {
		writeLog("Login");
		String[] str2 = str[0].split("\\?");
		String[] uInfo = str2[0].split("\\@");
		Connection c = SQLControl.SQLOperation.getConnect("userInfo", "anybuy", "CMPS115.");
		String sql = "select code from domainCode where emailDomain='" + uInfo[1] + "'";
		String emailCode = SQLControl.SQLOperation.readDatabase(c, sql);
		sql = "select id from " + emailCode + " where name='" + uInfo[0] + "'";
		String uid = SQLControl.SQLOperation.readDatabase(c, sql);
		if (emailCode == null) return "0x1C01";
		sql = "select psc from " + emailCode + " where name='" + uInfo[0] + "'";
		System.out.println(sql);
		if (str2[1].equals(SQLControl.SQLOperation.readDatabase(c, sql)) ) {
			c.close();
			int authToken = (int) (Math.random() * 10 * 0xFFFF);
			// TODO improve authToken algorithm to make it has a high security level.
			c = SQLControl.SQLOperation.getConnect("accessLog", "anybuy", "CMPS115.");
			sql = "select token from authLog where uid='" + emailCode + uid + "'";
			String usrStatus = SQLControl.SQLOperation.readDatabase(c, sql);
			if (usrStatus == null) {
				sql = "insert into authLog (uid, authTime, token) values ('" + emailCode + uid + "','" + System.currentTimeMillis() + "','" + authToken + "');";
				SQLControl.SQLOperation.writeData(c, sql);
			}
			else {
				sql = "update authLog set authTime='" + System.currentTimeMillis() + "' where uid='" + emailCode + uid + "';" ;
				SQLControl.SQLOperation.writeData(c, sql);
				sql = "update authLog set token='" + authToken + "' where uid='" + emailCode + uid + "';" ;
				SQLControl.SQLOperation.writeData(c, sql);
			}
			String sessionID = emailCode + uid + "?" + authToken;
			System.out.println(authToken);
			return sessionID;
		} else {
			c.close();
			return "0x1C02";
		}
	}
	
	static String placeOrder (String[] str) {
		// plo&sessionID&<Country>?<Product>?<Brand>?<Image>?<Quantity>
		writeLog("Place Order");
		
		// verify session
		String uid = sessionVerify(str[0]);
		if (uid.length() == 6 && uid.charAt(0) == '0' && uid.charAt(1) == 'x') return uid;
		
		// get data for order
		String[] order = str[1].split("\\?");
		String country = order[0];
		String product = order[1];
		String brand = order[2];
		String image = order[3];
		String quantity = order[4];
		
		// make string for sql command
		Connection c = SQLControl.SQLOperation.getConnect("generalOrder", "anybuy", "CMPS115.");
		String value = "'" + product + "','" + brand + "','" + image + "','" + quantity + "'," + " NULL";
		String sql = "INSERT INTO " + country +" (Product, Brand, Image, Quantity, orderID) VALUES (" + value + ");"; 
		
		// make new table for country if needed
		String countryStatus = SQLControl.SQLOperation.readDatabase(c, "SELECT * FROM" + country);
		if (countryStatus == null) {
			SQLControl.SQLOperation.createCountryTable(c, country);
		}
		
		// insert data into table
		System.out.println(sql);
		System.out.println(SQLOperation.writeData(c, sql));
		
		//c.close();
		return null;
	}
	
	static String giveRate (String[] str) {
		writeLog("Give Rate");
		return null;
	}
	
	static String cancelOrder (String[] str) {
		writeLog("Cancel Order");
		return null;
	}
	
	static String acceptRate (String[] str) {
		writeLog("Accept Rate");
		return null;
	}
	
	static String addCard (String[] str) {
		//adc&snok10000?538847&yoona?lim&amex=375987654321001&1220?95064
		
		String uid = sessionVerify(str[0]);
		if (uid.length() == 6 && uid.charAt(0) == '0' && uid.charAt(1) == 'x') return uid;
		
		
		String[] name = str[1].split("\\?");
		String[] cardNum = str[2].split("\\=");
		String[] expInfo = str[3].split("\\?");
		
		Connection c = SQLOperation.getConnect(uid, "anybuy", "CMPS115.");
		String cardStatus = SQLControl.SQLOperation.readDatabase(c, "select token from payment where cardNumber='" + cardNum[1] + "'");
		if (cardStatus != null) return "0x1E01";
		
		cardStatus = validateCardInfo(name, cardNum, expInfo);
		if ( !cardStatus.equals("0x01") ) return cardStatus;
		
		String value = "'" + name[0] + "','" + name[1] + "','" + cardNum[0] + "','" + cardNum[1] + "','" + expInfo[0] + "','" + expInfo[1] + "'";
		String sql = "INSERT INTO payment(fn, ln, issuer, cardNumber, exp, zip) VALUES(" + value + ");"; 
		System.out.println(sql);
		System.out.println(SQLOperation.writeData(c, sql));
		return "0x01";
	}
	
	private static String validateCardInfo(String[] name, String[] card, String[] exp) {
		return "0x01";
	}
	
	static String sessionVerify (String sessionID) {
		String[] veri = sessionID.split("\\?");
		Connection c = SQLControl.SQLOperation.getConnect("accessLog", "anybuy", "CMPS115.");
		String sql = "select token from authLog where uid='" + veri[0] + "'";
		String res = SQLOperation.readDatabase(c, sql);
		if (!veri[1].equals(res)) return "0x1D01";
		sql = "select authTime from authLog where uid='" + veri[0] + "'";
		Long l = Long.parseLong(SQLOperation.readDatabase(c, sql));
		if (System.currentTimeMillis() - l > 0x927C0 || System.currentTimeMillis() < l) return "0x1D02";
		sql = "update authLog set authTime='" + System.currentTimeMillis() + "' where uid='" + veri[0] + "';" ;
		SQLControl.SQLOperation.writeData(c, sql);
		return veri[0];
	}
	
	static String illegalInput() {
		writeLog("Illegal Input.");
		return null;
	}
	
	static void writeLog (String str) {
		System.out.println(str);
	}
	
	
}
