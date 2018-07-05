package com.gregbclement.spellingtime.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gregbclement.spellingtime.R;
import com.gregbclement.spellingtime.model.Score;

import java.text.DateFormat;
import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    Context thisContext;

    public ScoreAdapter(@NonNull Context context, List<Score> objects) {
        super(context, 0, objects);
        thisContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent,
                    false);
        }

        convertView.setTag(position);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(thisContext);

        String dateString = dateFormat.format(score.getDate());

        TextView scoreText = (TextView) convertView.findViewById(R.id.scoreText);
        scoreText.setText(dateString);
        // scoreText.setText(score.getScore().toString());
        RatingBar scoreHistoryRatingBar = convertView.findViewById(R.id.scoreHistoryRatingBar);

        scoreHistoryRatingBar.setRating(score.getScore());


        return convertView;
    }
}