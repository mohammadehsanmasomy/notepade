package com.example.masomi;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


import java.util.List;


public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {


    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private ImageButton addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.addBtn);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadNotes();


        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
            startActivity(intent);
        });
    }


    private void loadNotes() {
        new Thread(() -> {
            List<Note> list = NoteDatabase.getInstance(MainActivity.this).noteDao().getAllNotes();
            runOnUiThread(() -> {
                adapter = new NotesAdapter(list, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }


    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
        intent.putExtra("id", note.id);
        startActivity(intent);
    }
}