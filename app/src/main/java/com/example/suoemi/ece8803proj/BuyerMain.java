package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Suoemi on 3/17/2017.
 */

public class BuyerMain extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
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
    private Map<String, Object> usr_sell;
    private Map<String, Object> usr_buy;
    private double outp;
    private double outa;

    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private TextView sellusr;
    private TextView sellamt;
    private TextView buytot;
    private TextView pricetot;
    private ArrayList<String> allsell;
    private ArrayList<String> allbuy;

    private TextView title;
    private TableRow tr;

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private DatabaseReference databaseref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "BuyerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_main);

        this.btn = (Button) findViewById(R.id.begin_btn);
        this.btn2 = (Button) findViewById(R.id.energyreq_btn);
        this.btn3 = (Button)findViewById(R.id.buysett_btn);
        this.btn4 = (Button) findViewById(R.id.bm_logout) ;

        allsell = new ArrayList<>();
        allbuy = new ArrayList<>();

        databaseref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        mResultReceiver = new AddressResultReceiver(new Handler());
        mFetchAddressButton = (Button) findViewById(R.id.benterloc);

        mAddressRequested = false;
        mAddressOutput = "";

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollbuy);

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

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseref.child("ev drivers").child(muser.getUid()).child("join").setValue("1");
                ValueEventListener joinlist = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        ArrayList<String> allusr = new ArrayList<String>();
                        usr_sell = (Map<String, Object>) dataSnapshot.getValue();

                        for(Map.Entry<String, Object> entry : usr_sell.entrySet())
                        {
                            Map singlesell = (Map) entry.getValue();
                            //allsell.add((String) singlesell.get("username"));
                            allusr.add(entry.getKey());
                        }
                        for(int i = 0; i<allusr.size(); i++)
                        {
                            Log.d(TAG, "array list for seller: " + allusr.get(i));
                            databaseref.child("sellers").child(allusr.get(i)).child("join").setValue("0");
                        }

                        ValueEventListener joinlist2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.d(TAG, "usr_sell: " + allsell);
                                usr_buy = (Map<String, Object>) dataSnapshot.getValue();

                                for (Map.Entry<String, Object> entry : usr_buy.entrySet()) {
                                    Map singlebuy = (Map) entry.getValue();
                                    allbuy.add((String) singlebuy.get("username"));
                                }
                                Log.d(TAG, "usr_buy: " + allbuy);
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

        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
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
        buildGoogleApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    protected void displayAddressOutput() {
        mFetchAddressButton.setText(mAddressOutput);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.e("RESULT!!!", mAddressOutput);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(BuyerMain.this, getString(R.string.address_found), Toast.LENGTH_SHORT);
            }

        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                startIntentService();
            }
        }
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
