package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
		///* hho15's testing commands
		String str = "";
		// Register
		//str = "reg&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
		//System.out.println(IntermediateAPI.API.getCommand(str));
		// Login
		str = "lgi&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
		String res = IntermediateAPI.API.getCommand(str);
		System.out.println(res);
		String[] uInfo = res.split("\\?");
		// Place order
		str = "plo&" + uInfo[0] + "?" + uInfo[1] + "&AZ?Shoes?Nike?test.jpg?1";
		System.out.println(IntermediateAPI.API.getCommand(str));
		//*/
	}
}
