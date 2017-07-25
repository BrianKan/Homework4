package com.example.redfish.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.redfish.newsapp.Models.DBHelper;
import com.example.redfish.newsapp.Models.DatabaseUtils;
import com.example.redfish.newsapp.Models.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Redfish on 7/24/2017.
 */

// TODO Created a new RefreshTasks class to handle refreshing the data
 public class RefreshTasks {

        public static final String ACTION_REFRESH = "refresh";


        public static void refreshArticles(Context context) {
            ArrayList<NewsItem> result = null;
            URL url = NetworkUtils.makeURL();
            SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

            try {
                DatabaseUtils.deleteAll(db);
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
                DatabaseUtils.bulkInsert(db, result);

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            db.close();
        }
}
