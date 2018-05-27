package app.anybuy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import app.anybuy.R;

public class LegalActivity extends AppCompatActivity {

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.legalPageTable);
        tableLayout.setStretchAllColumns(true);

        TableRow tableRow = new TableRow(this);
        TextView tv = new TextView(this);
        tv.setText("I am a serious legal page. ୧(๑•̀⌄•́๑)૭✧");
        tableRow.addView(tv);
        tableLayout.addView(tableRow, new TableLayout.LayoutParams(FP, WC));
    }
}
