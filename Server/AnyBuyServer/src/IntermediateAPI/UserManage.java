package IntermediateAPI;

import java.sql.Connection;

public class UserManage {

	static String generateDomainCode(Connection c, String str) {
		String code = "";
		String[] str1 = str.split("\\.");
		for (int i = 0; i < str1.length; i++) if (str1[i].equals("")) return "0x1A07";
		if (str1.length >= 3) code += "" + str1[0].charAt(0) + str1[0].charAt(1) + str1[1].charAt(0) + str1[2].charAt(0);
		else code += "" + str1[0].charAt(0) + str1[0].charAt(1) + str1[1].charAt(0) + "x";
		
		String sql = "select emailDomain from domainCode where code='" + code + "'";
		
		for (int i = 0; SQLControl.SQLOperation.readDatabase(c, sql) != null; i++) {
			StringBuilder sb = new StringBuilder(code);
			if (i < 10) sb.replace(3, 4, "" + i);
			if (i >= 10 && i < 100) sb.replace(2, 4, "" + i);
			code = sb.toString();
			sql = "select emailDomain from domainCode where code='" + code + "'";
		}
		return code;
	}
	
	static String createDomainCode (Connection c, String str) {
		String code = generateDomainCode(c, str);
		if (code == "0x1A07") return code;
		System.out.println(str + " " + code);
		String sql = "INSERT INTO domainCode(emailDomain,code) VALUES('" + str + "','" + code + "');"; 
		SQLControl.SQLOperation.makeTable(c, code);
		return SQLControl.SQLOperation.writeData(c, sql);
	}
	
	
	
}
