package com.example.shaol.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shaol.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by shaol on 4/25/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final ListItemClickListener mOnClickListener;
    String[] mMovieInfo;
    Movie[] aMovieInfo;

    public interface ListItemClickListener {
        void onListItemClick(Movie movieInfo);
    }

    public MovieAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePicture;

        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePicture = itemView.findViewById(R.id.movie_image_li);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieInfo = aMovieInfo[adapterPosition];
            mOnClickListener.onListItemClick(movieInfo);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, int position) {
        Movie movie = aMovieInfo[position];
        String movieInfo = movie.getImage();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w342")
                .appendPath(movieInfo);
        Uri movieImage = builder.build();
        Picasso.get()
                .load(movieImage)
                .into(viewHolder.moviePicture);
    }

    @Override
    public int getItemCount() {
        if (aMovieInfo == null) return 0;
        return aMovieInfo.length;
    }

    public void setMovieInfo(Movie[] movieInfo) {
        aMovieInfo = movieInfo;
        notifyDataSetChanged();
    }
}
