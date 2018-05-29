package com.gregbclement.spellingtime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewSpellingList extends AppCompatActivity {
    TextToSpeech tts;
    SpellingList spellingList;
    List<SpellingWord> spellingWords = new ArrayList<>();
    SpellingListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spelling_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.spellingList = (SpellingList) getIntent().getSerializableExtra(ViewStudent.SPELLING_LIST_REFERENCE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        final View header = getLayoutInflater().inflate(R.layout.spelling_list_header, null);

        TextView listName = (TextView) header.findViewById(R.id.listNameTextView);
        listName.setText(spellingList.getName());



        final Context thisContext = this;
         adapter = new SpellingListItemAdapter(thisContext, spellingWords);

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.spellingListsLV);
        // listView.addHeaderView(header);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpellingWord word = spellingWords.get(position);

                // TODO: do something with this word
            }
        });

        listView.addHeaderView(header);

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
                switch (index) {
                    case 0:
                        // open
                        final SpellingWord wordToDelete = spellingWords.get(position);

                        SpellingWordRepository spellingWordRepository = new SpellingWordRepository(thisContext);

                        spellingWordRepository.deleteSpellingWord(wordToDelete, new NetworkCallback() {
                            @Override
                            public void onComplete(Object results) {
                                spellingWords.remove(wordToDelete);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        refreshSpellingList();

    }

    private void refreshSpellingList() {
        SpellingWordRepository repository = new SpellingWordRepository(this);
        repository.getListSpellingWords(spellingList, new NetworkCallback<List<SpellingWord>>() {
            @Override
            public void onComplete(List<SpellingWord> results) {
                spellingWords.clear();
                spellingWords.addAll(results);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public  void startQuiz(View view) {


        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra(ViewStudent.SPELLING_LIST_REFERENCE, spellingList);
        startActivity(intent);
    }

    public void Speak(View view) {

        tts.speak("This is a test", TextToSpeech.QUEUE_FLUSH, null,null);
    }

    public  void addWord(View view) {
        final EditText newWordText = (EditText)findViewById(R.id.newWordText);

        String newWord = newWordText.getText().toString();

        if(newWord == null || newWord == "") {
            return;
        }

        final SpellingWord newSpellingWord = new SpellingWord();
        newSpellingWord.setListId(spellingList.getId());
        newSpellingWord.setWord(newWord);
        newSpellingWord.setCreateDate(new Date());
        newSpellingWord.setLastModifiedDate(new Date());

        SpellingWordRepository spellingWordRepository = new SpellingWordRepository(this);
        spellingWordRepository.saveSpellingWord(newSpellingWord, new NetworkCallback<SpellingWord>() {
            @Override
            public void onComplete(SpellingWord results) {
                spellingWords.add(results);
                adapter.notifyDataSetChanged();
                newWordText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.spelling_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Context thisContext = this;
        if (item.getItemId() == R.id.delete_list) {
            // TODO: implement this

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this spelling list?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpellingListRepository spellingListRepository = new SpellingListRepository(thisContext);

                            spellingListRepository.deleteSpellingList(spellingList, new NetworkCallback() {
                                @Override
                                public void onComplete(Object results) {
                                    Intent resultIntent = new Intent();

                                    resultIntent.putExtra(MainActivity.REFRESH_NEEDED, "true");
                                    setResult(Activity.RESULT_OK, resultIntent);
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
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
