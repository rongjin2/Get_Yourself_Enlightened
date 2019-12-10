package com.example.justfordebugging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private TextView mytext;
    private RequestQueue mqueue;
    String what_they_say;
    private int state;
    private JSONObject famous_guy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = 99;
        setContentView(R.layout.activity_main);
        mytext = findViewById(R.id.textView);
        mytext.setVisibility(View.INVISIBLE);
        final Button selection = findViewById(R.id.setPhrase);
        mqueue = Volley.newRequestQueue(this);
        selection.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              mytext.setText(null);
                                              //selection.setBackgroundResource(R.drawable.poop_new);
                                              jsonparse();
                                          }
                                      }
        );
        final ImageButton conf = findViewById(R.id.conf);
        final ImageButton jobs = findViewById(R.id.jobs);
        final ImageButton shake = findViewById(R.id.shake);
        final ImageButton stephen = findViewById(R.id.stephen);
        final Button clear = findViewById(R.id.Clear);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 0;
                conf.setAlpha((float) 0.5);
                jobs.setAlpha((float) 1);
                shake.setAlpha((float) 1);
                stephen.setAlpha((float) 1);
                mytext.setText("");
            }
        });

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                conf.setAlpha((float) 1);
                jobs.setAlpha((float) 0.5);
                shake.setAlpha((float) 1);
                stephen.setAlpha((float) 1);
                mytext.setText("");
            }
        });

        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 2;
                conf.setAlpha((float) 1);
                jobs.setAlpha((float) 1);
                shake.setAlpha((float) 0.5);
                stephen.setAlpha((float) 1);
                mytext.setText("");
            }
        });

        stephen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 3;
                conf.setAlpha((float) 1);
                jobs.setAlpha((float) 1);
                shake.setAlpha((float) 1);
                stephen.setAlpha((float) 0.5);
                mytext.setText("");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 99;
                mytext.setText("");
                conf.setAlpha((float) 1);
                jobs.setAlpha((float) 1);
                shake.setAlpha((float) 1);
                stephen.setAlpha((float) 1);
            }
        });


    }


    private void jsonparse() {
        mytext.setText(null);
        mytext.setVisibility(View.VISIBLE);

        String url = "https://api.myjson.com/bins/120ibc";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray celebrity = response.getJSONArray("famous");
                    setup_quotes(celebrity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mqueue.add(request);
    }

    private int[] getrandom_quotes() {
        int[] index = new int[2];
        int get_random_celebrity = new Random().nextInt(4);
        int get_random_quotes = new Random().nextInt(10);
        index[0] = get_random_celebrity;
        index[1] = get_random_quotes;
        return index;
    }

    private void setup_quotes(JSONArray quotes) throws JSONException {
        int[] index_comb = getrandom_quotes();
        int random_celebrity = index_comb[0];
        int random_quote = index_comb[1];
        if (state == 99) {
            state = random_celebrity;
            famous_guy = quotes.getJSONObject(random_celebrity);
        } else {
            famous_guy = quotes.getJSONObject(state);
        }
        String mycelebrity = "";
        String full_name ="";
        if (state == 0) {
            mycelebrity = "confucius";
            full_name = "Confucius";
        } else if (state == 1) {
            mycelebrity = "jobs";
            full_name = "Steve Jobs";
        } else if (state == 2) {
            mycelebrity = "shakespeare";
            full_name = "William Shakespeare";
        } else if (state == 3) {
            mycelebrity = "stephen";
            full_name = "Stephen Hawking";
        }
        JSONArray rand_quotes = famous_guy.getJSONArray(mycelebrity);
        what_they_say = rand_quotes.getString(random_quote);
        String original = "As xx once said, ";
        String new_string = original.replace("xx", full_name);
        new_string = new_string + what_they_say;
        mytext.setText(new_string);
    }
}
