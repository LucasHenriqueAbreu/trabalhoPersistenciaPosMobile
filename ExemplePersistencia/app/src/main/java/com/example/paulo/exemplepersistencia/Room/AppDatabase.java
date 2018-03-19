package com.example.paulo.exemplepersistencia.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Paulo on 17/03/2018.
 */

@Database(entities = {Feed.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDao feedDao();
}
