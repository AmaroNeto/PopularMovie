package com.amaro.popularmovies.model;

import android.app.Application;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class MovieListViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final URL mUrl;

    public MovieListViewModelFactory(Application app, URL url) {
        mUrl = url;
        application = app;
    }

    @NonNull
    @Override
    public MoviesListViewModel create(@NonNull Class modelClass) {
        return new MoviesListViewModel(application, mUrl);
    }
}
