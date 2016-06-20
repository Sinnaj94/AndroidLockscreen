package org.bob.core.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.BodyEditorLoader;
import org.bob.core.SpriteFactory;

/**
 * Created by jeff on 19/06/16.
 */
public class Weinflasche extends Item {

    public static final String SPRITE_ID = "weinflasche";


    public Weinflasche(World world, SpriteFactory spriteFactory, BodyEditorLoader bel, Vector2 position, float scale) {
        super(world, spriteFactory, scale, position);

        create(world, spriteFactory, bel);
    }


    public void create(World world, SpriteFactory spriteFactory, BodyEditorLoader bel) {

        sprite = spriteFactory.get(Weinflasche.SPRITE_ID);

        float density = 1f;
        float friction = 0.9f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;

        bel.attachFixture(body, SPRITE_ID, fixtureDef, sprite.getWidth());
    }
}
