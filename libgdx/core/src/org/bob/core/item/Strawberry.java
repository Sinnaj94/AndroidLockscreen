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
public class Strawberry extends Item {

    public static final float SCALE = 0.5f;
    public static final String SPRITE_ID = "strawberry";


    public Strawberry(World world, SpriteFactory spriteFactory, BodyEditorLoader bel, Vector2 position) {
        super(world, spriteFactory, position);

        create(world, spriteFactory, bel);
    }


    public void create(World world, SpriteFactory spriteFactory, BodyEditorLoader bel) {

        sprite = spriteFactory.get(Strawberry.SPRITE_ID);

        float density = 1f;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;

        bel.attachFixture(body, SPRITE_ID, fixtureDef, sprite.getWidth());
    }

}
