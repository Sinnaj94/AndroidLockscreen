package com.mygdx.game;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

/**
 * Created by jeff on 09/06/16.
 */
public class LiveWallpaper extends AndroidLiveWallpaperService {

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Flap(), config);
    }
}
