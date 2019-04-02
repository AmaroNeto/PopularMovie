package com.amaro.popularmovies.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.data.review.ReviewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewModel> mReviews;
    private Context mContext;


    public ReviewAdapter(Context context, List<ReviewModel> reviews) {
        mContext = context;
        mReviews = reviews;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewModel review = mReviews.get(position);
        holder.title.setText(review.getContent());
        holder.subtitle.setText(review.getAuthor());
        holder.reviewModel = review;
    }

    @Override
    public int getItemCount() {
        return (mReviews == null) ? 0 : mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subtitle;
        ReviewModel reviewModel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }

}
