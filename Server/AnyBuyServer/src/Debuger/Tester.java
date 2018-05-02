package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
		String str = "reg&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "lgi&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		String res = IntermediateAPI.API.getCommand(str);
		System.out.println(res);
		String[] uInfo = res.split("\\?");
		str = "adc&snok10000?" + uInfo[1] + "&yoona?lim&amex=375987654321001&1220?95064";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "adc&snok10000?" + uInfo[1] + "&yoona?lim&amex=375987654321002&1220?95064";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "ldc&snok10000?" + uInfo[1];
		System.out.println(IntermediateAPI.API.getCommand(str));
		//dlc&sid&card#
		str = "dtc&snok10000?" + uInfo[1] + "&375987654321003";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "ldc&snok10000?" + uInfo[1];
		System.out.println(IntermediateAPI.API.getCommand(str));
		// Place order
		str = "plo&snok10000?" + uInfo[1] + "&AZ?Shoes?Nike?test.jpg?1";
		System.out.println(IntermediateAPI.API.getCommand(str));
	}
}
