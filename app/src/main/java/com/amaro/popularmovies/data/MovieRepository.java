package com.amaro.popularmovies.data;

import android.content.Context;
import android.os.AsyncTask;

import com.amaro.popularmovies.data.movie.MovieDao;
import com.amaro.popularmovies.data.movie.MovieModel;
import com.amaro.popularmovies.data.review.ReviewDao;
import com.amaro.popularmovies.data.review.ReviewModel;
import com.amaro.popularmovies.data.trailer.TrailerDao;
import com.amaro.popularmovies.data.trailer.TrailerModel;
import com.amaro.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepository {

    private static MovieRepository sInstance;

    private final Context mContext;
    private final ReviewDao mReviewDao;
    private final TrailerDao mTrailerDao;
    private final MovieDao mMovieDao;

    public static MovieRepository getInstance(Context context,
                                              MovieDao movieDao, ReviewDao reviewDao,
                                              TrailerDao trailerDao) {
        if (sInstance == null) {
            sInstance = new MovieRepository(context, movieDao, reviewDao, trailerDao);
        }
        return sInstance;
    }

    private MovieRepository(Context context, MovieDao movieDao,
                            ReviewDao reviewDao, TrailerDao trailerDao) {
        mContext = context;
        mMovieDao = movieDao;
        mReviewDao = reviewDao;
        mTrailerDao = trailerDao;
    }

    public LiveData<List<MovieModel>> loadAllMovies() {
        return mMovieDao.loadAllMovies();
    }

    public void insertMovie(MovieModel movie) {
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... contexts) {
                mMovieDao.insertMovie(movie);
                return null;
            }
        }.execute(mContext);
    }

    public void deleteMovie(MovieModel movie){
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... contexts) {
                mMovieDao.deleteMovie(movie);
                return null;
            }
        }.execute(mContext);
    }

}
