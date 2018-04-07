package com.example.trabablho3.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Paulo on 17/03/2018.
 */

@Database(entities = {Todo.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}
