package app.anybuy.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

import Object.LinkedList;
import Object.Node;
import Object.Order;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;



public class SellActivity extends AppCompatActivity {

    String maxOrder = "";
    String minOrder = "";

    private TextView textView;

    String sessionID;
    // use to get the location
    protected FusedLocationProviderClient mFusedLocationClient;

    double lattitude = -1;
    double longitude = -1;

    //use to get the address from the location
    List<Address> addresses;
    Geocoder geocoder;

    static String userCountryCode = null;

    //setter and getter to store the country code
    public static void setUserCountryCode(String str) {userCountryCode = str;}
    public static String getUserCountryCode() {return userCountryCode;}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        Button orderButton = (Button) findViewById(R.id.getOrderButtonID);

/*

        Button newAddress = (Button) findViewById(R.id.NewAddressbuttonID);
        Button deleteAddress = (Button) findViewById(R.id.deleteButton);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout01);
        tableLayout.setStretchAllColumns(true);

 */
        textView = (TextView) findViewById(R.id.locationTextViewID);


        //get the location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //ask the user to allow access to their location

        //if no permission to access the location, ask again
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SellActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        }

        //get the address from the location
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {

                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    //textView.setText("altetude: " + location.getLatitude() + " \n Longtitude: " + location.getLongitude());

                    lattitude = location.getLatitude();
                    longitude = location.getLongitude();

                    geocoder = new Geocoder(SellActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(lattitude, longitude, 1);

                        //get the info of the user
                        // String address = addresses.get(0).getAddressLine(0);
                        //String area = addresses.get(0).getLocality();
                        //String city = addresses.get(0).getAdminArea();
                        //String countryName = addresses.get(0).getCountryName();

                        setUserCountryCode(addresses.get(0).getCountryCode());

                        //String postalCode = addresses.get(0).getPostalCode();

                        textView.setText("contry code: "+ getUserCountryCode());
                    } catch (Exception e) {

                    }
                }

            }

        });



        /*



        try {
            Object o = SocketClient.Run(l);

            if (o.getClass().equals("".getClass())) {
                TableRow tableRow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText((String) o);
                tableRow.addView(tv);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                deleteAddress.setEnabled(false);
            } else if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                if (l.getLength() == 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView tv = new TextView(this);
                    tv.setText("No address found on profile.");
                    tableRow.addView(tv);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
                    deleteAddress.setEnabled(false);
                } else {
                    Node temp = l.head;
                    while (temp != null) {
                        Address a = (Address) temp.getObject();

                        for (int i = 0; i < 7; i++) {
                            TableRow tableRow = new TableRow(this);
                                System.out.println("Writing row " + i + ".");
                                TextView tv = new TextView(this);
                                if (i == 0){
                                    tv.setText(a.getFN() + " " + a.getLN());
                                    tableRow.addView(tv);
                                } else if (i == 1 && !a.getCom().equals("")){
                                    tv.setText(a.getCom());
                                    tableRow.addView(tv);
                                } else if (i == 2){
                                    tv.setText(a.getL1());
                                    tableRow.addView(tv);
                                } else if (i == 3 && !a.getL2().equals("")){
                                    tv.setText(a.getL2());
                                    tableRow.addView(tv);
                                } else if (i == 4){
                                    tv.setText(a.getCity() + ", " + a.getState());
                                    tableRow.addView(tv);
                                } else if (i == 5){
                                    tv.setText(a.getZip());
                                    tableRow.addView(tv);
                                } else if (i == 6) {
                                    tv.setText("");
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
         */
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(getUserCountryCode() + " heyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");


                String data = "";
                sessionID = MainActivity.getID();
                LinkedList l = new LinkedList();
                l.insert("lop");
                l.insert(sessionID);
                l.insert(getUserCountryCode());
                l.insert(10);

                System.out.println("noooooooooooooooooooooooooooooooooooo");

                try {

                    Object o = SocketClient.Run(l);
                    if (o.getClass().equals("".getClass())) System.out.println((String) o);
                    else if (o.getClass().equals(new LinkedList().getClass())) {
                        LinkedList l1 = (LinkedList) o;

                        Node temp = l1.end;

                        temp = temp.getPrev().getPrev();
                        while (temp != null) {

                            Order od = (Order) temp.getObject();


                            System.out.println(od.getImage() + " " + od.getBrand() + " " + od.getProduct() +
                                    " " + od.getQuantity() + " " + od.getCountry() + " " + od.getTimestamp());


                            data += "Product Name: " + od.getProduct() + "\nBrand Name: " + od.getBrand() +
                                    "\nQuantity: " + od.getQuantity() + "\nCountry Code: " + od.getCountry() + "\n" + "\n";

                            textView.setText(data);
                            if (temp.getNext().getNext() == null) maxOrder = ((Order)temp.getObject()).getImage();

                            temp = temp.getPrev();
                            System.out.println("next order");
                        }

                    } else System.out.println("lop function returned sth else.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("helllllllll yeaaaaaaaaaaaaaaaaaaaaaaaa");
            }
        });


    }

    // tryed to make a function for it but failed, feel free to try
}