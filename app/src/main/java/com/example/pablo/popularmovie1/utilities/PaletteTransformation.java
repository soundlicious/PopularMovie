package com.example.pablo.popularmovie1.utilities;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.squareup.picasso.Transformation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by pablo on 08/03/2018.
 * http://jakewharton.com/coercing-picasso-to-play-with-palette/
 */


public final class PaletteTransformation implements Transformation {
    private static final PaletteTransformation INSTANCE = new PaletteTransformation();
    private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

    public static PaletteTransformation instance() {
        return INSTANCE;
    }

    public static Palette getPalette(Bitmap bitmap) {
        return CACHE.get(bitmap);
    }

    private PaletteTransformation() {}

    @Override public Bitmap transform(Bitmap source) {
        Palette palette = Palette.from(source).generate();
        CACHE.put(source, palette);
        return source;
    }

    @Override
    public String key() {
        return getClass().getCanonicalName();
    }

    public static abstract class Callback
            implements com.squareup.picasso.Callback {

        public Callback() {
        }

        @Override
        public void onSuccess() {
            onPalette();
        }

        @Override
        public void onError() {
        }

        public abstract void onPalette();
    }
}