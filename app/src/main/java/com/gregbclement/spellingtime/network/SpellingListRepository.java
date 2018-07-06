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
import com.gregbclement.spellingtime.model.SpellingList;
import com.gregbclement.spellingtime.model.Student;
import com.gregbclement.spellingtime.network.NetworkCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author g.clement
 * A network repository class that provides CRUD capabilities for SpellingLists by making calls  to
 * the SpellingTime REST service
 */
public class SpellingListRepository {
    private static final String URL  ="https://gregbclement.com/api/SpellingList";
    Context context;
    public SpellingListRepository(Context context) {
        this.context = context;
    }

    public  void getStudentSpellingLists(Student student, final NetworkCallback callback ) {
        RequestQueue queue = Volley.newRequestQueue(context);


        String url = URL + "?studentId=" + student.getId() + "&inactive=false";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                SpellingList[] studentArray = gson.fromJson(response, SpellingList[].class);

                List<SpellingList> spellingLists = Arrays.asList(studentArray);

                Collections.sort(spellingLists);

                callback.onComplete(spellingLists);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public  void saveSpellingList(SpellingList spellingList, final NetworkCallback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(spellingList);
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

                            callback.onComplete(null);

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

    public void deleteSpellingList(SpellingList spellingList, final NetworkCallback callback) {
        String url = URL + "?id=" + spellingList.getId();
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
