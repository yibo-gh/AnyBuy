package com.example.ali.anybuy;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BuyActivity extends AppCompatActivity  {

    EditText productBrand;
    EditText productName;
    EditText county;
    EditText quantity;

    ImageView productImage;

    Button orderButton;


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

                String quantityNum = quantity.getText().toString();

                try
                {
                    int numberOfOrders = Integer.parseInt(quantityNum);
                }
                catch (NumberFormatException e)
                {
                    // handle the exception
                }

                //after giving the data to the back end we want to erase everything on the page so that the user can order another product
                productBrand.setText("");
                productName.setText("");
                quantity.setText("");
                county.setText("");

                productImage.setImageResource(R.drawable.ic_launcher_foreground);

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
