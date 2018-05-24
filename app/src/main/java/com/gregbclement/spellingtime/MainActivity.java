package com.gregbclement.spellingtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This was updated. And updated again.
        getStudentFeed();
    }


    private void getStudentFeed() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://gregbclement.com/api/Student";
        final TextView mTextView = (TextView) findViewById(R.id.text);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                // mTextView.setText("Response is: "+ response.substring(0,500));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        StudentRepository sr = new StudentRepository(this);

        sr.getStudents(new NetworkCallback<List<Student>>() {
            @Override
            public void onGetStudents(List<Student> students) {
                mTextView.setText(students.get(1).getName());
            }
        });
    }

}
