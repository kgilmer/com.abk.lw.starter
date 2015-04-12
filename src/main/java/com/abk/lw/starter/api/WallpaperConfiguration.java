package com.abk.lw.starter.api;

import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.abk.lw.starter.android.WallpaperService;

import java.util.HashSet;
import java.util.Set;

/**
 * Base configuration of wallpaper.  Not intended to be modified.
 * Add state to the concrete subclass for a particular wallpaper implementation.
 */
public abstract class WallpaperConfiguration {
    private int frameDelayMillis;
    private Point screenSize = new Point();
    private boolean debug = false;
    private PointF offset = new PointF();
    private PointF step = new PointF();
    private Point pixels = new Point();
    private Set<WallpaperService.UI_EVENT> uiEvents = new HashSet<>();
    private MotionEvent lastTouchEvent;

    public Point getScreenSize() {
        return screenSize;
    }

    public int getFrameDelayMillis() {
        return frameDelayMillis;
    }

    public void setFrameDelayMillis(int frameDelayMillis) {
        this.frameDelayMillis = frameDelayMillis;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public PointF getOffset() {
        return offset;
    }

    public PointF getStep() {
        return step;
    }

    public Point getPixels() {
        return pixels;
    }

    public void addEvent(WallpaperService.UI_EVENT offsetChanged) {
        uiEvents.add(offsetChanged);
    }

    public Set<WallpaperService.UI_EVENT> getUiEvents() {
        return uiEvents;
    }

    public void setLastTouchEvent(MotionEvent lastTouchEvent) {
        this.lastTouchEvent = lastTouchEvent;
    }

    public MotionEvent getLastTouchEvent() {
        return lastTouchEvent;
    }
}
