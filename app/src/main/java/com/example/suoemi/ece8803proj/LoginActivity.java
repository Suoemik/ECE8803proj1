package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private int n;

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.btn3 = (Button) findViewById(R.id.login_btn);
        this.btn4 = (Button) findViewById(R.id.signup_btn2);
        this.sw = (Button) findViewById(R.id.signup_switch);
        this.buyusr = (EditText) findViewById(R.id.inputusr);
        this.buypass = (EditText) findViewById(R.id.inputpass);

        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        final DbHandler dB = new DbHandler(this);
        final List<LoginData> evlist = dB.getAllEVLog();
        final List<LoginData> selllist = dB.getAllSellLog();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

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
                    Log.d(TAG, "signIn");
                    if (!validateForm()) {
                        return;
                    }

                    final String dbusr = buyusr.getText().toString();
                    String dbpass = buypass.getText().toString();
                    setusrnm(dbusr);
                    UsrNm = dbusr;

                    mAuth.signInWithEmailAndPassword(dbusr, dbpass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        Intent mIntent = new Intent(LoginActivity.this, SellerMain.class);
                                        mIntent.putExtra("FROM_ACTIVITY", "SignupActivity");
                                        startActivity(mIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else if (sw.getText().toString().equals("EV DRIVER")) {
                    Log.d(TAG, "signIn");
                    if (!validateForm()) {
                        return;
                    }

                    final String dbusr = buyusr.getText().toString();
                    String dbpass = buypass.getText().toString();
                    setusrnm(dbusr);

                    mAuth.signInWithEmailAndPassword(dbusr, dbpass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        Intent mIntent = new Intent(LoginActivity.this, BuyerMain.class);
                                        mIntent.putExtra("FROM_ACTIVITY", "SignupActivity");
                                        startActivity(mIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(buyusr.getText().toString())) {
            buyusr.setError("Required");
            result = false;
        } else {
            buyusr.setError(null);
        }

        if (TextUtils.isEmpty(buypass.getText().toString())) {
            buypass.setError("Required");
            result = false;
        } else {
            buypass.setError(null);
        }

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
