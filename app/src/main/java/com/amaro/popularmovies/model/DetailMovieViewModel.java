package com.amaro.popularmovies.model;

import android.app.Application;

import com.amaro.popularmovies.data.AppDatabase;
import com.amaro.popularmovies.data.MovieRepository;
import com.amaro.popularmovies.data.review.ReviewModel;
import com.amaro.popularmovies.data.trailer.TrailerModel;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailMovieViewModel extends AndroidViewModel {

    private static final String TAG = "DetailMovieViewModel";

    private LiveData<List<TrailerModel>> trailers;
    private LiveData<List<ReviewModel>> reviews;
    private MovieRepository repository;

    public DetailMovieViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        repository = MovieRepository.getInstance(getApplication(), database.movieDao(),
                database.reviewDao(), database.trailerDao());
    }

    public DetailMovieViewModel(@NonNull Application application, URL reviewURL, URL trailerURL) {
        super(application);
        trailers = new TrailerLiveData(application, trailerURL);
        reviews = new ReviewLiveData(application, reviewURL);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        repository = MovieRepository.getInstance(getApplication(), database.movieDao(),
                database.reviewDao(), database.trailerDao());
    }


    public LiveData<List<ReviewModel>> getReviews() {
        return reviews;
    }

    public LiveData<List<TrailerModel>> getTrailers() {
        return trailers;
    }
}
