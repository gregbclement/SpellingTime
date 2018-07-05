package com.gregbclement.spellingtime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewStudent extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Student newStudent = new Student();
    byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CircleImageView mImageView = (CircleImageView)findViewById(R.id.profileCircleImage);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.student);
        mImageView.setImageBitmap(bm);

        Student student = (Student) getIntent().getSerializableExtra(MainActivity.STUDENT_REFERENCE);
        if(student != null) {
            newStudent = student;
            EditText studentNameText = (EditText)findViewById(R.id.studentNameText);

            studentNameText.setText(student.getName());

            if(student.getPictureBitmap() != null) {
                mImageView.setImageBitmap(student.getPictureBitmap());
            }

            TextView tv = (TextView)findViewById(R.id.pageHeaderTextView);
            tv.setText("Edit Student");
        }
    }

    public  void takePicture(View view)  {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public  void saveStudent(View view) {
        StudentRepository studentRepository  = new StudentRepository(this);

        EditText studentNameText = (EditText)findViewById(R.id.studentNameText);


        newStudent.setName(studentNameText.getText().toString());


        if(imageBytes != null) {
            newStudent.setPicture(Base64.encodeToString(imageBytes,Base64.DEFAULT));
        }
        studentRepository.saveStudent(newStudent, new NetworkCallback() {
            @Override
            public void onComplete(Object results) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            imageBytes = baos.toByteArray();

            CircleImageView mImageView = (CircleImageView)findViewById(R.id.profileCircleImage);
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
