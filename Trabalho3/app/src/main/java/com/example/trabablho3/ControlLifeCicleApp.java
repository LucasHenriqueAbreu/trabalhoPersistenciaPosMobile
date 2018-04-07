package com.example.trabablho3;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.trabablho3.Room.AppDatabase;
import com.example.trabablho3.SQLite.TodoReaderDbHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ControlLifeCicleApp extends Application {

    public static AppDatabase dbRoom;
    public static DatabaseReference myRef;
    public static TodoReaderDbHelper dbSQLite;

    @Override
    public void onCreate() {
        super.onCreate();

        //SQLite database
        dbSQLite = new TodoReaderDbHelper(getApplicationContext());

        // Room Database
        dbRoom = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "database-todos"
        ).fallbackToDestructiveMigration().build();

        // Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
}
