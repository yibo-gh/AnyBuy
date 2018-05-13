package Debugger;

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

    
    public static Object run(LinkedList ll) throws Exception {
            Socket socket = new Socket("yg-home.site", 18416); 
            ObjectOutputStream os = null;  
            ObjectInputStream is = null;  
              
            try {  
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeObject(ll);  
                os.flush();  
                  
                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                Object obj = is.readObject();
                is.close();
                os.close();
                socket.close();
                return obj;
            } catch(IOException ex) {  
                logger.log(Level.SEVERE, null, ex);
            }
            return null;
    }
}