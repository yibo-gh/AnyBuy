package IntermediateAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import Object.Address;
import Object.Card;
import Object.InitialOrder;
import Object.LinkedList;
import Object.Node;
import Object.Order;
import Object.Offer;
import Object.User;
import Object.imageRequest;
import Object.UserOrderHis;
import Object.UserShippingInfo;
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
			if (!o.getClass().equals(new InitialOrder().getClass())) return "0x1002";
			InitialOrder io = (InitialOrder)o;
			orderList.insert(io);
			temp = temp.getNext();
		}
		
		LinkedList imgReq = new LinkedList();
		boolean hasImg = false;
		
		temp = orderList.head;
		while (temp != null) {
			InitialOrder io = (InitialOrder)temp.getObject();
			Order obj = io.getOrder();
			long time = System.currentTimeMillis();
			
			// make string for INSERT buy order into generalOrder
			Connection c = SQLControl.SQLOperation.getConnect("generalOrder");
			
			// make new table for country if needed
			String countryStatus = SQLControl.SQLOperation.readDatabase(c, "SELECT * FROM " + obj.getCountry());
			if (countryStatus == null) {
				SQLControl.SQLOperation.createCountryTable(c, obj.getCountry());
			}
			
			int lastOrderNum = getLastOrderNum(c, obj.getCountry());
			String orderID = obj.getCountry() + (lastOrderNum + 1);
			
			boolean imageExist = (!obj.getImage().equals("") );
			
			String value = "'" + obj.getProduct() + "','" + obj.getBrand() + "','" + obj.getQuantity() + "','" + imageExist +  "','" + time + "','" + orderID + "','0','" + uid + "'";
			String sql = "INSERT INTO `generalOrder`.`" + obj.getCountry() + "` (`Product`, `Brand`, `Quantity`, `Image`, `orderTime`, `orderID`, `orderStatus`,`uid`) VALUES (" + value + ");"; 
//			System.out.println(value);
			// insert data into table
			System.out.println(SQLOperation.updateData(c, sql));
			
			// get orderID that was just INSERT'ed
			
			// make string for INSERT orderID into user's account
			c.close();
			
			UserShippingInfo usi = io.getShippingInfo();
			c = SQLControl.SQLOperation.getConnect(uid);
			sql = "INSERT INTO `order` (`orderID`, `orderStatus`,`line1`,`city`,`state`,`zip`,`card`)"
					+ " VALUES ('" + orderID + "','0','" + usi.getLine1() + "','" + usi.getCity() + "','" 
					+ usi.getState() + "','" + usi.getZip() + "','" + usi.getCard() + "');";
			System.out.println(SQLOperation.updateData(c, sql));

			c.close();
			if (imageExist) {
				System.out.println(imageExist);
				File directory = new File("/Users/yiboguo/Desktop/serverRecieved/");
				if(!directory.exists()) {
					directory.mkdir();
				}
				BufferedImage image;
				File file = new File("/Users/yiboguo/Desktop/serverRecieved/" + orderID + ".jpg");
				try {
					URL url = new URL("https://yg-home.site/anybuy/KR/asiana.jpg");
					image = ImageIO.read(url);
					ImageIO.write(image, "jpg", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				String imageRes = acceptImage(obj.getImage(), orderID);
//				if(!imageRes.equalsIgnoreCase("0x01")) return imageRes;
				imgReq.insert(new imageRequest(obj.getImage(), orderID));
				hasImg = true;
			}
			temp = temp.getNext();
		}
		if (hasImg) return "0x01";
		
		return "0x01";
	}
	
	private static int getLastOrderNum(Connection c, String countryCode) throws SQLException {
		int SQLTotalLine = SQLOperation.countLine(c, countryCode);
		if (SQLTotalLine != 0) {
			String lastID = SQLControl.SQLOperation.readDatabase(c, "SELECT orderID FROM " + countryCode + " limit " + (SQLTotalLine - 1) + ",1");
			String temp1 = "";
			for (int i = 2; i < lastID.length(); i++) {
				if (i == 2 && Character.isDigit(lastID.charAt(i))) temp1 += lastID.charAt(2);
				else if (i > 2) temp1 += lastID.charAt(i);
			}
			return Integer.parseInt(temp1);
		} else if (countryCode.length() == 2) return 9999999;
		else if (countryCode.length() == 3) return 999999;
		else return 9999;
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
	
	static Object loadSold (LinkedList ll) throws SQLException{

		/**
		 * This LinkedList should includes one Node. 
		 * The First Node should contains sessionID.
		 * This function is different from loadCountryOrder() function in profile page.
		 */
		
		writeLog("Load Sold Record");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return loadPersonalOrderHistory(uid, "offer");
	}
	
	static Object loadBuy (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes one Node. 
		 * The First Node should contains sessionID.
		 * This function is different from loadCountryOrder() function in profile page.
		 */
		
		writeLog("Load Personal Order");
		
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		return loadPersonalOrderHistory(uid, "order");
	}
	
	private static LinkedList loadPersonalOrderHistory(String uid, String type) throws SQLException {
		Connection c = SQLOperation.getConnect(uid);
		String sql = "SELECT orderID, " + type + "Status FROM `" + type + "`";
		ResultSet rs = SQLOperation.readDatabaseRS(c, sql);
		
		LinkedList realLinkedList = new LinkedList();
		while (rs != null && rs.next()) {
			
			String orderID = rs.getString(1);
			String country = getCountryCodeWithOrderID(orderID);
			int orderStatus = rs.getInt(2);
			Connection c2 = SQLOperation.getConnect("generalOrder");
			sql = "SELECT Product, Brand, Quantity, orderID, orderTime FROM " + country + " where `orderID` = '" + orderID + "';";
			ResultSet rs2 = SQLOperation.readDatabaseRS(c2, sql);
			LinkedList temp2 = generateResWithRS(rs2, new Order());
			UserOrderHis uoh = convertOrderToUserOrderHis((Order)(temp2.head.getObject()), orderStatus);
			realLinkedList.insert(uoh);
			c2.close();
		}
		c.close();
		if (realLinkedList.getLength() > 1) realLinkedList = sortOrdersWithUOH(realLinkedList);
		return realLinkedList;
	}
	
	private static LinkedList sortOrdersWithUOH (LinkedList l) {
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

	static Object loadCountryOrder (LinkedList ll) throws SQLException {
		
		/**
		 * This LinkedList should includes 2 Nodes. 
		 * The First Node should contains sessionID.
		 * The second Node should contains country code.
		 * This function is different from loadPersonalOrder() function in profile page.
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
	
	public static Object loadPartialCountryOrder(LinkedList ll) throws SQLException {
		/**
		 * In this function, the input LinkedList may have different status.
		 * For initial inquiry, the LinkedList should includes 3 Nodes.
		 * The First Node, as usually, is sessionID.
		 * The second Node, should includes the country code the client wish to pull.
		 * The third Node is how much order info the client want. 
		 * So, initial inquiries should looks like: <sessionID>&<countryCode>&<amount>
		 * 
		 * However, things may be changed from the second inquiry.
		 * The second Node should be the largest order number in last pull.
		 * The third Node should be the smallest order number in last pull.
		 * The fourth Node should tell if the client want older orders or newer orders.
		 * The fifth Node should tells how much.
		 * As a conclusion, the LinkedList received should looks like this:
		 * <sessionID>&<lastMaxLineNum>&<lastMinLineNum>&<lastMaxOrderNum>&<lastMinOrderNum>&<olderOrNewer>&<howMuch>
		 * For older and newer part, please use 1 for Newer and 0 for Older.
		 * 
		 * If a client want order info from another country, please follow the instruction of initial inquiry.
		 */
		
		writeLog("Load partial order.");
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		if (ll.getLength() == 2) return initialLoad(ll);
		else if (ll.getLength() == 6) return continueLoad(ll);
		else return null;
	}
	
	private static Object initialLoad(LinkedList ll) throws SQLException {
		//<countryCode>&<amount>
		writeLog("Initialed partial order.");
		if (ll.getLength() !=  2 || !ll.head.getObject().getClass().equals(("".getClass()))
				|| !ll.head.getObject().getClass().equals(("".getClass())))
			return "0x1002";
		String country = ll.head.getObject().toString();
		int orderQuaRequested = Integer.parseInt(ll.head.getNext().getObject().toString());
		Connection c = SQLOperation.getConnect("generalOrder");
		int totalOrder = SQLOperation.countLine(c, country);
		if (totalOrder == 0) return "0x1F06";
		String sql;
		if (totalOrder < orderQuaRequested) sql = "select Product, Brand, Quantity, orderID, orderTime from " + country;
		else sql = "select Product, Brand, Quantity, orderID, orderTime from " 
				+ country + " limit " + (totalOrder - orderQuaRequested) + "," + totalOrder + ";";
		ResultSet rs = SQLOperation.readDatabaseRS(c, sql);
		LinkedList res = generateResWithRS(rs, new Order());
		res.insert("" + totalOrder); //Max row number
		res.insert("" + (totalOrder - orderQuaRequested -1)); //Min row number, 1 over the real min
		c.close();
		return res;
	}
	
	private static Object continueLoad(LinkedList ll) throws SQLException {
		//<lastMaxLineNum>&<lastMinLineNum>&<lastMaxOrderNum>&<lastMinOrderNum>&<olderOrNewer>&<howMuch>
		writeLog("Continued partial order.");
		
		if (ll.getLength() != 6) return "0x1000";
		Node temp = ll.head;
		Object o1 = temp.getObject();
		temp = temp.getNext();
		Object o2 = temp.getObject();
		temp = temp.getNext();
		Object o3 = temp.getObject();
		temp = temp.getNext();
		Object o4 = temp.getObject();
		temp = temp.getNext();
		Object o5 = temp.getObject();
		temp = temp.getNext();
		Object o6 = temp.getObject();
		
		if (!o1.getClass().equals("".getClass()) || !o2.getClass().equals("".getClass())
				|| !o3.getClass().equals("".getClass()) || !o4.getClass().equals("".getClass())
				|| !o5.getClass().equals("".getClass())|| !o6.getClass().equals("".getClass()))
			return "0x1002";
		
		int lastMaxLineNum = Integer.parseInt((String) o1);
		int lastMinLineNum = Integer.parseInt((String) o2);
		String lastMaxOrderNum = (String)o3;
		String lastMinOrderNum = (String)o4;
		int olderOrNewer = Integer.parseInt((String)o5);
		int howMuch = Integer.parseInt((String)o6);
		
		String countryCode = getCountryCodeWithOrderID(lastMaxOrderNum);
		Connection c = SQLOperation.getConnect("generalOrder");
		
		ResultSet rs = null;
		
		// Newer Order
		if (olderOrNewer == 1) {
			int totalLine = SQLOperation.countLine(c, countryCode);
			if (lastMaxLineNum == totalLine) return "0x1FA3";
			String sql = "seleect orderID from " + countryCode + " limit " + lastMaxLineNum + ";";
			rs = SQLOperation.readDatabaseRS(c, sql);
			// get true line number which has last max order number.
			int trueLine = lastMaxLineNum;
			while (rs != null && rs.next()) {
				String temp1 = rs.getString(1);
				if (getOrderNumberWithOrderID(countryCode, temp1)
						> getOrderNumberWithOrderID(countryCode, lastMaxOrderNum))
					trueLine--;
				else break;
			}
			
			if (trueLine + howMuch > totalLine) {
				sql = "select Product, Brand, Quantity, orderID, orderTime from "
						+ countryCode + " limit " + trueLine + "," + (totalLine - howMuch) + ";";
				rs = SQLOperation.readDatabaseRS(c, sql);
				LinkedList res = generateResWithRS(rs, new Order());
				res.insert("" + trueLine); //Max row number
				res.insert("" + (trueLine - howMuch -1)); //Min row number, 1 over the real min
				c.close();
				if (res.head == null) return "0x1FA1"; // Country table not found
				return res;
				
			} else {
				sql = "select Product, Brand, Quantity, orderID, orderTime from "
						+ countryCode + " limit " + (trueLine) + "," + (howMuch) + ";";
				ResultSet r = SQLOperation.readDatabaseRS(c, sql);
				LinkedList res = generateResWithRS(r, new Order());
				res.insert("" + trueLine); //Max row number
				res.insert("" + (trueLine - howMuch -1)); //Min row number, 1 over the real min
				c.close();
				c.close();
				if (res.head == null) return "0x1FA1"; // Country table not found
				return res;
			}
		}
		
		// Go older orders
		else if (olderOrNewer == 0) {
			if (lastMinLineNum == 0) return "0x1FA3";
			String sql = "select orderID from " + countryCode + " limit " + (lastMinLineNum + 1) + ";";
			rs = SQLOperation.readDatabaseRS(c, sql);
			// get true line number which has last max order number.
			int trueLine = 0;
			while (rs != null && rs.next()) {
				String temp1 = rs.getString(1);
				
				if (getOrderNumberWithOrderID(countryCode, temp1)
						<= getOrderNumberWithOrderID(countryCode, lastMinOrderNum)) {
					trueLine++;
				}
				else break;
			}
			
			if (trueLine - howMuch < 0) {
				sql = "select Product, Brand, Quantity, orderID, orderTime from " + countryCode + " limit " + trueLine + ";";
				rs = SQLOperation.readDatabaseRS(c, sql);
				LinkedList res = generateResWithRS(rs, new Order());
				res.insert("" + trueLine); //Max row number
				res.insert("" + (trueLine - howMuch -1)); //Min row number, 1 over the real min
				c.close();
				if (res.head == null) return "0x1FA1"; // Country table not found
				return res;
				
			} else {
				sql = "select Product, Brand, Quantity, orderID, orderTime from " + countryCode + " limit " + (trueLine - howMuch) + "," + howMuch + ";";
				ResultSet r = SQLOperation.readDatabaseRS(c, sql);
				LinkedList res = generateResWithRS(r, new Order());
				res.insert("" + trueLine); //Max row number
				res.insert("" + (trueLine - howMuch -1)); //Min row number, 1 over the real min
				c.close();
				c.close();
				if (res.head == null) return "0x1FA1"; // Country table not found
				return res;
			}
		}
		
		return null;
	}
	
	static String getCountryCodeWithOrderID(String str) {
		String res = "";
		res = res + str.charAt(0) + str.charAt(1);
		if (!Character.isDigit(str.charAt(2))) res += str.charAt(2);
		return res;
	}
	
	static int getOrderNumberWithOrderID(String countryCode, String orderID) {
		String res = "";
		for (int i = countryCode.length(); i < orderID.length(); i++) {
			res += orderID.charAt(i);
		}
		return Integer.parseInt(res);
	}
	
	static Object cancelOrder (LinkedList ll) throws SQLException {
		//<sessionID>&<orderID>
		writeLog("Cancel Order");
		Connection c;
		String sql, orderID;
		
		// Verify session
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		// Get orderID from list
		Object obj = ll.head.getObject();
		if (!obj.getClass().equals(("".getClass()))) {return "0x1002";}
		orderID = obj.toString();
		
		// Delete table for order's offers
		c = SQLControl.SQLOperation.getConnect("generalOffer");
		sql = "DROP TABLE " + orderID;
		System.out.println(SQLOperation.updateData(c, sql));
		c.close();
		
		// Delete order from order history
		c = SQLControl.SQLOperation.getConnect(uid);
		sql = "UPDATE order"
				+ " SET orderStatus = 5"
				+ " WHERE orderID = " + orderID
				+ ";";
		System.out.println(SQLOperation.updateData(c, sql));
		c.close();
		
		return "0x01";
	}
	
	static String giveRate (LinkedList ll) throws SQLException {
		writeLog("Give Rate");
		
		// Verify session
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		// Check that Offer object was given
		Object obj = ll.head.getObject();
		if (!obj.getClass().equals(new Offer().getClass())) return "NOT AN OFFER";
		Offer offer = (Offer)obj;
		
		// Connect to generalOffer
		Connection c = SQLControl.SQLOperation.getConnect("generalOffer");
		
		// Make new table for order's offers if needed
		String orderStatus = SQLControl.SQLOperation.readDatabase(c, "SELECT * FROM " + offer.getOrderID());
		if (orderStatus == null) {
			SQLControl.SQLOperation.createOfferTable(c, offer.getOrderID());
		}
		
		// Insert data into table, turn boolean acceptance to bit accept
		String accept;
		if (offer.getAcceptance()) {accept = "1";}
		else {accept = "0";}
		String value = "'" + offer.getSellerID() + "','" + offer.getRate() + "','" + offer.getExpressCost() + "','" + offer.getShippingMethod() + "','" + accept + "','" + offer.getRemark() + "'";
		String sql = "INSERT INTO " + offer.getOrderID() +" (sellerID, rate, expressCost, shippingMethod, acceptance, remark) VALUES (" + value + ");"; 
		System.out.println(SQLOperation.updateData(c, sql));
		c.close();
		
		c = SQLOperation.getConnect(uid);
		orderStatus = SQLControl.SQLOperation.readDatabase(c, "SELECT offerStatus FROM offer where orderID = '" + offer.getOrderID() + "';");
		System.out.println(orderStatus == null);
		if (orderStatus == null) {
			// offer status is 1 when a offer was initialed.
			sql = "INSERT INTO `offer` (`orderID`, `offerStatus`) VALUES ('" + offer.getOrderID() + "', '1');";
			System.out.println(SQLOperation.updateData(c, sql));
		} else return "0x1FB1";
		c.close();
		String country = getCountryCodeWithOrderID(offer.getOrderID());
		
		sql = "UPDATE `generalOrder`.`" + country + "` SET `orderStatus`='1' WHERE `orderID`='" + offer.getOrderID() + "';";
		c = SQLOperation.getConnect("generalOrder");
		SQLOperation.updateData(c, sql);
		
		sql = "SELECT uid FROM " + country + " where orderID = '" + offer.getOrderID() + "';";
		String uid2 = SQLControl.SQLOperation.readDatabase(c, sql);
		c.close();
		
		sql = "UPDATE `" + uid2 + "`.`order` SET `orderStatus` = '1' WHERE `orderID` = '" + offer.getOrderID() + "';";
		c = SQLOperation.getConnect(uid2);
		SQLOperation.updateData(c, sql);
		
		return "0x01";
	}
	
	static String acceptRate (LinkedList ll) throws SQLException {
		//<sessionID>&<offerObject>
		writeLog("Accept Rate");
		Connection c;
		String sql, orderID, sellerID;
		
		// Verify session
		String uid = checkSession(ll);
		if (!verifySessionRes(uid, ll)) return uid;
		
		// Get orderID and sellerID from list
		Object obj = ll.head.getObject();
		if (!obj.getClass().equals(new Offer().getClass())) return "NOT AN OFFER";
		Offer offer = (Offer)obj;
		orderID = offer.getOrderID();
		sellerID = offer.getSellerID();
		
		// Change acceptance of offer to 1
		c = SQLControl.SQLOperation.getConnect("generalOffer");
		sql = "UPDATE " + orderID
				+ " SET acceptance = 1"
				+ " WHERE sellerID = " + sellerID
				+ ";";
		System.out.println(SQLOperation.updateData(c, sql));
		c.close();
		
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
			} else {
				cardStatus = validateCardInfo(card.getFN(), card.getLN(), card.getCardNum(), card.getZip(), card.getExp());
				if ( !cardStatus.equals("0x01") ) {
					c.close();
					return cardStatus;
				}
				
				String value = "'" + card.getFN() + "','" + card.getLN() + "','" + card.getIssuser() + "','" + card.getCardNum() + "','" + card.getExp() + "','" + card.getZip() + "'";
				String sql = "INSERT INTO payment(fn, ln, issuer, cardNumber, exp, zip) VALUES(" + value + ");"; 
				System.out.println(SQLOperation.updateData(c, sql));
				c.close();
			}
			
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
		if (res.head == null) return "0x1E04";
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
			} else {
				String value = "('" + a.getFN() + "','" + a.getLN() + "','" + a.getCom() + "','" + a.getL1() + "','" + a.getL2() + "','" + a.getCity() + "','" + a.getState() + "','" + a.getZip() + "')";
				String sql = "INSERT INTO address(fn, ln, company, line1, line2, city, state, zip) VALUES" + value + ";";
				System.out.println(SQLOperation.updateData(c, sql));
				c.close();
			}
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
		while (rs!= null && rs.next()) {
			Address a = new Address(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
			ll.insert(a);
		}
		return ll;
	}
	
	private static LinkedList cardList (ResultSet rs) throws SQLException {
		LinkedList ll = new LinkedList();
		while (rs != null && rs.next()) {
			Card c = new Card(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6));
			ll.insert(c);
		}
		return ll;
	}
	
	private static LinkedList orderList (ResultSet rs) throws SQLException {
		LinkedList ll = new LinkedList();
		while (rs != null && rs.next()) {
			String country = "" + rs.getString(4).charAt(0) + rs.getString(4).charAt(1);
			if (!Character.isDigit(rs.getString(4).charAt(2))) country += rs.getString(4).charAt(2);
			Order o = new Order(rs.getString(1), rs.getString(2), rs.getInt(3), country,
					rs.getString(4), new Timestamp(rs.getLong(5)));
			ll.insert(o);
		}
		return ll;
	}
	
	private static UserOrderHis convertOrderToUserOrderHis (Order o, int orderStatus) {
		return new UserOrderHis(o, orderStatus);
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
