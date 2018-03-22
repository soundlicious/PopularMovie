package com.example.pablo.popularmovie.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    public static final int CODE_BOOKMARK = 100;
    public static final int CODE_BOOKMARK_WITH_ID = 101;

    public static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;

    public MovieProvider() {
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_BOOKMARK, CODE_BOOKMARK);
        matcher.addURI(authority, MoviesContract.PATH_BOOKMARK + "/#", CODE_BOOKMARK_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues value) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri newUri = null;
        switch (uriMatcher.match(uri)){
            case CODE_BOOKMARK:
                final Integer movie_id = value.getAsInteger(MoviesContract.BookmarkEntry.COLUMN_ID);
                long id = db.insert(MoviesContract.BookmarkEntry.TABLE_NAME, null, value);
                if (id != -1) {
                    newUri = ContentUris.withAppendedId(MoviesContract.BookmarkEntry.CONTENT_URI, movie_id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                } else
                    throw new SQLException("Failed to add a record into " + uri);
                break;
            default:
                throw new SQLException("Failed to add a record into " + uri);
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {

            case CODE_BOOKMARK:
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        MoviesContract.BookmarkEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            case CODE_BOOKMARK_WITH_ID:
                String id = uri.getLastPathSegment();
                numRowsDeleted = dbHelper.getWritableDatabase().delete(MoviesContract.BookmarkEntry.TABLE_NAME,
                        MoviesContract.BookmarkEntry.COLUMN_ID + " = ? ",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case CODE_BOOKMARK:
                cursor = dbHelper.getReadableDatabase().query(MoviesContract.BookmarkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_BOOKMARK_WITH_ID:
                String movieId = uri.getLastPathSegment();
                selectionArgs = new String[]{movieId};
                cursor = dbHelper.getReadableDatabase().query(MoviesContract.BookmarkEntry.TABLE_NAME,
                        projection,
                        MoviesContract.BookmarkEntry.COLUMN_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
