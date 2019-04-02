package com.amaro.popularmovies.model;

import android.app.Application;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class DetailMovieViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final URL mUrlReview;
    private final URL mUrlTrailer;

    public DetailMovieViewModelFactory(Application app, URL review, URL trailer) {
        mUrlTrailer = trailer;
        mUrlReview = review;
        application = app;
    }

    @NonNull
    @Override
    public DetailMovieViewModel create(@NonNull Class modelClass) {
        return new DetailMovieViewModel(application, mUrlReview, mUrlTrailer);
    }

}
