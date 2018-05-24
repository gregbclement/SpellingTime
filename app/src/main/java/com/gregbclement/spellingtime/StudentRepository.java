package com.gregbclement.spellingtime;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class StudentRepository {

    Context context;
    public  StudentRepository(Context context) {
        this.context = context;
    }


    public  void getStudents(final NetworkCallback callback ) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://gregbclement.com/api/Student";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Gson gson = new Gson();
                Student[] studentArray = gson.fromJson(response, Student[].class);

                List<Student> studentList = Arrays.asList(studentArray);

                callback.onGetStudents(studentList);
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
