package com.example.shaol.popularmovies;

import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shaol.popularmovies.data.MovieContract.MovieEntry;
import com.example.shaol.popularmovies.data.MoviePreferences;
import com.example.shaol.popularmovies.model.Movie;
import com.example.shaol.popularmovies.utils.JsonUtils;
import com.example.shaol.popularmovies.utils.NetworkUtils;

import java.net.URL;

import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE;
import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_RATING;
import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS;
import static com.example.shaol.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private static final int MOVIE_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.sort_popular);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        mEmptyView = findViewById(R.id.emptyView);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        loadMovieInfo();
    }

    private void loadMovieInfo() {
        String moviePreference = MoviePreferences.getMoviePreference();
        new FetchMovieInfoTask().execute(moviePreference);
    }

    @Override
    public void onListItemClick(Movie movieInfo) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        String movieTitle = movieInfo.getTitle();
        String moviePoster = movieInfo.getImage();
        String movieRating = movieInfo.getRating();
        String movieSynopsis = movieInfo.getSynopsis();
        String movieReleaseDate = movieInfo.getReleaseDate();
        int movieId = movieInfo.getId();
        intent.putExtra("Title", movieTitle);
        intent.putExtra("Poster", moviePoster);
        intent.putExtra("Rating", movieRating);
        intent.putExtra("Synopsis", movieSynopsis);
        intent.putExtra("ReleaseDate", movieReleaseDate);
        intent.putExtra("Id", movieId);
        startActivity(intent);
    }

    public class FetchMovieInfoTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String preference = params[0];

            URL movieRequestUrl = NetworkUtils.buildUrl(preference);

            try {
                String JsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                Movie[] movieImageInfo = JsonUtils.getMovieInfoFromJson(JsonResponse);

                return movieImageInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] moviePosterInfo) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieAdapter.setMovieInfo(moviePosterInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case R.id.sort_popular:
                MoviePreferences.sortPopular();
                loadMovieInfo();
                setTitle(R.string.sort_popular);
                return true;
            case R.id.sort_rating:
                MoviePreferences.sortRating();
                loadMovieInfo();
                setTitle(R.string.sort_rating);
                return true;
            case R.id.sort_favorites:
                MoviePreferences.sortFavorites();
                getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
                setTitle(R.string.sort_favorites);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MovieEntry._ID,
                MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_MOVIE_TITLE,
                MovieEntry.COLUMN_MOVIE_IMAGE,
                MovieEntry.COLUMN_MOVIE_RATING,
                MovieEntry.COLUMN_MOVIE_SYNOPSIS,
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };

        return new CursorLoader(this, MovieEntry.CONTENT_URI, projection, null, null, COLUMN_MOVIE_ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            Movie[] movieData = CursorConverter(data);
            mMovieAdapter.setMovieInfo(movieData);
        }
        getSupportLoaderManager().destroyLoader(MainActivity.MOVIE_LOADER);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.setMovieInfo(null);
    }

    private Movie[] CursorConverter(Cursor data) {
        Movie[] movieData = new Movie[data.getCount()];
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            int movieId = data.getInt(data.getColumnIndexOrThrow(COLUMN_MOVIE_ID));
            String movieName = data.getString(data.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE));
            String movieImage = data.getString(data.getColumnIndexOrThrow(COLUMN_MOVIE_IMAGE));
            String movieRating = data.getString(data.getColumnIndexOrThrow(COLUMN_MOVIE_RATING));
            String movieSynopsis = data.getString(data.getColumnIndexOrThrow(COLUMN_MOVIE_SYNOPSIS));
            String movieReleaseDate = data.getString(data.getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DATE));
            Movie aMovie = new Movie(movieId, movieName, movieImage, movieRating, movieSynopsis, movieReleaseDate);
            movieData[i] = aMovie;
        }
        return movieData;
    }
}
