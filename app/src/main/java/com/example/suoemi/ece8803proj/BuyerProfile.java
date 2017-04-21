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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

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
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(BuyerProfile.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(BuyerProfile.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                databaseref.child("ev drivers").child(muser.getUid()).child("phone number").setValue(userInputDialogEditText.getText().toString());
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

        loc_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loc_btn.setText("ON");
            }
        });

        pass_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, SignupActivity.class);
                startActivity(mIntent);
            }
        });

        usr_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(BuyerProfile.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(BuyerProfile.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                String getusr = userInputDialogEditText.getText().toString();
                                databaseref.child("ev drivers").child(muser.getUid()).child("username").setValue(userInputDialogEditText.getText().toString());
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

        pmnt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(BuyerProfile.this, PaymentInput.class);
                startActivity(mIntent);
            }
        });
    }
}
