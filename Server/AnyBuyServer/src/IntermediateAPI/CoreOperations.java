package IntermediateAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import Object.Address;
import Object.Card;
import Object.LinkedList;
import Object.Node;
import Object.Order;
import Object.User;
import Object.imageRequest;
import ServerManagement.FileRecivier;
import SQLControl.SQLOperation;

public class CoreOperations {

	private static FileRecivier server;

	static String register (LinkedList ll) throws SQLException {
		// This linked list should have length of 1. The Node should contains a user object.
		writeLog("Register");
		Object o = ll.head.getObject();
		if (!o.getClass().equals(new User().getClass())) return "0x1002";
		User u = (User) o;
		Connection c = SQLControl.SQLOperation.getConnect("userInfo");
		String emailDomainCode = SQLControl.SQLOperation.readDatabase(c, "select code from domainCode"
				+ " where emailDomain='" + u.getDomain() + "'");
		if (emailDomainCode == null) {
			emailDomainCode = ServerManagement.UserManage.createDomainCode(c, u.getDomain());
		}
		if (emailDomainCode == "0x1A07") return "0x1A07";
		emailDomainCode = SQLControl.SQLOperation.readDatabase(c, "select code from domainCode"
				+ " where emailDomain='" + u.getDomain() + "'");
		String usr = SQLControl.SQLOperation.readDatabase(c, "select psc from " + emailDomainCode + " where name='" + u.getUserName() + "'");
		if (usr != null) return "0x1A08";
		int uid = SQLOperation.countLine(c, emailDomainCode) + 10000;
		String sql = "INSERT INTO " + emailDomainCode + "(name,psc,id) VALUES('" + u.getUserName() + "','" + u.getPassword() + "','" + uid + "');";
		SQLControl.SQLOperation.updateData(c, sql);
		SQLOperation.createProfile(c, emailDomainCode + "" + uid);
		c.close();
		return "0x01";
	}
	
	static String login (LinkedList ll) throws SQLException {
		// This linked list should have length of 1. The Node should contains a user object.
		writeLog("Login");
		Object o = ll.head.getObject();
		if (!o.getClass().equals(new User().getClass())) return "0x1002";
		User u = (User) o;
		Connection c = SQLControl.SQLOperation.getConnect("userInfo");
		String sql = "select code from domainCode where emailDomain='" + u.getDomain() + "'";
		String emailCode = SQLControl.SQLOperation.readDatabase(c, sql);
		sql = "select id from " + emailCode + " where name='" + u.getUserName() + "'";
		String uid = SQLControl.SQLOperation.readDatabase(c, sql);
		if (emailCode == null) return "0x1C01";
		sql = "select psc from " + emailCode + " where name='" + u.getUserName() + "'";
		System.out.println(sql);
		if (u.getPassword().equals(SQLControl.SQLOperation.readDatabase(c, sql)) ) {
			c.close();
			int authToken = (int) (Math.random() * 10 * 0xFFFF);
			// TODO improve authToken algorithm to make it has a high security level.
			c = SQLControl.SQLOperation.getConnect("accessLog");
			sql = "select token from authLog where uid='" + emailCode + uid + "'";
			String usrStatus = SQLControl.SQLOperation.readDatabase(c, sql);
			if (usrStatus == null) {
				sql = "insert into authLog (uid, authTime, token) values ('" + emailCode + uid + "','" + System.currentTimeMillis() + "','" + authToken + "');";
				SQLControl.SQLOperation.updateData(c, sql);
			}
			else {
				sql = "update authLog set authTime='" + System.currentTimeMillis() + "' where uid='" + emailCode + uid + "';" ;
				SQLControl.SQLOperation.updateData(c, sql);
				sql = "update authLog set token='" + authToken + "' where uid='" + emailCode + uid + "';" ;
				SQLControl.SQLOperation.updateData(c, sql);
			}
			String sessionID = emailCode + uid + "?" + authToken;
			System.out.println(authToken);
			return sessionID;
		} else {
			c.close();
			return "0x1C02";
		}
	}
	
