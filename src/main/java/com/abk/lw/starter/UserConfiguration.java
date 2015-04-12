package com.abk.lw.starter;

import android.graphics.Paint;


import com.abk.lw.starter.api.WallpaperConfiguration;

import java.util.Hashtable;
import java.util.Map;

/**
 * This example class represents all of the wallpaper state.  It must extend WallpaperConfiguration.
 */
public class UserConfiguration extends WallpaperConfiguration {
    public static final String BACKGROUND_PAINT_KEY = "BACKGROUND_PAINT_KEY";
    public static final String FOREGROUND_PAINT_KEY = "FOREGROUND_PAINT_KEY";

    private int value = 0;
    private Map<String, Paint> paintMap = new Hashtable<>();

    public Map<String, Paint> getPaintMap() {
        return paintMap;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
