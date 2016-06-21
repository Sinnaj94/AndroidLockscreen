package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.BodyEditorLoader;
import org.bob.core.SpriteFactory;

/**
 * Created by jeff on 16/06/16.
 */
public class Item {

    public Body body;
    public float scale;
    public Vector2 position;
    public Sprite sprite;

    public Item(World world, SpriteFactory spriteFactory, Vector2 position) {
        this.position = position;
    }

    public void create(World world, SpriteFactory spriteFactory) {
    }

    public void create(World world, SpriteFactory spriteFactory, BodyEditorLoader bel) {
    }

    public void render(SpriteBatch batch) {

        position = body.getPosition();

        // get the degrees of rotation by converting from radians
        float degrees = (float) Math.toDegrees(body.getAngle());

        sprite.setPosition(position.x-(sprite.getWidth()/2), position.y - (sprite.getHeight()/2));
        sprite.setRotation(degrees);
        sprite.draw(batch);

    }


}



