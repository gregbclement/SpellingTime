package com.gregbclement.spellingtime.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.gregbclement.spellingtime.model.SpellingWordDefinition;
import com.gregbclement.spellingtime.model.SpellingList;
import com.gregbclement.spellingtime.model.SpellingWord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * A class to retrieve Spelling Words from the REST API
 */
public class SpellingWordRepository {
    private static final String URL = RESTConfig.BASE_URL + "SpellingWord";
    Context context;

    public SpellingWordRepository(Context context) {
        this.context = context;
    }

    /**
     * Gets the words that are associated with a SpellingList
     * @param list the SpellingList
     * @param callback a callback that is invoked when the data is retrieved from the server
     */
    public void getListSpellingWords(SpellingList list, final NetworkCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);


        String url = URL + "?listId=" + list.getId() + "&inactive=false";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                try {
                    // There is an instance where the JSON is malformed coming back from the server
                    // It is sometimes sending the definition over as a string, but the app is expecting
                    // and object.  This just does a quick correction of the raw json
                    response = response.replaceAll("\"wordDefinition\":\"\"", "\"wordDefinition\":{}");
                    SpellingWord[] spellingWordsArray = gson.fromJson(response, SpellingWord[].class);

                    List<SpellingWord> spellingWords = Arrays.asList(spellingWordsArray);


                    callback.onComplete(spellingWords);
                }catch (Exception e) {
                    Log.e("SpellingTime","Error deserializing", e);
                    Log.i("SpellingTime",response);
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
                                Log.e("SpellingTime","exception",e);
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
            Log.e("SpellingTime","exception",e);
        }
    }

    public void getWordDefinition(SpellingWord spellingWord, final NetworkCallback callback) {
        String url = URL + "/Definition?word=" + spellingWord.getWord();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response != null && response != "") {

                    response = response.replaceAll("\\\\u[0-9]{3}[A-Z a-z 0-9]", "");
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
