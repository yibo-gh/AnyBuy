package Debuger;

import java.sql.SQLException;

import Object.LinkedList;
import Object.User;

public class Tester {

	public static void main (String args[]) throws SQLException {
		LinkedList l = new LinkedList();
		l.insert("reg");
		User u = new User("yoona", "snsd.or.kr", "loveYOONA!");
		// reg&yoona@snsd.or.kr?loveYOONA!&useSSL=true
		l.insert(u);
		System.out.println(IntermediateAPI.API.getCommand(l));
//		str = "lgi&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
//		String res = IntermediateAPI.API.getCommand(str);
//		System.out.println(res);
//		String[] uInfo = res.split("\\?");
		/*
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
		*/
		// Load order list from AZ
//		str = "ldl&" + uInfo[0] + "?" + uInfo[1] + "&AZ";
//		System.out.println(IntermediateAPI.API.getCommand(str));
		// Place order
//		str = "plo&" + uInfo[0] + "?" + uInfo[1] + "&KR?Yoona\\'s Choice?Innisfree?test.jpg?1";
//		System.out.println(IntermediateAPI.API.getCommand(str));
	}
}
