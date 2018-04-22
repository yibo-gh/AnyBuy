package SQLControl;

import java.sql.*;

public class SQLOperation {
	
	public static void main(String args[]) {
		
	}
	
	public static Connection getConnect (String base, String user, String psc) {
		return new SQL(base, user, psc).connect();
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
	
	public static String writeData(Connection c, String sql) {
		try {
			if (c.createStatement().executeUpdate(sql) != 0) return "WTS";
			else return "0x1B01";
			// WTS = Write Success
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return "0x1B02";
		}
	}
	
	public static String makeTable (Connection c, String str) {
		String sql = "create table " + str + "( id Char(40),psc Char(50));";
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			return "0x1A04";
		}
		return "0x1A05";
	}
}
