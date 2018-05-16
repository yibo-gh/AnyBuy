package com.anybuy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anybuy.R;
import com.anybuy.Clients.SocketClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

import Object.*;

public class BuyActivity extends AppCompatActivity  {

    EditText productBrand;
    EditText productName;
    EditText county;
    EditText quantity;

    Uri imageURI;

    InputStream inputStream;

    String path = "didnt work";
    String fileNaame = "also didn't work";
    ImageView productImage;

    File file;
    Button orderButton;

    String imageURIStr = "";
    private static final int REQUEST_CODE = 1;
    LinearLayout  linearLayout;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    private String strPreProcess(String str){

        String res = str;

        if (str.contains("'")){
            String tempStr[] = str.split("\\'");
            res = "";
            for (int i = 0; i < tempStr.length; i++){
                if (i != tempStr.length - 1) res = res + tempStr[i] + "\\'";
                else res += tempStr[i];
            }
        }

        if (str.contains("\"")){
            String[] tempStr = str.split("\\\"");
            res = "";
            for (int i = 0; i < tempStr.length; i++){
                if (i != tempStr.length - 1) res = res + tempStr[i] + "\\\"";
                else res += tempStr[i];
            }
        }

        return res;
    }

    public static boolean isNumeric (String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        productBrand = (EditText) findViewById(R.id.productBrandEditTextID);
        productName = (EditText) findViewById(R.id.productNameEditTextID);
        quantity = (EditText) findViewById(R.id.quantityEditTextID);
        county = (EditText)findViewById(R.id.countryEditTextID);

        productImage = (ImageView) findViewById(R.id.productImageViewID);

        orderButton = (Button) findViewById(R.id.orderButtonID);

        linearLayout = (LinearLayout) findViewById(R.id.picsLayoutID);

        // When you click the imgae, you get to search through library and post a picture.
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();


            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Strings to set the edit text to them
                String productBrandstr = productBrand.getText().toString();
                String productNamestr = productName.getText().toString();
                String countrystr = county.getText().toString();
                String quantityNum = quantity.getText().toString();

                if (!isNumeric(quantityNum)) {
                    Toast.makeText(BuyActivity.this, "Invalid quantity.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    productBrandstr = strPreProcess(productBrandstr);
                    productNamestr = strPreProcess(productNamestr);


                    try
                    {
                        int numberOfOrders = Integer.parseInt(quantityNum);
                    }
                    catch (NumberFormatException e)
                    {
                        // handle the exception
                    }

                    String sessionID = MainActivity.getID();
                    //    combineBuyPage = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?" + imageURIStr +"?"+ quantityNum;
                    //    String res = SocketClient.run(combineBuyPage);
                    //    System.out.println(res);
                    //    System.out.println(sessionID);

                    LinkedList l = new LinkedList();
                    l.insert("plo");
                    l.insert(sessionID);
                    Order od = new Order(productNamestr, productBrandstr, Integer.parseInt(quantityNum),
                            countrystr, imageURIStr, new Timestamp(System.currentTimeMillis()));
                    System.out.println("name: " + od.getProduct());
                    l.insert(od);
                    try {
                        Object o = SocketClient.Run(l);
                        if (o.getClass().equals("".getClass())) System.out.println((String)o);
                        else if (o.getClass().equals(new LinkedList().getClass())){
                            LinkedList l1 = (LinkedList) o;
                            System.out.println(l1.getLength() + " image(s) requested.");
                        } else System.out.println("plo function returned sth else.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //productImage.get

                    //combine the Strings to get it all fixed up for the database api's
                    //combineBuyPagestr = "plo&sessionID&" + countrystr + "?" + productNamestr + "?" + productBrandstr + "?"<Image>?<Quantity>
                    //after giving the data to the back end we want to erase everything on the page so that the user can order another product
                    productBrand.setText("");
                    productName.setText("");
                    quantity.setText("");
                    county.setText("");

                    productImage.setImageResource(android.R.drawable.ic_input_add);
                }
<<<<<<< HEAD
                catch (NumberFormatException e)
                {
                    // handle the exception
                }

                String sessionID = MainActivity.getID();
            //    combineBuyPage = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?" + imageURIStr +"?"+ quantityNum;
            //    String res = SocketClient.run(combineBuyPage);
            //    System.out.println(res);
            //    System.out.println(sessionID);

                LinkedList l = new LinkedList();
                l.insert("plo");
                l.insert(sessionID);
                Order od = new Order(productNamestr, productBrandstr, Integer.parseInt(quantityNum),
                        countrystr, imageURIStr, new Timestamp(System.currentTimeMillis()));
                l.insert(od);
                try {
                    Object o = SocketClient.Run(l);
                    if (o.getClass().equals("".getClass())) System.out.println((String)o);
                    else if (o.getClass().equals(new LinkedList().getClass())){
                        LinkedList l1 = (LinkedList) o;
                        System.out.println(l1.getLength() + " image(s) requested.");
                    } else System.out.println("plo function returned sth else.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //productImage.get
                //combine the Strings to get it all fixed up for the database api's
                //combineBuyPagestr = "plo&sessionID&" + countrystr + "?" + productNamestr + "?" + productBrandstr + "?"<Image>?<Quantity>
                //after giving the data to the back end we want to erase everything on the page so that the user can order another product
                productBrand.setText("");
                productName.setText("");
                quantity.setText("");
                county.setText("");

                productImage.setImageResource(android.R.drawable.ic_input_add);
=======

>>>>>>> b9deab9ee6d89e16ef36919947ade4c0be8d3f3b
            }
        });



    }

    //choose from the phone pictures
    private void chooseImage()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            imageURI = data.getData();
            productImage.setImageURI(imageURI);

            imageURIStr = imageURI.toString();
            System.out.println("Heyyyyyyyyyyyyyyyyy && " + imageURIStr);

        }
    }
}
