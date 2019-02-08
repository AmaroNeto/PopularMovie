package com.amaro.popularmovies.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;

    private ListItemClickListener mOnClickListener;

    public MoviesListAdapter(Context context) {
        this.mContext = context;
    }

    public MoviesListAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    public void setOnListItemClick(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movieslist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.movie = movie;

        URL posterUrl = NetworkUtils.getMoviePosterURL(movie.getPosterUrl());
        Picasso.get()
                .load(posterUrl.toString())
                .resize(500, 700)
                .centerInside()
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView poster;
        Movie movie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            poster = itemView.findViewById(R.id.movie_poster);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(movie);
            }
        }
    }

}
