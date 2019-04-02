package com.amaro.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.data.AppDatabase;
import com.amaro.popularmovies.data.movie.MovieModel;
import com.amaro.popularmovies.model.MovieListViewModelFactory;
import com.amaro.popularmovies.model.MoviesListViewModel;
import com.amaro.popularmovies.utilities.NetworkUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesListActivity extends AppCompatActivity implements MoviesListAdapter.ListItemClickListener {

    private static final String TAG = MoviesListActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private RecyclerView mMoviesListRecycleView;
    private MoviesListAdapter adapter;
    private MoviesListViewModel mViewModel;

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private final static int NUMBER_OfColumns = 2;

    private AppDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieslist_activity);

        bindViewComponents();
        configMoviesListRecycleView();

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel(POPULAR);
    }

    private void bindViewComponents() {
        mProgressBar = findViewById(R.id.progress_bar);
        mMoviesListRecycleView = findViewById(R.id.recycle_view);
    }

    private void configMoviesListRecycleView() {
        mMoviesListRecycleView.setLayoutManager(new GridLayoutManager(this, NUMBER_OfColumns));
        mMoviesListRecycleView.setHasFixedSize(true);
    }

    private void showProgressbar(boolean show) {
        if(show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showRecycleView(boolean show) {
        if(show) {
            mMoviesListRecycleView.setVisibility(View.VISIBLE);
        } else {
            mMoviesListRecycleView.setVisibility(View.INVISIBLE);
        }
    }

    private void setRecycleViewData(List<MovieModel> movies) {
        adapter = new MoviesListAdapter(this, movies);
        adapter.setOnListItemClick(this);
        mMoviesListRecycleView.setAdapter(adapter);

        showProgressbar(false);
        showRecycleView(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sort_by_popular:
                loadDataFromServe(POPULAR);
                break;
            case R.id.sort_by_top_rated:
                loadDataFromServe(TOP_RATED);
                break;
            case R.id.sort_by_favorites:
                mViewModel.loadMoviesFromDb();
                mViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
                    @Override
                    public void onChanged(List<MovieModel> movieModels) {
                        Log.d(TAG, "Updating list of movies from DB");
                        setRecycleViewData(movieModels);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(MovieModel movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_DETAIL_DATA, movie);
        startActivity(intent);
    }

    private void loadDataFromServe(String sortBy) {
        if(!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            return;
        }

        showProgressbar(true);
        showRecycleView(false);
        mViewModel.reloadData(getApplication(), NetworkUtils.getPopularMoviesURL(sortBy));
        mViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                setRecycleViewData(movieModels);
            }
        });
    }

    private void setupViewModel(String sortBy) {
        mViewModel = ViewModelProviders.of(this, new MovieListViewModelFactory(getApplication(),
                NetworkUtils.getPopularMoviesURL(sortBy))).get(MoviesListViewModel.class);
        mViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                setRecycleViewData(movieModels);
            }
        });
    }
}
