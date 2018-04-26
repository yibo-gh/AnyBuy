package com.example.ali.anybuy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    public EditText email;
    public EditText password;
    public EditText repeatPassword;

    public Button register;
    String emailstr;
    String passwordstr;
    String emailPasswordForServer;

    String combine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = (EditText) findViewById(R.id.emailEditTextID);
        password = (EditText) findViewById(R.id.passwordEditTextID);
        repeatPassword = (EditText) findViewById(R.id.repeatPassEditTextID);


        register = (Button) findViewById(R.id.registerButtonID);



        //when we click the register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //store the email and password in different strings
                emailstr = email.getText().toString();
                passwordstr = password.getText().toString();

                // make sure the password is more than 6 letter
                if(passwordstr.length() < 6){

                    Toast.makeText(RegisterActivity.this, "Your password is less than 6 characters", Toast.LENGTH_LONG).show();
                }
                //check to see that the repeat password is the same as password and then continue
                //if not give a msg telling the user it is not complete
                else if(!repeatPassword.getText().toString().equals(passwordstr))
                {
                    Toast.makeText(RegisterActivity.this, "You didn't repeat the password correctly!", Toast.LENGTH_LONG).show();
                }

                if(passwordstr.length() < 5){

                }

                else{
                    Toast.makeText(RegisterActivity.this, "You are good", Toast.LENGTH_LONG).show();
                    combine = "reg&" + emailstr + "?" + passwordstr + "&useSSL=true";

                    System.out.println(combine);
                }
                // make the password and email the way the backend guys want it and store it into another string
            }
        });

    }
}