	static Object placeOrder (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes at least 2 Node. 
		 * The first Node should contains sessionID.
		 * Starting from the second node, each of them should includes an Order object.
		 * plo&sessionID&<Country>?<Product>?<Brand>?<Image>?<Quantity>
		 */
		
		writeLog("Place Order");
		
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		

		Node temp = ll.head;
		LinkedList orderList = new LinkedList();
		temp = ll.head;
		while (temp != null){
			Object o = temp.getObject();
			if (!o.getClass().equals(new Order().getClass())) return "0x1002";
			Order od = (Order)o;
			orderList.insert(od);
			temp = temp.getNext();
		}
		
		LinkedList imgReq = new LinkedList();
		boolean hasImg = false;
		
		temp = orderList.head;
		while (temp != null) {
			Order obj = (Order)temp.getObject();
			long time = System.currentTimeMillis();
			
			// make string for INSERT buy order into generalOrder
			Connection c = SQLControl.SQLOperation.getConnect("generalOrder");
			
			// make new table for country if needed
			String countryStatus = SQLControl.SQLOperation.readDatabase(c, "SELECT * FROM" + obj.getCountry());
			if (countryStatus == null) {
				SQLControl.SQLOperation.createCountryTable(c, obj.getCountry());
			}
			
			String orderID = obj.getCountry() + (SQLOperation.countLine(c, obj.getCountry()) + 10000);
			boolean imageExist = (!obj.getImage().equals("") );
			
			String value = "'" + obj.getProduct() + "','" + obj.getBrand() + "','" + imageExist + "','" + obj.getQuantity() + "','" + time + "','" + orderID + "'";
			String sql = "INSERT INTO " + obj.getCountry() +" (Product, Brand, Image, Quantity, orderTime, orderID) VALUES (" + value + ");"; 
			
			
			// insert data into table
			System.out.println(SQLOperation.updateData(c, sql));
			
			// get orderID that was just INSERT'ed
			
			// make string for INSERT orderID into user's account
			c.close();
			c = SQLControl.SQLOperation.getConnect(uid);
			sql = "INSERT INTO `order` (`orderID`) VALUES ('" + orderID + "');";
			System.out.println(SQLOperation.updateData(c, sql));

			c.close();
			if (imageExist) {
//				String imageRes = acceptImage(obj.getImage(), orderID);
//				if(!imageRes.equalsIgnoreCase("0x01")) return imageRes;
				imgReq.insert(new imageRequest(obj.getImage(), orderID));
				hasImg = true;
			}
			temp = temp.getNext();
		}
		if (hasImg) return imgReq;
		
		return "0x01";
	}
	
	public static String acceptImage(String img, String orderID) {
		System.out.println("image process started.");
		try {
            ServerManagement.Task.setID(orderID);
            ServerManagement.Task.setImage(img);
			server = new FileRecivier();
			System.out.println("Server Started");
            server.load();
            System.out.println("load file success");
        } catch (Exception e) {  
            return "0x1F04";
        }
		return "0x01";
	}
	
	static Object loadPersonalOrder (LinkedList ll) throws SQLException {
		// TODO Write this section.
		writeLog("Load Order");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return null;
	}

	static Object loadCountryOrder (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes 2 Nodes. 
		 * The First Node should contains sessionID.
		 * The second Node should contains country code.
		 * This function is different from loadPersonalOrder() function in profile page.
		 * plo&sessionID&<Country>?<Product>?<Brand>?<Image>?<Quantity>
		 */
		
		// ldl&sessionID&<Country>
		writeLog("Load Order List");
		
		// verify session
		
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		Node temp = ll.head;
		
		if (!temp.getObject().getClass().equals("".getClass())) return "0x1002";
		
		String country = (String)temp.getObject();
		
		// load orders from country table
		Connection c = SQLOperation.getConnect("generalOrder");
		String sql = "SELECT Product, Brand, Quantity, orderID, orderTime FROM " + country;
		ResultSet rs = SQLOperation.readDatabaseRS(c, sql);
		LinkedList res = generateResWithRS(rs, new Order());
		c.close();
		
		if (res.head == null) return "0x1F03"; // Country table not found
		
		return res;
	}
	
	static Object cancelOrder (LinkedList ll) throws SQLException {
		// TODO
		writeLog("Cancel Order");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return "0x01";
	}
	
	static String giveRate (LinkedList ll) throws SQLException {
		writeLog("Give Rate");
		// TODO
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return null;
	}
	
	static String acceptRate (LinkedList ll) throws SQLException {
		writeLog("Accept Rate");
		// TODO
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return null;
	}
	
