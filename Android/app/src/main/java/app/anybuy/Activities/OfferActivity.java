package app.anybuy.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.anybuy.R;
public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        boolean offerBool = false;
        EditText makeOffer = (EditText) findViewById(R.id.makeOfferEditTextID);

        TextView display =  (TextView) findViewById(R.id.offerPageTextView);
        EditText expressCost = (EditText) findViewById(R.id.expressCostEditViewID);
        EditText remark = (EditText)findViewById(R.id.remarkEditTextID);

        Button sendOfferButton = (Button) findViewById(R.id.sendOfferButtonID) ;
        String dataOrders = getIntent().getStringExtra("myData");

        //split the string to get the different info
        String[] splitByNextLine = dataOrders.split("\n");

        String getRemark = remark.getText().toString();
        for(int i = 0 ; i < splitByNextLine.length; i++)
        {
            System.out.println(splitByNextLine[i] + " ................. ");

        }

        /*
            splitByNextLine:    1: product Name
                                2: brand name
                                3.quantity
                                4.order number

         */

        //split by space
        String[] splitter = splitByNextLine[4].split(" ");

        //this string will return the orderID
        final String imageID = splitter[2];

        display.setText(dataOrders);
        System.out.println("the imageID is: " + imageID);



        //get the offer seller wants to make
        final String offerMade = makeOffer.getText().toString();
        int offerMadeInt = Integer.parseInt(offerMade);


        int expressC = Integer.parseInt(expressCost.getText().toString());
        System.out.println(imageID + "\n id: " + MainActivity.getID() + "\n rate: " +  offerMade + "\n express cost: " + expressC + "\nremark: " + getRemark );
        //when offerbutton is clicked send Data
        sendOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Offer makeOffer = new Offer(imageID, MainActivity.getID(), offerMade, ,offerBool, getRemark );
            }
        });



    }
}
