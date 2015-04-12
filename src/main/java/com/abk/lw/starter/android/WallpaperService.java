package com.abk.lw.starter.android;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.abk.lw.starter.UserConfiguration;
import com.abk.lw.starter.api.ConfigurationMapper;
import com.abk.lw.starter.api.WallpaperConfiguration;
import com.abk.lw.starter.api.WallpaperRenderer;
import com.google.common.base.Predicate;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Primary entrypoint of wallpaper.  Should not need to be modified.
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {
    public static final String SHARED_PREFS_NAME = WallpaperService.class.getName();

    @Inject Bus eventBus;
    @Inject
    ConfigurationMapper configMapper;
    @Inject
    UserConfiguration userConfig;
    @Inject
    WallpaperRenderer wallpaperRenderer;
    @Inject @Named("serializer") Runnable serializer;
    @Inject @Named("deserializer") Runnable deserializer;

    /**
     * Tracks the special condition in which the settings activities is in the foreground.
     * The desired behavior is that the wallpaper continues to execute so that the user
     * can see how settings changes affect it.
     */
    protected static boolean settingsActivityForeground = false;

    /**
     * Discrete events that occur and may be of interest when rendering a frame.
     */
    public enum UI_EVENT {
        OFFSET_CHANGED, FIRST_RENDER, TOUCH_EVENT;
    }

    /**
     * Wraps user preferences and a specified key.
     */
    public final class SettingsSelection {
        public final SharedPreferences prefs;
        public final String key;

        public SettingsSelection(SharedPreferences prefs, String key) {
            this.prefs = prefs;
            this.key = key;
        }
    }

    @Override
    public Engine onCreateEngine() {
        WallpaperApplication application = (WallpaperApplication) getApplication();
        application.getApplicationGraph().inject(this);

        //configuration
        SimpleEngine engine = new SimpleEngine(userConfig, wallpaperRenderer, configMapper, serializer, deserializer);
        eventBus.register(engine);

        return engine;
    }

    class SimpleEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {
        private final Handler handler = new Handler();
        private final WallpaperConfiguration userConfiguration;
        private final Predicate<SettingsSelection> settingsMapper;
        private final Runnable serializer;
        private final Runnable deserializer;
        private final HolderRenderer surfaceRenderer;
        private final SharedPreferences userPrefs;

        public SimpleEngine(WallpaperConfiguration c, Predicate<Canvas> renderer, Predicate<SettingsSelection> settingsMapper, Runnable serializer, Runnable deserializer) {
            this.userConfiguration = c;
            this.settingsMapper = settingsMapper;
            this.serializer = serializer;
            this.deserializer = deserializer;
            this.userPrefs = getSharedPreferences(SHARED_PREFS_NAME, 0);

            //Create debug renderer if requested in configuration.  For performance this cannot be changed at runtime.
            if (userConfiguration.isDebug()) {
                this.surfaceRenderer = new DebugSurfaceRenderer(handler, renderer, userConfiguration);
            } else {
                this.surfaceRenderer = new SurfaceRenderer(handler, renderer, userConfiguration);
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);

            this.surfaceRenderer.surfaceCreated(surfaceHolder);

            //Initialize state from prefs
            for (String key : userPrefs.getAll().keySet()) {
                settingsMapper.apply(new SettingsSelection(userPrefs, key));
            }
            userPrefs.registerOnSharedPreferenceChangeListener(this);

            if (deserializer != null) {
                deserializer.run();
            }

            userConfiguration.getUiEvents().add(UI_EVENT.FIRST_RENDER);
            surfaceRenderer.run();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            userPrefs.unregisterOnSharedPreferenceChangeListener(this);
            handler.removeCallbacks(surfaceRenderer);

            if (serializer != null) {
                serializer.run();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.surfaceRenderer.surfaceCreated(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                surfaceRenderer.run();
            } else if (!settingsActivityForeground) {
                //Only cancel the rendering if the settings activity is not in the background.
                handler.removeCallbacks(surfaceRenderer);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            userConfiguration.getScreenSize().set(width, height);
            surfaceRenderer.surfaceChanged(holder, format, width, height);

            surfaceRenderer.run();
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            surfaceRenderer.surfaceDestroyed(holder);
            handler.removeCallbacks(surfaceRenderer);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            userConfiguration.setLastTouchEvent(event);
            userConfiguration.addEvent(UI_EVENT.TOUCH_EVENT);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            userConfiguration.getOffset().set(xOffset, yOffset);
            userConfiguration.getStep().set(xStep, yStep);
            userConfiguration.getPixels().set(xPixels, yPixels);
            userConfiguration.addEvent(UI_EVENT.OFFSET_CHANGED);
        }

        /* (non-Javadoc)
         * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
         */
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences prefs, final String key) {
            settingsMapper.apply(new SettingsSelection(prefs, key));
            surfaceRenderer.run();
        }

        @Subscribe public void render(WallpaperSettings.SettingsLoadedEvent event) {
            surfaceRenderer.run();
        }
    }
}
