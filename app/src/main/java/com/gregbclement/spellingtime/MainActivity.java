package com.gregbclement.spellingtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_REFERENCE = "com.gregbclement.spellingtime.STUDENT_REFERENCE";
    public  static  final   String REFRESH_NEEDED = "com.gregbclement.spellingtime.REFRESH_NEEDED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showStudentList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addNew) {
            Intent intent = new Intent(this, NewStudent.class);

            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        showStudentList();
    }

    public void showAddNewStudent(View view) {
        Intent intent = new Intent(this, NewStudent.class);

        startActivity(intent);

        ListView listView = (ListView) findViewById(R.id.studentsLv);

    }

    private boolean headerAdded;
    private void showStudentList() {

        StudentRepository sr = new StudentRepository(this);
        final Context thisContext = this;


        sr.getStudents(new NetworkCallback<List<Student>>() {
            @Override
            public void onComplete(final List<Student> students) {
                StudentAdapter adapter = new StudentAdapter(thisContext, students);

                ListView listView = (ListView) findViewById(R.id.studentsLv);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Student student = students.get(position - 1);
                        Intent intent = new Intent(thisContext, ViewStudent.class);
                        intent.putExtra(STUDENT_REFERENCE, student);
                        startActivity(intent);
                    }
                });

                if(!headerAdded) {
                    final View header = getLayoutInflater().inflate(R.layout.main_activity_header, null);
                    listView.addHeaderView(header);
                    headerAdded = true;
                }
            }
        });
    }

}
