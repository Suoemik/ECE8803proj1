package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SellerInput";

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energybid_input);

        this.savedamt = getSharedPreferences("amount", MODE_PRIVATE);
        this.savedprice = getSharedPreferences("price", MODE_PRIVATE);
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        btn =(Button)findViewById(R.id.energybidOKbtn);
        final DbHandler dB = new DbHandler(this);
        final LoginActivity loginActivity = new LoginActivity();

        this.selleramttxt = (EditText)findViewById(R.id.editTextenergyreq);
        this.sellerpricetxt = (EditText)findViewById(R.id.editTextenergyprice);

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

                if(selleramt.length()!=0) {
                    databaseref.child("sellers").child(muser.getUid()).child("bid amount").setValue(selleramt);
                }
                if(sellerprice.length()!=0) {
                    databaseref.child("sellers").child(muser.getUid()).child("bid price").setValue(sellerprice);
                }

                Intent mIntent = new Intent(SellerInput.this, SellerMain.class);
                mIntent.putExtra("FROM_ACTIVITY", "SellerInput");
                startActivity(mIntent);
            }
        });
    }


}

