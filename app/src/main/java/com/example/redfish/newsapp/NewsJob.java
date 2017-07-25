package com.example.redfish.newsapp;

import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.JobParameters;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Redfish on 7/24/2017.
 */
//TODO Created a new JobService class
public class NewsJob extends JobService {
    AsyncTask backgroundTask;
// TODO Creates a task and asks for a refresh in background task
    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                //TODO Creates a toast to show refersh
                Toast.makeText(NewsJob.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                RefreshTasks.refreshArticles(NewsJob.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);

            }
        };
        backgroundTask.execute();
        return true;
    }

    //TODO stops the job if it exists
    @Override
    public boolean onStopJob(JobParameters job) {
            if (backgroundTask != null) backgroundTask.cancel(false);
        return true;
    }
}
