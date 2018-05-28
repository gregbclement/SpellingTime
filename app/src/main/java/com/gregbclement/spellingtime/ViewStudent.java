package com.gregbclement.spellingtime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStudent extends AppCompatActivity {

    Student student;
    List<SpellingList> spellingLists;
    public static final String SPELLING_LIST_REFERENCE = "com.gregbclement.spellingtime.SPELLING_LIST_REFERENCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        this.student = (Student) getIntent().getSerializableExtra(MainActivity.STUDENT_REFERENCE);


        final View header = getLayoutInflater().inflate(R.layout.view_student_header, null);

        TextView studentName = (TextView) header.findViewById(R.id.studentName);
        studentName.setText(student.getName());

        CircleImageView circleImageView = (CircleImageView) header.findViewById(R.id.studentPicture);

        circleImageView.setImageBitmap(student.getPictureBitmap());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final Context thisContext = this;

        SpellingListRepository repository = new SpellingListRepository(this);
        repository.getStudentSpellingLists(student, new NetworkCallback<List<SpellingList>>() {
            @Override
            public void onComplete(List<SpellingList> results) {
                spellingLists = results;

                SpellingListAdapter adapter = new SpellingListAdapter(thisContext, spellingLists);

                ListView listView = (ListView) findViewById(R.id.spellingListsLV);
                listView.addHeaderView(header);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SpellingList spellingList = spellingLists.get(position);

                        Intent intent = new Intent(thisContext, ViewSpellingList.class);
                        intent.putExtra(SPELLING_LIST_REFERENCE, spellingList);
                        startActivity(intent);
                    }
                });
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
            // TODO: add a new list
        }
        return true;
    }

}
