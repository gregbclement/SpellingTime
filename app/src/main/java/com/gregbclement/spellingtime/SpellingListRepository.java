package com.gregbclement.spellingtime;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
