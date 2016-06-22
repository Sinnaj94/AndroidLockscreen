package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.BodyEditorLoader;
import org.bob.core.SpriteFactory;

/**
 * Abstract class represents a game item.
 * Items can be spawned in the game.
 *
 * Created by jeff on 16/06/16.
 */
public abstract class Item {

    /*
     * Fields
     */
    public Body body;
    public float scale;
    public Vector2 position;
    public Sprite sprite;

    /**
     * Constructor
     *
     * @param world the world
     * @param spriteFactory the sprite factory
     * @param position the position of the item
     */
    public Item(World world, SpriteFactory spriteFactory, Vector2 position) {
        this.position = position;
    }

    /**
     * Create game object.
     *
     * @param world the world
     * @param spriteFactory the sprite factory
     */
    public void create(World world, SpriteFactory spriteFactory) {
    }

    /**
     * Create game object.
     *
     * @param world the world
     * @param spriteFactory the sprite factory
     * @param bel physics meta information
     */
    public void create(World world, SpriteFactory spriteFactory, BodyEditorLoader bel) {
    }

    /**
     * Render item on the screen.
     * @param batch the sprite batch
     */
    public void render(SpriteBatch batch) {

        // Position to draw the item
        position = body.getPosition();

        // get the degrees of rotation by converting from radians
        float degrees = (float) Math.toDegrees(body.getAngle());

        // Set the position at the sprite
        sprite.setPosition(position.x-(sprite.getWidth()/2), position.y - (sprite.getHeight()/2));
        sprite.setRotation(degrees); // Rotate sprite
        sprite.draw(batch); // Draw sprite at batch

    }

}



