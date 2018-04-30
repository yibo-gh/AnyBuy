package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
//		String str = "reg&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
//		System.out.println(IntermediateAPI.API.getCommand(str));
//		String str = "lgi&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
//		System.out.println(IntermediateAPI.API.getCommand(str));
//		String str = "adc&snok10000?553587&yoona?lim&amex=375987654321001&1220?95064";
//		System.out.println(IntermediateAPI.API.getCommand(str));
		String str = "plo&snok10000?553587&AZ?Shoes?Nike?test.jpg?1";
		System.out.println(IntermediateAPI.API.getCommand(str));
	}
}
