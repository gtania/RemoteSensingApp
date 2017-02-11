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
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpPutTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class EditXorafiActivity extends AppCompatActivity {

    /**
     * Selected field id
     */
    int field_id;

    /**
     * Initialize view
     * Load data of the selected item
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_xorafi);

        System.out.println("in new view");

        // get data from previous intent
        this.field_id = (int) getIntent().getIntExtra("id", 0);

        String onoma_xorafiou = getIntent().getStringExtra("onoma_xorafiou");
        String perioxi_xorafiou = getIntent().getStringExtra("perioxi_xorafiou");
        String etos_kaliergias = getIntent().getStringExtra("etos_kaliergias");
        String onoma_kaliergiti = getIntent().getStringExtra("onoma_kaliergiti");


        EditText onoma_xorafiou_textfield   = (EditText)findViewById(R.id.onoma_xorafiou_edit);
        EditText perioxi_xorafiou_textfield   = (EditText)findViewById(R.id.perioxi_xorafiou_edit);
        EditText etos_kaliergias_textfield   = (EditText)findViewById(R.id.etos_kaliergias_edit);
        EditText onoma_kaliergiti_textfield   = (EditText)findViewById(R.id.onoma_kaliergiti_edit);

        onoma_xorafiou_textfield.setText(onoma_xorafiou);
        perioxi_xorafiou_textfield.setText(perioxi_xorafiou);
        etos_kaliergias_textfield.setText(etos_kaliergias);
        onoma_kaliergiti_textfield.setText(onoma_kaliergiti);
    }

    /**
     * Update Xorafi
     *
     * @param view
     */
    public void updateXorafi(View view)
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        EditText onoma_xorafiou   = (EditText)findViewById(R.id.onoma_xorafiou_edit);
        EditText perioxi_xorafiou   = (EditText)findViewById(R.id.perioxi_xorafiou_edit);
        EditText etos_kaliergias   = (EditText)findViewById(R.id.etos_kaliergias_edit);
        EditText onoma_kaliergiti   = (EditText)findViewById(R.id.onoma_kaliergiti_edit);

        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("onoma_xorafiou", onoma_xorafiou.getText().toString()));
        data.add(new BasicNameValuePair("perioxi_xorafiou", perioxi_xorafiou.getText().toString() ));
        data.add(new BasicNameValuePair("etos_kaliergias", etos_kaliergias.getText().toString()));
        data.add(new BasicNameValuePair("onoma_kaliergiti", onoma_kaliergiti.getText().toString()));

        HttpPutTask put = new HttpPutTask( HttpTaskHandler.baseUrl + deviceId + "/fields/" + this.field_id, data, new AsyncResponse() {
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

                    Intent intent = new Intent(EditXorafiActivity.this, XorafiaActivity.class);
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
     * Delete Xorafi
     *
     * @param view
     */
    public void deleteXorafi(View view)
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        HttpDeleteTask delete = new HttpDeleteTask(HttpTaskHandler.baseUrl + deviceId + "/fields/" + this.field_id, new AsyncResponse() {
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

                    Intent intent = new Intent(EditXorafiActivity.this, XorafiaActivity.class);
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
