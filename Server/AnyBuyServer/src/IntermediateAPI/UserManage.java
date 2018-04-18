package IntermediateAPI;

import java.sql.Connection;

public class UserManage {

	static String getDomainCode(Connection c, String str) {
		String code = "";
		String[] str1 = str.split("\\.");
		if (str1.length > 3) code += str1[0].charAt(0) + str1[0].charAt(1) + str1[1].charAt(0) + str1[2].charAt(0);
		else code += "" + str1[0].charAt(0) + str1[0].charAt(1) + str1[1].charAt(0) + "x";
		String sql = "select emailDomain from domainCode where code='" + code + "'";
		for (int i = 0; SQLControl.SQLOperation.readDatabase(c, sql) != null; i++) {
			StringBuilder sb = new StringBuilder(code);
			if (i < 10) sb.replace(3, 4, "" + i);
			if (i >= 10 && i < 100) sb.replace(2, 4, "" + i);
			code = sb.toString();
			sql = "select emailDomain from domainCode where code='" + code + "'";
		} return code;
	}
	
	static String createDomainCode (Connection c, String str) {
		System.out.println(str + " " + getDomainCode(c, str));
		String sql = "INSERT INTO domainCode(emailDomain,code) VALUES('" + str + "','" + getDomainCode(c, str) + "');"; 
		// INSERT INTO `userInfo`.`domainCode` (`emailDomain`, `code`) VALUES ('sample.com', 'sacx');
		return SQLControl.SQLOperation.writeData(c, sql);
	}
	
}
