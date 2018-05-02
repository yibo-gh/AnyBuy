package com.example.ali.anybuy;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public EditText email1;
    public EditText password1;

    public Button login1;
    String emailstr1;
    String passwordstr1;

    String combine1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email1 = (EditText) findViewById(R.id.emailEditTextID1);
        password1 = (EditText) findViewById(R.id.passwordEditTextID1);

        if (android.os.Build.VERSION.SDK_INT > 9) {
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

               // if(!repeatPassword.getText().toString().equals(password1str))
               // {
                //    Toast.makeText(MainActivity.this, "Email or Username is incorrect!", Toast.LENGTH_LONG).show();
                //}



                //else{
                    Toast.makeText(MainActivity.this, "You are good to log in", Toast.LENGTH_LONG).show();
                    combine1 = "reg&" + emailstr1 + "?" + passwordstr1 + "&useSSL=true";
                 //   String res = SocketClient.run(combine1);
                  //  System.out.println(res);
                    System.out.println(combine1);
                //}


                //if the email and password were walid, then go to home page
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
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
