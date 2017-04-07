package com.example.suoemi.ece8803proj;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.joptimizer.optimizers.LPOptimizationRequest;
import com.joptimizer.optimizers.LPPrimalDualMethod;
import com.joptimizer.optimizers.OptimizationResponse;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by Suoemi on 3/30/2017.
 */

public class Output extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int c1;
    private int u1;
    private int returnCode;
    private double[] sol;
    TableRow tr;
    TextView sellusr;
    TextView sellamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ouput);

        RelativeLayout relLay = (RelativeLayout) findViewById(R.id.info);
        TableLayout tabLay = (TableLayout) findViewById(R.id.tableinfo);

        sellusr = new TextView(this);
        sellamt = new TextView(this);
        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 0, 10, 10);

        sellusr.setText("Seller ID");
        //sellusr.setLayoutParams(params);
        sellusr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sellusr.setTypeface(null, Typeface.BOLD);
        //sellusr.setPadding(5, 5, 5, 0);
        tr.addView(sellusr);

        sellamt.setText("Bidding Amount");
        //sellamt.setLayoutParams(params);
        sellamt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sellamt.setTypeface(null, Typeface.BOLD);
        //sellamt.setPadding(5, 5, 5, 0);
        tr.addView(sellamt);

        tabLay.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        try {
            CostMin();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < sol.length; i++)
        {
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            sellusr = new TextView(this);
            sellamt = new TextView(this);

            sellusr.setText("Seller" + i);
            sellusr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(sellusr);

            sellamt.setText(Double.toString(sol[i]));
            sellamt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(sellamt);

            tabLay.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void CostMin() throws Exception {
        DbHandler db = new DbHandler(this);
        LPOptimizationRequest or = new LPOptimizationRequest();
        List<LoginData> loginDatasell = db.getAllSellLog();
        for(LoginData loginData : loginDatasell)
        {
            if(loginData.getCheck() == 1)
            {
                c1 = loginData.getePrice();
                u1 = loginData.geteBid();
            }
        }

        double[] c = new double[] {c1, 40, 20};
        double[][] A = new double[][] {{1, 1, 1}};
        double[] B = new double[] {110};
        double[] lb = new double[] {0, 0, 0};
        double[] ub = new double[] {u1, 100, 50};

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
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Output Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

