package com.example.shaol.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shaol.popularmovies.data.MovieContract.MovieEntry;
import com.example.shaol.popularmovies.data.MovieDbHelper;
import com.example.shaol.popularmovies.data.MoviePreferences;
import com.example.shaol.popularmovies.model.Review;
import com.example.shaol.popularmovies.model.Trailer;
import com.example.shaol.popularmovies.utils.JsonUtils;
import com.example.shaol.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

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
    TextView tTrailerView;
    TextView tReviewView;
    CheckBox mCheckBox;
    ListView mTrailerView;
    LinearLayout mReviewView;
    boolean favorite;

    String poster;
    String movieId;
    String title;
    String rating;
    String synopsis;
    String releaseDate;

    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;
    Trailer[] aTrailer;
    Review[] aReview;

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
        mCheckBox = findViewById(R.id.checkbox);
        mTrailerView = findViewById(R.id.trailerView);
        mReviewView = findViewById(R.id.reviewView);
        tTrailerView = findViewById(R.id.movie_trailers);
        tReviewView = findViewById(R.id.movie_reviews);

        poster = getIntent().getStringExtra("Poster");
        title = getIntent().getStringExtra("Title");
        rating = getIntent().getStringExtra("Rating");
        synopsis = getIntent().getStringExtra("Synopsis");
        releaseDate = getIntent().getStringExtra("ReleaseDate");
        int id = getIntent().getIntExtra("Id", 0);
        movieId = String.valueOf(id);

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

        favorite = search(movieId);
        mCheckBox.setChecked(favorite);

        if(MoviePreferences.getMoviePreference().equals("favorites")){
            tTrailerView.setVisibility(View.GONE);
            mTrailerView.setVisibility(View.GONE);
            tReviewView.setVisibility(View.GONE);
            mReviewView.setVisibility(View.GONE);
        } else {
            new loadMovieTrailers().execute(movieId, "videos");
            new loadMovieReviews().execute(movieId, "reviews");
        }
    }

    public void onCheckBoxClicked(View view){

        if(favorite){
            deleteMovie();
            mCheckBox.setChecked(false);
            if(MoviePreferences.getMoviePreference().equals("favorites")){
                getContentResolver().notifyChange(MovieEntry.CONTENT_URI, null);
            }
        } else {
            insertMovie();
            mCheckBox.setChecked(true);
        }
    }

    private boolean search(String id){
        MovieDbHelper mDbHelper = new MovieDbHelper(this);
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String[] projection = {MovieEntry.COLUMN_MOVIE_ID};
        String selection = MovieEntry.COLUMN_MOVIE_ID + " =?";
        String[] selectionArgs = {id};
        Cursor cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        try{ if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }} finally {
            cursor.close();
        }
    }

    private void insertMovie(){
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movieId);
        values.put(MovieEntry.COLUMN_MOVIE_IMAGE, poster);
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, title);
        values.put(MovieEntry.COLUMN_MOVIE_RATING, rating);
        values.put(MovieEntry.COLUMN_MOVIE_SYNOPSIS, synopsis);
        values.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);

        Uri newUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);
        getContentResolver().notifyChange(newUri, null);
    }

    private void deleteMovie(){
        MovieDbHelper mDbHelper = new MovieDbHelper(this);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        String selection = MovieEntry.COLUMN_MOVIE_ID + " =?";
        String[] selectionArgs = {movieId};
        database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
    }

    public class loadMovieTrailers extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {
            String movieId = params[0];
            String detail = params[1];

            URL movieRequestUrl = NetworkUtils.buildDetailUrl(movieId, detail);

            try {
                String JsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                Trailer[] movieTrailerInfo = JsonUtils.getMovieTrailersFromJson(JsonResponse);

                return movieTrailerInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] movieTrailerInfo) {
            aTrailer = movieTrailerInfo;
            mTrailerAdapter = new TrailerAdapter(DetailActivity.this, aTrailer);
            mTrailerView.setAdapter(mTrailerAdapter);
            int views = mTrailerAdapter.getCount();
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
            ViewGroup.LayoutParams params = mTrailerView.getLayoutParams();
            params.height = (views * height);
            mTrailerView.setLayoutParams(params);
            mTrailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Trailer trailer = aTrailer[i];
                    String key = trailer.getKey();

                    Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));

                    try {
                        startActivity(youTubeIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(browserIntent);
                    }
                }
            });
        }
    }

    public class loadMovieReviews extends AsyncTask<String, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {
            String movieId = params[0];
            String detail = params[1];

            URL movieRequestUrl = NetworkUtils.buildDetailUrl(movieId, detail);

            try {
                String JsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                Review[] movieReviewInfo = JsonUtils.getMovieReviewsFromJson(JsonResponse);

                return movieReviewInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] movieReviewInfo) {
            aReview = movieReviewInfo;
            mReviewAdapter = new ReviewAdapter(DetailActivity.this, aReview);
            final int views = mReviewAdapter.getCount();

            for (int i = 0; i < views; i++) {
                View view = mReviewAdapter.getView(i, null, null);
                mReviewView.addView(view);
            }
        }
    }
}
