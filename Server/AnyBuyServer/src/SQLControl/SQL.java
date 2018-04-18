package SQLControl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL {

	String base, user, psc;
	
	SQL(String b, String u, String p){
		this.base = b;
		this.user = u;
		this.psc = p;
	}
	
	Connection connect() {
		Connection conn;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://yg-home.site:3306/" + this.base + "?verifyServerCertificate=true&useSSL=true";
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url,this.user,this.psc);
			if(!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
				return conn;
			}
		}catch(ClassNotFoundException e){
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
}
