package app.anybuy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Object.LinkedList;
import Object.Offer;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;
public class OfferActivity extends AppCompatActivity {
    private static String offer;
    private static String express;
    private static String remarkstr;

    public static String getRemarkstr() {
        return remarkstr;
    }

    public static void setRemarkstr(String remark) {
        OfferActivity.remarkstr = remark;
    }

    public static void setOffer(String offer) {
        OfferActivity.offer = offer;
    }

    public static void setExpress(String express) {
        OfferActivity.express = express;
    }

    public static String getOffer() {
        return offer;
    }

    public static String getExpress() {
        return express;
    }

    EditText expressCost;
    EditText makeOffer;
    EditText remark;
    Spinner shippingOption;
    private int sOp;

    boolean offerBool;
    String []sID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

         offerBool = false;
         makeOffer = (EditText) findViewById(R.id.makeOfferEditTextID);
         shippingOption = (Spinner) findViewById(R.id.shipSpinner);

         TextView display =  (TextView) findViewById(R.id.offerPageTextView);
         expressCost = (EditText) findViewById(R.id.expressCostEditViewID);
         remark = (EditText)findViewById(R.id.remarkEditTextID);

        Button sendOfferButton = (Button) findViewById(R.id.sendOfferButtonID) ;
        String dataOrders = getIntent().getStringExtra("myData");

        //split the string to get the different info
        String[] splitByNextLine = dataOrders.split("\n");

        final String[] shippingOpt = new String[] {"Express", "First Class",
            "Priority First Class", "Other"};
        final int[] sOpInt = new int[] {1, 2, 3, 4};

        final String getRemark = remark.getText().toString();
        for(int i = 0 ; i < splitByNextLine.length; i++)
        {
            System.out.println(splitByNextLine[i] + " ................. ");

        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shippingOpt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shippingOption.setAdapter(adapter);

        shippingOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sOp = sOpInt[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                sOp = 1;

            }
        });

        //split by space
        String[] splitter = splitByNextLine[4].split(" ");

        //this string will return the orderID
        final String imageID = splitter[2];

        display.setText(dataOrders);
        System.out.println("the imageID is: " + imageID);

        sID = MainActivity.getID().split("\\?");
        //System.out.println(imageID + "\n id: " + MainActivity.getID() + "\n rate: " +  offerMade + "\n express cost: " + expressC + "\nremark: " + getRemark );
        //when offerbutton is clicked send Data
        sendOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                setOffer(makeOffer.getText().toString());
                setExpress(expressCost.getText().toString());
                setRemarkstr(remark.getText().toString());

                //make sure none of the edit texts are empty.
                if (getOffer().matches("")) {
                    Toast.makeText(OfferActivity.this, "You did not enter a rate!", Toast.LENGTH_SHORT).show();
                } else if (getExpress().matches("")) {
                    Toast.makeText(OfferActivity.this, "You did not enter a express cost!", Toast.LENGTH_SHORT).show();
                } else if (sOp == 4 && getRemarkstr().matches("")) {
                    Toast.makeText(OfferActivity.this, "Please tell buyer how you want to send it!", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(getOffer() + "+++++++++" + getExpress() + "++++++++++" + getRemarkstr());
                    //get the offer seller wants to make
                    int offerMadeInt = Integer.parseInt(getOffer());

                    int expressC = Integer.parseInt(getExpress());

                    System.out.println(imageID + "\n id: " + MainActivity.getID() + "\n rate: " + offerMadeInt + "\n express cost: " + expressC + "\nremark: " + getRemarkstr());
                    Offer makeOffer = new Offer(imageID, sID[0], offerMadeInt, expressC, sOp, offerBool, getRemarkstr());

                    LinkedList linkedList = new LinkedList();
                    linkedList.insert("gvr");
                    linkedList.insert(MainActivity.getID());
                    linkedList.insert(makeOffer);

                    try {
                        String o = (String) SocketClient.Run(linkedList);
                    } catch (Exception e) {
                        System.out.println("didnt work");
                    }


                    Intent intent = new Intent(OfferActivity.this, SellActivity.class);
                    startActivity(intent);

               }
            }
        });

    }
}
