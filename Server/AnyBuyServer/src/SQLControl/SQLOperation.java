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
			if (c.createStatement().executeUpdate(sql) != 0) return "CDS";
			else return "0x1A03";
			// CDS = Create Domain Success
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return "0x1A02";
		} 
	}
}
