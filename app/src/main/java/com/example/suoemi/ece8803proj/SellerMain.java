package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import static com.example.suoemi.ece8803proj.SellerInput.selleramt;
import static com.example.suoemi.ece8803proj.SellerInput.sellerprice;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class SellerMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        Intent mIntent = getIntent();
        String prevAct = mIntent.getStringExtra("FROM_ACTIVITY");

        if(prevAct.equals("BuyerMain")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Would you like to participate in this energy auction?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            Button btn = (Button) findViewById(R.id.energybid_btn);
                            Button btn2 = (Button) findViewById(R.id.energybid_btn);
                            Button btn3 = (Button) findViewById(R.id.selsett_btn);
                            Button btn4 = (Button) findViewById(R.id.logout_btn);

                            if (btn.getText().toString().equals(" ") || btn2.getText().toString().equals(" ")) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SellerMain.this);
                                builder2
                                        .setTitle("One or more of your input data is 0")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("Increase", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(SellerMain.this, SellerInput.class));
                                            }
                                        })
                                        .setNegativeButton("Accept", null)                        //Do nothing on no
                                        .show();
                            } else {
                                AlertDialog.Builder builder2_0 = new AlertDialog.Builder(SellerMain.this);
                                builder2_0
                                        .setTitle("Would you like to change current bidding data?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(SellerMain.this, "Yes button pressed",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SellerMain.this, SellerInput.class));
                                            }
                                        })
                                        .setNegativeButton("No", null)                        //Do nothing on no
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)                        //Do nothing on no
                    .show();

            Button btn = (Button)findViewById(R.id.energybid_btn);
            Button btn2 = (Button)findViewById(R.id.energyprice_btn);
            Button btn3 = (Button) findViewById(R.id.selsett_btn);
            Button btn4 = (Button) findViewById(R.id.logout_btn);
            Button btn5 = (Button) findViewById(R.id.output_btn);

            btn.setText(selleramt);
            btn2.setText(sellerprice);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                    startActivity(mIntent);
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                    startActivity(mIntent);
                }
            });

            btn5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, Output.class);
                    startActivity(mIntent);
                }
            });

        }
        else if(prevAct.equals("SellerInput"))
        {
            RemoteViews remoteV = new RemoteViews(getPackageName(), R.layout.sell_main);
            Button btn = (Button)findViewById(R.id.energybid_btn);
            Button btn2 = (Button)findViewById(R.id.energyprice_btn);
            Button btn3 = (Button) findViewById(R.id.selsett_btn);
            Button btn4 = (Button) findViewById(R.id.logout_btn);
            Button btn5 = (Button) findViewById(R.id.output_btn);

            btn.setText(selleramt);
            btn2.setText(sellerprice);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                    startActivity(mIntent);
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                    startActivity(mIntent);
                }
            });

            btn5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, Output.class);
                    startActivity(mIntent);
                }
            });
        }
        else {
            Button btn = (Button)findViewById(R.id.energybid_btn);
            Button btn2 = (Button)findViewById(R.id.energyprice_btn);
            Button btn3 = (Button) findViewById(R.id.selsett_btn);
            Button btn4 = (Button) findViewById(R.id.logout_btn);
            Button btn5 = (Button) findViewById(R.id.output_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                    startActivity(mIntent);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                    startActivity(mIntent);
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                    startActivity(mIntent);
                }
            });

            btn5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, Output.class);
                    startActivity(mIntent);
                }
            });

        }

    }
}
