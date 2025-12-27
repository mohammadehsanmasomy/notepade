package com.example.masomi;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class NoteEditorActivity extends AppCompatActivity {


    private EditText titleEt, textEt;
    private Button saveBtn;
    private int id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        titleEt = findViewById(R.id.titleEt);
        textEt = findViewById(R.id.textEt);
        saveBtn = findViewById(R.id.saveBtn);


        id = getIntent().getIntExtra("id", -1);


        if (id != -1) loadNote();


        saveBtn.setOnClickListener(v -> saveNote());
    }


    private void loadNote() {
        new Thread(() -> {
            Note note = NoteDatabase.getInstance(this).noteDao().getNoteById(id);
            runOnUiThread(() -> {
                titleEt.setText(note.title);
                textEt.setText(note.text);
            });
        }).start();
    }


    private void saveNote() {
        String title = titleEt.getText().toString();
        String text = textEt.getText().toString();


        new Thread(() -> {
            NoteDatabase db = NoteDatabase.getInstance(this);
            if (id == -1) {
                db.noteDao().insertNote(new Note(title, text));
            } else {
                db.noteDao().updateNote(new Note(id, title, text));
            }
            finish();
        }).start();
    }
}