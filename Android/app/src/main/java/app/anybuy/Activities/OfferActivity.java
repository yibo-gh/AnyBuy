package app.anybuy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import app.anybuy.R;

public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        TextView display =  (TextView) findViewById(R.id.offerPageTextView);
        display.setText(SellActivity.getGetStrID());
    }
}
