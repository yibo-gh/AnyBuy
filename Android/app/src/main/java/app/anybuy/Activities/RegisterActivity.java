package app.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anybuy.R;
import com.anybuy.Clients.SocketClient;

import Object.LinkedList;
import Object.User;

public class RegisterActivity extends AppCompatActivity {

    public EditText email;
    public EditText password;
    public EditText repeatPassword;

    public Button register;
    String emailstr;
    String passwordstr;
    String exist = "0x1A08";
    String interalError1 = "0x1A04";
    String interalError2 = "0x1A05";


    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = (EditText) findViewById(R.id.emailEditTextID);
        password = (EditText) findViewById(R.id.passwordEditTextID);
        repeatPassword = (EditText) findViewById(R.id.repeatPassEditTextID);


        register = (Button) findViewById(R.id.registerButtonID1);




        //when we click the register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //store the email and password in different strings
                emailstr = email.getText().toString();
                passwordstr = password.getText().toString();

                // make sure the password is more than 6 letter
                if (passwordstr.length() < 6) {

                    Toast.makeText(RegisterActivity.this, "Your password is less than 6 characters", Toast.LENGTH_LONG).show();
                }
                //check to see that the repeat password is the same as password and then continue
                //if not give a msg telling the user it is not complete
                else if (!repeatPassword.getText().toString().equals(passwordstr)) {
                    Toast.makeText(RegisterActivity.this, "You didn't repeat the password correctly!", Toast.LENGTH_LONG).show();
                } else {

                    LinkedList l = new LinkedList();
                    l.insert("reg");
                    String uInfo[] = emailstr.split("\\@");
                    String res = "";
                    if (uInfo.length == 2) {
                        String[] domain = uInfo[1].split("\\.");
                        if(domain.length >= 2){
                            User u = new User(uInfo[0], uInfo[1], passwordstr);
                            l.insert(u);
                            try {
                                Object o = SocketClient.Run(l);
                                if (o.getClass().equals("".getClass())) {
                                    res = (String) o;
                                    System.out.println(res);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Invalid email address " + res, Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Invalid email address " + res, Toast.LENGTH_LONG).show();
                        return;
                    }

                 //   combine = "reg&" + emailstr + "?" + passwordstr + "&useSSL=true";
                 //   String res = SocketClient.run(combine);
                 //   System.out.println(res);
                 //   System.out.println(combine);

                    if (res.equals(exist)) {
                        Toast.makeText(RegisterActivity.this, "Account already Exist", Toast.LENGTH_LONG).show();
                    } else if (res.equals(interalError1) || res.equals(interalError2)) {
                        Toast.makeText(RegisterActivity.this, "Server Internal Error " + res, Toast.LENGTH_LONG).show();
                    } else if (res.equals("0x1A07")) {
                        Toast.makeText(RegisterActivity.this, "Invalid email address " + res, Toast.LENGTH_LONG).show();
                    } else {
                    //    combineRegisterPage= "lgi&" + emailstr + "?" + passwordstr + "&useSSL=true";

                        l = new LinkedList();
                        l.insert("lgi");
                        String[] uInfo2 = emailstr.split("\\@");
                        if (uInfo.length == 2) {
                            User u = new User(uInfo2[0], uInfo2[1], passwordstr);
                            l.insert(u);
                            try {
                                Object o = (String)SocketClient.Run(l);
                                if (o.getClass().equals("".getClass())) {
                                    String str = (String) o;
                                    MainActivity.setSessionID(str);
                                }
                                System.out.println(MainActivity.getID());
                            } catch (Exception e) {
                                MainActivity.setSessionID("");
                                e.printStackTrace();
                            }
                        }
                        else MainActivity.setSessionID("");


                     //   MainActivity.setSessionID(SocketClient.run(combineRegisterPage));
                     //   System.out.println("com = " + combineRegisterPage);


                        Toast.makeText(RegisterActivity.this, "You are good", Toast.LENGTH_LONG).show();
                        System.out.println(MainActivity.getID());

                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);


                     //   String combineRegisterPage= "lgi&" + emailstr + "?" + passwordstr + "&useSSL=true";
                     //   String sessionIDRegisterPage = SocketClient.run(combineRegisterPage);

                    }


                    // make the password and email the way the backend guys want it and store it into another string
                }
            }
        });

    }
}
