package app.anybuy.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import app.anybuy.Clients.SocketClient;
import app.anybuy.R;
import Object.LinkedList;
import Object.Order;
import Object.UserOrderHis;

public class OrderDetailActivity extends AppCompatActivity {

    private static String orderID = "";

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    private static String imgURL = "";

    private Bitmap bmImg;
    private ImageView imView;

    public static void setOrderID (String str){
        orderID = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("orderID received: " + orderID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.OrderDetailTable);
        tableLayout.setStretchAllColumns(true);

        imView = (ImageView) findViewById(R.id.detailImage);

        LinkedList ll = new LinkedList();
        ll.insert("lod");
        ll.insert(MainActivity.getID());
        if (orderID == null){
            Toast.makeText(OrderDetailActivity.this, "Illegal Order ID!", Toast.LENGTH_LONG).show();
            return;
        }
        ll.insert(orderID);

        try {
            Object obj = SocketClient.Run(ll);
            if (obj.getClass().equals("".getClass())) {
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) obj);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
            } else {
                UserOrderHis uoh = (UserOrderHis) obj;
                Order o = uoh.getOrder();

                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                String text = orderID + " " + orderStatus(uoh.getOrderStatus()) + "\n" + "Made by: " + uoh.getOrder().getBrand() +
                       "\n" + uoh.getOrder().getQuantity() + " item(s) requested from " + uoh.getOrder().getCountry()
                        + "\n"  + "Ordered at " + uoh.getOrder().getTimestamp() + "\n";
                tv.setText(text);
                tableRow.addView(tv);

                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
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
}