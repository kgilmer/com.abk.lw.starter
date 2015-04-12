package com.abk.lw.starter.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.abk.lw.starter.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * This is the settings activity for the wallpaper.
 */
public class WallpaperSettings extends PreferenceActivity {

    public static class SettingsLoadedEvent {};

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                WallpaperService.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.settings);

        WallpaperApplication application = (WallpaperApplication) getApplication();
        application.getApplicationGraph().inject(this);

        bus.post(new SettingsLoadedEvent());
    }
}
