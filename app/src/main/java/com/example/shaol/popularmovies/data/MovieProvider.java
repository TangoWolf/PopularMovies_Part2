package com.example.shaol.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.shaol.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by shaol on 5/3/2018.
 */

public class MovieProvider extends ContentProvider {

    private MovieDbHelper mDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                cursor = database.query(MovieEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                long id = database.insert(MovieEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated;

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                rowsUpdated = database.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Update not supported for " + uri);
        }
    }
}
