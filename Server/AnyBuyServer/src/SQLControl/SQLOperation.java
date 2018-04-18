package SQLControl;

import java.sql.*;

public class SQLOperation {
	
	public static void main(String args[]) {
		
	}
	
	public static Connection getConnect (String base, String user, String psc) {
		return new SQL(base, user, psc).connect();
	}
	
	public static String readDatabase(Connection c, String sql) {
		Statement stat;
		try {
			ResultSet rst = c.createStatement().executeQuery(sql);
			if (rst.next()) return rst.getString(1);
		} catch (SQLException e) {
			return null;
		}
		return null;
	}
}
