package com.gregbclement.spellingtime.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregbclement.spellingtime.R;
import com.gregbclement.spellingtime.model.SpellingList;

import java.util.List;

public class SpellingListAdapter  extends ArrayAdapter<SpellingList> {

    public SpellingListAdapter(@NonNull Context context, List<SpellingList> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpellingList spellingList = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spelling_list_item, parent,
                    false);
        }

        convertView.setTag(position);



        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(spellingList.getName());

        ImageView image = (ImageView)convertView.findViewById(R.id.spellingListIcon);
        image.setImageResource(R.drawable.list_icon_2);

        return convertView;
    }
}