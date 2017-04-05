package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class BuyerProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_prof);

        Button logoutbtn = (Button) findViewById(R.id.bplogout_btn);
        Button mainbtn = (Button) findViewById(R.id.bpmain_btn);
        Button loc_btn = (Button) findViewById(R.id.bloc_btn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, LoginActivity.class);
                startActivity(mIntent);
            }
        });

        mainbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, BuyerMain.class);
                startActivity(mIntent);
            }
        });
    }
}
