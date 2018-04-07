package com.example.trabablho3.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    public void create(Todo todo);
    @Query("SELECT * FROM TODO")
    public List<Todo> read();
    @Update
    public void update(Todo todo);
    @Delete
    public void delete(Todo todo);
}
