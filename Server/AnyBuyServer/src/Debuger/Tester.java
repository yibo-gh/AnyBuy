package Debuger;

import java.sql.SQLException;

public class Tester {

	public static void main (String args[]) throws SQLException {
		String str = "reg&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "lgi&yoona1@snsd.or.kr?loveYOONA!&useSSL=true";
		String res = IntermediateAPI.API.getCommand(str);
		System.out.println(res);
		String[] uInfo = res.split("\\?");
		str = "ada&snok10000?" + uInfo[1] + "&yoona?lim&SM Ent?Yeongdong-daero 513?Gangnam-gu?Seoul?KR?00000";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "ada&snok10000?" + uInfo[1] + "&yoona?lim&?Yeongdong-daero 513?Gangnam-gu?Seoul?KR?00000";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "lda&snok10000?" + uInfo[1];
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "dta&snok10000?" + uInfo[1] + "&Yeongdong-daero 513";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "ada&snok10000?" + uInfo[1] + "&yoona?lim&?Yeongdong-daero 513?Gangnam-gu?Seoul?KR?00000";
		System.out.println(IntermediateAPI.API.getCommand(str));
		// Place order
		str = "plo&" + uInfo[0] + "?" + uInfo[1] + "&AZ?Shoes?Nike?test.jpg?1";
		System.out.println(IntermediateAPI.API.getCommand(str));
		// Load order 30 from AZ
		str = "ldo&" + uInfo[0] + "?" + uInfo[1] + "&30?AZ";
		System.out.println(IntermediateAPI.API.getCommand(str));
	}
}
