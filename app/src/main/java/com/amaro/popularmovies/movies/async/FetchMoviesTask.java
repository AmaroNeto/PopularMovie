package com.amaro.popularmovies.movies.async;

import android.os.AsyncTask;

import com.amaro.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class FetchMoviesTask extends AsyncTask<URL, Void, String> {
    private AsyncTaskDelegate delegate = null;

    public FetchMoviesTask(AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL moviesUrl = urls[0];
        String moviesResults = null;
        try {
            moviesResults = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moviesResults;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(delegate != null)
            delegate.processFinish(result);
    }
}
