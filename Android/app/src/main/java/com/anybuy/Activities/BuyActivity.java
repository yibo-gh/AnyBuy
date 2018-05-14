package com.anybuy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anybuy.R;
import com.anybuy.Clients.SocketClient;

import java.sql.Timestamp;

import Object.*;

public class BuyActivity extends AppCompatActivity  {

    EditText productBrand;
    EditText productName;
    EditText county;
    EditText quantity;

    Uri imageURI;

    ImageView productImage;

    Button orderButton;
    String combineBuyPage;

    String imageURIStr = "";
    private static final int REQUEST_CODE = 1;
    LinearLayout  linearLayout;
    private final int PICK_IMAGE_REQUEST = 1;
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
                String combineBuyPagestr;
                String quantityNum = quantity.getText().toString();

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

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

}
