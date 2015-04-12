package com.abk.lw.starter;

import android.graphics.Color;
import android.graphics.Paint;

import com.abk.lw.starter.android.WallpaperService;
import com.abk.lw.starter.api.ConfigurationMapper;

/**
 * An example mapper that updates the UserConfiguration based on choices the user makes in the Settings activity.
 *
 * Created by kgilmer on 4/10/15.
 */
public class SettingsConfigurationMapper implements ConfigurationMapper {

    private final UserConfiguration userConfiguration;

    public SettingsConfigurationMapper(UserConfiguration c) {
        this.userConfiguration = c;
    }

    @Override
    public boolean apply(WallpaperService.SettingsSelection s) {
        //The selection will give us the key of the user preference that has changed.
        if (s.key.equals("start")) {
            //User set the start from value in the wallpaper settings.
            userConfiguration.setValue(Integer.parseInt(s.prefs.getString(s.key, "0")));
        } else if (s.key.equals("theme")) {
            //User set the color theme.
            String theme = s.prefs.getString(s.key, "Light");
            if (theme.equals("Light")) {
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(40f);
                userConfiguration.getPaintMap().put(UserConfiguration.FOREGROUND_PAINT_KEY, p);
                p = new Paint();
                p.setColor(Color.BLUE);
                userConfiguration.getPaintMap().put(UserConfiguration.BACKGROUND_PAINT_KEY, p);
            } else if (theme.equals("Dark")) {
                Paint p = new Paint();
                p.setColor(Color.BLACK);
                p.setTextSize(40f);
                userConfiguration.getPaintMap().put(UserConfiguration.FOREGROUND_PAINT_KEY, p);
                p = new Paint();
                p.setColor(Color.YELLOW);
                userConfiguration.getPaintMap().put(UserConfiguration.BACKGROUND_PAINT_KEY, p);
            } else {
                throw new IllegalArgumentException("Unknown theme string.  Check settings.xml");
            }
        } else if (s.key.equals("speed")) {
            //User set the speed.
            String speed = s.prefs.getString(s.key, "Slow");
            if (speed.equals("Slow")) {
                userConfiguration.setFrameDelayMillis(250);
            } else if (speed.equals("Fast")) {
                userConfiguration.setFrameDelayMillis(50);
            } else {
                throw new IllegalArgumentException("Unknown settings key.  Check settings.xml.");
            }
        } else {
            throw new IllegalArgumentException("Unknown settings key.  Check settings.xml.");
        }

        return true;
    }
}
