package com.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anybuy.Clients.SocketClient;
import com.anybuy.R;
import Object.Card;
import Object.LinkedList;
import Object.Node;

import java.net.Socket;

public class PaymentActivity extends AppCompatActivity {

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button newpayment = (Button) findViewById(R.id.newCardButtonID);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout01);
        tableLayout.setStretchAllColumns(true);

        LinkedList l = new LinkedList();
        l.insert("ldc");
        l.insert(MainActivity.getID());

        try {
            Object o = SocketClient.Run(l);
            if (o.getClass().equals("".getClass())) {
                System.out.println((String) o);
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) o);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
            } else {
                l = (LinkedList) o;
                System.out.println("length of LinkedList is " + l.getLength());
                if (l.getLength() == 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView tv = new TextView(this);
                    tv.setText("No card found on profile.");
                    tableRow.addView(tv);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                } else {
                    Node temp = l.head;
                    while (temp != null) {
                        Card c = (Card) temp.getObject();

                        for (int i = 0; i < 4; i++) {
                            TableRow tableRow = new TableRow(this);
                            System.out.println("Writing row " + i + ".");
                            TextView tv = new TextView(this);
                            if (i == 0){
                                tv.setText(c.getFN() + " " + c.getLN());
                                tableRow.addView(tv);
                            } else if (i == 1){
                                tv.setText(c.getIssuser() + " " + c.getCardNum());
                                tableRow.addView(tv);
                            } else if (i == 2){
                                tv.setText(c.getExp() + " " + c.getZip());
                                tableRow.addView(tv);
                            } else if (i == 3) {
                                tv.setText("");
                                tableRow.addView(tv);
                            }

                            tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                        }
                        temp = temp.getNext();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        newpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, PaymentAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
