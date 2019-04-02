package com.amaro.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.amaro.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String API_KEY = BuildConfig.API_KEY;
    final static String MOVIEDB_BASE_URL =
            "http://api.themoviedb.org/3/movie/";
    final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    final static String PARAM_QUERY = "api_key";

    final static String YOUTUBE_URL = "http://www.youtube.com/watch";

    final static String REVIEWS = "reviews";
    final static String TRAILERS = "trailers";

    public static URL getPopularMoviesURL(String sortBy){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getMoviePosterURL(String poster){
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(poster)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getReviewURL(String movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getTrailersURL(String movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(TRAILERS)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri getYoutubeUri(String source){
        Uri builtUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter("v", source)
                .build();

        return builtUri;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
