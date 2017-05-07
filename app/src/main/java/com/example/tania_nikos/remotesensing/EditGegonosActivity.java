package com.example.tania_nikos.remotesensing;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpPutTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.Models.Event;
import com.example.tania_nikos.remotesensing.Models.Field;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class EditGegonosActivity extends ActionBarActivity {

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
        String eidos_kaliergias = getIntent().getStringExtra("eidos");


        EditText onoma_gegonotos_textfield   = (EditText)findViewById(R.id.onoma_gegonos_edit);
        EditText perigrafi_gegonotos_textfield   = (EditText)findViewById(R.id.perigrafi_gegonos_edit);
        EditText onoma_xorafiou_textfield   = (EditText)findViewById(R.id.onoma_xwrafiou_gegonos_edit);
        EditText perioxi_xorafiou_textfield   = (EditText)findViewById(R.id.perioxi_xwrafiou_gegonos_edit);
        EditText etos_kaliergias_textfield   = (EditText)findViewById(R.id.etos_kaliergias_gegonos_edit);
        EditText onoma_kaliergiti_textfield   = (EditText)findViewById(R.id.onoma_kaliergiti_gegonos_edit);
        EditText eidos_kaliergeias_textfield   = (EditText)findViewById(R.id.eidos_kaliergeias_gegonos_edit);

        onoma_gegonotos_textfield.setText(onoma_gegonotos);
        perigrafi_gegonotos_textfield.setText(perigrafi_gegonotos);
        onoma_xorafiou_textfield.setText(onoma_xorafiou);
        perioxi_xorafiou_textfield.setText(perioxi_xorafiou);
        etos_kaliergias_textfield.setText(etos_kaliergias);
        onoma_kaliergiti_textfield.setText(onoma_kaliergiti);
        eidos_kaliergeias_textfield.setText(eidos_kaliergias);
    }

    /**
     * Update XEvent
     *
     * @param view
     */
    public void updateGegonos(View view)
    {
        Spiner.show(this);
        String deviceId = Device.getId(this);

        EditText onoma_gegonotos  = (EditText)findViewById(R.id.onoma_gegonos_edit);
        EditText perigrafi_gegonotos   = (EditText)findViewById(R.id.perigrafi_gegonos_edit);
        EditText onoma_xorafiou   = (EditText)findViewById(R.id.onoma_xwrafiou_gegonos_edit);
        EditText perioxi_xorafiou   = (EditText)findViewById(R.id.perioxi_xwrafiou_gegonos_edit);
        EditText etos_kaliergias   = (EditText)findViewById(R.id.etos_kaliergias_gegonos_edit);
        EditText onoma_kaliergiti   = (EditText)findViewById(R.id.onoma_kaliergiti_gegonos_edit);
        EditText eidos_kaliergias   = (EditText)findViewById(R.id.eidos_kaliergeias_gegonos_edit);

        // Set up data for post
        ContentValues data = new ContentValues();
        data.put("device_id", deviceId);
        data.put("onoma_gegonotos", onoma_gegonotos.getText().toString());
        data.put("perigrafi_gegonotos", perigrafi_gegonotos.getText().toString());
        data.put("onoma_xorafiou", onoma_xorafiou.getText().toString());
        data.put("perioxi_xorafiou", perioxi_xorafiou.getText().toString() );
        data.put("etos_kaliergias", etos_kaliergias.getText().toString());
        data.put("onoma_kaliergiti", onoma_kaliergiti.getText().toString());
        data.put("eidos", eidos_kaliergias.getText().toString());

        Event event = new Event(this);
        int rows_affected = event.updateEvent(this.event_id, data);
        if (rows_affected > 0) {
            Toast.makeText(
                    getApplicationContext(),
                    "Αποθήκευση επιτυχής",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(EditGegonosActivity.this, GegonotaActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Αποθήκευση μη επιτυχής",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

    /**
     * Delete Event
     *
     * @param view
     */
    public void deleteGegonos(View view)
    {
        Spiner.show(this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        removeEvent();
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

    public void removeEvent()
    {

        String deviceId = Device.getId(this);

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
                Spiner.dismiss();

            }
        });
        new HttpTaskHandler().execute(delete);
    }
}
