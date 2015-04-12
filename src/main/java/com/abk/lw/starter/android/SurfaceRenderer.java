package com.abk.lw.starter.android;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.abk.lw.starter.api.WallpaperConfiguration;
import com.google.common.base.Predicate;

/**
 * Created by kgilmer on 4/8/15.
 */
final class SurfaceRenderer implements HolderRenderer {
    private final Handler handler;
    private SurfaceHolder surfaceHolder;
    private final Predicate<Canvas> worldRenderer;
    private final WallpaperConfiguration configuration;
    private Canvas canvas;

    public SurfaceRenderer(Handler handler, Predicate<Canvas> worldRenderer, WallpaperConfiguration configuration) {
        this.handler = handler;
        this.worldRenderer = worldRenderer;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        if (surfaceHolder != null) {
            try {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        worldRenderer.apply(canvas);
                        configuration.getUiEvents().clear();
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                // Reschedule the next redraw
                handler.removeCallbacks(this);
                handler.postDelayed(this, configuration.getFrameDelayMillis());
            } catch (IllegalArgumentException e) {
                //Ignore
            } catch (IllegalStateException e) {
                surfaceHolder = null;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceHolder = null;
    }
}
