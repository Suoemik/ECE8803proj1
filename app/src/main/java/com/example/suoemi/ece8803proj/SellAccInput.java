package com.example.suoemi.ece8803proj;

import android.content.Intent;
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
 * Created by Suoemi on 4/21/2017.
 */

public class SellAccInput extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_input);

        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        Button addbtn = (Button) findViewById(R.id.sellaccadd);
        final EditText input = (EditText) findViewById(R.id.sellpaymenttext);
        String prevact = getIntent().getStringExtra("FROM_ACTIVITY");

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseref.child("sellers").child(muser.getUid()).child("paypal").setValue(input.getText().toString());
                Intent mIntent = new Intent(SellAccInput.this, SellerProfile.class);
                mIntent.putExtra("FROM_ACTIVITY", "SellAccInput");
                startActivity(new Intent(SellAccInput.this, SellerProfile.class));
            }
        });
    }
}
