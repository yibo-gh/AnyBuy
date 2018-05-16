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
		String sql = "create table " + str + "( name Char(40),psc Char(50), id int(8));";
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
			sql = "create table payment ( fn Char(20), ln Char(20), issuer Char(4), cardNumber Char(16), exp Char(4), zip Char(5) );";
			c.createStatement().executeUpdate(sql);
			sql = "create table address ( fn Char(20), ln Char(20), company Char(255), line1 Char(255), line2 Char(255), city Char(255), state Char(2), zip Char(5) );";
			c.createStatement().executeUpdate(sql);
			sql = "CREATE TABLE `" + userId + "`.`order` (\r\n" + 
					"  `orderID` CHAR(20) NOT NULL,\r\n" + 
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
				+ " orderID VARCHAR(10) NOT NULL PRIMARY KEY),"
				+ " orderStatus INT(1) NOT NULL";
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// Failed CREATE TABLE for country
			return "0x1F01";
		}
		// Failed CREATE TABLE for country
		return "0x1F01";
	}
	
	public static int countLine(Connection c, String tableName) throws SQLException {
		String sql = "select count(*) as rowCount from " + tableName;
		ResultSet rset = c.createStatement().executeQuery(sql);
		rset.next();
		int rtn = rset.getInt("rowCount");
//		int rtn = rset.getMetaData().
		return rtn;
	}
	
}
