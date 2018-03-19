package com.example.paulo.exemplepersistencia.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Paulo on 17/03/2018.
 */

@Dao
public interface FeedDao {
    @Insert
    public void create(Feed feed);
    @Query("SELECT * FROM FEED")
    public List<Feed> read();
    @Update
    public void update(Feed feed);
    @Delete
    public void delete(Feed feed);
}
