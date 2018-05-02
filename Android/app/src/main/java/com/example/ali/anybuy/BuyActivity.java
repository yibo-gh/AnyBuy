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
    EditText quantity;

    ImageView productImage;

    Button orderButton;
    Button addButton;

    LinearLayout  linearLayout;
    private final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        productBrand = (EditText) findViewById(R.id.productBrandEditTextID);
        productName = (EditText) findViewById(R.id.productNameEditTextID);
        quantity = (EditText) findViewById(R.id.quantityEditTextID);

        productImage = (ImageView) findViewById(R.id.productImageViewID);

        orderButton = (Button) findViewById(R.id.orderButtonID);
        addButton = (Button) findViewById(R.id.addProductEditTextID);

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

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new ImageView widget
                ImageView newImageView = new ImageView((getApplicationContext()));

                //initially set the pic be a normal pic
                newImageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));

                //set an image for Image view
                //for this part I want to choose a image view from the mobile
                //So I will use the same method I used for our initial image view
                newImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseImage();
                    }
                });

                //create a layout pramete for our image view
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                //add rule to our layout parameters
                //add the image view below the previous image view
                layoutParams.addRule(RelativeLayout.BELOW, addButton.getId());
                newImageView.setLayoutParams(layoutParams);

                linearLayout.addView(newImageView);

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

/*
    // Initialize a new ImageView widget
                ImageView iv = new ImageView(getApplicationContext());

                // Set an image for ImageView
                iv.setImageDrawable(getDrawable(R.drawable.animal));

                // Create layout parameters for ImageView
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                // Add rule to layout parameters
                // Add the ImageView below to Button
                lp.addRule(RelativeLayout.BELOW, btn.getId());

                // Add layout parameters to ImageView
                iv.setLayoutParams(lp);

                // Finally, add the ImageView to layout
                rl.addView(iv);
*/
}
