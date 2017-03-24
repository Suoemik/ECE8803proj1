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

public class BuyerInput extends AppCompatActivity {
    public static EditText buyeramttxt;

    public static String buyeramt;
    public static SharedPreferences savedreq;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energyreq_input);

        savedreq = getSharedPreferences("requirement", MODE_PRIVATE);

        btn =(Button)findViewById(R.id.energyreqOKbtn);
        buyeramttxt = (EditText)findViewById(R.id.editTextenergyreq);

        buyeramttxt.setText(savedreq.getString("buyerreq", buyeramt));

        btn.setOnClickListener(saveButtonListener);
    }

    public View.OnClickListener saveButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            buyeramt = buyeramttxt.getText().toString();
            savedreq.edit().putString("buyerreq", buyeramt);
            savedreq.edit().commit();

            Intent mIntent = new Intent(BuyerInput.this, BuyerMain.class);
            mIntent.putExtra("FROM_ACTIVITY", "BuyerInput");
            startActivity(mIntent);
            }
        };
}

