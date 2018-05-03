package com.example.ali.anybuy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    public static String run(String str) {
        System.out.println("in client");
        try {
            Socket socket = new Socket("yg-home.site", 18416);
            socket.setSoTimeout(6000);

            System.out.println("Connection Established");

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String result = "";
<<<<<<< HEAD
           // while (result.indexOf("bye") == -1) {
                printWriter.println(str);
=======
>>>>>>> ced8eeb7d53d1baf411a6923ab73e6f8ed581432

               printWriter.println(str);
                printWriter.flush();

                result = bufferedReader.readLine();
<<<<<<< HEAD
           // }

=======
>>>>>>> ced8eeb7d53d1baf411a6923ab73e6f8ed581432
            

            printWriter.close();
            bufferedReader.close();
            socket.close();
            return result;

        } catch (Exception e) {
            System.out.println("Exception:" + e);
            return "0x1001";
        }
    }
}
