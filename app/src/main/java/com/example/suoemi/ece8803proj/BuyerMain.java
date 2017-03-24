package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class BuyerMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_main);

        Button btn =(Button)findViewById(R.id.begin_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerMain.this, SellerMain.class);
                mIntent.putExtra("FROM_ACTIVITY", "BuyerMain");
                startActivity(mIntent);
            }
        });
    }
}
