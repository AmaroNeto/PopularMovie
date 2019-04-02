package com.amaro.popularmovies.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.data.AppDatabase;
import com.amaro.popularmovies.data.MovieRepository;
import com.amaro.popularmovies.data.movie.MovieModel;
import com.amaro.popularmovies.data.review.ReviewModel;
import com.amaro.popularmovies.data.trailer.TrailerModel;
import com.amaro.popularmovies.model.DetailMovieViewModel;
import com.amaro.popularmovies.model.DetailMovieViewModelFactory;
import com.amaro.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{

    public final static String TAG = "MovieDetailActivity";
    public final static String MOVIE_DETAIL_DATA = "MovieDetailData";
    private MovieModel mMovie;

    private TextView mTitle;
    private TextView mOverview;
    private TextView mRanking;
    private TextView mDate;
    private ImageView mPoster;

    private ToggleButton mBtFavorite;
    private ProgressBar mProgressBarTrailer;
    private RecyclerView mTrailersRecyclerView;
    private ProgressBar mProgressBarReview;
    private RecyclerView mReviewRecyclerView;

    private TrailerAdapter adapter;
    private ReviewAdapter adapterReview;
    private DetailMovieViewModel mViewModel;

    private MovieRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        bindViewComponents();
        configRecycleViews();

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        repository = MovieRepository.getInstance(getApplication(), database.movieDao(),
                database.reviewDao(), database.trailerDao());

        mMovie = getIntent().getExtras().getParcelable(MOVIE_DETAIL_DATA);

        if(mMovie != null) {
            loadData();
            mBtFavorite.setEnabled(false);
            configFavoriteButton(mMovie.getId());

            showTrailerProgressbar(true);
            showTrailerRecycleView(false);

            showReviewProgressbar(true);
            showReviewRecycleView(false);
            setupViewModel(String.valueOf(mMovie.getId()));
        }

    }

    private void bindViewComponents() {
        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mOverview = findViewById(R.id.overview);
        mPoster = findViewById(R.id.movie_poster);
        mRanking = findViewById(R.id.ranking);
        mBtFavorite = findViewById(R.id.bt_favorite);

        mProgressBarTrailer = findViewById(R.id.progress_bar_trailer);
        mProgressBarReview = findViewById(R.id.progress_bar_review);
        mTrailersRecyclerView = findViewById(R.id.recycle_view_trailers);
        mReviewRecyclerView = findViewById(R.id.recycle_view_reviews);
    }

    private void configRecycleViews() {
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailersRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecyclerView.setHasFixedSize(true);
    }

    private void configFavoriteButton(int id) {
        new AsyncTask<Context, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Context... contexts) {
                MovieModel movie = repository.findMovieById(id);
                return new Boolean(movie != null);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                mBtFavorite.setChecked(aBoolean);
                mBtFavorite.setEnabled(true);
                mBtFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked) {
                            mMovie.setFavorite(true);
                            repository.insertMovie(mMovie);
                        } else {
                            repository.deleteMovie(mMovie);
                        }

                    }
                });
            }
        }.execute(this);
    }

    private void setRecycleViewTrailer(List<TrailerModel> trailers) {
        adapter = new TrailerAdapter(this, trailers);
        adapter.setOnListItemClick(this);
        mTrailersRecyclerView.setAdapter(adapter);

        showTrailerProgressbar(false);
        showTrailerRecycleView(true);
    }

    private void setRecycleViewReview(List<ReviewModel> reviews) {
        adapterReview = new ReviewAdapter(this, reviews);
        mReviewRecyclerView.setAdapter(adapterReview);

        showReviewProgressbar(false);
        showReviewRecycleView(true);
    }

    private void showTrailerProgressbar(boolean show) {
        if(show) {
            mProgressBarTrailer.setVisibility(View.VISIBLE);
        } else {
            mProgressBarTrailer.setVisibility(View.GONE);
        }
    }

    private void showTrailerRecycleView(boolean show) {
        if(show) {
            mTrailersRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mTrailersRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showReviewProgressbar(boolean show) {
        if(show) {
            mProgressBarReview.setVisibility(View.VISIBLE);
        } else {
            mProgressBarReview.setVisibility(View.GONE);
        }
    }

    private void showReviewRecycleView(boolean show) {
        if(show) {
            mReviewRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mReviewRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void loadData() {
        mTitle.setText(mMovie.getTitle());
        mDate.setText(mMovie.getReleaseDate());
        mRanking.setText(mMovie.getVoteAverage() + "/10");
        mOverview.setText(mMovie.getOverview());

        URL posterUrl = NetworkUtils.getMoviePosterURL(mMovie.getPosterUrl());
        Picasso.get()
                .load(posterUrl.toString())
                .resize(120, 160)
                .centerCrop()
                .into(mPoster);
    }

    @Override
    public void onListItemClick(TrailerModel trailer) {
        if(!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW,
                NetworkUtils.getYoutubeUri(trailer.getKey()));
        try {
            this.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.youtube_error, Toast.LENGTH_LONG).show();
        }
    }

    private void setupViewModel(String movieId) {
        mViewModel = ViewModelProviders.of(this, new DetailMovieViewModelFactory(getApplication(),
                NetworkUtils.getReviewURL(movieId), NetworkUtils.getTrailersURL(movieId))).get(DetailMovieViewModel.class);
        mViewModel.getTrailers().observe(this, new Observer<List<TrailerModel>>() {
            @Override
            public void onChanged(List<TrailerModel> trailerModels) {
                Log.d(TAG, "Updating list of trailers from LiveData in ViewModel");
                setRecycleViewTrailer(trailerModels);
            }
        });

        mViewModel.getReviews().observe(this, new Observer<List<ReviewModel>>() {
            @Override
            public void onChanged(List<ReviewModel> reviewModels) {
                Log.d(TAG, "Updating list of reviews from LiveData in ViewModel");
                setRecycleViewReview(reviewModels);
            }
        });
    }
}
