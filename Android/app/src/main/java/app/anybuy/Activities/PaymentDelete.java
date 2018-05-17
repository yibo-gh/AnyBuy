package app.anybuy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import app.anybuy.Clients.SocketClient;

import Object.LinkedList;
import Object.Card;
import Object.Node;


public class PaymentDelete extends AppCompatActivity {

    TextView result;
    Spinner spinner;
    Button button;
    Object o;
    String[] options;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_delete);

        result = (TextView) findViewById(R.id.result);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.deleteCardButton);
        LinkedList l = new LinkedList();
        l.insert("ldc");
        l.insert(MainActivity.getID());
        LinkedList cList = new LinkedList();

        try {
            o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                Node temp = l.head;
                int i = 1;
                while (temp != null){
                    String str = i + ". ";
                    Card cd = (Card) temp.getObject();
                    str = str + cd.getFN() + " " + cd.getLN();
                    str = str + "\n" + cd.getIssuser() + " " + cd.getCardNum();
                    str = str + "\n" + cd.getExp() + " " + cd.getZip();
                    cList.insert(str);
                    i++;
                    temp = temp.getNext();
                }
            } else {
                cList.insert("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        options = new String[cList.getLength()];

        Node temp = cList.head;

        int i = 0;
        while (temp != null){
            options[i] = (String) temp.getObject();
            temp = temp.getNext();
            System.out.println(options[i]);
            i++;
        }


        System.out.println("spinner initialized.");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        System.out.println("adapter is null = " + adapter == null);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                result.setText(((TextView)arg1).getText());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinkedList l = new LinkedList();
                l.insert("dtc");
                l.insert(MainActivity.getID());
                LinkedList obj = (LinkedList) o;
                Node temp = obj.head;
                for (int i = 1; i < Integer.parseInt("" + result.getText().charAt(0)); i++) {
                    temp = temp.getNext();
                }
                System.out.println(temp.getObject().getClass());
                l.insert((String)((Card)temp.getObject()).getCardNum());
                try {
                    Object o = SocketClient.Run(l);
                    System.out.println((String)o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PaymentDelete.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}
