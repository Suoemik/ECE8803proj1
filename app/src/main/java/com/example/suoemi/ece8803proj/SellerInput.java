package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Suoemi on 3/22/2017.
 */

public class SellerInput extends AppCompatActivity {
    public static EditText selleramttxt;
    public static EditText sellerpricetxt;

    public static String selleramt;
    public static String sellerprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energybid_input);

        Button btn =(Button)findViewById(R.id.energybidOKbtn);
        selleramttxt = (EditText)findViewById(R.id.editTextselleramnt);
        sellerpricetxt = (EditText)findViewById(R.id.editTextselleramnt);
        selleramt = selleramttxt.getText().toString();
        System.out.print(selleramt);
        selleramt = sellerpricetxt.getText().toString();

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerInput.this, SellerMain.class);
                mIntent.putExtra("FROM_ACTIVITY", "SellerInput");
                startActivity(mIntent);
            }
        });
    }
}
