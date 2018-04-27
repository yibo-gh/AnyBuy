package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
		String str = "reg&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "lgi&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		
	}
}
