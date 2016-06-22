package org.bob.core.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.SpriteFactory;

/**
 * Wheel game item.
 * <p/>
 * Class is a implementation of the
 * abstract Item class. It can be used
 * to specify an object which can be spawned
 * on the screen.
 * <p/>
 * <p/>
 * Created by jeff on 19/06/16.
 */
public class Wheel extends Item {

    /*
     * Fields
     */

    public static final float SCALE = 1.6f;
    public static final String SPRITE_ID = "autoreifen";

    /**
     * Constructor
     *
     * @param world         the box2d world
     * @param spriteFactory the sprite factory
     * @param position      the position of the item
     */
    public Wheel(World world, SpriteFactory spriteFactory, Vector2 position) {
        super(world, spriteFactory, position);

        create(world, spriteFactory);
    }

    @Override
    public void create(World world, SpriteFactory spriteFactory) {

        sprite = spriteFactory.get(Wheel.SPRITE_ID);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(sprite.getWidth() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }


}
