package com.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anybuy.R;

public class ProfileActivity extends AppCompatActivity {

    Button orderbutton;

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
