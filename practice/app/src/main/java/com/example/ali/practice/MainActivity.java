package com.example.ali.practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String[] arr = {"a", "b", "c", "d"};
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.layoutID);

        Button button = (Button) findViewById(R.id.buttonID);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < 3; i++) {
                    final TextView textView = new TextView(MainActivity.this);
                    textView.setText(arr[i]);
                    textView.setPadding(0, 200, 0, 0);
                    textView.setBackgroundColor(0x55FF0000);

                    textView.setId(i);
                    System.out.println("The ID : " + textView.getId());

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, NextPageActivity.class);
                            intent.putExtra("ali", textView.getText());

                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(textView);
                }
            }
        });

    }
}
