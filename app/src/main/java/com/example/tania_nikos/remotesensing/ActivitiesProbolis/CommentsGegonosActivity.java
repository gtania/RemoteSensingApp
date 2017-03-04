package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.CommentsDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.CustomCommentsListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class CommentsGegonosActivity extends Activity {

    /**
     * Data variables
     */
    protected JSONArray eventImages;
    protected String jsonArray;
    protected int position;
    protected ListView listView ;
    protected JSONArray commentsArray;
    protected CustomCommentsListAdapter adapter;
    protected int image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_gegonos);
        InternetHandler.checkInternet(this);

        Intent intent = getIntent();
        jsonArray = intent.getStringExtra("eventImages");
        position = intent.getIntExtra("position", 0);
        Log.d("APP", "IN COMMENTS" + jsonArray);

        try {

            eventImages = new JSONArray(jsonArray);
            JSONObject image = eventImages.getJSONObject(position);

            image_id = image.getInt("id");
            listView = (ListView) findViewById(R.id.comments_list_gegonos);
        } catch (JSONException e) {
            Log.d("APP", " JSON EXCEPTION");

            e.printStackTrace();
        }

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + "images/events/"+ Integer.toString(image_id) +"/getcomments", new AsyncResponse() {
            public void processFinish(Response response) {

                try {
                    //parse response
                    commentsArray = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Integer> ids = new ArrayList<Integer>();
                List<String> comments_texts = new ArrayList<String>();
                for (int i =0 ; i< commentsArray.length(); i++)
                {
                    try {
                        JSONObject comment = commentsArray.getJSONObject(i);
                        ids.add(comment.getInt("id"));
                        comments_texts.add(comment.getString("text"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                final String[] comments = {
                        "tesst here yaergsagsdgsddgf dsfgsdfg sdfgsdfgsdfgsdfgsdfg sdfgsdfgdsf gsdfgsdfgsdfgsdfgsdfgsdfgsdf gsdf gsdfgsdfgsdfgsdf g",
                        "test there"
                };
                Log.d("APP", "before dHand");
                CommentsDeleteHandler deleteHandler = new CommentsDeleteHandler(
                        CommentsDeleteHandler.events,
                        ids.toArray( new Integer[ids.size()]),//ids.toArray(new Integer[ids.size()]),
                        comments_texts.toArray(new String[comments_texts.size()]),//comments_texts.toArray(new String[comments_texts.size()]),
                        new AsyncResponse() {
                            @Override
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

                                    finish();
                                    Intent intent = new Intent(CommentsGegonosActivity.this, TabsGegonosActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("eventImages", eventImages.toString());
                                    startActivity(intent);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Διαγραφή μη επιτυχής",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    });
                                }
                            }
                        });

                Log.d("THIS", "before Custom Adaptr");
                adapter = new CustomCommentsListAdapter(CommentsGegonosActivity.this,
                        comments_texts.toArray(new String[comments_texts.size()]),
                        deleteHandler);

                Log.d("THIS", "before Seting list vie");
                // adapter.notifyDataSetChanged();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }

                });

                Log.d("THIS", "After SET SET");
            }
        });
        new HttpTaskHandler().execute(get);
    }

    /**
     * Save Comment
     *
     * @param view
     */
    public void saveComment(View view)
    {
        Spiner.show(this);

        EditText comment = (EditText)  findViewById(R.id.gegonos_comment);

        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("comment_text", comment.getText().toString()));

        HttpPostTask post = new HttpPostTask( HttpTaskHandler.baseUrl + "images/events/"+ image_id +"/postcomments", data, new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
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

                    Intent intent = new Intent(CommentsGegonosActivity.this, TabsGegonosActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("eventImages", eventImages.toString());
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
