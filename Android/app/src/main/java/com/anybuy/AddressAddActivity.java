package com.anybuy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddressAddActivity extends AppCompatActivity {

    EditText FN;
    EditText LN;
    EditText com;
    EditText line1;
    EditText line2;
    EditText city;
    EditText state;
    EditText zip;

    Button AddAddressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        FN = (EditText) findViewById(R.id.FNeditText);
        LN = (EditText) findViewById(R.id.LNeditText);
        com = (EditText) findViewById(R.id.comeditText);
        line1 = (EditText) findViewById(R.id.Addressline1editText);
        line2 = (EditText) findViewById(R.id.Addressline2editText);
        city = (EditText) findViewById(R.id.cityeditText);
        state = (EditText) findViewById(R.id.stateeditText);
        zip = (EditText) findViewById(R.id.zipeditText);

        AddAddressButton = (Button) findViewById(R.id.AddAddressbuttonID);

        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









                Intent intent = new Intent(AddressAddActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });
    }
}
