package org.bob.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jannis on 19.06.2016.
 */
public class Platform  {

    public Body body;
    public ShapeRenderer renderer;
    public float positionY;
    public float width;

    public Platform(World world, Camera camera, float positionY, float width){
        this.positionY = positionY;
        this.width = width;

        create(world, camera);
    }

    public void create(World world, Camera camera){

        renderer = new ShapeRenderer();

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);

        fixtureDef.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setTransform(0, positionY, 0);

        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
/*
        renderer.begin(ShapeRenderer.ShapeType.Line); // shape type
        renderer.setColor(1, 0.5F, 0.5F, 1); // line's color
        renderer.line(position.x, position.y, touchPos.x, touchPos.y);  // shape's set of coordinates (x1,y1,x2,y2)
        renderer.end();
*/
    }

    public void dispose() {
        if(renderer != null) {
            renderer.dispose();
            renderer = null;
        }

        body = null;
    }
}
