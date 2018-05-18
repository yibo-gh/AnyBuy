package app.anybuy.Clients;

import Object.LinkedList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {
    private final static Logger logger = Logger.getLogger(SocketClient.class.getName());

    public static Object Run(LinkedList ll) throws Exception {
<<<<<<< HEAD:Android/app/src/main/java/app/anybuy/Clients/SocketClient.java
        Socket socket = new Socket("anybuy.app", 18416);
=======
        Socket socket = new Socket("67.180.244.170", 18416);
>>>>>>> 3c12cd02c34c5c0f1a98e6a0bf97d849b429c776:Android/app/src/main/java/app/anybuy/Clients/SocketClient.java
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