	static String addCard (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes at least 2 Node. 
		 * The first Node should contains sessionID.
		 * Starting from the second node, each of them should includes a card Object.
		 * adc&<sessionID>&<FN>?<LN>&<issuer>=<cardNum>?<zip>
		 */
		
		// verify session
		
		writeLog("Add Card.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		Node temp = ll.head;
		
		LinkedList cardList = new LinkedList();
		boolean cardExist = false;
		while (temp != null){
			if (!temp.getObject().getClass().equals(new Card().getClass())) return "0x1002";
			Card card = (Card)temp.getObject();
			cardList.insert(card);
			
			Connection c = SQLOperation.getConnect(uid);
			String cardStatus = SQLControl.SQLOperation.readDatabase(c, "select issuer from payment where cardNumber='" + card.getCardNum() + "'");
			if (cardStatus != null) {
				c.close();
				cardExist = true;
			}
			cardStatus = validateCardInfo(card.getFN(), card.getLN(), card.getCardNum(), card.getZip(), card.getExp());
			if ( !cardStatus.equals("0x01") ) {
				c.close();
				return cardStatus;
			}
			
			String value = "'" + card.getFN() + "','" + card.getLN() + "','" + card.getIssuser() + "','" + card.getCardNum() + "','" + card.getExp() + "','" + card.getZip() + "'";
			String sql = "INSERT INTO payment(fn, ln, issuer, cardNumber, exp, zip) VALUES(" + value + ");"; 
			System.out.println(SQLOperation.updateData(c, sql));
			c.close();
			temp = temp.getNext();
		}
		if (cardExist) return "0x1E01";
		else return "0x01";
	}
	
	private static String validateCardInfo(String fn, String ln, String cardNum, String zip, String exp) {
		return "0x01";
	}

	static Object loadCard (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes 1 Node. 
		 * The Node should contains sessionID.
		 * ldc&<sessionID>
		 */
		
		writeLog("Load Card.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		Connection c = SQLOperation.getConnect(uid);
		String sql = "SELECT * FROM payment";
		ResultSet rs = SQLOperation.readDatabaseRS(c, sql);
		LinkedList res = generateResWithRS(rs, new Card());
		c.close();
		if (res.head == null) res.insert("0x1E04");
		return res;
	}
	
	static String deleteCard (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes at least 2 Nodes. 
		 * The first Node should contains sessionID.
		 * Starting from the second Node, each Node should include a card number to be removed.
		 * dlc&<sessionID>&<cardNum>
		 */
		
		writeLog("Delete Card.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		Node temp = ll.head;
		
		boolean cardNotExist = false;
		
		while (temp != null) {
			if (!temp.getObject().getClass().equals("".getClass())) return "0x1002";
			String cardNum = (String) temp.getObject();
			
			String sql = "delete from payment where cardNumber=" + cardNum + ";";
			Connection c = SQLControl.SQLOperation.getConnect(uid);
			String cardStatus = SQLControl.SQLOperation.readDatabase(c, "select issuer from payment where cardNumber='" + cardNum + "'");
			if (cardStatus == null) {
				c.close();
				cardNotExist = true;
			} else {
				String res = SQLControl.SQLOperation.updateData(c, sql);
				c.close();
				if (res != "UPS") return res;
			}
			temp = temp.getNext();
		}
		if (cardNotExist) return "0x1E04";
		return "0x01";
	}
	
	static String addAddress(LinkedList ll) throws SQLException {
		/**
		 * This LinkedList should includes at least 2 Nodes. 
		 * The first Node should contains sessionID.
		 * Starting from the second Node, each Node should includes a address object.
		 * dlc&<sessionID>&<address>
		 */
		
		writeLog("Add Address.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		Node temp = ll.head;
		
		boolean addressExist = false;
		
		while (temp != null) {
			if (!(temp.getObject().getClass().equals(new Address().getClass()))) return "0x1002";
			Address a = (Address) temp.getObject();
			Connection c = SQLOperation.getConnect(uid);
			String addStatus = SQLControl.SQLOperation.readDatabase(c, "select line2 from address where line1='" + a.getL1() + "'");
			if (addStatus != null) {
				c.close();
				addressExist = true;
			}
			
			String value = "('" + a.getFN() + "','" + a.getLN() + "','" + a.getCom() + "','" + a.getL1() + "','" + a.getL2() + "','" + a.getCity() + "','" + a.getState() + "','" + a.getZip() + "')";
			String sql = "INSERT INTO address(fn, ln, company, line1, line2, city, state, zip) VALUES" + value + ";";
			System.out.println(SQLOperation.updateData(c, sql));
			c.close();
			temp = temp.getNext();
		}
		
		if (addressExist) return "0x1E06";
		return "0x01";
	}
	
	static Object loadAddress (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes 1 Node. 
		 * The Node should contains sessionID.
		 * ldc&<sessionID>
		 */
		
		writeLog("load Address.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		Connection c = SQLOperation.getConnect(uid);
		String sql = "SELECT * FROM address";
		ResultSet rs = SQLOperation.readDatabaseRS(c, sql);
		LinkedList res = generateResWithRS(rs, new Address());
		c.close();
		if (res.head == null) return "0x1E05";
		return res;
	}
	
	static String deleteAddress(LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes at least 2 Nodes. 
		 * The first Node should contains sessionID.
		 * Starting from the second Node, each Node should include a line1 to be removed.
		 * dla&<sessionID>&<L1>
		 */
		
		writeLog("Delete Address.");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		Node temp = ll.head;
		
		boolean addressNotExist = false;
		
		while (temp != null) {
			if (!temp.getObject().getClass().equals("".getClass())) return "0x1002";
			String line1 = (String)temp.getObject();
			Connection c = SQLControl.SQLOperation.getConnect(uid);
			String cardStatus = SQLControl.SQLOperation.readDatabase(c, "select zip from address where line1='" + line1 + "'");
			if (cardStatus == null) {
				c.close();
				addressNotExist = true;
			} else {
				String sql = "delete from address where line1='" + line1 + "';";
				String res = SQLControl.SQLOperation.updateData(c, sql);
				c.close();
				if (res != "UPS") return res;
			}
			temp = temp.getNext();
		}
		if (addressNotExist) return "0x1E07";
		return "0x01";
	}
	
	private static LinkedList generateResWithRS(ResultSet rs, Object o) throws SQLException {
		if (o.getClass().equals(new Address().getClass())) return addressList(rs);
		if (o.getClass().equals(new Card().getClass())) return cardList(rs);
		if (o.getClass().equals(new Order().getClass())) return orderList(rs);
		return null;
	}
	
	private static LinkedList addressList(ResultSet rs) throws SQLException {
		LinkedList ll = new LinkedList();
		while (rs.next()) {
			Address a = new Address(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
			ll.insert(a);
		}
		return ll;
	}
	
	private static LinkedList cardList (ResultSet rs) throws SQLException {
		LinkedList ll = new LinkedList();
		while (rs.next()) {
			Card c = new Card(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6));
			ll.insert(c);
		}
		return ll;
	}
	
	private static LinkedList orderList (ResultSet rs) throws SQLException {
		LinkedList ll = new LinkedList();
		while (rs.next()) {
			Order o = new Order(rs.getString(1), rs.getString(2), rs.getInt(3),
					("" + rs.getString(4).charAt(0) + rs.getString(4).charAt(1) + rs.getString(4).charAt(2)),
					rs.getString(4), new Timestamp(rs.getLong(5)));
			ll.insert(o);
		}
		return ll;
	}
	
	static String sessionVerify (String sessionID) throws SQLException {
		if (sessionID.equalsIgnoreCase("") || sessionID.equalsIgnoreCase(null)) return "0x1D03";
		String[] veri = sessionID.split("\\?");
		Connection c = SQLControl.SQLOperation.getConnect("accessLog");
		String sql = "select token from authLog where uid='" + veri[0] + "'";
		String res = SQLOperation.readDatabase(c, sql);
		if (!veri[1].equals(res)) {
			c.close();
			return "0x1D01";
		}
		sql = "select authTime from authLog where uid='" + veri[0] + "'";
		Long l = Long.parseLong(SQLOperation.readDatabase(c, sql));
		if (System.currentTimeMillis() - l > 0x927C0 || System.currentTimeMillis() < l) return "0x1D02";
		sql = "update authLog set authTime='" + System.currentTimeMillis() + "' where uid='" + veri[0] + "';" ;
		SQLControl.SQLOperation.updateData(c, sql);
		c.close();
		return veri[0];
	}
	
	static String illegalInput() {
		writeLog("Illegal Input.");
		return null;
	}
	
	/*
	 * In functions, do the following to verify sessionID
	 * String uid = checkSession(ll);
	 * if (!verifySessionRes(uid, ll)) return uid;
	 */
	
	static String checkSession(LinkedList ll) throws SQLException {
		Node temp = ll.head;
		if (!temp.getObject().getClass().equals("".getClass())) return "0x1003";
		return sessionVerify((String)temp.getObject());
	}
	
	static boolean verifySessionRes(String uid, LinkedList ll) {
		if (uid.length() == 6 && uid.charAt(0) == '0' && uid.charAt(1) == 'x') return false;
		API.voidHead(ll);
		return true;
	}
	
	public static void writeLog (String str) {
		System.out.println(str);
	}
	
}
