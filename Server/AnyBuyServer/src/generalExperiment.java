
import java.sql.SQLException;
 
public class generalExperiment {
    public static void main(String[] args) throws SQLException {
    	System.out.println(getID().getClass());
    	System.out.println(getID().getClass().equals("String".getClass()));
    }
    
    static Object getID() { return "test";}
}
