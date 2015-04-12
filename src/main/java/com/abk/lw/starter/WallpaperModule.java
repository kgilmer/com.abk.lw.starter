package com.abk.lw.starter;

import android.content.Context;

import com.abk.lw.starter.android.WallpaperService;
import com.abk.lw.starter.android.WallpaperSettings;
import com.abk.lw.starter.api.ConfigurationMapper;
import com.abk.lw.starter.api.WallpaperRenderer;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This Dagger module provides all of the components that are injected into the Wallpaper Engine at start time.
 * Modify this class according to your needs.
 *
 */
@Module(injects = {WallpaperSettings.class, WallpaperService.class})
public class WallpaperModule {

    private final Context context;

    public WallpaperModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton Bus provideBus() {
        return new Bus();
    }

    @Provides @Singleton
    UserConfiguration provideUserConfiguration() {
        return (new ConfigurationSupplier()).get();
    }
    @Provides
    ConfigurationMapper provideConfigurationMapper(UserConfiguration uc) {
        return new SettingsConfigurationMapper(uc);
    }

    @Provides
    WallpaperRenderer provideWallpaperRenderer(UserConfiguration uc) {
        return new CounterRenderer(uc);
    }

    @Provides @Named("serializer")
    Runnable provideSerializer(final UserConfiguration uc) {
        return new StateSerializer(context, uc);
    }

    @Provides @Named("deserializer")
    Runnable provideDeserializer(final UserConfiguration uc) {
        return new StateDeserializer(context, uc);
    }
}
