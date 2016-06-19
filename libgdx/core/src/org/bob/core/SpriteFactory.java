package org.bob.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by jeff on 19/06/16.
 */
public class SpriteFactory {

    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    TextureAtlas textureAtlas;
    float scale;

    public SpriteFactory(float scale){
        this.scale = scale;

        loadSprites();
    }

    private void loadSprites() {

        textureAtlas = new TextureAtlas("sprite/game.atlas");

        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float width = sprite.getWidth() * scale;
            float height = sprite.getHeight() * scale;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    public Sprite get(String spriteId) {
        return sprites.get(spriteId);
    }
}
