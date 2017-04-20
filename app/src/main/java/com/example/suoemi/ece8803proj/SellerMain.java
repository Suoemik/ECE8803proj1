package com.example.suoemi.ece8803proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joptimizer.optimizers.LPOptimizationRequest;
import com.joptimizer.optimizers.LPPrimalDualMethod;
import com.joptimizer.optimizers.OptimizationResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by Suoemi on 3/17/2017.
 */


public class SellerMain extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    private int c1;
    private int u1;
    private int returnCode;
    private double[] sol;
    private double[] c;
    private double[] B;
    private double[] ub;
    private Map<String, Object> usr_sell;
    private Map<String, Object> usr_buy;
    private double outp;
    private double outa;
    private ArrayList<String> allsell;
    private ArrayList<String> allbuy;

    private ArrayList<Double> bidamt;
    private ArrayList<Double> bidpr;
    private ArrayList<Double> evreq;

    private TextView sellusr;
    private TextView sellamt;
    private TextView buytot;
    private TextView pricetot;
    private TextView title;
    private TableRow tr;
    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SellerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        Intent mIntent = getIntent();
        final DbHandler dbHandler = new DbHandler(this);
        final List<LoginData> loginDatas = dbHandler.getAllSellLog();
        btn = (Button) findViewById(R.id.energybid_btn);
        btn2 = (Button) findViewById(R.id.energyprice_btn);
        btn3 = (Button) findViewById(R.id.selsett_btn);
        btn4 = (Button) findViewById(R.id.logout_btn);
        btn5 = (Button) findViewById(R.id.output_btn);
        final LoginActivity loginActivity = new LoginActivity();
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();
        allsell = new ArrayList<String>();
        allbuy = new ArrayList<String>();

        evreq = new ArrayList<>();
        bidamt = new ArrayList<>();
        bidpr = new ArrayList<>();

        String prevact = getIntent().getStringExtra("FROM_ACTIVITY");

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

        TableLayout tabLay = (TableLayout) findViewById(R.id.tableinfosell);
        title = new TextView(this);
        tr = new TableRow(this);

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        params1.setMargins(30, 100, 10, 10);
        params1.span = 2;

        title.setText("OUTPUT");
        title.setLayoutParams(params1);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTypeface(null, Typeface.BOLD);
        tr.addView(title);

        tabLay.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        ValueEventListener checklist = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerMain.this);
                    builder
                            .setTitle("Would you like to participate in this energy auction?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something

                                    if (btn.getText().toString().equals("0") || btn2.getText().toString().equals("0")) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SellerMain.this);
                                        builder2
                                                .setTitle("One or more of your input data is 0")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton("Increase", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(SellerMain.this, SellerInput.class));
                                                    }
                                                })
                                                .setNegativeButton("Accept", null)                        //Do nothing on no
                                                .show();
                                    } else {
                                        AlertDialog.Builder builder2_0 = new AlertDialog.Builder(SellerMain.this);
                                        builder2_0
                                                .setTitle("Would you like to change current bidding data?")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(SellerMain.this, "Yes button pressed",
                                                                Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SellerMain.this, SellerInput.class));
                                                    }
                                                })
                                                .setNegativeButton("No", null)                        //Do nothing on no
                                                .show();
                                    }
                                    databaseref.child("sellers").child(muser.getUid()).child("min").setValue("1");

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){

                                    databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
                                    databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
                                    databaseref.child("sellers").child(muser.getUid()).child("bid amount").setValue("0");
                                    databaseref.child("sellers").child(muser.getUid()).child("bid price").setValue("0");
                                }
                            })
                            .show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseref.child("sellers").child(muser.getUid()).child("join").addValueEventListener(checklist);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ValueEventListener checklist2 = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("1")) {
                    ValueEventListener joinlist = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get Post object and use the values to update the UI

                            usr_sell = new HashMap<String, Object>();
                            usr_sell = (Map<String, Object>) dataSnapshot.getValue();
                            Log.d(TAG, "Seller size : " + usr_sell.size());


                            for (Map.Entry<String, Object> entry : usr_sell.entrySet()) {
                                Map singlesell =(Map) entry.getValue();

                                allsell.add((String) singlesell.get("username"));

                                bidamt.add(Double.valueOf((String) singlesell.get("bid amount")));
                                Log.d(TAG, "Other size : " + bidamt.size());

                                bidpr.add(Double.valueOf((String) singlesell.get("bid price")));
                            }

                            ValueEventListener joinlist2 = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "Seller username list: " + allsell);
                                    usr_buy = new HashMap<String, Object>();
                                    usr_buy = (Map<String, Object>) dataSnapshot.getValue();
                                    LPOptimizationRequest or = new LPOptimizationRequest();

                                    for (Map.Entry<String, Object> entry : usr_buy.entrySet()) {
                                        Map singlebuy = (Map) entry.getValue();

                                        allbuy.add((String) singlebuy.get("username"));
                                        evreq.add(Double.valueOf((String) singlebuy.get("ev req")));
                                    }
                                    Log.d(TAG, "Buyer username list: " + allbuy);

                                    c = new double[usr_sell.size()];
                                    for (int i = 0; i < c.length; i++) {
                                        c[i] = bidpr.get(i).doubleValue();
                                        System.out.print("Price" + c);
                                    }

                                    double[][] A = new double[][]{{1, 1, 1}};

                                    B = new double[usr_buy.size()];
                                    for (int i = 0; i < B.length; i++) {
                                        B[i] = evreq.get(i).doubleValue();
                                    }

                                    double[] lb = new double[]{0, 0, 0};

                                    ub = new double[usr_sell.size()];
                                    for (int i = 0; i < ub.length; i++) {
                                        ub[i] = bidamt.get(i).doubleValue();
                                    }

                                    or.setC(c);
                                    or.setA(A);
                                    or.setB(B);
                                    or.setLb(lb);
                                    or.setUb(ub);
                                    or.setDumpProblem(true);

                                    //optimization
                                    LPPrimalDualMethod opt = new LPPrimalDualMethod();

                                    opt.setLPOptimizationRequest(or);
                                    try {
                                        returnCode = opt.optimize();
                                        assertEquals("success ", OptimizationResponse.SUCCESS, returnCode);
                                        sol = opt.getOptimizationResponse().getSolution();
                                        String log = "Solution: " + Arrays.toString(sol);
                                        Log.d("Solution:: ", log);
                                    } catch (Exception e) {
                                        fail(e.toString());
                                    }

                                    for (int i = 0; i < sol.length; i++) {
                                        outa += sol[i];
                                        outp += c[i]*sol[i];
                                    }
                                    Log.d(TAG, "Output after: " + outa);

                                    TableLayout tabLay = (TableLayout) findViewById(R.id.tableinfosell);
                                    RelativeLayout relLay = (RelativeLayout) findViewById(R.id.relsell);

                                    for (int i = 0; i < sol.length; i++) {
                                        tr = new TableRow(SellerMain.this);
                                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                        sellusr = new TextView(SellerMain.this);
                                        sellamt = new TextView(SellerMain.this);

                                        sellusr.setText("Seller " + (i+1) + " Initial Amt: " + ub[i]);
                                        sellusr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        tr.addView(sellusr);

                                        sellamt.setText(Double.toString(Math.round(sol[i])));
                                        sellamt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        tr.addView(sellamt);

                                        tabLay.addView(tr, new TableLayout.LayoutParams(
                                                TableRow.LayoutParams.FILL_PARENT,
                                                TableRow.LayoutParams.WRAP_CONTENT));
                                    }

                                    TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                    params1.setMargins(30, 100, 10, 10);
                                    params1.span = 2;

                                    buytot = new TextView(SellerMain.this);
                                    pricetot = new TextView(SellerMain.this);
                                    buytot.setLayoutParams(params1);
                                    pricetot.setLayoutParams(params1);

                                    buytot.setText("EV driver total amount: " + outa);
                                    buytot.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    pricetot.setText("EV driver total cost: " + outp);
                                    pricetot.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                    tabLay.addView(buytot);
                                    tabLay.addView(pricetot);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message
                                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                    // ...
                                }
                            };
                            databaseref.child("ev drivers").addValueEventListener(joinlist2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    };
                    databaseref.child("sellers").addValueEventListener(joinlist);

                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                            startActivity(mIntent);
                        }
                    });

                    btn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                            startActivity(mIntent);
                        }
                    });

                    btn3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                            startActivity(mIntent);
                        }
                    });

                    btn4.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
                            databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
                            mAuth.signOut();
                            Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                            startActivity(mIntent);
                        }
                    });

                    btn5.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent mIntent = new Intent(SellerMain.this, Output.class);
                            startActivity(mIntent);
                        }
                    });

