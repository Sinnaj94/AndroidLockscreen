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
 * Managing all spawnable game sprites.
 * Loads sprites from texture atlas and scales
 * the sprites to their correct size.
 *
 * Created by jeff on 19/06/16.
 */
public class SpriteFactory {

    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    TextureAtlas textureAtlas;

    public SpriteFactory() {
        loadSprites();
    }

    /**
     * Loads sprites from texture atlas file.
     */
    private void loadSprites() {

        // Load combined sprites
        textureAtlas = new TextureAtlas("sprite/game.atlas");

        // Split sprites
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        // Instantiate all sprites with their specified scale
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

            sprite.setSize(width, height); // Set scaled size
            sprite.setOrigin(width / 2, height / 2); // Set origin in the middle

            sprites.put(region.name, sprite); // Add to list
        }
    }

    /**
     * Getter for returning the sprite
     * by its key.
     *
     * @param spriteId the sprite Id
     * @return the sprite or null
     */
    public Sprite get(String spriteId) {
        return sprites.get(spriteId);
    }

    /**
     * Cleanup
     */
    public void dispose() {
        sprites.clear();
        if(textureAtlas != null){
            textureAtlas.dispose();
            textureAtlas = null;
        }
    }
}
