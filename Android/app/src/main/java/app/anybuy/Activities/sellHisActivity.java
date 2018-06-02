package app.anybuy.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Object.LinkedList;
import Object.Node;
import Object.UserOrderHis;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;

public class sellHisActivity extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
    String userOrderSearchOption = "";

    Spinner searchOpt;
    EditText searchKeyword;


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
        searchOpt = (Spinner) findViewById(R.id.sellhisopt);
        searchKeyword = (EditText) findViewById(R.id.sellhiskeyword);
        String[] options = new String[]{"Load All","Search Product Name", "Search Product Brand", "Search Order ID"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchOpt.setAdapter(adapter);

        searchOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2){
                    case 1: userOrderSearchOption = "lda";
                    case 2: userOrderSearchOption = "spn";
                    case 3: userOrderSearchOption = "spb";
                    case 4: userOrderSearchOption = "soi";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });







        LinkedList l = new LinkedList();
        l.insert("lds");
        l.insert(MainActivity.getID());

        System.out.println("LinkedList initialed.");

        try {
            Object o = SocketClient.Run(l);
            System.out.println("Server return received.");
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

                        for (int i = 0; i < 6; i++) {
                            TableRow tableRow = new TableRow(this);
                            System.out.println("Writing row " + i + ".");
                            TextView tv = new TextView(this);
                            if (i == 0){
                                tv.setText(uoh.getOrder().getImage() + " " + orderStatus(uoh.getOrderStatus()));
                                tableRow.addView(tv);
                            } else if (i == 1){
                                tv.setText(uoh.getOrder().getProduct());
                                tableRow.addView(tv);
                            } else if (i == 2){
                                tv.setText("Made by: " + uoh.getOrder().getBrand());
                                tableRow.addView(tv);
                            } else if (i == 3){
                                tv.setText(uoh.getOrder().getQuantity() + " item(s) requested from " + uoh.getOrder().getCountry());
                                tableRow.addView(tv);
                            } else if (i == 4){
                                tv.setText("Ordered at " + uoh.getOrder().getTimestamp());
                                tableRow.addView(tv);
                            } else if (i == 5){
                                tv.setText("\n");
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
