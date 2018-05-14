package com.example.shaol.popularmovies.utils;

import android.content.Context;
import android.util.Log;

import com.example.shaol.popularmovies.model.Movie;
import com.example.shaol.popularmovies.model.Review;
import com.example.shaol.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Arrays;

/**
 * Created by shaol on 4/25/2018.
 */

public final class JsonUtils {
    private static final String RESULTS = "results";

    private static final String ID = "id";
    private static final String USER_RATING = "vote_average";
    private static final String TITLE = "original_title";
    private static final String IMAGE = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String RELEASEDATE = "release_date";

    private static final String NAME = "name";
    private static final String KEY = "key";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private static final String MESSAGE_CODE = "cod";

    public static Movie[] getMovieInfoFromJson(String JsonString) throws JSONException {
        Movie[] parsedMovieInfo = null;

        JSONObject startString = new JSONObject(JsonString);

        if (startString.has(MESSAGE_CODE)) {
            int errorCode = startString.getInt(MESSAGE_CODE);

            switch(errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray results = startString.getJSONArray(RESULTS);
        parsedMovieInfo = new Movie[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);

            int id = movie.getInt(ID);
            String title = movie.getString(TITLE);
            String image = movie.getString(IMAGE);
            image = image.substring(1);
            String rating = movie.getString(USER_RATING);
            String synopsis = movie.getString(SYNOPSIS);
            String releaseDate = movie.getString(RELEASEDATE);

            Movie aMovie = new Movie(id, title, image, rating, synopsis, releaseDate);
            parsedMovieInfo[i] = aMovie;
        }
        return parsedMovieInfo;
    }

    public static Trailer[] getMovieTrailersFromJson(String JsonString) throws JSONException {
        Trailer[] parsedTrailerInfo = null;

        JSONObject startString = new JSONObject(JsonString);

        if (startString.has(MESSAGE_CODE)) {
            int errorCode = startString.getInt(MESSAGE_CODE);

            switch(errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray results = startString.getJSONArray(RESULTS);
        parsedTrailerInfo = new Trailer[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject trailer = results.getJSONObject(i);

            String name = trailer.getString(NAME);
            String key = trailer.getString(KEY);

            Trailer aTrailer = new Trailer(name, key);
            parsedTrailerInfo[i] = aTrailer;
        }
        return parsedTrailerInfo;
    }

    public static Review[] getMovieReviewsFromJson(String JsonString) throws JSONException {
        Review[] parsedReviewInfo = null;

        JSONObject startString = new JSONObject(JsonString);

        if (startString.has(MESSAGE_CODE)) {
            int errorCode = startString.getInt(MESSAGE_CODE);

            switch(errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray results = startString.getJSONArray(RESULTS);
        parsedReviewInfo = new Review[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);

            String author = movie.getString(AUTHOR);
            String review = movie.getString(CONTENT);

            Review aReview = new Review(author, review);
            parsedReviewInfo[i] = aReview;
        }
        return parsedReviewInfo;
    }
}
