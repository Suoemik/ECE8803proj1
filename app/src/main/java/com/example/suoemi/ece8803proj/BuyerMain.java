package com.example.suoemi.ece8803proj;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

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
    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_main);

        this.btn = (Button) findViewById(R.id.begin_btn);
        this.btn2 = (Button) findViewById(R.id.energyreq_btn);
        this.btn3 = (Button)findViewById(R.id.buysett_btn);
        this.btn4 = (Button) findViewById(R.id.bm_logout) ;
        final DbHandler dbHandler = new DbHandler(this);
        final LoginActivity loginActivity = new LoginActivity();
        final List<LoginData> loginDatas = dbHandler.getAllEVLog();
        final List<LoginData> sellList = dbHandler.getAllSellLog();

        mResultReceiver = new AddressResultReceiver(new Handler());
        mFetchAddressButton = (Button) findViewById(R.id.benterloc);

        mAddressRequested = false;
        mAddressOutput = "";

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(LoginData loginData : sellList)
                {
                    if(loginData.getCheck()==1){
                        loginData.setJoin(1);
                    }
                }
                startActivity(new Intent(BuyerMain.this, Output.class));
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

                for(LoginData loginData : loginDatas){
                        loginData.setCheck(0);
                        dbHandler.updateEVLoginData(loginData);
                        String log = "Id: " + loginData.getId() + ", Name: "
                                + loginData.getUsername() + ", Password: "
                                + loginData.getPassword() + ", Current: " + loginData.getCheck()
                                + ", Req: " + loginData.getEVReq();
                        Log.d("Logout:: ", log);
                }
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
        for(LoginData loginData : loginDatas) {
             if (loginData.getUsername().equals(loginActivity.getusrnm())) {
                int req = loginData.getEVReq();
                btn2.setText(Integer.toString(req));
            }
        }
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
}
