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

public class SellerMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("There is a new auction within you vicinity. Accept?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SellerMain.this);
                        builder2
                                .setTitle("Would you like to change your energy bid data?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(SellerMain.this, "Yes button pressed",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SellerMain.this, SellerInput.class));
                                    }
                                })
                                .setNegativeButton("No", null)						//Do nothing on no
                                .show();
                    }
                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();
    }
}
