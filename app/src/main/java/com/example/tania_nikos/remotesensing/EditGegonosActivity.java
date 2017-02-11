package com.example.tania_nikos.remotesensing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpPutTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class EditGegonosActivity extends AppCompatActivity {

    /**
     * Event id of the selected
     */
    int event_id;

    /**
     * Initialize view
     * Load data of selected item
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gegonos);

        // get data from previous intent
        this.event_id = (int) getIntent().getIntExtra("id", 0);

        String onoma_gegonotos = getIntent().getStringExtra("onoma_gegonotos");
        String perigrafi_gegonotos = getIntent().getStringExtra("perigrafi_gegonotos");
        String onoma_xorafiou = getIntent().getStringExtra("onoma_xorafiou");
        String perioxi_xorafiou = getIntent().getStringExtra("perioxi_xorafiou");
        String etos_kaliergias = getIntent().getStringExtra("etos_kaliergias");
        String onoma_kaliergiti = getIntent().getStringExtra("onoma_kaliergiti");


        EditText onoma_gegonotos_textfield   = (EditText)findViewById(R.id.onoma_gegonos_edit);
        EditText perigrafi_gegonotos_textfield   = (EditText)findViewById(R.id.perigrafi_gegonos_edit);
        EditText onoma_xorafiou_textfield   = (EditText)findViewById(R.id.onoma_xwrafiou_gegonos_edit);
        EditText perioxi_xorafiou_textfield   = (EditText)findViewById(R.id.perioxi_xwrafiou_gegonos_edit);
        EditText etos_kaliergias_textfield   = (EditText)findViewById(R.id.etos_kaliergias_gegonos_edit);
        EditText onoma_kaliergiti_textfield   = (EditText)findViewById(R.id.onoma_kaliergiti_gegonos_edit);

        onoma_gegonotos_textfield.setText(onoma_gegonotos);
        perigrafi_gegonotos_textfield.setText(perigrafi_gegonotos);
        onoma_xorafiou_textfield.setText(onoma_xorafiou);
        perioxi_xorafiou_textfield.setText(perioxi_xorafiou);
        if(etos_kaliergias != "")etos_kaliergias_textfield.setText(etos_kaliergias);
        onoma_kaliergiti_textfield.setText(onoma_kaliergiti);
    }

    /**
     * Update XEvent
     *
     * @param view
     */
    public void updateGegonos(View view)
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        EditText onoma_gegonotos  = (EditText)findViewById(R.id.onoma_gegonos_edit);
        EditText perigrafi_gegonotos   = (EditText)findViewById(R.id.perigrafi_gegonos_edit);
        EditText onoma_xorafiou   = (EditText)findViewById(R.id.onoma_xwrafiou_gegonos_edit);
        EditText perioxi_xorafiou   = (EditText)findViewById(R.id.perioxi_xwrafiou_gegonos_edit);
        EditText etos_kaliergias   = (EditText)findViewById(R.id.etos_kaliergias_gegonos_edit);
        EditText onoma_kaliergiti   = (EditText)findViewById(R.id.onoma_kaliergiti_gegonos_edit);

        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("onoma_gegonotos", onoma_gegonotos.getText().toString()));
        data.add(new BasicNameValuePair("perigrafi_gegonotos", perigrafi_gegonotos.getText().toString()));
        data.add(new BasicNameValuePair("onoma_xorafiou", onoma_xorafiou.getText().toString()));
        data.add(new BasicNameValuePair("perioxi_xorafiou", perioxi_xorafiou.getText().toString() ));
        data.add(new BasicNameValuePair("etos_kaliergias", etos_kaliergias.getText().toString()));
        data.add(new BasicNameValuePair("onoma_kaliergiti", onoma_kaliergiti.getText().toString()));

        HttpPutTask put = new HttpPutTask( HttpTaskHandler.baseUrl + deviceId + "/events/" + this.event_id, data, new AsyncResponse() {
            public void processFinish(Response response) {

                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("In call UPdateeee" + jObject.toString());

                if (response.status_code == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Αποθήκευση επιτυχής",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

                    Intent intent = new Intent(EditGegonosActivity.this, GegonotaActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Αποθήκευση μη επιτυχής",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }

            }
        });
        new HttpTaskHandler().execute(put);

    }

    /**
     * Delete Event
     *
     * @param view
     */
    public void deleteGegonos(View view)
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        HttpDeleteTask delete = new HttpDeleteTask(HttpTaskHandler.baseUrl + deviceId + "/events/" + this.event_id, new AsyncResponse() {
            public void processFinish(Response response) {
                if (response.status_code == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Διαγραφή επιτυχής",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

                    Intent intent = new Intent(EditGegonosActivity.this, GegonotaActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Αποθήκευση μη επιτυχής",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }

            }
        });
        new HttpTaskHandler().execute(delete);
    }
}
