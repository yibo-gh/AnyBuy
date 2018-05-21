package app.anybuy.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Object.Address;
import Object.LinkedList;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;


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
    String f, l, co, l1, l2, c, s, z;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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


        AddAddressButton = (Button) findViewById(R.id.addAddressbuttonID);


        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                f = FN.getText().toString();
                f = BuyActivity.strPreProcess(f);
                l = LN.getText().toString();
                l = BuyActivity.strPreProcess(l);
                co = com.getText().toString();
                co = BuyActivity.strPreProcess(co);
                l1 = line1.getText().toString();
                l1 = BuyActivity.strPreProcess(l1);
                l2 = line2.getText().toString();
                l2 = BuyActivity.strPreProcess(l2);
                c = city.getText().toString();
                s = state.getText().toString();
                z = zip.getText().toString();

                LinkedList ll = new LinkedList();
                ll.insert("ada");
                ll.insert(MainActivity.getID());
                System.out.println(f + " " + l + " " + co + " " + l1 + " " + l2 + " " + c + " " + s + " " + z);
                Address a = new Address(f, l, co, l1, l2, c, s, z);
                ll.insert(a);
                try {
                    String str = (String) SocketClient.Run(ll);
                    System.out.println(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(AddressAddActivity.this, AddressActivity.class);
                startActivity(intent);

            }
        });



    }
}
