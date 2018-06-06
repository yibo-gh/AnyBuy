package app.anybuy.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Object.LinkedList;
import Object.Order;
import Object.UserOrderHis;
import Object.Node;
import Object.Offer;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;

public class OrderDetailActivity extends AppCompatActivity {

    private static String orderID = "";
    private static String acceptanceget;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    private static String imgURL = "";
    private static int orderStatus;

    private ImageView imView;

    public static void setOrderID (String str){
        orderID = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("orderID received: " + orderID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        TableLayout orderTable = (TableLayout)findViewById(R.id.OrderDetailTable);
        orderTable.setStretchAllColumns(true);
        TableRow tableRow = new TableRow(this);


        TableLayout rateTable = (TableLayout)findViewById(R.id.offerTable);
        rateTable.setStretchAllColumns(true);
        TableRow rateTableRow = new TableRow(this);

        imView = (ImageView) findViewById(R.id.detailImage);

        LinkedList ll = new LinkedList();
        ll.insert("lod");
        ll.insert(MainActivity.getID());
        if (orderID == null){
            Toast.makeText(OrderDetailActivity.this, "Illegal Order ID!", Toast.LENGTH_LONG).show();
            return;
        }
        ll.insert(orderID);
        TextView tv = new TextView(this);

        try {
            Object obj = SocketClient.Run(ll);
            if (obj.getClass().equals("".getClass())) {
                tv = new TextView(this);
                tv.setText((String) obj);
                tableRow.addView(tv);
                orderTable.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
            } else {
                UserOrderHis uoh = (UserOrderHis) obj;
                Order o = uoh.getOrder();

                orderStatus = uoh.getOrderStatus();
                String text = orderID + " " + orderStatus(orderStatus) + "\n" + "Made by: " + uoh.getOrder().getBrand() +
                       "\n" + uoh.getOrder().getQuantity() + " item(s) requested from " + uoh.getOrder().getCountry()
                        + "\n"  + "Ordered at " + uoh.getOrder().getTimestamp() + "\n";
                tv = new TextView(this);
                tv.setText(text);
                tableRow.addView(tv);

                orderTable.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                System.out.println("Line added.");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ll = new LinkedList();
        ll.insert("cke");
        ll.insert(MainActivity.getID());
        ll.insert(orderID);
        try {
            String img = (String) SocketClient.Run(ll);
            if (img.equals("0x1002")) Toast.makeText(OrderDetailActivity.this, "Internal Error!", Toast.LENGTH_LONG).show();
            if (img != null && !img.equals("false")) imgURL = "http://anybuy.app/img/" + orderID + "." + img;
            System.out.println("Try to set image " + imgURL);
            imView.setImageBitmap(returnBitMap(imgURL));
            System.out.println("Image success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (orderStatus == 1) {
            System.out.println("Requesting rate details");
            ll = new LinkedList();
            ll.insert("lor");
            ll.insert(MainActivity.getID());
            ll.insert(orderID);
            System.out.println(ll.getLength());
            try {
                Object o = SocketClient.Run(ll);
                System.out.println("Is null returned? " + (o == null));
                if (o.getClass().equals("".getClass())) {
                    Toast.makeText(OrderDetailActivity.this, "Internal Error!" + o.toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    String text = "Offers:\n";
                    tv = new TextView(this);
                    tv.setText(text);
                    rateTableRow.addView(tv);
                    rateTable.addView(rateTableRow, new TableLayout.LayoutParams(FP, WC));
                    ll = (LinkedList)o;
                    System.out.println("res length " + ll.getLength());
                    Node temp = ll.head;
                    while (temp != null){
                        rateTableRow = new TableRow(this);
                        Offer of = (Offer) temp.getObject();
                        final String sellerID = of.getSellerID();
                        text = "\n\nRate: " + of.getRate() + "\nShipping Cost: " + of.getExpressCost() +
                                "\nShipping Method: " + getShippingMethod(of.getShippingMethod()) + "\n";
                        tv = new TextView(this);
                        tv.setText(text);
                        rateTableRow.addView(tv);
                        rateTable.addView(rateTableRow, new TableLayout.LayoutParams(FP, WC));
                        rateTableRow = new TableRow(this);

                        System.out.println("ready to load buttons");

                        Button b = new Button(this);
                        b.setText("Accept this rate");
                        b.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {

                                LinkedList linkedList = new LinkedList();
                                linkedList.insert("art");
                                linkedList.insert(MainActivity.getID());
                                linkedList.insert(orderID);
                                linkedList.insert(sellerID);
                                if (sellerID == "") {
                                    Toast.makeText(OrderDetailActivity.this, "Internal Error SID Not Found.", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                try{
                                    //    acceptanceget = (String) SocketClient.Run(linkedList);
                                    Object o = SocketClient.Run(linkedList);
                                    if (o != null) acceptanceget = (String) o;
                                }catch (Exception e){
                                    System.out.println("the accept button didn't give data to backend");
                                }
                                System.out.println("order ID is " + orderID);

                                System.out.println("-=-=-=-=-=-=- " + acceptanceget);

                                Intent intent = new Intent(OrderDetailActivity.this, OrderDetailActivity.class);
                                startActivity(intent);

                            }
                        });
                        rateTableRow.addView(b);
                        rateTable.addView(rateTableRow, new TableLayout.LayoutParams(FP, WC));
                        System.out.println("Everything loaded.");
                        temp = temp.getNext();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap returnBitMap(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
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

    private static String getShippingMethod (int i) {
        switch (i){
            case 1: return "Express";
            case 2: return "First Class Mail";
            case 3: return "Priority First Class Mail";
            case 4: return "Other";
            default: return "";
        }
    }
}
