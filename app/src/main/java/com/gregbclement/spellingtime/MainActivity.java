package com.gregbclement.spellingtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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

/**
 * @author g.clement
 *
 */
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

    /**
     * Creates the Menu UI
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    /**
     * Callback for when a menu item is clicked
     * @param item
     * @return
     */
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

    /**
     * Shows the new student view
     * @param view
     */
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

                SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.studentsLv);
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
                    listView.addHeaderView(header, null, false);
                    headerAdded = true;
                }

                Log.i("SpellingTime","We Got Students!!!");

                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "open" item
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());

                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));

                        deleteItem.setWidth(300);
                        // set item width
                        // add to menu
                        deleteItem.setIcon(R.drawable.trash_icon);
                        menu.addMenuItem(deleteItem);
                    }
                };
                listView.setMenuCreator(creator);

                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        final Student student = students.get(position);
                        switch (index) {
                            case 0:
                                new AlertDialog.Builder(thisContext)
                                        .setTitle("Confirm Delete")
                                        .setMessage("Are you sure you want to delete " + student.getName())
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                StudentRepository sr = new StudentRepository(thisContext);
                                                sr.deleteStudent(student, new NetworkCallback() {
                                                    @Override
                                                    public void onComplete(Object results) {
                                                        showStudentList();
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .show();

                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
            }
        });
    }
}
