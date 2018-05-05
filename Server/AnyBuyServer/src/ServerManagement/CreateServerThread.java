package ServerManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import IntermediateAPI.API;

//Thread Class
    public class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
        private static PrintWriter printWriter;
 
        public CreateServerThread(Socket s)throws IOException {
            client = s;
 
            bufferedReader =new BufferedReader(new InputStreamReader(client.getInputStream()));
             
            printWriter =new PrintWriter(client.getOutputStream(),true);
            System.out.println("Client(" + getName() +") come in...");
             
            start();
        }
 
        public void run() {
            try {
                String line = bufferedReader.readLine();
                
                while (line != null) {
                    System.out.println("Client(" + getName() +") say: " + line);
                    String status = API.getCommand(line);
                    pushToClient(status);
                    System.out.println("pushed message: " + status);
                    line = bufferedReader.readLine();
                }
                
                printWriter.println("bye, Client(" + getName() +")!");
                 
                System.out.println("Client(" + getName() +") exit!");
                printWriter.close();
                bufferedReader.close();
                client.close();
            }catch (IOException | SQLException e) {}
        }
        
        public static void pushToClient(String str) {
        	printWriter.println(str);
        }
    }