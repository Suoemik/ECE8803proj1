package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class BuyerProfile extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_prof);

        Button logoutbtn = (Button) findViewById(R.id.bplogout_btn);
        Button mainbtn = (Button) findViewById(R.id.bpmain_btn);
        final Button loc_btn = (Button) findViewById(R.id.bloc_btn);
        Button no_btn = (Button) findViewById(R.id.bphone_btn);
        Button pass_btn = (Button) findViewById(R.id.bpass_btn);
        Button usr_btn = (Button) findViewById(R.id.busern_btn);
        Button pmnt = (Button) findViewById(R.id.df_pyment_btn);

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

        no_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(BuyerProfile.this);
                builder2
                        .setTitle("Change phone number?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(BuyerProfile.this, SellerInput.class));
                            }
                        })
                        .setNegativeButton("No", null)                        //Do nothing on no
                        .show();
            }
        });

        loc_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loc_btn.setText("ON");
            }
        });

        pass_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, BuyerMain.class);
                startActivity(mIntent);
            }
        });

        usr_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, BuyerMain.class);
                startActivity(mIntent);
            }
        });

        pmnt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, PaymentInput.class);
                startActivity(mIntent);
            }
        });
    }
}
