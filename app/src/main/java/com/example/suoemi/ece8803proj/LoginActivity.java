package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
/**
 * Created by Suoemi on 3/17/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btn3;
    private Button btn4;
    private Button sw;
    public EditText buyusr;
    private EditText buypass;
    public String UsrNm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.btn3 = (Button) findViewById(R.id.login_btn);
        this.btn4 = (Button) findViewById(R.id.signup_btn2);
        this.sw = (Button) findViewById(R.id.signup_switch);
        this.buyusr = (EditText) findViewById(R.id.inputusr);
        this.buypass = (EditText) findViewById(R.id.inputpass);
        final DbHandler dB = new DbHandler(this);
        final List<LoginData> evlist = dB.getAllEVLog();
        final List<LoginData> selllist = dB.getAllSellLog();

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw.getText().toString().length() == 0) {
                    sw.setText("SELLER");
                }
                else if (sw.getText().toString().equals("SELLER")) {
                    sw.setText("EV DRIVER");
                }
                else {
                    sw.setText("SELLER");
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sw.getText().toString().equals("SELLER")) {
                    String dbusr = buyusr.getText().toString();
                    String dbpass = buypass.getText().toString();
                    setusrnm(dbusr);
                    UsrNm = dbusr;

                    for (LoginData loginData : selllist) {
                        if (dbusr.equals(loginData.getUsername()) && dbpass.equals(loginData.getPassword())) {
                            loginData.setCheck(1);
                            dB.updateSellLoginData(loginData);
                            String log = "Id: " + loginData.getId() + ", Name: "
                                    + loginData.getUsername() + ", Password: "
                                    + loginData.getPassword() + ", Current: "
                                    + loginData.getCheck();
                            Log.d("Sell Login:: ", log);
                            startActivity(new Intent(LoginActivity.this, BuyerMain.class));

                            Intent mIntent = new Intent(LoginActivity.this, SellerMain.class);
                            mIntent.putExtra("FROM_ACTIVITY", "LoginActivity");
                            startActivity(mIntent);
                        } else if (dbusr.length() == 0 || dbpass.length() == 0) {
                            Toast.makeText(LoginActivity.this, "Error, no input", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error, Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (sw.getText().toString().equals("EV DRIVER")) {
                    String dbusr = buyusr.getText().toString();
                    String dbpass = buypass.getText().toString();
                    setusrnm(dbusr);
                    UsrNm = dbusr;

                    for (LoginData loginData : evlist) {
                        if (dbusr.equals(loginData.getUsername()) && dbpass.equals(loginData.getPassword())) {
                            loginData.setCheck(1);
                            dB.updateEVLoginData(loginData);
                            String log = "Id: " + loginData.getId() + ", Name: "
                                    + loginData.getUsername() + ", Password: "
                                    + loginData.getPassword() + ", Current: "
                                    + loginData.getCheck();
                            Log.d("EV Login:: ", log);
                            startActivity(new Intent(LoginActivity.this, BuyerMain.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error, Incorrect EV username or password", Toast.LENGTH_SHORT).show();
                        }

                        dB.updateEVLoginData(loginData);
                        String log = "Id: " + loginData.getId() + ", Name: "
                                + loginData.getUsername() + ", Password: "
                                + loginData.getPassword();
                        Log.d("EV Login:: ", log);
                    }

                    if (dbusr.length() == 0 || dbpass.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Error, no input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error, no user type", Toast.LENGTH_SHORT);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public void setusrnm(String usrnm){
        this.UsrNm = usrnm;
    }

    public String getusrnm(){
        String usrnm = UsrNm;
        return UsrNm;
    }
}
