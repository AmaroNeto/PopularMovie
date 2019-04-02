package com.amaro.popularmovies.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.amaro.popularmovies.data.review.ReviewModel;
import com.amaro.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class ReviewLiveData extends LiveData<List<ReviewModel>> {

    private final Context mContext;
    private final URL mUrl;
    private final String TAG = "ReviewLiveData";

    public ReviewLiveData(Context context, URL url) {
        mContext = context;
        mUrl = url;
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String reviewResults = null;
                try {
                    reviewResults = NetworkUtils.getResponseFromHttpUrl(mUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return reviewResults;
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

                List<ReviewModel> reviewsArray = new ArrayList<ReviewModel>();

                for(int i = 0; i < result.length(); i++) {
                    JSONObject reviewJson = result.getJSONObject(i);
                    ReviewModel review = new ReviewModel(reviewJson.getString("id"));
                    review.setAuthor(reviewJson.getString("author"));
                    review.setContent(reviewJson.getString("content"));

                    reviewsArray.add(review);
                }

                setValue(reviewsArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
