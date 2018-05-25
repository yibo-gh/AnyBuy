package app.anybuy.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;

import Object.*;
import app.anybuy.Clients.SocketClient;
import app.anybuy.R;


public class BuyActivity extends AppCompatActivity  {

    EditText productBrand;
    EditText productName;
    EditText quantity;
    EditText country;
    Spinner spinner;
    Spinner addressspinner;
    Spinner paymentspinner;
    Object o;
    private static String[] cards;
    private static String[] addresses;

    Uri imageURI;

    InputStream inputStream;

    String path = "didnt work";
    String fileNaame = "also didn't work";
    ImageView productImage;

    File file;
    Button orderButton;

    String imageURIStr = "";
    String countryStr;
    String addressStr;
    String paymentStr;

    private static final int REQUEST_CODE = 1;
    LinearLayout  linearLayout;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    public static String strPreProcess(String str){

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
        spinner = (Spinner) findViewById(R.id.spinnerCountry);
        productImage = (ImageView) findViewById(R.id.productImageViewID);

        country = (EditText) findViewById(R.id.countryEditTextID);

        orderButton = (Button) findViewById(R.id.orderButtonID);


        addressspinner = (Spinner) findViewById(R.id.AddressSpinner);
        paymentspinner = (Spinner) findViewById(R.id.PaymentSpinner);

        // When you click the imgae, you get to search through library and post a picture.
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        LinkedList l = new LinkedList();
        l.insert("ldc");
        l.insert(MainActivity.getID());
        LinkedList cList = new LinkedList();

        try {
            o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                Node temp = l.head;
                int i = 1;
                while (temp != null){
                    String cstr = i + ". ";
                    Card cd = (Card) temp.getObject();
                    cstr = cstr + cd.getFN() + " " + cd.getLN();
                    cstr = cstr + "\n" + cd.getIssuser() + " " + cd.getCardNum();
                    cstr = cstr + "\n" + cd.getExp() + " " + cd.getZip();
                    cList.insert(cstr);
                    i++;
                    temp = temp.getNext();
                }
            } else {
                cList.insert("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cards = new String[cList.getLength()];

        Node temp = cList.head;

        int i = 0;
        while (temp != null){
            cards[i] = (String) temp.getObject();
            temp = temp.getNext();
            System.out.println(cards[i]);
            i++;
        }


        l = new LinkedList();
        l.insert("lda");
        l.insert(MainActivity.getID());
        LinkedList aList = new LinkedList();
        try {
            o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                temp = l.head;
                 i = 1;
                while (temp != null){
                    String astr = i + ". ";
                    Address a = (Address) temp.getObject();
                    astr += a.getFN();
                    astr += " ";
                    astr += a.getLN();
                    astr += ", ";
                    astr = astr + a.getCom() + "\n" + a.getL1() + "\n" + a.getL2();
                    astr = astr + "\n" + a.getCity() + ", " + a.getState() + " " + a.getZip();
                    aList.insert(astr);
                    i++;
                    temp = temp.getNext();
                }
            } else aList.insert("null");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addresses = new String[aList.getLength()];

        temp = aList.head;

        i = 0;
        while (temp != null){
            addresses[i] = (String) temp.getObject();
            temp = temp.getNext();
            System.out.println(addresses[i]);
            i++;
        }


        final String[] countryCode = {""

                ,""

                , "CA"
                , "CAB", "CBC", "CMB"
                , "CNB", "CNL"
                , "CNS", "CNT", "CNU", "CON"
                , "CPE", "CQC", "CSK"
                , "CTY"

                ,""

                , "IR"

                ,""

                , "TW"
                , "RHK", "RMO"
                , "RML", "RTW"

                ,""

                , "KR"
                , "RKR", "RKP"

                ,""

                , "SG"

                ,""

                , "US", "KDC"
                , "KAL", "KAZ", "KAR"
                , "KCA", "KCO"
                , "KCT", "KDE", "KFL"
                , "KGA", "KHI", "KID"
                , "KIL", "KIN", "KIA"
                , "KKS", "KKY", "KLA"
                , "KME", "KMD", "KMA"
                , "KMI", "KMN", "KMS"
                , "KMO", "KMT", "KNE"
                , "KNV", "KNH", "KNJ"
                , "KNM", "KNY", "KNC"
                , "KND", "KOH", "KOK"
                , "KOR", "KPA", "KRI"
                , "KSC", "KSD", "KTN"
                , "KTX", "KUT", "KVT"
                , "KVA", "KWA", "KWV"
                , "KWI", "KWY"};

        final String[] options = {"Please select where you want your item."

                ,"------ ( •̀ .̫ •́ )✧ ------"

                , "Canada"
                , "Canada - Alberta", "Canada - British Columbia", "Canada - Manitoba"
                , "Canada - New Brunswick" , "Canada - Newfoundland and Labrador"
                , "Canada - NW Territories", "Canada - Nova Scotia", "Canada - Nunavut", "Canada - Ontario"
                , "Canada - Prince Edward Island", "Canada - Quebec", "Canada - Saskatchewan"
                , "Canada - Yukon"

                ,"------ (๑¯◡¯๑) ------"

                , "Islamic Republic of Iran جمهوری اسلامی ایران"

                ,"------ (๑•̀ㅂ•́) ✧ ------"

                , "Republic of China 中華民國"
                , "Republic of China - Hong Kong 中華民國 香港", "Republic of China - Macau中華民國 澳門"
                , "Republic of China - Mainland 中華民國 大陸", "Republic of China - Taiwan 中華民國 台灣"

                ,"------ ლ(╹◡╹ლ) ------"

                , "Republic of Korea 대한민국"
                , "Republic of Korea - South 대한민국 남부", "Republic of Korea - North 대한민국 북부"

                ,"------ (๑•́ ∀ •̀๑) ------"

                , "Republic of Singapore 新加坡共和國"

                ,"------ (ง •̀_•́)ง ------"

                , "United States", "United States - Washington D.C."
                , "United States - Alabama"
                , "United States - Alaska", "United States - Arizona", "United States - Arkansas"
                , "United States - California the Great", "United States - Colorado"
                , "United States - Connecticut", "United States - Delaware", "United States - Florida"
                , "United States - Georgia", "United States - Hawaii", "United States - Idaho"
                , "United States - Illinois", "United States - Indiana", "United States - Iowa"
                , "United States - Kansas", "United States - Kentucky", "United States - Louisiana"
                , "United States - Maine", "United States - Maryland", "United States - Massachusetts"
                , "United States - Michigan", "United States - Minnesota" , "United States - Mississippi"
                , "United States - Missouri", "United States - Montana", "United States - Nebraska"
                , "United States - Nevada", "United States - New Hampshire", "United States - New Jerset"
                , "United States - New Mexico", "United States - New York", "United States - N. Carolina"
                , "United States - N.Dakota", "United States - Ohio", "United States - Oklahoma"
                , "United States - Oregon", "United States - Pennsylvania", "United States - Rhode Island"
                , "United States - S. Carolina", "United States - S. Dakota", "United States - Tennessee"
                , "United States - Texas", "United States - Utah", "United States - Vermont"
                , "United States - Virginia", "United States - Washington", "United States - W. Virginia"
                , "United States - Wisconsin", "United States - Wyoming"};


        System.out.println("spinner initialized.");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cards);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addresses);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        System.out.println("adapter is null = " + adapter == null);
        spinner.setAdapter(adapter);
        addressspinner.setAdapter(adapter1);
        paymentspinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String str = adapter.getItem(arg2);
                System.out.println("Choice is " + str);
                countryStr = countryCode[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

            addressspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        paymentspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Strings to set the edit text to them
                String productBrandstr = productBrand.getText().toString();
                String productNamestr = productName.getText().toString();
                String quantityNum = quantity.getText().toString();

                if (!isNumeric(quantityNum)) {
                    Toast.makeText(BuyActivity.this, "Invalid quantity.", Toast.LENGTH_LONG).show();
                    return;
                }
               else {
                    productBrandstr = strPreProcess(productBrandstr);
                    productNamestr = strPreProcess(productNamestr);


                    try {
                        int numberOfOrders = Integer.parseInt(quantityNum);
                    } catch (NumberFormatException e) {
                        // handle the exception
                    }

                    String sessionID = MainActivity.getID();
                    //    combineBuyPage = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?" + imageURIStr +"?"+ quantityNum;
                    //    String res = SocketClient.run(combineBuyPage);
                    //    System.out.println(res);
                    //    System.out.println(sessionID);

                    if (countryStr.equals("")) {
                        Toast.makeText(BuyActivity.this, "Invalid Country Selection.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    LinkedList l = new LinkedList();
                    l.insert("plo");
                    l.insert(sessionID);


                    Order od = new Order(productNamestr, productBrandstr, Integer.parseInt(quantityNum),
                            countryStr , "false", new Timestamp(System.currentTimeMillis()))
                            ;
                    System.out.println("name: " + od.getProduct());
                    l.insert(od);

                    try {
                        Object o = SocketClient.Run(l);
                        if (o.getClass().equals("".getClass())) System.out.println((String) o);
                        else if (o.getClass().equals(new LinkedList().getClass())) {
                            LinkedList l1 = (LinkedList) o;
                            System.out.println(l1.getLength() + " image(s) requested.");
                        } else System.out.println("plo function returned sth else.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    System.out.println("wowwwwwwwwwwwwwwwwwwwwww : ProductName: " + productBrandstr + "\nProductBrand: "+ productBrandstr +
                            "\nQuantity: " + Integer.parseInt(quantityNum) + "\ncoutry: "
                            + countryStr + "\nImage url: " + false + "\ntime: " + new Timestamp(System.currentTimeMillis())  );


                    //productImage.get

                    //combine the Strings to get it all fixed up for the database api's
                    //combineBuyPagestr = "plo&sessionID&" + countrystr + "?" + productNamestr + "?" + productBrandstr + "?"<Image>?<Quantity>
                    //after giving the data to the back end we want to erase everything on the page so that the user can order another product


                    productBrand.setText("");
                    productName.setText("");
                    quantity.setText("");

                    productImage.setImageResource(android.R.drawable.ic_input_add);

                }

/*
                String sessionID = MainActivity.getID();
            //    combineBuyPage = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?" + imageURIStr +"?"+ quantityNum;
            //    String res = SocketClient.run(combineBuyPage);
            //    System.out.println(res);
            //    System.out.println(sessionID);

                LinkedList l = new LinkedList();
                l.insert("plo");
                l.insert(sessionID);
                Order od = new Order(productNamestr, productBrandstr, Integer.parseInt(quantityNum),
                        countryStr, imageURIStr, new Timestamp(System.currentTimeMillis()));
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

                productImage.setImageResource(android.R.drawable.ic_input_add);


                country.setText("");


                productImage.setImageResource(android.R.drawable.ic_input_add);
*/
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
