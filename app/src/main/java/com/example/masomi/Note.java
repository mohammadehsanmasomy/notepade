package com.example.masomi;



import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;


    public String title;
    public String text;

    public Note(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }


    @Ignore
    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }
}