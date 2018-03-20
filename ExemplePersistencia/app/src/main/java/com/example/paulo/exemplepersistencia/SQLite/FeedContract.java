package com.example.paulo.exemplepersistencia.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Paulo on 17/03/2018.
 */

//comentario teste commit

public class FeedContract implements BaseColumns {
    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
