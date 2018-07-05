package com.gregbclement.spellingtime.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gregbclement.spellingtime.R;
import com.gregbclement.spellingtime.model.SpellingList;
import com.gregbclement.spellingtime.model.Student;
import com.gregbclement.spellingtime.network.NetworkCallback;
import com.gregbclement.spellingtime.network.SpellingListRepository;
import com.gregbclement.spellingtime.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewList extends AppCompatActivity {

    Student student;

    EditText listName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        this.student = (Student) getIntent().getSerializableExtra(MainActivity.STUDENT_REFERENCE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.student = (Student) getIntent().getSerializableExtra(MainActivity.STUDENT_REFERENCE);
        listName = (EditText)findViewById(R.id.newListNameText);

        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        while(calendar.get(Calendar.DAY_OF_WEEK) != 2) {
            calendar.add(Calendar.DATE, -1);

        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        listName.setText(sdf.format(calendar.getTime()) + " - Spelling List");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public  void saveList(View view) {
        SpellingList spellingList = new SpellingList();

        spellingList.setName(listName.getText().toString());
        spellingList.setStudentId(student.getId());
        spellingList.setCreatedDate(new Date());


        SpellingListRepository spellingListRepository = new SpellingListRepository(this);
        spellingListRepository.saveSpellingList(spellingList, new NetworkCallback() {
            @Override
            public void onComplete(Object results) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(MainActivity.REFRESH_NEEDED, "true");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
