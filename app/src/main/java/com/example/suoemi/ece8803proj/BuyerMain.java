package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class BuyerMain extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected Location mLastLocation;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    private Button mFetchAddressButton;

    private int c1;
    private int u1;
    private int returnCode;
    private double[] sol;
    private double[] c;
    private double[] B;
    private double[] ub;
    private double[][] A;
    private double[] lb;
    private Map<String, Object> usr_sell;
    private Map<String, Object> usr_buy;
    private double outp;
    private double outa;

    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button mSeekBar;

    private TextView sellusr;
    private TextView sellamt;
    private TextView buytot;
    private TextView pricetot;
    private ArrayList<String> allsell;
    private ArrayList<String> allbuy;

    private ArrayList<Double> bidamt;
    private ArrayList<Double> bidpr;
    private ArrayList<Double> evreq;

    private TextView title;
    private TableRow tr;

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayBlockingQueue<Integer> mQueue;
    private int clicked;
    private static Thread sNetworkThreadSend;
    private static Thread sNetworkThread;
    private static Thread sNetworkThreadReceive;

    private AtomicBoolean mStop;
    private Runnable mNetworkRunnableSend;
    private OutputStream mOutputStream;
    private Socket mSocket;
    private Runnable mNetworkRunnable;
    private Runnable mNetworkRunnableReceive;

    private static final String TAG = "BuyerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_main);

        this.btn = (Button) findViewById(R.id.begin_btn);
        this.btn2 = (Button) findViewById(R.id.energyreq_btn);
        this.btn3 = (Button) findViewById(R.id.buysett_btn);
        this.btn4 = (Button) findViewById(R.id.bm_logout);

        allsell = new ArrayList<>();
        allbuy = new ArrayList<>();

        evreq = new ArrayList<>();
        bidamt = new ArrayList<>();
        bidpr = new ArrayList<>();
        mQueue = new ArrayBlockingQueue<Integer>(1);
        sNetworkThreadSend = null;
        sNetworkThread = null;
        sNetworkThreadReceive = null;
        mOutputStream = null;
        mSocket = null;
        mStop = new AtomicBoolean(false);

        mSeekBar = (Button) findViewById(R.id.ardbtn);
        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        mFetchAddressButton = (Button) findViewById(R.id.benterloc);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollbuy);

        clicked = 0;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        try {
            InetAddress address = InetAddress.getByName("78.46.84.171");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change boolean value
                //for (int i = 0; i < sol.length; i++) {
                //  if(i>=0) {
                clicked = 1;
                mQueue.clear();
                mQueue.offer(1);
                //}
                // }
            }
        });

        mNetworkRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(InetAddress.getByName("192.168.43.161"), 6666);
                    mOutputStream = mSocket.getOutputStream();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                    mStop.set(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    mStop.set(true);
                }

                mQueue.clear(); // we only want new values

                try {
                    while(!mStop.get()){
                        int val = mQueue.take();
                        if(val >= 0){
                            mOutputStream.write((val+"\n").getBytes());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    try {
                        mStop.set(true);
                        if(mOutputStream != null) mOutputStream.close();
                        if(mSocket != null) mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                sNetworkThread = null;
            }
        };

//        mNetworkRunnableSend = new Runnable() {
//            @Override
//            public void run() {
//                log("starting network thread for sending");
//                String urlBase = "http://" + "192.168.43.161" + "/arduino/analog/3/";
//                String url;
//                try {
//                    while (!mStop.get()) {
//                        int val = mQueue.take();
//                        if (val >= 0) {
//                            HttpClient httpClient = new DefaultHttpClient();
//                            url = urlBase.concat(String.valueOf(val));
//                            HttpResponse response = httpClient.execute(new HttpGet(url));
//                        }
//                    }
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                sNetworkThreadSend = null;
//            }
//        };

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

        TableLayout tabLay = (TableLayout) findViewById(R.id.tableinfobuy);
        title = new TextView(this);
        tr = new TableRow(this);

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        params1.setMargins(30, 100, 5, 10);
        params1.span = 2;

        title.setText("OUTPUT");
        title.setLayoutParams(params1);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTypeface(null, Typeface.BOLD);
        tr.addView(title);

        tabLay.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("1");
                ValueEventListener joinlist = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        ArrayList<String> allusr = new ArrayList<String>();
                        usr_sell = new HashMap<String, Object>();
                        usr_sell = (Map<String, Object>) dataSnapshot.getValue();

                        for (Map.Entry<String, Object> entry : usr_sell.entrySet()) {
                            Map singlesell = (Map) entry.getValue();
                            //allsell.add((String) singlesell.get("username"));
                            allusr.add(entry.getKey());
                        }
                        for (int i = 0; i < allusr.size(); i++) {
                            Log.d(TAG, "array list for seller: " + allusr.get(i));
                            databaseref.child("sellers").child(allusr.get(i)).child("join").setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                databaseref.child("sellers").addValueEventListener(joinlist);

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(BuyerMain.this, BuyerProfile.class));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("0");
                mAuth.signOut();
                startActivity(new Intent(BuyerMain.this, LoginActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("0");
                Intent mIntent = new Intent(BuyerMain.this, BuyerInput.class);
                mIntent.putExtra("FROM_ACTIVITY", "BuyerMain");
                startActivity(mIntent);
            }
        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String req = dataSnapshot.getValue(String.class);
                btn2.setText(req);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseref.child("ev drivers").child(muser.getUid()).child("ev req").addValueEventListener(postListener);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ValueEventListener buylist = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("1")) {

                    ValueEventListener checklist2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            final ArrayList<String> allusr = new ArrayList<String>();
                            final ArrayList<String> newarr = new ArrayList<String>();

                            usr_sell = new HashMap<String, Object>();
                            usr_sell = (Map<String, Object>) dataSnapshot.getValue();

                            for (Map.Entry<String, Object> entry : usr_sell.entrySet()) {
                                Map singlesell = (Map) entry.getValue();
                                allsell.add((String) singlesell.get("username"));
                                newarr.add((String) singlesell.get("join"));
                                allusr.add(entry.getKey());
                            }
//
//                for(int i = 0; i<allusr.size(); i++) {
//                    if (newarr.get(i).equals("1")) {
//                        ValueEventListener joinlist = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {

                            for (Map.Entry<String, Object> entry : usr_sell.entrySet()) {
                                Map singlesell = (Map) entry.getValue();

                                allsell.add((String) singlesell.get("username"));
                                bidamt.add(Double.valueOf((String) singlesell.get("bid amount")));
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

                                    A = new double[1][usr_sell.size()];
                                    for (int i = 0; i < c.length; i++) {
                                        A[0][i] = 1;
                                        System.out.print("A: " + A);
                                    }

                                    B = new double[usr_buy.size()];
                                    for (int i = 0; i < B.length; i++) {
                                        B[i] = evreq.get(i).doubleValue();
                                    }

                                    lb = new double[usr_sell.size()];
                                    for (int i = 0; i < c.length; i++) {
                                        lb[i] = 0;
                                        System.out.print("LB: " + lb);
                                    }

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
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BuyerMain.this, "Seller(s) value too low or not enough sellers", Toast.LENGTH_SHORT).show();
                                            }
                                            //fail(e.toString());
                                        });
                                    }

                                    if (sol != null && sol.length >= 1) {
                                        for (int i = 0; i < sol.length; i++) {
                                            outa += sol[i];
                                            outp += c[i] * sol[i];
                                        }
                                        Log.d(TAG, "Output after: " + outa);


                                        TableLayout tabLay = (TableLayout) findViewById(R.id.tableinfobuy);
                                        RelativeLayout relLay = (RelativeLayout) findViewById(R.id.relbuy);

                                        for (int i = 0; i < sol.length; i++) {
                                            tr = new TableRow(BuyerMain.this);
                                            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                            sellusr = new TextView(BuyerMain.this);
                                            sellamt = new TextView(BuyerMain.this);

                                            sellusr.setText("Seller " + (i + 1) + " Bid Amt: " + ub[i] + " k" +
                                                    "Wh");
                                            sellusr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            tr.addView(sellusr);

                                            sellamt.setText("Cala Amt: " + Double.toString(Math.round(sol[i])) + " kWh");
                                            sellamt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            tr.addView(sellamt);

                                            tabLay.addView(tr, new TableLayout.LayoutParams(
                                                    TableRow.LayoutParams.FILL_PARENT,
                                                    TableRow.LayoutParams.WRAP_CONTENT));
                                        }

//                                    mSeekBar.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v)
//                                        {
//                                            //change boolean value
//                                            for (int i = 0; i < sol.length; i++) {
//                                                if(i>=0) {
//                                                    mQueue.clear();
//                                                    mQueue.offer(i + 1);
//                                                }
//                                            }
//                                        }
//                                    });
//
//                                    mNetworkRunnableSend = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            String urlBase = "http://"+ "" +"/arduino/analog/13/";
//                                            String url;
//                                            try {
//                                                while(!mStop.get()){
//                                                    int val = mQueue.take();
//                                                    if(val >= 0){
//                                                        HttpClient httpClient = new DefaultHttpClient();
//                                                        url = urlBase.concat(String.valueOf(val));
//                                                        HttpResponse response = httpClient.execute(new HttpGet(url));
//                                                    }
//                                                }
//                                            } catch (ClientProtocolException e) {
//                                                e.printStackTrace();
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            sNetworkThreadSend = null;
//                                        }
//                                    };

                                        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        params1.setMargins(30, 100, 10, 10);
                                        params1.span = 2;

                                        buytot = new TextView(BuyerMain.this);
                                        pricetot = new TextView(BuyerMain.this);
                                        buytot.setLayoutParams(params1);
                                        pricetot.setLayoutParams(params1);

                                        buytot.setText("EV driver total energy amount: " + Math.round(outa) + " kWh");
                                        buytot.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        pricetot.setText("EV driver total cost: " + "$" + Math.round(outp));
                                        pricetot.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                        tabLay.addView(buytot);
                                        tabLay.addView(pricetot);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message
                                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                    // ...
                                }
                            };
                            databaseref.child("ev drivers").addValueEventListener(joinlist2);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                // Getting Post failed, log a message
