package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Suoemi on 3/17/2017.
 */


public class SellerMain extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        Intent mIntent = getIntent();
        String prevAct = mIntent.getStringExtra("FROM_ACTIVITY");
        final DbHandler dbHandler = new DbHandler(this);
        final List<LoginData> loginDatas = dbHandler.getAllSellLog();
        this.btn = (Button) findViewById(R.id.energybid_btn);
        this.btn2 = (Button) findViewById(R.id.energyprice_btn);
        this.btn3 = (Button) findViewById(R.id.selsett_btn);
        this.btn4 = (Button) findViewById(R.id.logout_btn);
        this.btn5 = (Button) findViewById(R.id.output_btn);
        final LoginActivity loginActivity = new LoginActivity();

        RemoteViews remoteV = new RemoteViews(getPackageName(), R.layout.sell_main);

        for(LoginData loginData : loginDatas){
            if(loginData.getJoin() == 1)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Would you like to participate in this energy auction?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                for(LoginData loginData : loginDatas)
                                {
                                    if(loginData.getUsername().equals(loginActivity.getusrnm()))
                                    {
                                        loginData.setJoin(1);
                                    }
                                }
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
                        for(LoginData loginData : loginDatas) {
                            loginData.setCheck(0);
                            dbHandler.updateSellLoginData(loginData);
                            String log = "Id: " + loginData.getId() + ", Name: " + loginData.getUsername()
                                    + ", Password: " + loginData.getPassword() + ", Current: "
                                    + loginData.getCheck() + ", Amt: " + loginData.geteBid() + ", Price: " + loginData.getePrice();
                            Log.d("Logout Sell:: ", log);
                            Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                            startActivity(mIntent);
                        }
                    }
                });

                btn5.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent mIntent = new Intent(SellerMain.this, Output.class);
                        startActivity(mIntent);
                    }
                });

                for(LoginData loginData1 : loginDatas) {
                    if (loginData.getUsername().equals(loginActivity.getusrnm())) {
                        int ebid = loginData1.geteBid();
                        int eprice = loginData1.getePrice();
                        btn.setText(Integer.toString(ebid));
                        btn2.setText(Integer.toString(eprice));
                    }
                }
            }
        }

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
                for(LoginData loginData : loginDatas) {
                    loginData.setCheck(0);
                    dbHandler.updateSellLoginData(loginData);
                    String log = "Id: " + loginData.getId() + ", Name: " + loginData.getUsername()
                            + ", Password: " + loginData.getPassword() + ", Current: "
                            + loginData.getCheck() + ", Amt: " + loginData.geteBid() + ", Price: " + loginData.getePrice();
                    Log.d("Logout Sell:: ", log);
                    Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                    startActivity(mIntent);
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerMain.this, Output.class);
                startActivity(mIntent);
            }
        });

        for(LoginData loginData : loginDatas) {
            if (loginData.getUsername().equals(loginActivity.getusrnm())) {
                int ebid = loginData.geteBid();
                int eprice = loginData.getePrice();
                btn.setText(Integer.toString(ebid));
                btn2.setText(Integer.toString(eprice));
            }
        }

    }

}
