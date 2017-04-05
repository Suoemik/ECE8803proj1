package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;
/**
 * Created by Suoemi on 3/17/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btn3;
    private Button btn4;
    private Switch sw;
    private EditText buyusr;
    private EditText buypass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn4 = (Button) findViewById(R.id.signup_btn2);
        sw = (Switch) findViewById(R.id.signup_switch);
        final DbHandler dB = new DbHandler(this);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    btn3 = (Button) findViewById(R.id.login_btn);
                    buyusr = (EditText) findViewById(R.id.inputusr);
                    buypass = (EditText) findViewById(R.id.inputpass);


                    btn3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String dbusr = buyusr.getText().toString();
                            String dbpass = buypass.getText().toString();
                            List<LoginData> logindat = dB.getAllEVLog();

                            for (LoginData loginData : logindat) {
                                if(dbusr.equals(loginData.getUsername()) && dbpass.equals(loginData.getPassword())){
                                    loginData.setCheck(1);
                                    dB.updateEVLoginData(loginData);
                                    String log = "Id: " + loginData.getId() + " ,Name: " + loginData.getUsername() + " ,Password: " + loginData.getPassword() + " ,Current: " + loginData.getCheck();
                                    Log.d("Account:: ", log);
                                    startActivity(new Intent(LoginActivity.this, BuyerMain.class));
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Error, Incorrect username or password", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                } else {
                    btn3 = (Button) findViewById(R.id.login_btn);
                    buyusr = (EditText) findViewById(R.id.inputusr);
                    buypass = (EditText) findViewById(R.id.inputpass);

                    btn3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String dbusr = buyusr.getText().toString();
                            String dbpass = buypass.getText().toString();
                            List<LoginData> logindat = dB.getAllSellLog();

                            for (LoginData loginData : logindat) {
                                if(dbusr.equals(loginData.getUsername()) && dbpass.equals(loginData.getPassword())){
                                    loginData.setCheck(1);
                                    Intent mIntent = new Intent(LoginActivity.this, SellerMain.class);
                                    mIntent.putExtra("FROM_ACTIVITY", "LoginActivity");
                                    startActivity(mIntent);                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Error, Incorrect username or password", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    });
                }
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}
