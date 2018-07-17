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
import com.gregbclement.spellingtime.model.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class StudentRepository {

    private static final String URL = RESTConfig.BASE_URL + "Student";
    Context context;

    public StudentRepository(Context context) {
        this.context = context;
    }

    public void saveStudent(Student student, final NetworkCallback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(student);
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
        }
    }

    public void deleteStudent(Student student, final NetworkCallback callback) {
        String url = URL + "?id=" + student.getId();
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                callback.onComplete(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getStudents(final NetworkCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Student[] studentArray = gson.fromJson(response, Student[].class);

                List<Student> studentList = Arrays.asList(studentArray);

                callback.onComplete(studentList);
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
