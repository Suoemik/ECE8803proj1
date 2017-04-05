package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class SellerProfile extends AppCompatActivity {
    public static Button locbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_prof);

        Button logoutbtn = (Button) findViewById(R.id.splogout_btn);
        Button mainbtn = (Button) findViewById(R.id.spmain_btn);
        locbtn = (Button) findViewById(R.id.bloc_btn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerProfile.this, LoginActivity.class);
                startActivity(mIntent);
            }
        });

        mainbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerProfile.this, SellerMain.class);
                startActivity(mIntent);
            }
        });

        locbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (locbtn.getText().toString()=="OFF"){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerProfile.this);
                builder
                        .setTitle("Turn on your location?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SellerProfile.this, "Location On",
                                        Toast.LENGTH_SHORT).show();
                                locbtn.setText("ON");
                            }
                        })
                        .setNegativeButton("Cancel", null)                        //Do nothing on no
                        .show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerProfile.this);
                    builder
                            .setTitle("Turn off your location?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SellerProfile.this, "Location Off",
                                            Toast.LENGTH_SHORT).show();
                                    locbtn.setText("OFF");
                                }
                            })
                            .setNegativeButton("Cancel", null)                        //Do nothing on no
                            .show();
                }
            }
        });
    }
}
