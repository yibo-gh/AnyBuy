package ServerManagement;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import IntermediateAPI.API;
import Object.LinkedList;

//Thread Class
    public class CreateServerThread extends Thread {
        private Socket client;
        private ObjectInputStream is = null;  
        private static ObjectOutputStream os = null; 
        
 
        public CreateServerThread(Socket s)throws IOException {
            this.client = s;
            System.out.println("Client(" + getName() +") come in...");
             
            start();
        }
 
        public void run() {
            try {
            	is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));  
                os = new ObjectOutputStream(client.getOutputStream());  

                Object obj = is.readObject();  
                LinkedList input = (LinkedList)obj;  
                System.out.println("Client said " + (String)input.head.getObject());
                
                while (input != null) {
                    System.out.println("Client(" + getName() +") say: " + input);
                    Object status = API.getCommand(input);
                    LinkedList ll = new LinkedList();
                    ll.insert(status);
                    pushToClient(ll);
                    System.out.println("pushed message: " + status);
                    input = (LinkedList)obj;
                }
                
                LinkedList pushBack = new LinkedList();
                pushBack.insert("bye, Client(" + getName() +")!");
                pushToClient(pushBack);
                 
                System.out.println("Client(" + getName() +") exit!");
                client.close();
            }catch (IOException e) {} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

		public static void pushToClient(Object o) throws IOException {
        	os.writeObject(o);  
            os.flush();  
        }
    }