package com.abk.lw.starter.api;

import com.abk.lw.starter.android.WallpaperService;
import com.google.common.base.Predicate;

/**
 * Implementations map user preference key value pair changes onto the UserConfiguration class.
 *
 * Implementations should be modified when Wallpaper settings or the UserConfiguration class is changed.
 */
public interface ConfigurationMapper extends Predicate<WallpaperService.SettingsSelection> {
}
