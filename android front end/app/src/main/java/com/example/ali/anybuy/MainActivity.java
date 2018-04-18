package com.example.ali.anybuy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buy = (Button) findViewById(R.id.buyButtonID3);
        Button sell = (Button) findViewById(R.id.sellButtonID4);
        Button profile = (Button) findViewById(R.id.profileButtonID);
        Button logout = (Button) findViewById(R.id.logoutButtonID3);

        //open the buy page if buy button was clicked
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

        //open the buy page if buy button was clicked
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(intent);
            }
        });

    }


}
