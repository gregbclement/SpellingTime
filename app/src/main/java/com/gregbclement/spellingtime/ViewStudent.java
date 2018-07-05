package com.gregbclement.spellingtime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStudent extends AppCompatActivity {

    Student student;
    List<SpellingList> spellingLists;
    SpellingListAdapter adapter;
    public static final String SPELLING_LIST_REFERENCE = "com.gregbclement.spellingtime.SPELLING_LIST_REFERENCE";
    final int ADD_LIST_CODE = 100;
    final int VIEW_LIST_CODE = 101;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);




        header = getLayoutInflater().inflate(R.layout.view_student_header, null);
        setupStudentInformation();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context thisContext = this;

        spellingLists = new ArrayList<>();

        adapter = new SpellingListAdapter(thisContext, spellingLists);

        final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.spellingListsLV);

        listView.addHeaderView(header, null ,false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpellingList spellingList = spellingLists.get(position - 1);

                Intent intent = new Intent(thisContext, ViewSpellingList.class);
                intent.putExtra(SPELLING_LIST_REFERENCE, spellingList);
                startActivityForResult(intent, VIEW_LIST_CODE);
            }
        });

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
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new AlertDialog.Builder(thisContext)
                                .setTitle("Confirm Delete")
                                .setMessage("Are you sure you want to delete this spelling list?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SpellingList spellingList = spellingLists.get(position);
                                        SpellingListRepository spellingListRepository = new SpellingListRepository(thisContext);

                                        spellingListRepository.deleteSpellingList(spellingList, new NetworkCallback() {
                                            @Override
                                            public void onComplete(Object results) {
                                                Intent resultIntent = new Intent();

                                                refreshStudentSpellingLists();
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
        refreshStudentSpellingLists();

    }

    @Override
    public void onResume() {
        super.onResume();
        setupStudentInformation();
    }

    private void setupStudentInformation() {


        this.student = (Student) getIntent().getSerializableExtra(MainActivity.STUDENT_REFERENCE);
        TextView studentName = (TextView) header.findViewById(R.id.studentName);
        studentName.setText(student.getName());

        CircleImageView circleImageView = (CircleImageView) header.findViewById(R.id.studentPicture);

        circleImageView.setImageBitmap(student.getPictureBitmap());
    }

    private void refreshStudentSpellingLists() {
        SpellingListRepository repository = new SpellingListRepository(this);
        repository.getStudentSpellingLists(student, new NetworkCallback<List<SpellingList>>() {
            @Override
            public void onComplete(List<SpellingList> results) {
                spellingLists.clear();
                spellingLists.addAll(results);
                // spellingLists = results;

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.view_student_menu, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (VIEW_LIST_CODE):
            case (ADD_LIST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra(MainActivity.REFRESH_NEEDED);

                    if ("true".equals(returnValue)) {
                        refreshStudentSpellingLists();
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Context thisContext = this;
        if (item.getItemId() == R.id.delete_student) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this student")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StudentRepository sr = new StudentRepository(thisContext);
                            sr.deleteStudent(student, new NetworkCallback() {
                                @Override
                                public void onComplete(Object results) {
                                    finish();
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();


        }

        if (item.getItemId() == R.id.addNew) {
            Intent intent = new Intent(this, NewList.class);
            intent.putExtra(MainActivity.STUDENT_REFERENCE, student);
            startActivityForResult(intent, ADD_LIST_CODE);

        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if(item.getItemId() == R.id.edit_student) {
            Intent intent = new Intent(thisContext, NewStudent.class);
            intent.putExtra(MainActivity.STUDENT_REFERENCE, student);
            startActivity(intent);
        }
        return true;
    }
}
