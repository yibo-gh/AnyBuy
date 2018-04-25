package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
//		String str = "reg&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
//		System.out.println(IntermediateAPI.API.getCommand(str));
		String str = "lgi&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		
	}
}
