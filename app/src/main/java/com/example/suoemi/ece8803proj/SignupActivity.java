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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Button sw;
    private EditText inpnum;
    private EditText inpusr;
    private EditText inppass;
    public int evcount;
    public int sellcount;
    private Map<String, Object> map;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.btn = (Button) findViewById(R.id.signup_btn);
        this.btn2 = (Button) findViewById(R.id.login_btn2);
        this.sw = (Button) findViewById(R.id.signup_switch);
        this.inpnum = (EditText)findViewById(R.id.inputnum);
        this.inpusr = (EditText)findViewById(R.id.inputusr);
        this.inppass = (EditText)findViewById(R.id.inputpass);

        map = new HashMap<String, Object>();
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final DbHandler dB = new DbHandler(this);
        final List<LoginData> evlist = dB.getAllEVLog();
        final List<LoginData> selllist = dB.getAllSellLog();

        Log.d("S count: ", "ID "+sellcount);

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
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

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

        btn.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               if (sw.getText().toString().equals("SELLER")) {
                   Log.d(TAG, "signUp");
                   if (!validateForm()) {
                       return;
                   }

                   String dbnum = inpnum.getText().toString();
                   final String dbusr = inpusr.getText().toString();
                   String dbpass = inppass.getText().toString();

                   mAuth.createUserWithEmailAndPassword(dbnum, dbpass)
                             .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                                       if (task.isSuccessful()) {

                                           map.put("username", dbusr);
                                           map.put("email", task.getResult().getUser().getEmail());

                                           databaseref.child("sellers").child(task.getResult().getUser().getUid()).updateChildren(map);

                                           startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                           finish();
                                       } else {
                                           Toast.makeText(SignupActivity.this, "Sign Up Failed",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                   }
               else if (sw.getText().toString().equals("EV DRIVER")) {
                    Log.d(TAG, "signUp");
                       if (!validateForm()) {
                           return;
                       }
                       String dbnum = inpnum.getText().toString();
                       final String dbusr = inpusr.getText().toString();
                       String dbpass = inppass.getText().toString();

                       mAuth.createUserWithEmailAndPassword(dbnum, dbpass)
                               .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                                       if (task.isSuccessful()) {
                                           map.put("username", dbusr);
                                           map.put("email", task.getResult().getUser().getEmail());

                                           databaseref.child("ev drivers").child(task.getResult().getUser().getUid()).updateChildren(map);

                                           Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                           mIntent.putExtra("FROM_ACTIVITY", "SignupActivity");
                                           startActivity(mIntent);
                                           finish();
                                       } else {
                                           Toast.makeText(SignupActivity.this, "Sign Up Failed",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                   for (LoginData loginData : selllist) {
                       if (inpusr.getText().toString().equals(loginData.getUsername())) {
                           Toast.makeText(SignupActivity.this, "Username already exists ", Toast.LENGTH_SHORT).show();
                       }
                   }
               }else{
                   Toast.makeText(SignupActivity.this, "Error, no user type", Toast.LENGTH_SHORT);
               }
           }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(inpnum.getText().toString())) {
            inpnum.setError("Required");
            result = false;
        } else {
            inpnum.setError(null);
        }

        if (TextUtils.isEmpty(inppass.getText().toString())) {
            inppass.setError("Required");
            result = false;
        } else {
            inppass.setError(null);
        }

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}