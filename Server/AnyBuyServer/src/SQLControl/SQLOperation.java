package SQLControl;

import java.sql.*;

public class SQLOperation {
	
	public static void main(String args[]) {
		
	}
	
	public static Connection getConnect (String base) {
		return new SQL(base, "anybuy", "CMPS115.").connect();
	}
	
	public static String readDatabase(Connection c, String sql) {
		try {
			ResultSet rst = c.createStatement().executeQuery(sql);
			if (rst.next()) return rst.getString(1);
		} catch (SQLException e) {
			System.out.println("0x1B02");
			return null;
		}
		return null;
	}
	
	public static ResultSet readDatabaseRS(Connection c, String sql) {
		try {
			return c.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static String updateData(Connection c, String sql) {
		try {
			if (c.createStatement().executeUpdate(sql) != 0) return "UPS";
			else return "0x1B01";
			// WTS = Write Success
		} catch (SQLException e) {
			return "0x1B02";
		}
	}
	
	public static String makeTable (Connection c, String str) {
		String sql = "create table `" + str + "` ( "
				+ "name Char(40) NOT NULL,"
				+ "psc Char(50) NOT NULL,"
				+ "id int(8) NOT NULL,"
				+ "PRIMARY KEY (`name`));";
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			return "0x1A04";
		}
		return "0x1A05";
	}
	
	public static String createProfile(Connection c, String userId) {
		try {
			String sql = "CREATE DATABASE " + userId;
			c.createStatement().executeUpdate(sql);
			c = SQLControl.SQLOperation.getConnect(userId);
			sql = "create table `payment` ( "
					+ "`fn` Char(20) NOT NULL, "
					+ "`ln` Char(20) NOT NULL, "
					+ "`issuer` Char(4) NOT NULL, "
					+ "`cardNumber` Char(19) NOT NULL, "
					+ "`exp` Char(4) NOT NULL, "
					+ "`zip` Char(6) NOT NULL,"
					+ "PRIMARY KEY (`cardNumber`));";
			c.createStatement().executeUpdate(sql);
			sql = "create table `address` ( "
					+ "`index` INT NOT NULL AUTO_INCREMENT,"
					+ "`fn` Char(20) NOT NULL, "
					+ "`ln` Char(20) NOT NULL, "
					+ "`company` Char(255) NOT NULL, "
					+ "`line1` Char(255) NOT NULL, "
					+ "`line2` Char(255) NOT NULL, "
					+ "`city` Char(255) NOT NULL, "
					+ "`state` Char(2) NOT NULL, "
					+ "`zip` Char(6) NOT NULL,"
					+ " PRIMARY KEY (`index`));";
			c.createStatement().executeUpdate(sql);
			sql = "CREATE TABLE `" + userId + "`.`order` (\r\n" + 
					"  `orderID` CHAR(20) NOT NULL,\r\n" + 
					"  `orderStatus` INT NOT NULL,\r\n" + 
					"  `line1` CHAR(255) NOT NULL,\r\n" + 
					"  `city` CHAR(255) NOT NULL,\r\n" + 
					"  `state` CHAR(2) NOT NULL,\r\n" + 
					"  `zip` CHAR(6) NOT NULL,\r\n" + 
					"  `card` CHAR(19) NOT NULL,\r\n" +
					"  PRIMARY KEY (`orderID`));\r\n" + 
					"";
			c.createStatement().executeUpdate(sql);
			sql = "CREATE TABLE `" + userId + "`.`offer` (\r\n" + 
					"  `orderID` VARCHAR(10) NOT NULL,\r\n" + 
					"  `offerStatus` INT NOT NULL,\r\n" + 
					"  PRIMARY KEY (`orderID`));\r\n";
			c.createStatement().executeUpdate(sql);
			return "0x01";
		} catch (SQLException e) {
			return "0x1A05";
		}
	}
	
	// Create table for country in generalOrder
	public static String createCountryTable(Connection c, String country) {
		String sql = "CREATE TABLE " + country + "(Product VARCHAR(45) NOT NULL,"
				+ " Brand VARCHAR(45) NOT NULL, Quantity INT(10) NOT NULL, Image VARCHAR(255) NULL,"
				+ " orderTime VARCHAR(45) NULL,"
				+ " orderID VARCHAR(10) NOT NULL PRIMARY KEY,"
				+ " orderStatus CHAR(1),"
				+ " uid CHAR(10))";
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// Failed CREATE TABLE for country
			return "0x1F01";
		}
		// Failed CREATE TABLE for country
		return "0x1F01";
	}
	
	// Create table for order's offers in generalOffer
	public static String createOfferTable(Connection c, String orderID) {
		String sql = "CREATE TABLE " + orderID
				+ " ("
				+ " sellerID VARCHAR(45) NOT NULL"
				+ ", rate DOUBLE(12,2) NOT NULL"
				+ ", expressCost DOUBLE(12,2) NOT NULL"
				+ ", shippingMethod INT(1) NOT NULL"
				+ ", acceptance TINYINT(4) NOT NULL"
				+ ", remark VARCHAR(140) NULL"
				+ ", PRIMARY KEY (sellerID)"
				+ " );";
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// Failed CREATE TABLE for order
			return "0xPAIN";
		}
		// Successfully CREATE TABLE for order
		return "0x01";
	}
	
	public static int countLine(Connection c, String tableName) throws SQLException {
		String sql = "select count(*) as rowCount from " + tableName;
		ResultSet rset = c.createStatement().executeQuery(sql);
		rset.next();
		int rtn = rset.getInt("rowCount");
		return rtn;
	}
	
}
