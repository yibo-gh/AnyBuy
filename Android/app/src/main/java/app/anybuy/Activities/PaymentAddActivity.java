package app.anybuy.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import app.anybuy.R;

import app.anybuy.Clients.SocketClient;
import Object.LinkedList;
import Object.Card;

public class PaymentAddActivity extends AppCompatActivity {

    EditText FN, LN,  CN, ZP, EXP;
    Button addCardButton;
    String f, l, i, c, z, e;

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
        setContentView(R.layout.activity_payment_add);

        FN = (EditText) findViewById(R.id.FNeditText);
        LN = (EditText) findViewById(R.id.LNeditText);
        CN = (EditText) findViewById(R.id.CardNumEditText);
        ZP = (EditText) findViewById(R.id.zipEditText);
        EXP = (EditText) findViewById(R.id.expEditText);

        addCardButton = (Button) findViewById(R.id.AddCardButtonID);
        System.out.println("Card created.");

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                f = FN.getText().toString();
                l = LN.getText().toString();
                c = CN.getText().toString();
                z = ZP.getText().toString();
                e = EXP.getText().toString();

                if (c.charAt(0) == '4') i = "visa";
                else if (c.charAt(0) == '5') i = "mstc";
                else if (c.charAt(0) == '3' && c.length() == 15) i = "amex";
                else if (c.charAt(0) == '3' && c.length() == 16) i = "jcbx";
                else if (c.charAt(0) == '6' && c.charAt(1) == '0' && c.charAt(2) == '1' && c.charAt(3) == '1'
                        && c.length() == 16) i = "dscv";
                else if (c.charAt(0) == '6' && c.charAt(1) == '2' && (c.length() == 16 || c.length() == 19)) i = "unpy";
                else i = "unkn";


                LinkedList ll = new LinkedList();
                ll.insert("adc");
                ll.insert(MainActivity.getID());
                Card cd = new Card(f, l, i, c, e, z);
                System.out.println("FN = " + cd.getFN());
                ll.insert(cd);
                try {
                    String str = (String) SocketClient.Run(ll);
                    System.out.println(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(PaymentAddActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}
