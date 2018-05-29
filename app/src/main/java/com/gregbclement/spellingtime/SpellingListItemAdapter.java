package com.gregbclement.spellingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SpellingListItemAdapter  extends ArrayAdapter<SpellingWord> {

    public SpellingListItemAdapter(@NonNull Context context, List<SpellingWord> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpellingWord spellingWord = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spelling_list_item_item, parent,
                    false);
        }

        convertView.setTag(position);



        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(spellingWord.getWord());

        ImageView image = (ImageView)convertView.findViewById(R.id.spellingListIcon);
        image.setImageResource(R.drawable.word);

        return convertView;
    }
}