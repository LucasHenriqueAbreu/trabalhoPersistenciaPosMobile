package com.example.paulo.exemplepersistencia;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.paulo.exemplepersistencia.Room.AppDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Paulo on 17/03/2018.
 */

public class ControlLifeCycleApp extends Application {
    public static AppDatabase db;
    public static DatabaseReference myRef;
    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "database-feeds"
            ).build();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
}
