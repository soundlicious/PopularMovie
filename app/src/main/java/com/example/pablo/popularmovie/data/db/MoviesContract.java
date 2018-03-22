package com.example.pablo.popularmovie.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pablo on 19/03/2018.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.pablo.popularmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKMARK = "bookmark";

    public static final class BookmarkEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BOOKMARK)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "bookmark";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_NAME = "movie_name";
        public static final String COLUMN_ORIGINAL_NAME = "movie_original_name";
        public static final String COLUMN_POSTER = "movie_poster_link";
        public static final String COLUMN_THUMBMAIL = "movie_thumbmail_link";
        public static final String COLUMN_SYNOPSIS = "movie_synopsis";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_average";

        public static Uri BuildBoorkmarkUriWithID(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath("" + id)
                    .build();
        }
    }

}
