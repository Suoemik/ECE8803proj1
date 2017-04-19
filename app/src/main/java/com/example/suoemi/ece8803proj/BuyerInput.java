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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suoemi on 3/22/2017.
 */

public class BuyerInput extends AppCompatActivity {
    public EditText buyeramttxt;
    public String buyeramt;
    public SharedPreferences savedreq;

    private Map<String, Object> map;
    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "BuyerInput";

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energyreq_input);

        this.savedreq = getSharedPreferences("requirement", MODE_PRIVATE);

        map = new HashMap<String, Object>();
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        btn =(Button)findViewById(R.id.energyreqOKbtn);
        final DbHandler dB = new DbHandler(this);
        final LoginActivity loginActivity = new LoginActivity();
        buyeramttxt = (EditText)findViewById(R.id.editTextenergyreq);

        buyeramttxt.setText(savedreq.getString("buyerreq", buyeramt));
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                buyeramt = buyeramttxt.getText().toString();
                savedreq.edit().putString("buyerreq", buyeramt);
                savedreq.edit().commit();

                if(buyeramt.length()!=0) {
                    map.put("ev req", buyeramt);
                    databaseref.child("ev drivers").child(muser.getUid()).updateChildren(map);
                }

                Intent mIntent = new Intent(BuyerInput.this, BuyerMain.class);
                mIntent.putExtra("FROM_ACTIVITY", "BuyerInput");
                startActivity(mIntent);
            }
        });
    }

}

