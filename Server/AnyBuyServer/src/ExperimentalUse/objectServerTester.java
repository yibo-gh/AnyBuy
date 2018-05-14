package ExperimentalUse;

import java.io.*;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
  
public class objectServerTester {  
  
    private final static Logger logger = Logger.getLogger(objectServerTester.class.getName());  
      
    public static void main(String[] args) throws IOException {  
        ServerSocket server = new ServerSocket(10000);  
  
        while (true) {  
            Socket socket = server.accept();  
            invoke(socket);  
        }  
    }  
  
    private static void invoke(final Socket socket) throws IOException {  
        new Thread(new Runnable() {  
            public void run() {  
                ObjectInputStream is = null;  
                ObjectOutputStream os = null;  
                try {  
                    is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));  
                    os = new ObjectOutputStream(socket.getOutputStream());  
  
                    Object obj = is.readObject();  
                    String user = (String)obj;  
                    System.out.println(user);  
  
                    os.writeObject(user);  
                    os.flush();  
                } catch (IOException ex) {  
                    logger.log(Level.SEVERE, null, ex);  
                } catch(ClassNotFoundException ex) {  
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
        }).start();  
    }  
}  