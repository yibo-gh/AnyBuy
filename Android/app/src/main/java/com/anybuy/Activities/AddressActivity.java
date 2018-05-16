package com.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Object.LinkedList;
import Object.Address;
import Object.Node;

import com.anybuy.Clients.SocketClient;
import com.anybuy.R;

public class AddressActivity extends AppCompatActivity {

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Button newAddress = (Button) findViewById(R.id.NewAddressbuttonID);
        Button deleteAddress = (Button) findViewById(R.id.deleteButton);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout01);
        tableLayout.setStretchAllColumns(true);

        LinkedList l = new LinkedList();
        l.insert("lda");
        l.insert(MainActivity.getID());


        try {
            Object o = SocketClient.Run(l);
            if (o.getClass().equals("".getClass())) {
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) o);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                deleteAddress.setEnabled(false);
            } else if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                if (l.getLength() == 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView tv = new TextView(this);
                    tv.setText("No address found on profile.");
                    tableRow.addView(tv);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                    deleteAddress.setEnabled(false);
                } else {
                    Node temp = l.head;
                    while (temp != null) {
                        Address a = (Address) temp.getObject();

                        for (int i = 0; i < 7; i++) {
                            TableRow tableRow = new TableRow(this);
                                System.out.println("Writing row " + i + ".");
                                TextView tv = new TextView(this);
                                if (i == 0){
                                    tv.setText(a.getFN() + " " + a.getLN());
                                    tableRow.addView(tv);
                                } else if (i == 1 && !a.getCom().equals("")){
                                    tv.setText(a.getCom());
                                    tableRow.addView(tv);
                                } else if (i == 2){
                                    tv.setText(a.getL1());
                                    tableRow.addView(tv);
                                } else if (i == 3 && !a.getL2().equals("")){
                                    tv.setText(a.getL2());
                                    tableRow.addView(tv);
                                } else if (i == 4){
                                    tv.setText(a.getCity() + ", " + a.getState());
                                    tableRow.addView(tv);
                                } else if (i == 5){
                                    tv.setText(a.getZip());
                                    tableRow.addView(tv);
                                } else if (i == 6) {
                                    tv.setText("");
                                    tableRow.addView(tv);
                                }

                            tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                            System.out.println("Line added.");
                        }
                        temp = temp.getNext();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddressAddActivity.class);
                startActivity(intent);
            }
        });

        deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, DeleteAddress.class);
                startActivity(intent);
            }
        });

    }
}
