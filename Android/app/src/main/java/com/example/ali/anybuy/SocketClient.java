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
           // while (result.indexOf("bye") == -1) {
                printWriter.println(str);

               printWriter.println(str);
                printWriter.flush();

                result = bufferedReader.readLine();
           // }



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
