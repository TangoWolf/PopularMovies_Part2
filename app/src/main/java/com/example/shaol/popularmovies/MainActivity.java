package com.example.shaol.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.shaol.popularmovies.data.MoviePreferences;
import com.example.shaol.popularmovies.model.Movie;
import com.example.shaol.popularmovies.utils.JsonUtils;
import com.example.shaol.popularmovies.utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    //TODO set up the detail view layout

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.sort_popular);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        loadMovieInfo();
    }

    private void loadMovieInfo() {
        String moviePreference = MoviePreferences.getMoviePreference(this);
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
        intent.putExtra("Title", movieTitle);
        intent.putExtra("Poster", moviePoster);
        intent.putExtra("Rating", movieRating);
        intent.putExtra("Synopsis", movieSynopsis);
        intent.putExtra("ReleaseDate", movieReleaseDate);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
