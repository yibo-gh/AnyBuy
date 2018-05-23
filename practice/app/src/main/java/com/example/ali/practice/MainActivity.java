package com.example.ali.practice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.layoutID);

        for(int i = 0 ; i < 20; i++) {
            TextView textView = new TextView(this);
            textView.setText("Hello");
            textView.setPadding(0, 200, 0, 0);

            linearLayout.addView(textView);
        }

    }
}
