package org.bob.core.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.BodyEditorLoader;
import org.bob.core.SpriteFactory;

/**
 * Strawberry game item.
 * <p/>
 * Class is a implementation of the
 * abstract Item class. It can be used
 * to specify an object which can be spawned
 * on the screen.
 * <p/>
 * <p/>
 * Created by jeff on 19/06/16.
 */
public class Strawberry extends Item {

    /*
     * Fields
     */

    public static final float SCALE = 0.5f;
    public static final String SPRITE_ID = "strawberry";

    /**
     * Constructor
     *
     * @param world         the box2d world
     * @param spriteFactory the sprite factory
     * @param position      the position of the item
     */
    public Strawberry(World world, SpriteFactory spriteFactory, BodyEditorLoader bel, Vector2 position) {
        super(world, spriteFactory, position);

        create(world, spriteFactory, bel);
    }


    @Override
    public void create(World world, SpriteFactory spriteFactory, BodyEditorLoader bel) {

        // Load sprite
        sprite = spriteFactory.get(Strawberry.SPRITE_ID);

        // Add body definition and position
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        // Attach body to world
        body = world.createBody(bodyDef);

        // Specify physics attribute
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        // Load bounding polygon fron json
        bel.attachFixture(body, SPRITE_ID, fixtureDef, sprite.getWidth());
    }

}
