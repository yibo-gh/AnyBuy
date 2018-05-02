
import java.sql.SQLException;
 
public class generalExperiment {
    public static void main(String[] args) throws SQLException {

		String str = "reg&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		System.out.println(IntermediateAPI.API.getCommand(str));
		str = "lgi&yoona@snsd.or.kr?loveYOONA!&useSSL=true";
		String res = IntermediateAPI.API.getCommand(str);
		System.out.println(res);
		String[] uInfo = res.split("\\?");
		str = "ada&snok10000?" + uInfo[1] + "&yoona?lim&SM 엔터테인먼트?Yeongdong-daero 513?Gangnam-gu?Seoul?KR?00000";
		System.out.println(IntermediateAPI.API.getCommand(str));
    }
}
