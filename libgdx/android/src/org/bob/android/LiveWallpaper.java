package org.bob.android;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

import org.bob.core.Game;

/**
 * Entry point for android wallpaper service.
 * Class inherits from WallpaperService and libgdx AndroidLiveWallpaperService.
 * AndroidLiveWallpaperService adds special support for libgdx Android-Wallpapers.
 * <p/>
 * Created by jeff on 09/06/16.
 */
public class LiveWallpaper extends AndroidLiveWallpaperService {

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Game(), config);
    }
}
