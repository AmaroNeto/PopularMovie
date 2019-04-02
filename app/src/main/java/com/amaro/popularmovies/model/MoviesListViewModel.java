package com.amaro.popularmovies.model;


import android.app.Application;

import com.amaro.popularmovies.data.AppDatabase;
import com.amaro.popularmovies.data.MovieRepository;
import com.amaro.popularmovies.data.movie.MovieModel;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MoviesListViewModel extends AndroidViewModel {

    private static final String TAG = "MoviesListViewModel";

    private LiveData<List<MovieModel>> movies;
    private MovieRepository repository;

    public MoviesListViewModel(Application application, URL url) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        repository = MovieRepository.getInstance(getApplication(), database.movieDao(),
                database.reviewDao(), database.trailerDao());
        movies = new MovieLiveData(application,  url);
    }

    public void reloadData(Application application, URL url) {
        movies = new MovieLiveData(application,  url);
    }

    public void loadMoviesFromDb() {
        movies = repository.loadAllMovies();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movies;
    }

}
