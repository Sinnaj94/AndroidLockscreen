package org.bob.core.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.bob.core.SpriteFactory;

/**
 * Created by jeff on 19/06/16.
 */
public class Wheel extends Item {

    public static final String SPRITE_ID = "autoreifen";
    public float radius = 50f;


    public Wheel(World world, SpriteFactory spriteFactory, Vector2 position, float scale){
        super(world, spriteFactory, scale, position);

        create(world, spriteFactory);
    }



    public void create(World world, SpriteFactory spriteFactory){

        sprite = spriteFactory.get(Wheel.SPRITE_ID);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0.9f;
        //fixtureDef.density = 5f;
        fixtureDef.restitution = 0.8f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }



}
