package Debuger;

import java.io.BufferedInputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.net.Socket;  
import java.util.logging.Level;  
import java.util.logging.Logger;

import Object.LinkedList;  
  
public class objectClientTester {  
      
    private final static Logger logger = Logger.getLogger(objectClientTester.class.getName());  
      
    public static void main(String[] args) throws Exception {  
        for (int i = 0; i < 1; i++) {  
            Socket socket = null;  
            ObjectOutputStream os = null;  
            ObjectInputStream is = null;  
              
            try {  
                socket = new Socket("localhost", 18416);  
      
                os = new ObjectOutputStream(socket.getOutputStream());  
                LinkedList ll = new LinkedList();
                ll.insert("test.");
                os.writeObject(ll);  
                os.flush();  
                  
                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                Object obj = is.readObject();  
                if (obj != null) { 
                	System.out.println(obj.getClass());
                    LinkedList res = (LinkedList)obj;
                    System.out.println((String)res.head.getObject());  
                }  
            } catch(IOException ex) {  
                logger.log(Level.SEVERE, null, ex);  
            } finally {  
                try {  
                    is.close();  
                } catch(Exception ex) {}  
                try {  
                    os.close();  
                } catch(Exception ex) {}  
                try {  
                    socket.close();  
                } catch(Exception ex) {}  
            }  
        }  
    }
}