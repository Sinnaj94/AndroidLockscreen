package org.bob.core.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    public Sprite sprite;
    public float radius = 50f;


    public Grape(World world, SpriteFactory spriteFactory, Vector2 position, float scale){
        super(scale, position);

        create(world, spriteFactory);
    }



    public void create(World world, SpriteFactory spriteFactory){

        this.sprite = spriteFactory.get(Grape.SPRITE_ID);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        //fixtureDef.density = 5f;
        //fixtureDef.friction = 0.4f;
        //fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }


    public void render(SpriteBatch batch) {

        // get the position of the fruit from Box2D
        position = body.getPosition();

        // get the degrees of rotation by converting from radians
        float degrees = (float) Math.toDegrees(body.getAngle());

        sprite.setPosition(position.x, position.y);
        sprite.setRotation(degrees);
        sprite.draw(batch);


        if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        shapeRenderer.end();
    }
}
