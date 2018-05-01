package com.example.shaol.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by shaol on 4/25/2018.
 */

public class DetailActivity extends AppCompatActivity {
    ImageView mBackgroundImage;
    ImageView mMoviePoster;
    TextView mMovieTitle;
    TextView mMovieRating;
    TextView mMovieSynopsis;
    TextView mMovieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBackgroundImage = findViewById(R.id.background_image);
        mMoviePoster = findViewById(R.id.poster_image);
        mMovieTitle = findViewById(R.id.movie_title);
        mMovieRating = findViewById(R.id.movie_rating);
        mMovieSynopsis = findViewById(R.id.movie_synopsis);
        mMovieReleaseDate = findViewById(R.id.movie_releasedate);

        String poster = getIntent().getStringExtra("Poster");
        String title = getIntent().getStringExtra("Title");
        String rating = getIntent().getStringExtra("Rating");
        String synopsis = getIntent().getStringExtra("Synopsis");
        String releaseDate = getIntent().getStringExtra("ReleaseDate");

        rating = rating + "/10";
        releaseDate = "Released: " + releaseDate;

        mMovieTitle.setText(title);
        mMovieRating.setText(rating);
        mMovieSynopsis.setText(synopsis);
        mMovieReleaseDate.setText(releaseDate);

        Uri.Builder backgroundBuilder = new Uri.Builder();
        backgroundBuilder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w500")
                .appendPath(poster);
        Uri backgroundImage = backgroundBuilder.build();
        Picasso.get()
                .load(backgroundImage)
                .into(mBackgroundImage);

        Uri.Builder posterBuilder = new Uri.Builder();
        posterBuilder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(poster);
        Uri posterImage = posterBuilder.build();
        Picasso.get()
                .load(posterImage)
                .into(mMoviePoster);

        setTitle(title);
    }
}
