package com.amaro.popularmovies.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.amaro.popularmovies.data.movie.MovieModel;
import com.amaro.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieLiveData extends LiveData<List<MovieModel>> {

    private final Context mContext;
    private final URL mUrl;
    private final String TAG = "MovieLiveData";

    public MovieLiveData(Context context, URL url) {
        mContext = context;
        mUrl = url;
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String moviesResults = null;
                try {
                    moviesResults = NetworkUtils.getResponseFromHttpUrl(mUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return moviesResults;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                parseResult(s);
            }
        }.execute();
    }

    private void parseResult(String s) {
            if(s != null && !TextUtils.isEmpty((String) s)) {
                try {
                    JSONObject mbMovieList = new JSONObject((String) s);
                    JSONArray result = mbMovieList.getJSONArray("results");
                    Log.d(TAG,"JSON "+result.toString());

                    List<MovieModel> moviesArray = new ArrayList<MovieModel>();

                    for(int i = 0; i < result.length(); i++) {
                        JSONObject movieJson = result.getJSONObject(i);
                        MovieModel movie = new MovieModel();
                        movie.setId(movieJson.getInt("id"));
                        movie.setTitle(movieJson.getString("original_title"));
                        movie.setOverview(movieJson.getString("overview"));
                        movie.setPosterUrl(movieJson.getString("poster_path"));
                        movie.setReleaseDate(movieJson.getString("release_date"));
                        movie.setVoteAverage(movieJson.getDouble("vote_average"));

                        moviesArray.add(movie);
                    }

                    setValue(moviesArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

}
