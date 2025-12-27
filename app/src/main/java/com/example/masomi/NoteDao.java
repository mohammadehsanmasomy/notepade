package com.example.masomi;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
public interface NoteDao {


    @Insert
    void insertNote(Note note);


    @Update
    void updateNote(Note note);


    @Delete
    void delete(Note note);


    @Query("SELECT * FROM Note ORDER BY id DESC")
    List<Note> getAllNotes();


    @Query("SELECT * FROM Note WHERE id = :id LIMIT 1")
    Note getNoteById(int id);
}