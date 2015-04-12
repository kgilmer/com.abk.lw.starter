package com.abk.lw.starter.api;

import android.graphics.Canvas;

import com.google.common.base.Predicate;

/**
 * Implementations draw on a canvas.  This is the fundamental action of the live wallpaper.
 */
public interface WallpaperRenderer extends Predicate<Canvas> {
}