//                    ValueEventListener postListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // Get Post object and use the values to update the UI
//                            String ebid = dataSnapshot.getValue(String.class);
//                            btn.setText(ebid);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Getting Post failed, log a message
//                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                            // ...
//                        }
//                    };
//                    ValueEventListener postListener2 = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // Get Post object and use the values to update the UI
//                            String eprice = dataSnapshot.getValue(String.class);
//                            btn2.setText(eprice);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Getting Post failed, log a message
//                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                            // ...
//                        }
//                    };
//                    databaseref.child("sellers").child(muser.getUid()).child("bid amount").addValueEventListener(postListener);
//                    databaseref.child("sellers").child(muser.getUid()).child("bid price").addValueEventListener(postListener2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseref.child("sellers").child(muser.getUid()).child("min").addValueEventListener(checklist2);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (prevact == "SellerInput") {
            Log.d(TAG, "From Input");
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
                    databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                    startActivity(mIntent);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                    startActivity(mIntent);
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
                    databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
                    mAuth.signOut();
                    Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                    startActivity(mIntent);
                }
            });

            btn5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent mIntent = new Intent(SellerMain.this, Output.class);
                    startActivity(mIntent);
                }
            });

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String ebid = dataSnapshot.getValue(String.class);
                    btn.setText(ebid);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            ValueEventListener postListener2 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String eprice = dataSnapshot.getValue(String.class);
                    btn2.setText(eprice);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };

            databaseref.child("sellers").child(muser.getUid()).child("bid amount").addValueEventListener(postListener);
            databaseref.child("sellers").child(muser.getUid()).child("bid price").addValueEventListener(postListener2);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                startActivity(mIntent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerMain.this, SellerInput.class);
                startActivity(mIntent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerMain.this, SellerProfile.class);
                startActivity(mIntent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
                databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
                mAuth.signOut();
                Intent mIntent = new Intent(SellerMain.this, LoginActivity.class);
                startActivity(mIntent);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(SellerMain.this, Output.class);
                startActivity(mIntent);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String ebid = dataSnapshot.getValue(String.class);
                btn.setText(ebid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String eprice = dataSnapshot.getValue(String.class);
                btn2.setText(eprice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        databaseref.child("sellers").child(muser.getUid()).child("bid amount").addValueEventListener(postListener);
        databaseref.child("sellers").child(muser.getUid()).child("bid price").addValueEventListener(postListener2);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
        databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        databaseref.child("sellers").child(muser.getUid()).child("join").setValue("0");
        databaseref.child("sellers").child(muser.getUid()).child("min").setValue("0");
    }
}
