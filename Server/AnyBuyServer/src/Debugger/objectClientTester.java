package Debugger;

import java.io.BufferedInputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.net.Socket;  
import java.util.logging.Level;  
import java.util.logging.Logger;

import Object.LinkedList;
import Object.User;  
  
public class objectClientTester {  
      
    private final static Logger logger = Logger.getLogger(objectClientTester.class.getName());  
      
    public static void main(String[] args) throws Exception {
            Socket socket = null;  
            ObjectOutputStream os = null;  
            ObjectInputStream is = null;  
              
            try {  
                socket = new Socket("10.0.3.1", 18416);  
      
                os = new ObjectOutputStream(socket.getOutputStream());  
                LinkedList ll = new LinkedList();
                ll.insert("reg");
                ll.insert(new User("Tiffiny", "snsd.or.kr", "loveYOONA!"));
                os.writeObject(ll);  
                os.flush();  
                  
                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                Object obj = is.readObject();  
                if (obj != null) { 
                	System.out.println(obj.getClass());
                    LinkedList res = (LinkedList)obj;
                    System.out.println((String)res.head.getObject());  
                }  
                is.close();
                os.close();
                socket.close();
            } catch(IOException ex) {  
                logger.log(Level.SEVERE, null, ex);  
            }
    }
}