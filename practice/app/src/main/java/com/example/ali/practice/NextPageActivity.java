package com.example.ali.practice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NextPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);

        TextView textView2 = (TextView) findViewById(R.id.textView2ID);

        String i = getIntent().getStringExtra("ali");


        textView2.setText(i);
    }
}
