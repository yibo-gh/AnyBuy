package app.anybuy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TableLayout;

import app.anybuy.R;
import Object.LinkedList;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableLayout02);
        tableLayout.setStretchAllColumns(true);

        LinkedList l = new Object.LinkedList();




    }
}
