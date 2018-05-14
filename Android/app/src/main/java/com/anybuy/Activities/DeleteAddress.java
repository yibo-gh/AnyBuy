package com.anybuy.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.anybuy.Clients.SocketClient;
import com.anybuy.R;

import Object.LinkedList;
import Object.Address;
import Object.Node;

public class DeleteAddress extends Activity {

    TextView result;
    Spinner spinner;

    private static String[] options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        Spinner spinnerspinner = (Spinner) findViewById(R.id.spinner);
        System.out.println("is spinner null? " + (result == null));

        LinkedList l = new LinkedList();
        l.insert("lda");
        l.insert(MainActivity.getID());
        LinkedList aList = new LinkedList();
        try {
            Object o = SocketClient.Run(l);
            if (o.getClass().equals(new LinkedList().getClass())){
                l = (LinkedList) o;
                Node temp = l.head;
                int i = 1;
                while (temp != null){
                    String str = i + ". ";
                    Address a = (Address) temp.getObject();
                    str += a.getFN();
                    str += " ";
                    str += a.getLN();
                    str += ", ";
                    str = str + a.getCom() + "\n" + a.getL1() + "\n" + a.getL2();
                    str = str + "\n" + a.getCity() + ", " + a.getState() + " " + a.getZip();
                    aList.insert(str);
                    i++;
                    temp = temp.getNext();
                }
            } else aList.insert("null");
        } catch (Exception e) {
            e.printStackTrace();
        }

        options = new String[aList.getLength()];

        Node temp = aList.head;

        int i = 0;
        while (temp != null){
            options[i] = (String) temp.getObject();
            temp = temp.getNext();
            System.out.println(options[i]);
            i++;
        }


        System.out.println("spinner initialized.");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        System.out.println("adapter is null = " + adapter == null);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                result.setText(((TextView)arg1).getText());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

}