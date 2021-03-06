package com.example.redfish.newsapp.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Redfish on 7/24/2017.
 */

//TODO Created new class DBHelper to create the SQL Table /subclass SQLiteopenhelper

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsArticles1.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " + Contract.TABLE_ARTICLES.TABLE_NAME + " ("+
                Contract.TABLE_ARTICLES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_DATE + " DATE, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_THUMBURL + " TEXT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_URL + " TEXT" +
                "); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table " + Contract.TABLE_ARTICLES.TABLE_NAME + " if exists;");
    }
}
