package com.example.tania_nikos.remotesensing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class EisagwgiGegonosActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eisagwgi_gegonos);
        InternetHandler.checkInternet(this);
    }

    /**
     * Save Gegonos to the back end
     *
     * @param view
     */
    public void saveGegonos(View view)
    {
        Spiner.show(this);
        String deviceId = Device.getId(this);

        EditText onoma_gegonotos   = (EditText)findViewById(R.id.onoma_gegonotos);
        EditText perigrafi_gegonotos   = (EditText)findViewById(R.id.perigrafi_gegonos_edit);
        EditText onoma_xorafiou   = (EditText)findViewById(R.id.onoma_xorafiou_gegonos);
        EditText perioxi_xorafiou   = (EditText)findViewById(R.id.perioxi_xorafiou_gegonos);
        EditText etos_kaliergias   = (EditText)findViewById(R.id.etos_kaliergias_gegonos);
        EditText onoma_kaliergiti   = (EditText)findViewById(R.id.onoma_kaliergiti_gegonos);
        EditText eidos_kaliergeias   = (EditText)findViewById(R.id.eidos_kaliergeias_gegonos);


        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("onoma_gegonotos", onoma_gegonotos.getText().toString()));
        data.add(new BasicNameValuePair("perigrafi_gegonotos", perigrafi_gegonotos.getText().toString() ));
        data.add(new BasicNameValuePair("onoma_xorafiou", onoma_xorafiou.getText().toString()));
        data.add(new BasicNameValuePair("perioxi_xorafiou", perioxi_xorafiou.getText().toString() ));
        data.add(new BasicNameValuePair("etos_kaliergias", etos_kaliergias.getText().toString()));
        data.add(new BasicNameValuePair("onoma_kaliergiti", onoma_kaliergiti.getText().toString()));
        data.add(new BasicNameValuePair("eidos", eidos_kaliergeias.getText().toString()));

        HttpPostTask post = new HttpPostTask( HttpTaskHandler.baseUrl + deviceId + "/events", data, new AsyncResponse() {
            public void processFinish(Response response) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("In call TRERERERERRE" + jObject.toString());

                if (response.status_code == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Εγγραφή επιτυχής",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

                    Intent intent = new Intent(EisagwgiGegonosActivity.this, GegonotaActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Εγγραφή μη επιτυχής",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
                Spiner.dismiss();
            }
        });

        new HttpTaskHandler().execute(post);
    }
}
