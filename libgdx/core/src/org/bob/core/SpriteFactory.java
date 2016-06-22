package org.bob.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import org.bob.core.item.Coconut;
import org.bob.core.item.Grape;
import org.bob.core.item.Strawberry;
import org.bob.core.item.Weinflasche;
import org.bob.core.item.Wheel;

import java.util.HashMap;

/**
 * Created by jeff on 19/06/16.
 */
public class SpriteFactory {

    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    TextureAtlas textureAtlas;

    public SpriteFactory() {

        loadSprites();
    }

    private void loadSprites() {

        textureAtlas = new TextureAtlas("sprite/game.atlas");

        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float scale = 1f;

            if (region.name.equals(Coconut.SPRITE_ID)) {
                scale = scale * Coconut.SCALE;
            } else if (region.name.equals(Grape.SPRITE_ID)) {
                scale = scale * Grape.SCALE;
            } else if (region.name.equals(Wheel.SPRITE_ID)) {
                scale = scale * Wheel.SCALE;
            } else if (region.name.equals(Weinflasche.SPRITE_ID)) {
                scale = scale * Weinflasche.SCALE;
            } else if (region.name.equals(Strawberry.SPRITE_ID)) {
                scale = scale * Strawberry.SCALE;
            }

            float width = sprite.getWidth() * scale;
            float height = sprite.getHeight() * scale;

            sprite.setSize(width, height);
            sprite.setOrigin(width / 2, height / 2);

            sprites.put(region.name, sprite);
        }
    }

    public Sprite get(String spriteId) {
        return sprites.get(spriteId);
    }

    public void dispose() {
        sprites.clear();
        if(textureAtlas != null){
            textureAtlas.dispose();
            textureAtlas = null;
        }
    }
}
