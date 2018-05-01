package com.example.shaol.popularmovies.data;

import android.content.Context;

/**
 * Created by shaol on 4/28/2018.
 */

public class MoviePreferences {

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static String DEFAULT_DRAW_PREFERENCE = POPULAR;

    private static String mSortPeference = POPULAR;

    public static String getMoviePreference(Context context) {
        return mSortPeference;
    }

    public static void sortPopular() {mSortPeference = POPULAR;}

    public static void sortRating() {mSortPeference = TOP_RATED;}

    private static String getDefaultMoviePreference() {
        return DEFAULT_DRAW_PREFERENCE;
    }
}
