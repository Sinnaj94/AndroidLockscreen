package org.bob.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import org.bob.core.Game;

import static com.badlogic.gdx.tools.texturepacker.TexturePacker.process;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.width/2;
		config.height = Game.height/2;

		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 512;
		settings.maxHeight = 512;
		process(settings, "gfx/texturepack", "sprite", "game");

		new LwjglApplication(new Game(), config);
	}
}
