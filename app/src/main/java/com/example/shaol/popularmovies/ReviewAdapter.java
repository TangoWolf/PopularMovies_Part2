package com.example.shaol.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shaol.popularmovies.model.Review;

/**
 * Created by shaol on 5/8/2018.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    Review[] mReviews;

    public ReviewAdapter(Context context, Review[] reviews) {
        super(context, 0, reviews);
        mReviews = reviews;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        View item = view;
        if (item == null) {
            item = LayoutInflater.from(getContext()).inflate(R.layout.review_view_item, parent, false);
        }

        Review currentReview = mReviews[position];

        TextView reviewerTextView = item.findViewById(R.id.review_author_view);
        reviewerTextView.setText(currentReview.getReviewer());

        TextView reviewTextView = item.findViewById(R.id.review_content_view);
        reviewTextView.setText(currentReview.getReview());

        return item;
    }
}
