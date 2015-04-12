package com.abk.lw.starter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.abk.lw.starter.api.WallpaperRenderer;


/**
 * An example renderer that just increments a counter on the canvas.  The counter is offset based on the the offset of the wallpaper.
 * Created by kgilmer on 4/8/15.
 */
public class CounterRenderer implements WallpaperRenderer {

    private final com.abk.lw.starter.UserConfiguration state;

    public CounterRenderer(UserConfiguration state) {
        this.state = state;
    }

    @Override
    public boolean apply(Canvas c) {
        Paint backgroundPaint = state.getPaintMap().get(UserConfiguration.BACKGROUND_PAINT_KEY);
        Paint foregroundPaint = state.getPaintMap().get(UserConfiguration.FOREGROUND_PAINT_KEY);

        int value = state.getValue();

        float startX = 400f + (float) state.getPixels().x;

        c.drawRect(0f, 0f, state.getScreenSize().x, state.getScreenSize().y, backgroundPaint);
        c.drawText(Integer.toString(value), startX, 300f, foregroundPaint);

        state.setValue(value + 1);

        return true;
    }
}
