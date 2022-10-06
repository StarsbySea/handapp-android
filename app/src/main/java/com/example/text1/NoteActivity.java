package com.example.text1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class NoteActivity extends Activity {

    private EditText editText;
    private View addNoteButton;

    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;
    OnItemClickListener noteClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Note note = notesAdapter.getItem(position);
            notesBox.remove(note);
            Log.d(App.TAG, "Deleted note, ID: " + note.getId());
            updateNotes();
        }
    };

/*    private String getTxtFromAssets(String fileName) {
        String result = "";
        try {
            InputStream is = getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (MissingSplitsManagerFactory.create(this).disableAppIfMissingRequiredSplits() == false) {
//            ObjectBox.init(this); // Skip app initialization.
//        }

        setContentView(R.layout.main);

        setUpViews();

        notesBox = ObjectBox.get().boxFor(Note.class);

        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.name).build();
/*
        SharedPreferences setting = getSharedPreferences("com.example.text1", 0);
        Boolean user_first = setting.getBoolean("DB_FIRST", true);
        if (user_first) {// 第一次则导入数据
            setting.edit().putBoolean("DB_FIRST", false).commit();
            Gson gson = new Gson();
            String jsonString = getTxtFromAssets("name_explain_img.json");

            ArrayList<Note> list = new ArrayList<Note>();
            Type listType = new TypeToken<List<Note>>() {
            }.getType();
            ArrayList<Note> user = gson.fromJson(jsonString, listType);

            notesBox.put(user);
        }*/
        updateNotes();
    }

    private void updateNotes() {
        List<Note> notes = notesQuery.find();
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        ListView listView = findViewById(R.id.listViewNotes);
        listView.setOnItemClickListener(noteClickListener);

        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        addNoteButton.setEnabled(false);

        editText = findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNote();
                return true;
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onAddButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setName(noteText);
        note.setExplain(comment);
        note.setImage("This is image.");
        notesBox.put(note);
        Log.d(App.TAG, "Inserted new note, ID: " + note.getId());

        updateNotes();
    }

}