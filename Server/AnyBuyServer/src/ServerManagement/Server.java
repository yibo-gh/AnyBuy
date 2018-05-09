package ServerManagement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ServerSocket {
    private static final int SERVER_PORT = 18416;

    public Server() throws IOException {
        super(SERVER_PORT);
        IntermediateAPI.CoreOperations.writeLog("Server ready. Port " + this.getLocalPort());
 
        try {
            while (true) {
                Socket socket = accept();
                //Start another thread when requests received.
                new CreateServerThread(socket);
            }
        }catch (IOException e) {
        }finally {
            close();
        }
    }
}