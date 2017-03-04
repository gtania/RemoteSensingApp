package com.example.tania_nikos.remotesensing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpPutTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class EditXorafiActivity extends ActionBarActivity {

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
        InternetHandler.checkInternet(this);

        System.out.println("in new view");

        // get data from previous intent
        this.field_id = (int) getIntent().getIntExtra("id", 0);

        String onoma_xorafiou = getIntent().getStringExtra("onoma_xorafiou");
        String perioxi_xorafiou = getIntent().getStringExtra("perioxi_xorafiou");
        String etos_kaliergias = getIntent().getStringExtra("etos_kaliergias");
        String onoma_kaliergiti = getIntent().getStringExtra("onoma_kaliergiti");
        String eidos_kaliergeias = getIntent().getStringExtra("eidos");

        EditText onoma_xorafiou_textfield   = (EditText)findViewById(R.id.onoma_xorafiou_edit);
        EditText perioxi_xorafiou_textfield   = (EditText)findViewById(R.id.perioxi_xorafiou_edit);
        EditText etos_kaliergias_textfield   = (EditText)findViewById(R.id.etos_kaliergias_edit);
        EditText onoma_kaliergiti_textfield   = (EditText)findViewById(R.id.onoma_kaliergiti_edit);
        EditText eidos_kaliergeias_textfield   = (EditText)findViewById(R.id.eidos_kaliergeias_edit);

        onoma_xorafiou_textfield.setText(onoma_xorafiou);
        perioxi_xorafiou_textfield.setText(perioxi_xorafiou);
        etos_kaliergias_textfield.setText(etos_kaliergias);
        onoma_kaliergiti_textfield.setText(onoma_kaliergiti);
        eidos_kaliergeias_textfield.setText(eidos_kaliergeias);
    }

    /**
     * Update Xorafi
     *
     * @param view
     */
    public void updateXorafi(View view)
    {
        Spiner.show(this);
        String deviceId = Device.getId(this);

        EditText onoma_xorafiou   = (EditText)findViewById(R.id.onoma_xorafiou_edit);
        EditText perioxi_xorafiou   = (EditText)findViewById(R.id.perioxi_xorafiou_edit);
        EditText etos_kaliergias   = (EditText)findViewById(R.id.etos_kaliergias_edit);
        EditText onoma_kaliergiti   = (EditText)findViewById(R.id.onoma_kaliergiti_edit);
        EditText eidos_kaliergeias   = (EditText)findViewById(R.id.eidos_kaliergeias_edit);

        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("onoma_xorafiou", onoma_xorafiou.getText().toString()));
        data.add(new BasicNameValuePair("perioxi_xorafiou", perioxi_xorafiou.getText().toString() ));
        data.add(new BasicNameValuePair("etos_kaliergias", etos_kaliergias.getText().toString()));
        data.add(new BasicNameValuePair("onoma_kaliergiti", onoma_kaliergiti.getText().toString()));
        data.add(new BasicNameValuePair("eidos", eidos_kaliergeias.getText().toString()));

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
                Spiner.dismiss();
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

        Spiner.show(this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        removeField();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Αυτή η ενέργεια θα διαγράψει και όλες τις συνδεδεμένες φωτογραφείες.\nΕίστε σίγουροι για την διαγραφή?").setPositiveButton("Ναι", dialogClickListener)
                .setNegativeButton("Οχι", dialogClickListener).show();


    }

    public void removeField()
    {
        String deviceId = Device.getId(this);
        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + deviceId + "/fields/" + this.field_id + "/images", new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                JSONArray fieldImages = null;
                try {
                    //parse response
                    fieldImages = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Get images call  : ","file Deleted :" + response.status_code);


                if (response.status_code == 200) {
                    for (int i =0 ; i< fieldImages.length(); i++)
                    {
                        try {
                            JSONObject field = fieldImages.getJSONObject(i);
                            File file = new File(field.getString("filepath"));
                            Log.d("ALMOST : ","file Deleted :" + file.getPath());

                            if (file.exists()) {
                                if (file.delete()) {
                                    Log.d("DELETE : ","file Deleted :" + file.getPath());
                                } else {
                                    Log.d("DELETE : ","file not Deleted :" + file.getPath());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                removeFromDb();
            }

        });

        new HttpTaskHandler().execute(get);
    }

    protected void removeFromDb()
    {
        String deviceId = Device.getId(this);
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
                Spiner.dismiss();

            }
        });
        new HttpTaskHandler().execute(delete);
    }
}
