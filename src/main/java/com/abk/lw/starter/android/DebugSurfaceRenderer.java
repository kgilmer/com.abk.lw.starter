package com.abk.lw.starter.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.abk.lw.starter.api.WallpaperConfiguration;
import com.google.common.base.Predicate;

/**
 * Created by kgilmer on 4/8/15.
 */
final class DebugSurfaceRenderer implements com.abk.lw.starter.android.HolderRenderer {
    private final Handler handler;
    private SurfaceHolder surfaceHolder;
    private final Predicate<Canvas> worldRenderer;
    private final WallpaperConfiguration configuration;

    private long totalTime = -1;
    private long cycles = 0;

    private Paint debugPaintForeground;
    private Paint debugPaintBackground;

    private Canvas canvas;

    public DebugSurfaceRenderer(Handler handler, Predicate<Canvas> worldRenderer, WallpaperConfiguration configuration) {
        this.handler = handler;
        this.worldRenderer = worldRenderer;
        this.configuration = configuration;

        this.debugPaintForeground = new Paint();
        this.debugPaintForeground.setColor(Color.WHITE);
        this.debugPaintForeground.setTextSize(20f);
        this.debugPaintBackground = new Paint();
        this.debugPaintBackground.setColor(Color.DKGRAY);
    }

    @Override
    public void run() {
        if (surfaceHolder != null) {
            try {
                long localTotalTime;
                try {
                    localTotalTime = System.currentTimeMillis();
                    cycles++;

                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        long localRenderTime = System.currentTimeMillis();
                        worldRenderer.apply(canvas);
                        long renderTime = System.currentTimeMillis() - localRenderTime;
                        if (configuration.isDebug()) {
                            renderDebug(canvas, totalTime, renderTime, cycles);
                        }
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
                totalTime = System.currentTimeMillis() - localTotalTime;
            } catch (IllegalArgumentException e) {
                //Ignore
            } catch (IllegalStateException e) {
                surfaceHolder = null;
            }
        }
    }

    private void renderDebug(Canvas c, long totalTime, long renderTime, long cycles) {
        float x = 50f;
        float y = configuration.getScreenSize().y - 150f;

        c.drawRect(x, y, x + 200f, y - 20f, debugPaintBackground);
        c.drawText(Long.toString(totalTime), x, y, debugPaintForeground);
        c.drawText(Long.toString(renderTime), x + 60f, y, debugPaintForeground);
        c.drawText(Long.toString(cycles), x + 120f, y, debugPaintForeground);
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
