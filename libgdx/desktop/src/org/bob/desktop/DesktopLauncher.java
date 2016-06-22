package org.bob.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import org.bob.core.Game;

import static com.badlogic.gdx.tools.texturepacker.TexturePacker.process;

/**
 * Desktop game launcher.
 */
public class DesktopLauncher {

    /**
     * Main game launcher function.
     *
     * @param arg arguments
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1080 / 2;
        config.height = 1920 / 2;

        // Pack sprite to texture
        TexturePacker.Settings settings = new TexturePacker.Settings();
        process(settings, "gfx/texturepack", "sprite", "game");

        // Launch desktop game
        new LwjglApplication(new Game(), config);
    }
}
