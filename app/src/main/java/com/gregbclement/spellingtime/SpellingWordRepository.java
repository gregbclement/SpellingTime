package com.gregbclement.spellingtime;

import android.content.Context;
import android.view.textservice.SpellCheckerInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpellingWordRepository {
    private static  final  String BASE_URL = "https://gregbclement.com/";
    private static final String URL = BASE_URL + "api/SpellingWord";
    Context context;

    public SpellingWordRepository(Context context) {
        this.context = context;
    }

    public void getListSpellingWords(SpellingList list, final NetworkCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);


        String url = URL + "?listId=" + list.getId() + "&inactive=false";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                SpellingWord[] spellingWordsArray = gson.fromJson(response, SpellingWord[].class);

                List<SpellingWord> spellingWords = Arrays.asList(spellingWordsArray);


                callback.onComplete(spellingWords);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void saveSpellingWord(SpellingWord spellingWord, final NetworkCallback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(spellingWord);
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Gson gson = new Gson();
                            SpellingWord dbWord  = gson.fromJson(response.toString(), SpellingWord.class);

                            callback.onComplete(dbWord);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            queue.add(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getWordDefinition(SpellingWord spellingWord, final NetworkCallback callback) {
        String url = BASE_URL + "Spelling/GetWordDefinition?word=" + spellingWord.getWord();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response != null && response != "") {

                    Gson gson = new Gson();
                    SpellingWordDefinition definition = gson.fromJson(response, SpellingWordDefinition.class);


                    callback.onComplete(definition);
                }
                else {
                    callback.onComplete(null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void deleteSpellingWord(SpellingWord spellingWord, final NetworkCallback callback) {
        String url = URL + "?id=" + spellingWord.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                callback.onComplete(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
}
