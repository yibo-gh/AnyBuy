package com.anybuy.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anybuy.R;
import com.anybuy.Clients.SocketClient;

import Object.*;

public class MainActivity extends AppCompatActivity {

    public EditText email1;
    public EditText password1;

    static String sessionID;
    String emailstr1;
    String passwordstr1;
    String loginerror1 = "0x1C01";
    String loginerror2 = "0x1C02";
    String loginerror4 = "0x1001";

    public static String getID() {
        return sessionID;
    }




    public static void setSessionID(String str){
        sessionID = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email1 = (EditText) findViewById(R.id.emailEditTextID1);
        password1 = (EditText) findViewById(R.id.passwordEditTextID1);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Button logIn = (Button) findViewById(R.id.loginID);

        Button register1 = (Button) findViewById(R.id.registerButtonID1);

        //open the home page if login button was clicked
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               emailstr1 = email1.getText().toString();
                passwordstr1 = password1.getText().toString();

                LinkedList ll = new LinkedList();
                ll.insert("lgi");
                String[] uInfo = emailstr1.split("\\@");
                if (uInfo.length == 2) {
                    User u = new User(uInfo[0], uInfo[1], passwordstr1);
                    ll.insert(u);
                    try {
                        System.out.println(ll.getClass() + " " + ll.getLength());
                        Object o = SocketClient.Run(ll);
                        if (o.getClass().equals("".getClass())) {
                            String str = (String) o;
                            setSessionID(str);
                            System.out.println(getID());
                        }
                        System.out.println(sessionID);
                    } catch (Exception e) {
                        sessionID = "";
                        e.printStackTrace();
                    }
                }
                else sessionID = "";

               // if(!repeatPassword.getText().toString().equals(password1str))
               // {
                //    Toast.makeText(MainActivity.this, "Email or Username is incorrect!", Toast.LENGTH_LONG).show();
                //}

                //else{


                    //combine1 = "lgi&" + emailstr1 + "?" + passwordstr1 + "&useSSL=true";
                    //sessionID = SocketClient.run(combine1);

                   // System.out.println(combine1);
                   // System.out.println(sessionID);
                    if(sessionID.equals(loginerror1)) {
                        Toast.makeText(MainActivity.this, "User not found.", Toast.LENGTH_LONG).show();
                    }else if(sessionID.equals(loginerror2) || emailstr1.equals("") || passwordstr1.equals("")) {
                        Toast.makeText(MainActivity.this, "Invalid Username and/or Password.", Toast.LENGTH_LONG).show();
                    }
                    else if(sessionID.equals(loginerror4) || sessionID.equals(loginerror4)) {
                        Toast.makeText(MainActivity.this, "Illegal Connection.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this, "You are good to log in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    //}

            }



        });



        //open the register page if register button was clicked
        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


}
