package com.example.trabablho3.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoReader.db";

    public TodoReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TodoContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void create(Todo feed) {
        //Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TodoContract.COLUMN_NAME_NOME, feed.nome);
        values.put(TodoContract.COLUMN_NAME_DESCRICAO, feed.descricao);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TodoContract.TABLE_NAME, null, values);

        db.close();
    }

    public List<Todo> read() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                TodoContract._ID,
                TodoContract.COLUMN_NAME_NOME,
                TodoContract.COLUMN_NAME_DESCRICAO
        };

        String sortOrder = TodoContract.COLUMN_NAME_NOME + " DESC";

        Cursor cursor = db.query(
                TodoContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Todo> feeds = new ArrayList<>();

        while (cursor.moveToNext()) {
            Todo feed = new Todo();
            feed._id = cursor.getInt(cursor.getColumnIndex(TodoContract._ID));
            feed.nome = cursor.getString(cursor.getColumnIndex(TodoContract.COLUMN_NAME_NOME));
            feed.descricao = cursor.getString(cursor.getColumnIndex(TodoContract.COLUMN_NAME_DESCRICAO));
            feeds.add(feed);
        }

        return feeds;
    }

    public int update(Todo feed) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.COLUMN_NAME_NOME, feed.nome);
        values.put(TodoContract.COLUMN_NAME_DESCRICAO, feed.descricao);

        String selection = TodoContract._ID + " = ?";
        String[] selectionArgs = {"" + feed._id};

        int count = db.update(
                TodoContract.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return count;
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = TodoContract._ID + " = ?";
        String[] selectionArgs = {"" + id};

        int deletedRows = db.delete(
                TodoContract.TABLE_NAME,
                selection,
                selectionArgs);

        db.close();
    }
}