package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.EisagwgiXorafiActivity;
import com.example.tania_nikos.remotesensing.Helpers.CommentsDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.CustomCommentsListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.CustomListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.ListDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.R;
import com.example.tania_nikos.remotesensing.XorafiaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class CommentsActivity extends Activity {

    /**
     * Data variables
     */
    protected JSONArray fieldImages;
    protected String jsonArray;
    protected int position;
    protected ListView listView ;
    protected JSONArray commentsArray;
    protected CustomCommentsListAdapter adapter;
    protected int image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        InternetHandler.checkInternet(this);

        Intent intent = getIntent();
        jsonArray = intent.getStringExtra("fieldImages");
        position = intent.getIntExtra("position", 0);
        Log.d("APP", "IN COMMENTS" + jsonArray);
        try {

            fieldImages = new JSONArray(jsonArray);
            JSONObject image = fieldImages.getJSONObject(position);

            image_id = image.getInt("id");
            listView = (ListView) findViewById(R.id.comments_list);
        } catch (JSONException e) {
            Log.d("APP", " JSON EXCEPTION");

            e.printStackTrace();
        }

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + "images/fields/"+ Integer.toString(image_id) +"/comments", new AsyncResponse() {
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


                            Log.d("APP", "before dHand");
                            CommentsDeleteHandler deleteHandler = new CommentsDeleteHandler(
                                    CommentsDeleteHandler.fields,
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
                                                Intent intent = new Intent(CommentsActivity.this, TabsXwrafiActivity.class);
                                                intent.putExtra("position", position);
                                                intent.putExtra("fieldImages", fieldImages.toString());
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
                            adapter = new CustomCommentsListAdapter(CommentsActivity.this,
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

    public void saveComment(View view)
    {
        Spiner.show(this);
        EditText comment = (EditText)  findViewById(R.id.comment_textview);

        // Set up data for post
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("comment_text", comment.getText().toString()));

        HttpPostTask post = new HttpPostTask( HttpTaskHandler.baseUrl + "images/fields/"+ image_id +"/comments", data, new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

                    Intent intent = new Intent(CommentsActivity.this, TabsXwrafiActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("fieldImages", fieldImages.toString());
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

    public void home(View view)
    {
        Intent intent = new Intent(CommentsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
