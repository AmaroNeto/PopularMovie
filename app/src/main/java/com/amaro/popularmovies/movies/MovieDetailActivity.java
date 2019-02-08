package com.amaro.popularmovies.movies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {

    public final static String MOVIE_DETAIL_DATA = "MovieDetailData";
    private Movie mMovie;

    private TextView mTitle;
    private TextView mOverview;
    private TextView mRanking;
    private TextView mDate;
    private ImageView mPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        bindViewComponents();

        mMovie = getIntent().getExtras().getParcelable(MOVIE_DETAIL_DATA);

        if(mMovie != null) {
            loadData();
        }

    }

    private void bindViewComponents(){
        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mOverview = findViewById(R.id.overview);
        mPoster = findViewById(R.id.movie_poster);
        mRanking = findViewById(R.id.ranking);
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
}
