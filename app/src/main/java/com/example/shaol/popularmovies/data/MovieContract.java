package com.example.shaol.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shaol on 5/3/2018.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.shaol.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "MovieID";
        public static final String COLUMN_MOVIE_TITLE = "Title";
        public static final String COLUMN_MOVIE_IMAGE = "Image";
        public static final String COLUMN_MOVIE_RATING = "Rating";
        public static final String COLUMN_MOVIE_SYNOPSIS = "Synopsis";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "ReleaseDate";
    }
}
