package com.example.paulo.exemplepersistencia.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FeedContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void create(Feed feed) {
        //Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedContract.COLUMN_NAME_TITLE, feed.title);
        values.put(FeedContract.COLUMN_NAME_SUBTITLE, feed.subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedContract.TABLE_NAME, null, values);

        db.close();
    }

    public void read() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                FeedContract._ID,
                FeedContract.COLUMN_NAME_TITLE,
                FeedContract.COLUMN_NAME_SUBTITLE
        };

        String sortOrder = FeedContract.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                FeedContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Feed> feeds = new ArrayList<>();

        while (cursor.moveToNext()) {
            Feed feed = new Feed();
            feed._id = cursor.getInt(cursor.getColumnIndex(FeedContract._ID));
            feed.title = cursor.getString(cursor.getColumnIndex(FeedContract.COLUMN_NAME_TITLE));
            feed.subtitle = cursor.getString(cursor.getColumnIndex(FeedContract.COLUMN_NAME_SUBTITLE));
            feeds.add(feed);
        }
    }

    public int update(Feed feed) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedContract.COLUMN_NAME_TITLE, feed.title);
        values.put(FeedContract.COLUMN_NAME_SUBTITLE, feed.subtitle);

        String selection = FeedContract._ID + " = ?";
        String[] selectionArgs = {"" + feed._id};

        int count = db.update(
                FeedContract.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return count;
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = FeedContract._ID + " = ?";
        String[] selectionArgs = {"" + id};

        int deletedRows = db.delete(
                FeedContract.TABLE_NAME,
                selection,
                selectionArgs);

        db.close();
    }
}