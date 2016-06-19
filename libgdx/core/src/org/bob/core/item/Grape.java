package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by jeff on 19/06/16.
 */
public class Grape extends Item {

    public static final String SPRITE_ID = "grape";
    public Sprite sprite;

    public Grape(World world,Sprite sprite, float scale){
        super(scale);

        this.sprite = sprite;

        create(world);
    }

    public void create(World world){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(100, 300);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(50.010f * scale);
        circle.setPosition(new Vector2(-0.500f,0f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        //fixtureDef.density = 0.5f;
        //fixtureDef.friction = 0.4f;
        //fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }


    public void render(SpriteBatch batch) {

        // get the position of the fruit from Box2D
        Vector2 position = body.getPosition();
        System.out.println(position);

        // get the degrees of rotation by converting from radians
        float degrees = (float) Math.toDegrees(body.getAngle());

        sprite.setPosition(position.x-(50.010f * scale), position.y-(50.010f * scale));
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }
}
