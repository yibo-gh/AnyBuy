package com.example.ali.anybuy;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BuyActivity extends AppCompatActivity  {

    EditText productBrand;
    EditText productName;
    EditText county;
    EditText quantity;

    ImageView productImage;

    Button orderButton;
    String combine2;


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
                try{chooseImage();}
                catch (Exception ex){}

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
                combine2 = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?test.jpg?"+ quantityNum;
                String res = SocketClient.run(combine2);
                System.out.println(res);
                System.out.println(sessionID);
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
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            productImage.setImageURI(data.getData());

        }
    }
}
