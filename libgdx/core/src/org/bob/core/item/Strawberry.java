package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.SpriteFactory;

/**
 * Created by jeff on 19/06/16.
 */
public class Strawberry extends Item {

    public static final String SPRITE_ID = "strawberry";
    public float radius = 50f;


    public Strawberry(World world, SpriteFactory spriteFactory, Vector2 position, float scale) {
        super(world, spriteFactory, scale, position);

        create(world, spriteFactory);
    }


    public void create(World world, SpriteFactory spriteFactory) {

        sprite = spriteFactory.get(Strawberry.SPRITE_ID);

        float density = 1f;
        float friction = 0.9f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        Vector2[] vertices = new Vector2[9];

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
