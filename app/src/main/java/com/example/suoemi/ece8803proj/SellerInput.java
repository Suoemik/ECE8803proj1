package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Suoemi on 3/22/2017.
 */

public class SellerInput extends AppCompatActivity {
    public EditText selleramttxt;
    public EditText sellerpricetxt;

    public String selleramt;
    public String sellerprice;
    public SharedPreferences savedamt;
    public SharedPreferences savedprice;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energybid_input);

        savedamt = getSharedPreferences("amount", MODE_PRIVATE);
        savedprice = getSharedPreferences("price", MODE_PRIVATE);

        btn =(Button)findViewById(R.id.energybidOKbtn);
        final DbHandler dB = new DbHandler(this);
        final LoginActivity loginActivity = new LoginActivity();

        selleramttxt = (EditText)findViewById(R.id.editTextenergyreq);
        sellerpricetxt = (EditText)findViewById(R.id.editTextenergyprice);

        selleramttxt.setText(savedamt.getString("selleramt", selleramt));
        sellerpricetxt.setText(savedprice.getString("sellerprice", sellerprice));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selleramt = selleramttxt.getText().toString();
                savedamt.edit().putString("selleramt", selleramt);
                savedamt.edit().commit();
                sellerprice = sellerpricetxt.getText().toString();
                savedprice.edit().putString("sellerprice", sellerprice);
                savedprice.edit().commit();

                Log.d("Reading: ", "Reading all account..");
                List<LoginData> loginDatas = dB.getAllSellLog();

                for (LoginData loginData : loginDatas) {
                    if(loginData.getUsername().equals(loginActivity.buyusr.getText().toString())){
                        loginData.seteBid(Integer.parseInt(selleramt));
                        loginData.setePrice(Integer.parseInt(sellerprice));
                        dB.updateSellLoginData(loginData);
                        String log = "Id: " + loginData.getId() + ", Name: " + loginData.getUsername()
                                + ", Password: " + loginData.getPassword() + ", Current: "
                                + loginData.getCheck() + ", Amt: " + loginData.geteBid() + ", Price" + loginData.getePrice();
                        Log.d("Input Sell:: ", log);
                    }
                }

                Intent mIntent = new Intent(SellerInput.this, SellerMain.class);
                mIntent.putExtra("FROM_ACTIVITY", "SellerInput");
                startActivity(mIntent);
            }
        });
    }


}

