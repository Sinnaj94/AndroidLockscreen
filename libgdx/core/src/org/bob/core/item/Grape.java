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
public class Grape extends Item {

    public static final String SPRITE_ID = "grape";
    public float radius = 50f;


    public Grape(World world, SpriteFactory spriteFactory, Vector2 position, float scale){
        super(world, spriteFactory, scale, position);

        create(world, spriteFactory);
    }



    public void create(World world, SpriteFactory spriteFactory){

        sprite = spriteFactory.get(Grape.SPRITE_ID);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0.2f; // Make it bounce a little bit


        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }



}
