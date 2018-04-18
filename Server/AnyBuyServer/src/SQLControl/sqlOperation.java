package SQLControl;

import java.sql.*;

public class sqlOperation {

	public static void main(String[] args) 
    {
        Connection conn;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://yg-home.site:3306/userInfo?verifyServerCertificate=true&useSSL=true";
        String user = "anybuy";
        String password = "CMPS115.";
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,password);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
             // sth to do
             
            conn.close();
        }catch(ClassNotFoundException e){
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
	
}
