package com.example.shaol.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shaol.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by shaol on 5/3/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_MOVIE_IMAGE + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_ENTRIES);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
