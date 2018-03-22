package com.example.pablo.popularmovie.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pablo on 19/03/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularMovie.db";
    private static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_BOOKMARK_TABLE =
            "CREATE TABLE " + MoviesContract.BookmarkEntry.TABLE_NAME + " (" +
                    MoviesContract.BookmarkEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_ORIGINAL_NAME + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_THUMBMAIL + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    MoviesContract.BookmarkEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +

                    " UNIQUE (" + MoviesContract.BookmarkEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.BookmarkEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
