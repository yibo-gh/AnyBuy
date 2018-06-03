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

    //getter and setter to get the city and state and zipcode from address class

    private Card selectedCard;
    private Address selectedAddress;

    static Card[] cArray;
    static Address[] aArray;

    EditText productBrand;
    EditText productName;
    EditText quantity;
    EditText country;
    EditText imageURL;
    Spinner spinner;
    Spinner addressSpinner;
    Spinner paymentSpinner;
    Object o;
    private static String[] cards;
    private static String[] addresses;

    Button orderButton;
    String countryStr;

    private static final int REQUEST_CODE = 1;

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
        country = (EditText) findViewById(R.id.countryEditTextID);
        imageURL = (EditText) findViewById(R.id.imageURL);

        orderButton = (Button) findViewById(R.id.orderButtonID);


        addressSpinner = (Spinner) findViewById(R.id.AddressSpinner);
        paymentSpinner = (Spinner) findViewById(R.id.PaymentSpinner);

        selectedCard = null;
        selectedAddress = null;
        cArray = null;
        aArray = null;



        LinkedList l = new LinkedList();
        l.insert("ldc");
        l.insert(MainActivity.getID());

        try {
            o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                cArray = new Card[l.getLength()];
                cards = new String[l.getLength()];
                Node temp = l.head;
                int i = 0;
                while (temp != null) {
                    Card cd = (Card) temp.getObject();
                    cArray[i] = cd;
                    cards[i] = cd.getIssuser() + " " + cd.getCardNum();
                    i++;
                    temp = temp.getNext();
                }
            } else {
                cards = new String[]{"Please go to profile and add a new card"};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        l = new LinkedList();
        l.insert("lda");
        l.insert(MainActivity.getID());
        try {
            o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                aArray = new Address[l.getLength()];
                addresses = new String[l.getLength()];
                Node temp = l.head;
                int i = 0;
                while (temp != null){
                    Address a = (Address) temp.getObject();
                    aArray[i] = a;
                    addresses[i] = a.getFN() + " " + a.getLN() + ", " + a.getL1()
                            + " @ " + a.getCity() + ", " + a.getState() + " " + a.getZip();
                    i++;
                    temp = temp.getNext();
                }
            } else addresses = new String[] {"Please go to profile and add a new address"};
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println("adapter is null = " + (adapter == null));
        spinner.setAdapter(adapter);
        paymentSpinner.setAdapter(adapter1);
        addressSpinner.setAdapter(adapter2);

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

            addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (aArray != null) selectedAddress = aArray[arg2];
                else selectedAddress = null;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                if (aArray != null) selectedAddress = aArray[0];
                else selectedAddress = null;

            }
        });

        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (cArray != null) selectedCard = cArray[arg2];
                else selectedCard = null;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                if (cArray != null) selectedCard = cArray[0];
                else selectedCard = null;

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Strings to set the edit text to them
                String productBrandstr = productBrand.getText().toString();
                String productNamestr = productName.getText().toString();
                String quantityNum = quantity.getText().toString();
                String imageUrlStr = imageURL.getText().toString();

                System.out.println("Fields filled.");

                if (!isNumeric(quantityNum) || quantityNum.equals("")) {
                    Toast.makeText(BuyActivity.this, "Invalid quantity.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    productBrandstr = strPreProcess(productBrandstr);
                    productNamestr = strPreProcess(productNamestr);


                    try {
                        int numberOfOrders = Integer.parseInt(quantityNum);
                    } catch (NumberFormatException e) {
                        // handle the exception
                    }

                    String sessionID = MainActivity.getID();

                    //get the spinner's values and store them in a string
                    //            String addressOfOrder = addressSpinner.getSelectedItem().toString();
                    //            String paymentMethodStr = paymentSpinner.getSelectedItem().toString();

                    System.out.println("Spinner value received.");

                    //    combineBuyPage = "plo&" + sessionID + "&" + countrystr + "?" + productNamestr + "?" + productBrandstr +"?" + imageURIStr +"?"+ quantityNum;
                    //    String res = SocketClient.run(combineBuyPage);
                    //    System.out.println(res);
                    //    System.out.println(sessionID);


                    if (countryStr.equals("")) {
                        Toast.makeText(BuyActivity.this, "Invalid Country Selection.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (productBrandstr.equals("") || productNamestr.equals("")){
                        Toast.makeText(BuyActivity.this, "Invalid Product Info.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    LinkedList l = new LinkedList();
                    l.insert("plo");
                    l.insert(sessionID);


                    System.out.println("imageUrlStr = \"\" " + (imageUrlStr.equals("")));

                    if (selectedAddress == null) {
                        Toast.makeText(BuyActivity.this, "Invalid Shipping Info Selected.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectedCard == null) {
                        Toast.makeText(BuyActivity.this, "Invalid Payment Info Selected.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Order od = new Order(productNamestr, productBrandstr, Integer.parseInt(quantityNum),
                            countryStr, imageUrlStr, new Timestamp(System.currentTimeMillis()));


                    UserShippingInfo usi = new UserShippingInfo(selectedAddress.getL1(), selectedAddress.getCity(),
                            selectedAddress.getState(), selectedAddress.getZip(), selectedCard.getCardNum());
                    InitialOrder io = new InitialOrder(od, usi);
                    l.insert(io);

                    try {
                        SocketClient.Run(l);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(BuyActivity.this, BuyActivity.class);
                    startActivity(intent);

                }

            }

        });


    }

}
