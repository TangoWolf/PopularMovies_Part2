package com.example.shaol.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shaol.popularmovies.model.Trailer;

/**
 * Created by shaol on 5/8/2018.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    Trailer [] mTrailers;

    public TrailerAdapter(Context context, Trailer[] trailers) {
        super(context, 0, trailers);
        mTrailers = trailers;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        View listItem = view;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.trailer_view_item, parent, false);
        }

        Trailer currentTrailer = mTrailers[position];

        TextView trailerTextView = listItem.findViewById(R.id.trailerViewItem);
        trailerTextView.setText(currentTrailer.getName());

        return listItem;
    }
}
