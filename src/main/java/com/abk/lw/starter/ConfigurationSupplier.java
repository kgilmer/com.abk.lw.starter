package com.abk.lw.starter;

import android.graphics.Color;
import android.graphics.Paint;

import com.google.common.base.Supplier;

/**
 * An example configuration supplier that returns an instance of the sample UserConfiguration.
 *
 * Created by kgilmer on 4/11/15.
 */
public class ConfigurationSupplier implements Supplier<UserConfiguration> {

    @Override
    public UserConfiguration get() {
        final UserConfiguration c = new UserConfiguration();

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(40f);
        c.getPaintMap().put(UserConfiguration.FOREGROUND_PAINT_KEY, p);
        p = new Paint();
        p.setColor(Color.BLUE);
        c.getPaintMap().put(UserConfiguration.BACKGROUND_PAINT_KEY, p);

        //Setting debug to true will cause total render time, render time, and frame count to be overlayed on wallpaper.
        c.setDebug(true);

        return c;
    }
}
