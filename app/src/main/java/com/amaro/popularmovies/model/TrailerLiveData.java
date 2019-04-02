package com.amaro.popularmovies.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.amaro.popularmovies.data.trailer.TrailerModel;
import com.amaro.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class TrailerLiveData extends LiveData<List<TrailerModel>> {

    private final Context mContext;
    private final URL mUrl;
    private final String TAG = "TrailerLiveData";

    public TrailerLiveData(Context context, URL url) {
        mContext = context;
        mUrl = url;
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String trailersResults = null;
                try {
                    trailersResults = NetworkUtils.getResponseFromHttpUrl(mUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return trailersResults;
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
                JSONArray result = mbMovieList.getJSONArray("youtube");
                Log.d(TAG,"JSON "+result.toString());

                List<TrailerModel> trailerArray = new ArrayList<TrailerModel>();

                for(int i = 0; i < result.length(); i++) {
                    JSONObject trailerJson = result.getJSONObject(i);
                    TrailerModel trailer = new TrailerModel();
                   trailer.setType(trailerJson.getString("type"));
                   trailer.setTitle(trailerJson.getString("name"));
                   trailer.setKey(trailerJson.getString("source"));

                    trailerArray.add(trailer);
                }

                setValue(trailerArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
