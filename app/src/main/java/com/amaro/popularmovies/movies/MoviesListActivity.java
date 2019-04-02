package com.amaro.popularmovies.movies;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.movies.async.AsyncTaskDelegate;
import com.amaro.popularmovies.movies.async.FetchMoviesTask;
import com.amaro.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesListActivity extends AppCompatActivity implements MoviesListAdapter.ListItemClickListener,
        AsyncTaskDelegate {

    private ProgressBar mProgressBar;
    private RecyclerView mMoviesListRecycleView;
    private List<Movie> mMovies;

    private static final String POPULAR = "popular";
    private final static int NUMBER_OfColumns = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieslist_activity);

        bindViewComponents();
        configMoviesListRecycleView();

        loadData(POPULAR);
    }

    private void bindViewComponents() {
        mProgressBar = findViewById(R.id.progress_bar);
        mMoviesListRecycleView = findViewById(R.id.recycle_view);
    }

    private void configMoviesListRecycleView() {
        mMoviesListRecycleView.setLayoutManager(new GridLayoutManager(this, NUMBER_OfColumns));
        mMoviesListRecycleView.setHasFixedSize(true);
    }

    private void loadData(String sortBy) {

        if(!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            return;
        }

        showProgressbar(true);
        showRecycleView(false);

        URL listMoviesUrl = NetworkUtils.getPopularMoviesURL(sortBy);
        new FetchMoviesTask(this).execute(listMoviesUrl);
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

    private void setRecycleViewData(List<Movie> movies) {
        MoviesListAdapter mMoviesAdapter = new MoviesListAdapter(this, movies);
        mMoviesAdapter.setOnListItemClick(this);
        mMoviesListRecycleView.setAdapter(mMoviesAdapter);
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
                loadData("popular");
                break;
            case R.id.sort_by_top_rated:
                loadData("top_rated");
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_DETAIL_DATA, movie);
        startActivity(intent);
    }

    @Override
    public void processFinish(Object output) {
        if(output != null && !TextUtils.isEmpty((String) output)) {
            try {
                JSONObject mbMovieList = new JSONObject((String) output);
                JSONArray result = mbMovieList.getJSONArray("results");

                List<Movie> moviesArray = new ArrayList<Movie>();

                for(int i = 0; i < result.length(); i++) {
                    JSONObject movieJson = result.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(movieJson.getString("original_title"));
                    movie.setOverview(movieJson.getString("overview"));
                    movie.setPosterUrl(movieJson.getString("poster_path"));
                    movie.setReleaseDate(movieJson.getString("release_date"));
                    movie.setVoteAverage(movieJson.getDouble("vote_average"));

                    moviesArray.add(movie);
                }

                setRecycleViewData(moviesArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        showProgressbar(false);
        showRecycleView(true);
    }
}
