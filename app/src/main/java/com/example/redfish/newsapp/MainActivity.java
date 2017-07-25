package com.example.redfish.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redfish.newsapp.Models.Contract;
import com.example.redfish.newsapp.Models.DBHelper;
import com.example.redfish.newsapp.Models.DatabaseUtils;
import com.example.redfish.newsapp.Models.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
//TODO Modified Item.xml to be more pleasing
//TODO Added JobService NewsJob to AndroidManifest.xml
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>,newsAdapter.ItemClickListener{
    static final String TAG = "mainactivity";
    private ProgressBar progress;
    private RecyclerView rv;
    private newsAdapter adapter;

    //TODO Added a new Cursor
    private Cursor cursor;
    private SQLiteDatabase db;

    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress =(ProgressBar)findViewById(R.id.progressBar);
        rv=(RecyclerView)findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        // TODO Created a new Shared Preferences to check whether or not this is the first time app is installed
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);
        if (isFirst) {
            // TODO Loads database into the recyclerview
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }
        ScheduleUtilities.scheduleRefresh(this);
    }
    //TODO implemented the functions from newsAdapter
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        adapter = new newsAdapter(cursor, this);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
// TODO Changed from Task Execute to Load
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            load();
        }
        return true;
    }

    /** TODO Implementing/Replacing an AsyncTaskLoader, Code from Preexecute into onStartLoading, from doinBackground to
     *  loadinbackground, and from OnPostExecute to onloadFinished
     *  Test:
     */
    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            @Override
            protected void onStartLoading(){
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progress.setVisibility(View.GONE);
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);

        adapter = new newsAdapter(cursor, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    // Old Asynctask code
    //
//        class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> implements newsAdapter.ItemClickListener {
//        String query;
//        ArrayList<NewsItem> data;
//
//        NetworkTask(String s) {
//            query = s;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progress.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<NewsItem> doInBackground(URL... params) {
//
//            ArrayList<NewsItem> result = null;
//            URL url = NetworkUtils.makeURL();
//            Log.d(TAG, "url: " + url.toString());
//            try {
//                String json = NetworkUtils.getResponseFromHttpUrl(url);
//                result=NetworkUtils.parseJSON(json);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<NewsItem> data) {
//            this.data=data;
//            super.onPostExecute(data);
//            progress.setVisibility(View.GONE);
//            if (data != null) {
//                newsAdapter adapter = new newsAdapter(data, this);
//                rv.setAdapter(adapter);
//
//            }
//        }
// End of Asynctask
        //

        @Override
        public void onItemClick(Cursor cursor,int clickedItemIndex) {
            cursor.moveToPosition(clickedItemIndex);
            String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URL));
            Log.d(TAG, String.format("Url %s", url));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    //TODO Old OpenWebpage code
        public void openWebPage(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
//TODO Added Load to restart the loader on button press
    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
    }
}

