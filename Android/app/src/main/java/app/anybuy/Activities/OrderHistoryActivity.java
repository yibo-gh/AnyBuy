package app.anybuy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import app.anybuy.Clients.SocketClient;
import app.anybuy.R;
import Object.LinkedList;
import Object.UserOrderHis;
import Object.Node;

public class OrderHistoryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_order_history);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout02);
        tableLayout.setStretchAllColumns(true);

        LinkedList l = new LinkedList();
        l.insert("ldl");
        l.insert(MainActivity.getID());

        try {
            Object o = SocketClient.Run(l);
            if (o.getClass().equals("".getClass())) {
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) o);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
            } else if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                if (l.getLength() == 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView tv = new TextView(this);
                    tv.setText("No history order.");
                    tableRow.addView(tv);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                } else {
                    Node temp = l.head;
                    while (temp != null) {
                        UserOrderHis uoh = (UserOrderHis) temp.getObject();
                        for (int i = 0; i < 7; i++) {
                            TableRow tableRow = new TableRow(this);
                            System.out.println("Writing row " + i + ".");
                            TextView tv = new TextView(this);
                            final String orderID = uoh.getOrder().getImage();
                            int ordStatus = uoh.getOrderStatus();
                            if (i == 0) {
                                tv.setText(orderID + " " + orderStatus(ordStatus));
                                tableRow.addView(tv);
                                ImageView img = new ImageView(this);
                                Drawable draw = MainActivity.loadImageFromOrderID(orderID);
                                img.setImageDrawable(draw);
                                tableRow.addView(img);
                            } else if (i == 2){
                                tv.setText("Made by: " + uoh.getOrder().getBrand());
                                tableRow.addView(tv);
                                // Cancel button
                            } else if (i == 3){
                                tv.setText(uoh.getOrder().getQuantity() + " item(s) requested from " + uoh.getOrder().getCountry());
                                tableRow.addView(tv);
                            } else if (i == 4){
                                tv.setText("Ordered at " + uoh.getOrder().getTimestamp());
                                tableRow.addView(tv);
                            } else if (i == 5){
                                tv.setText("\n");
                                tableRow.addView(tv);
                            } else if (i == 6){
                                Button bt = new Button(this);
                                if (ordStatus == 0 | ordStatus == 1) {
                                    bt.setText("Cancel");
                                    bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            LinkedList l = new LinkedList();
                                            l.insert("cco");
                                            l.insert(MainActivity.getID());
                                            l.insert(orderID);
                                            try {
                                                SocketClient.Run(l);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Intent intent = new Intent(OrderHistoryActivity.this, OrderHistoryActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    tableRow.addView(bt);
                                }
                                System.out.println("Current orderID is " + orderID);
                                Button bt2 = new Button(this);
                                bt2.setText("View detail");
                                bt2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       OrderDetailActivity.setOrderID(orderID);
                                        Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                tableRow.addView(bt2);
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


    }

    private static String orderStatus(int status){
        switch (status){
            case 0: return "Placed";
            case 1: return "Rate provided";
            case 2: return "Rate accepted";
            case 3: return "Shipped";
            case 4: return "Delivered";
            case 5: return "Cancelled";
        }
        return "";
    }
}
