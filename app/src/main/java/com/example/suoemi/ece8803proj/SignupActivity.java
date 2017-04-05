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

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Switch sw;
    private EditText inpnum;
    private EditText inpusr;
    private EditText inppass;
    public int evcount;
    public int sellcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btn2 = (Button) findViewById(R.id.login_btn2);
        sw = (Switch) findViewById(R.id.signup_switch);
        final DbHandler dB = new DbHandler(this);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    btn = (Button) findViewById(R.id.signup_btn);
                    inpnum = (EditText)findViewById(R.id.inputnum);
                    inpusr = (EditText)findViewById(R.id.inputusr);
                    inppass = (EditText)findViewById(R.id.inputpass);


                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(inpnum != null && inppass != null && inpusr != null) {
                                String dbnum = inpnum.getText().toString();
                                String dbusr = inpusr.getText().toString();
                                String dbpass = inppass.getText().toString();

                                Log.d("Insert:", "Inserting ..");
                                dB.addEVLog(new LoginData(dbusr, dbpass, dbnum, 0, 0, 0));

                                List<LoginData> loginDataList = dB.getAllEVLog();
                                for (LoginData loginData : loginDataList) {
                                    String log = "Id: " + loginData.getId() + " ,Name: " + loginData.getUsername() + " ,Password: " + loginData.getPassword() + " ,Current: " + loginData.getCheck();
                                    Log.d("Account:: ", log);
                                }

                            }

                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                        }
                    });
                } else {
                    btn = (Button) findViewById(R.id.signup_btn);
                    inpnum = (EditText)findViewById(R.id.inputnum);
                    inpusr = (EditText)findViewById(R.id.inputusr);
                    inppass = (EditText)findViewById(R.id.inputpass);

                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(inpnum != null && inppass != null && inpusr != null) {
                                String dbnum = inpnum.getText().toString();
                                String dbusr = inpusr.getText().toString();
                                String dbpass = inppass.getText().toString();

                                Log.d("Insert:", "Inserting ..");
                                dB.addEVSell(new LoginData(dbusr, dbpass, dbnum, 0, 0, 0));
                                Log.d("Reading: ", "Reading all shops..");
                                List<LoginData> loginDataList = dB.getAllSellLog();
                            }

                            Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
                            mIntent.putExtra("FROM_ACTIVITY", "SignupActivity");
                            startActivity(mIntent);
                        }
                    });
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}