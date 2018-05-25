package app.anybuy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import app.anybuy.Clients.SocketClient;
import app.anybuy.R;

public class sellHisActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_sell_his);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout03);
        tableLayout.setStretchAllColumns(true);

        Object.LinkedList l = new Object.LinkedList();
        l.insert("lds");
        l.insert(MainActivity.getID());

        try {
            Object o = SocketClient.Run(l);
            if (o.getClass().equals("".getClass())) {
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) o);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
            } else if (o.getClass().equals(new Object.LinkedList().getClass())){
                l = (Object.LinkedList) o;
                if (l.getLength() == 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView tv = new TextView(this);
                    tv.setText("No history order.");
                    tableRow.addView(tv);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                } else {
                    Object.Node temp = l.head;
                    while (temp != null) {
                        Object.UserOrderHis uoh = (Object.UserOrderHis) temp.getObject();

                        for (int i = 0; i < 6; i++) {
                            TableRow tableRow = new TableRow(this);
                            System.out.println("Writing row " + i + ".");
                            TextView tv = new TextView(this);
                            if (i == 0){
                                tv.setText(uoh.getOrder().getImage() + " " + orderStatus(uoh.getOrderStatus()));
                                tableRow.addView(tv);
                            } else if (i == 1){
                                tv.setText(uoh.getOrder().getImage());
                                tableRow.addView(tv);
                            } else if (i == 2){
                                tv.setText(uoh.getOrder().getProduct());
                                tableRow.addView(tv);
                            } else if (i == 3){
                                tv.setText("Made by: " + uoh.getOrder().getBrand());
                                tableRow.addView(tv);
                            } else if (i == 4){
                                tv.setText(uoh.getOrder().getQuantity() + " item(s) requested from " + uoh.getOrder().getCountry());
                                tableRow.addView(tv);
                            } else if (i == 5){
                                tv.setText("Ordered at " + uoh.getOrder().getTimestamp());
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
