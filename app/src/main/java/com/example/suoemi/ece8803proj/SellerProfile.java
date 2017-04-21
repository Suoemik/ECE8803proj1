package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class SellerProfile extends AppCompatActivity {
    public static Button locbtn;

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_prof);

        Button logoutbtn = (Button) findViewById(R.id.splogout_btn);
        Button mainbtn = (Button) findViewById(R.id.spmain_btn);
        locbtn = (Button) findViewById(R.id.sloc_btn);
        Button usr = (Button) findViewById(R.id.susern_btn);
        Button pno = (Button) findViewById(R.id.sphone_btn);
        Button pmnt = (Button) findViewById(R.id.df_pyment_btn);

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

        pmnt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerProfile.this, PaymentInput.class);
                startActivity(mIntent);
            }
        });

        usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SellerProfile.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(SellerProfile.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                databaseref.child("sellers").child(muser.getUid()).child("username").setValue(userInputDialogEditText.getText().toString());
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        pno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SellerProfile.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(SellerProfile.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                databaseref.child("sellers").child(muser.getUid()).child("phone number").setValue(userInputDialogEditText.getText().toString());
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
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
