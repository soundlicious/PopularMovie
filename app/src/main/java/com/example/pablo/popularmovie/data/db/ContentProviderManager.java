package com.example.pablo.popularmovie.data.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.pablo.popularmovie.data.models.MovieDetail;

import java.util.ArrayList;

import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_ID;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_NAME;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_ORIGINAL_NAME;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_POSTER;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_RELEASE_DATE;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_SYNOPSIS;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_THUMBMAIL;
import static com.example.pablo.popularmovie.data.db.MoviesContract.BookmarkEntry.COLUMN_VOTE_AVERAGE;

/**
 * Created by pablo on 19/03/2018.
 */

public class ContentProviderManager implements IProviderManager {


    private final ContentResolver cResolver;

    public ContentProviderManager(Context context) {
        cResolver = context.getContentResolver();
    }

    @Override
    public void bookmarkMovie(MovieDetail movieDetail) {
        final ContentValues value = new ContentValues();
        value.put(COLUMN_ID, movieDetail.getId());
        value.put(COLUMN_NAME, movieDetail.getTitle());
        value.put(COLUMN_ORIGINAL_NAME, movieDetail.getOriginalTitle());
        value.put(COLUMN_POSTER, movieDetail.getPosterPath());
        value.put(COLUMN_THUMBMAIL, movieDetail.getBackdropPath());
        value.put(COLUMN_SYNOPSIS, movieDetail.getOverview());
        value.put(COLUMN_RELEASE_DATE, movieDetail.getReleaseDate());
        value.put(COLUMN_VOTE_AVERAGE, movieDetail.getVoteAverage());
        cResolver.insert(MoviesContract.BookmarkEntry.CONTENT_URI, value);
    }

    @Override
    public MovieDetail queryMovie(int id) {
        MovieDetail movieDetail = null;
        Cursor c = cResolver.query(MoviesContract.BookmarkEntry.BuildBoorkmarkUriWithID(id), null, null, null, null);
        if (c.moveToNext()){
            movieDetail = new MovieDetail();
            movieDetail.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            movieDetail.setTitle(c.getString(c.getColumnIndex(COLUMN_NAME)));
            movieDetail.setOriginalTitle(c.getString(c.getColumnIndex(COLUMN_ORIGINAL_NAME)));
            movieDetail.setPosterPath(c.getString(c.getColumnIndex(COLUMN_POSTER)));
            movieDetail.setBackdropPath(c.getString(c.getColumnIndex(COLUMN_THUMBMAIL)));
            movieDetail.setOverview(c.getString(c.getColumnIndex(COLUMN_SYNOPSIS)));
            movieDetail.setReleaseDate(c.getString(c.getColumnIndex(COLUMN_RELEASE_DATE)));
            movieDetail.setVoteAverage(c.getDouble(c.getColumnIndex(COLUMN_VOTE_AVERAGE)));
        }
        return movieDetail;
    }

    @Override
    public ArrayList<MovieDetail> queryMovies() {
        ArrayList<MovieDetail> movies = new ArrayList<>();
        Cursor c = cResolver.query(MoviesContract.BookmarkEntry.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()){
            MovieDetail movieDetail = new MovieDetail();
            movieDetail.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            movieDetail.setTitle(c.getString(c.getColumnIndex(COLUMN_NAME)));
            movieDetail.setOriginalTitle(c.getString(c.getColumnIndex(COLUMN_ORIGINAL_NAME)));
            movieDetail.setPosterPath(c.getString(c.getColumnIndex(COLUMN_POSTER)));
            movieDetail.setBackdropPath(c.getString(c.getColumnIndex(COLUMN_THUMBMAIL)));
            movieDetail.setOverview(c.getString(c.getColumnIndex(COLUMN_SYNOPSIS)));
            movieDetail.setReleaseDate(c.getString(c.getColumnIndex(COLUMN_RELEASE_DATE)));
            movieDetail.setVoteAverage(c.getDouble(c.getColumnIndex(COLUMN_VOTE_AVERAGE)));
            movies.add(movieDetail);
        }
        return movies;
    }

    @Override
    public void deleteMovie(int id) {
        cResolver.delete(MoviesContract.BookmarkEntry.BuildBoorkmarkUriWithID(id), null, null);
    }
}