//                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                                // ...
//                            }
////                        };
//                        databaseref.child("sellers").addValueEventListener(joinlist);

                            btn3.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    startActivity(new Intent(BuyerMain.this, BuyerProfile.class));
                                }
                            });

                            btn4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("0");
                                    mAuth.signOut();
                                    startActivity(new Intent(BuyerMain.this, LoginActivity.class));
                                }
                            });

                            btn2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("0");
                                    Intent mIntent = new Intent(BuyerMain.this, BuyerInput.class);
                                    mIntent.putExtra("FROM_ACTIVITY", "BuyerMain");
                                    startActivity(mIntent);
                                }
                            });
//                    }
//                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    };
                    databaseref.child("sellers").addValueEventListener(checklist2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseref.child("ev drivers").child(muser.getUid()).child("join").addValueEventListener(buylist);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mFetchAddressButton.setText(String.valueOf(mLastLocation.getLatitude()) +", " + String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void log(String s){
        Log.d(">==< "+TAG+" >==<", s);
    }

    @Override
    public void onStart() {

        mStop.set(false);
        if(sNetworkThreadSend == null){
            sNetworkThreadSend = new Thread(mNetworkRunnableSend);
            sNetworkThreadSend.start();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("0");
        mStop.set(true);
        mQueue.clear();
        if(sNetworkThreadSend != null) sNetworkThreadSend.interrupt();
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
