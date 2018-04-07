package com.example.trabablho3.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Paulo on 17/03/2018.
 */

public class TodoContract implements BaseColumns {
    public static final String TABLE_NAME = "todo";
    public static final String COLUMN_NAME_NOME = "nome";
    public static final String COLUMN_NAME_DESCRICAO = "descricao";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NOME + " TEXT," +
                    COLUMN_NAME_DESCRICAO + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
