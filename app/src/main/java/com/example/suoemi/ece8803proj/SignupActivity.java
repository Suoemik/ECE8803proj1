package com.example.suoemi.ece8803proj;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class SignupActivity extends AppCompatActivity {

    Button btn =(Button)findViewById(R.id.signup_btn);
    Button btn2 =(Button)findViewById(R.id.login_btn);
    Switch swi = (Switch)findViewById(R.id.signup_switch);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SellerProfile.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

}