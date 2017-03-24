package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.SharedPreferences;
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
    public static SharedPreferences savedamt;
    public static SharedPreferences savedprice;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energybid_input);

        savedamt = getSharedPreferences("amount", MODE_PRIVATE);
        savedprice = getSharedPreferences("price", MODE_PRIVATE);

        btn =(Button)findViewById(R.id.energybidOKbtn);
        selleramttxt = (EditText)findViewById(R.id.editTextselleramnt);
        sellerpricetxt = (EditText)findViewById(R.id.editTextselleramnt);

        selleramttxt.setText(savedamt.getString("selleramt", "0"));
        sellerpricetxt.setText(savedprice.getString("sellerprice", "0"));

        btn.setOnClickListener(saveButtonListener);
    }

    public View.OnClickListener saveButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            selleramt = selleramttxt.getText().toString();
            savedamt.edit().putString("selleramt", selleramt);
            savedamt.edit().commit();
            sellerprice = sellerpricetxt.getText().toString();
            savedprice.edit().putString("sellerprice", sellerprice);
            savedprice.edit().commit();

            Intent mIntent = new Intent(SellerInput.this, SellerMain.class);
            mIntent.putExtra("FROM_ACTIVITY", "SellerInput");
            startActivity(mIntent);
            }
        };
}

