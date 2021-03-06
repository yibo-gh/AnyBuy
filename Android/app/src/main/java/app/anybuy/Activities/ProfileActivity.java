package app.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;



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
        Button Legal = (Button) findViewById(R.id.legalButtonID);
        Button Payment = (Button) findViewById(R.id.PaymentButtonID);
        Button sellHis = (Button) findViewById(R.id.sellhistoryID);
        Button buyHis = (Button) findViewById(R.id.ordersButtonID);

        Legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, LegalActivity.class);
                startActivity(intent);
            }
        });

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

        sellHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, sellHisActivity.class);
                startActivity(intent);
            }
        });

        buyHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
