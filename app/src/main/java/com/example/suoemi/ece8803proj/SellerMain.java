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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SellerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        Intent mIntent = getIntent();
        final DbHandler dbHandler = new DbHandler(this);
        final List<LoginData> loginDatas = dbHandler.getAllSellLog();
        btn = (Button) findViewById(R.id.energybid_btn);
        btn2 = (Button) findViewById(R.id.energyprice_btn);
        btn3 = (Button) findViewById(R.id.selsett_btn);
        btn4 = (Button) findViewById(R.id.logout_btn);
        btn5 = (Button) findViewById(R.id.output_btn);
        final LoginActivity loginActivity = new LoginActivity();
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();


        RemoteViews remoteV = new RemoteViews(getPackageName(), R.layout.sell_main);

        for(LoginData loginData : loginDatas){
            if(loginData.getJoin()==1){
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
                        mAuth.signOut();
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

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        String ebid = dataSnapshot.getValue(String.class);
                        btn.setText(ebid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                ValueEventListener postListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        String eprice = dataSnapshot.getValue(String.class);
                        btn2.setText(eprice);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                databaseref.child("sellers").child(muser.getUid()).child("bid amount").addValueEventListener(postListener);
                databaseref.child("sellers").child(muser.getUid()).child("bid price").addValueEventListener(postListener2);
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
                mAuth.signOut();
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

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String ebid = dataSnapshot.getValue(String.class);
                btn.setText(ebid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String eprice = dataSnapshot.getValue(String.class);
                btn2.setText(eprice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseref.child("sellers").child(muser.getUid()).child("bid amount").addValueEventListener(postListener);
        databaseref.child("sellers").child(muser.getUid()).child("bid price").addValueEventListener(postListener2);
    }

}
