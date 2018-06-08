package com.gregbclement.spellingtime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Quiz extends AppCompatActivity implements SwipeTarget {
    SpellingList spellingList;
    List<SpellingWord> spellingWords = new ArrayList<>();
    Integer wordIndex = 0;
    SpellingWordRepository spellingWordRepository;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spellingWordRepository = new SpellingWordRepository(this);
        this.spellingList = (SpellingList) getIntent().getSerializableExtra(ViewStudent.SPELLING_LIST_REFERENCE);

        spellingWordRepository.getListSpellingWords(spellingList, new NetworkCallback<List<SpellingWord>>() {
            @Override
            public void onComplete(List<SpellingWord> results) {
                spellingWords.clear();
                spellingWords.addAll(results);
                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

                pb.setMax(spellingWords.size());
                showWord();
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        RatingBar bar = (RatingBar) findViewById(R.id.ratingBar);
        bar.setStepSize(1);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating == 0) {
                    return;
                }

                Score score = new Score();
                score.setDate(Calendar.getInstance().getTime());
                score.setScore((int) rating);
                SpellingWord spellingWord = spellingWords.get(wordIndex);

                if (spellingWord.getScores() == null) {
                    spellingWord.setScores(new ArrayList<Score>());
                }
                spellingWord.getScores().add(score);
                spellingWord.setLastScore((int) rating);
                spellingWord.setCurrentScore((int) rating);

                spellingWordRepository.saveSpellingWord(spellingWord, new NetworkCallback() {
                    @Override
                    public void onComplete(Object results) {
                        ratingBar.setRating(0);
                        if (wordIndex == spellingWords.size() - 1) {
                            showFinalStep();
                        }
                        NextWord(null);
                    }
                });
            }
        });

        View layoutView = findViewById(R.id.quizLayout);

        layoutView.setOnTouchListener(new OnSwipeTouchListener(this, this));
    }

    private void showFinalStep() {
        View quizWordLayout = findViewById(R.id.quizWordLayout);

        quizWordLayout.setVisibility(View.GONE);

        View quizCompleteLayout = findViewById(R.id.quizCompleteLayout);
        quizCompleteLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context thisContext = this;
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void NextWord(View view) {
        if (wordIndex < spellingWords.size() - 1) {
            wordIndex++;
            showWord();
        }
    }

    public void speakWord(View view) {
        SpellingWord word = spellingWords.get(wordIndex);
        tts.speak(word.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void PrevWord(View view) {
        if (wordIndex > 0) {
            wordIndex--;
            showWord();
        }
    }

    public  void goBack(View view) {
        finish();
    }

    private void showWord() {
        if (spellingWords.size() == 0) {
            return;
        }

        final SpellingWord word = spellingWords.get(wordIndex);

        TextView wordText = (TextView) findViewById(R.id.spellingWordText);

        wordText.setText(word.getWord());

        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress(wordIndex);

        TextView label = (TextView) findViewById(R.id.progressLabel);

        label.setText((wordIndex + 1) + " of " + spellingWords.size());

        spellingWordRepository.getWordDefinition(word, new NetworkCallback() {
            @Override
            public void onComplete(Object results) {
                SpellingWordDefinition definition = (SpellingWordDefinition) results;

                word.setWordDefinition(definition);
            }
        });
    }

    @Override
    public void SwipeLeft() {
        NextWord(null);
    }

    @Override
    public void SwipeRight() {
        PrevWord(null);
    }

    @Override
    public void SwipeUp() {

    }

    @Override
    public void SwipeDown() {

    }
}
