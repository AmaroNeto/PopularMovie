package com.amaro.popularmovies.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amaro.popularmovies.R;
import com.amaro.popularmovies.data.trailer.TrailerModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<TrailerModel> mTrailers;
    private Context mContext;

    private  ListItemClickListener mOnClickListener;

    public TrailerAdapter(Context context, List<TrailerModel> trailers) {
        mContext = context;
        mTrailers = trailers;
    }

    public interface ListItemClickListener {
        void onListItemClick(TrailerModel trailer);
    }

    public void setOnListItemClick(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrailerModel trailer = mTrailers.get(position);
        holder.title.setText(trailer.getTitle());
        holder.subtitle.setText(trailer.getType());
        holder.trailer = trailer;
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null) ? 0 : mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView subtitle;
        TrailerModel trailer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(trailer);
            }
        }
    }
}
