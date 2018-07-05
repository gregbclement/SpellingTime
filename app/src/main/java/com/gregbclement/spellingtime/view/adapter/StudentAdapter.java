package com.gregbclement.spellingtime.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.gregbclement.spellingtime.R;
import com.gregbclement.spellingtime.model.Student;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends ArrayAdapter<Student> {

    public StudentAdapter(@NonNull Context context,  List<Student> objects) {
        super(context, 0, objects);
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_item, parent,
                    false);
        }

        convertView.setTag(position);



        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(student.getName());



        CircleImageView circleImageView = (CircleImageView)convertView.findViewById(R.id.studentsLv);

        circleImageView.setImageBitmap(student.getPictureBitmap());

        return convertView;
    }
}
