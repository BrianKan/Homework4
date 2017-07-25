package com.example.redfish.newsapp.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

// TODO Importing all static constants from the Contract
import static com.example.redfish.newsapp.Models.Contract.TABLE_ARTICLES.*;

/**
 * Created by Redfish on 7/24/2017.
 */


// TODO Created new DatabaseUtils to access the database
public class DatabaseUtils {
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_PUBLISHED_DATE + " DESC"
        );
        return cursor;
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> articles) {

        db.beginTransaction();
        try {
            for (NewsItem a : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_TITLE, a.getTitle());
                cv.put(COLUMN_NAME_ABSTRACT, a.getDescription());
                cv.put(COLUMN_NAME_PUBLISHED_DATE, a.getPublishedAt());
                cv.put(COLUMN_NAME_THUMBURL, a.getUrlToImage());
                cv.put(COLUMN_NAME_URL, a.getUrl());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }
}
