package app.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD:Android/app/src/main/java/app/anybuy/Activities/ProfileActivity.java
=======

>>>>>>> 3c12cd02c34c5c0f1a98e6a0bf97d849b429c776:Android/app/src/main/java/app/anybuy/Activities/ProfileActivity.java
import app.anybuy.R;

public class ProfileActivity extends AppCompatActivity {

    Button orderbutton;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Button Address = (Button) findViewById(R.id.AddressButtonID);

        Button Payment = (Button) findViewById(R.id.PaymentButtonID);


        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
