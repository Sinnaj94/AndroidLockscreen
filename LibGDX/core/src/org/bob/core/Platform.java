package org.bob.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Platform body. Used by box2d as the main ground
 * for keeping all the objects on the screens.
 *
 * Created by Jannis on 19.06.2016.
 */
public class Platform  {

    /*
     * Fields
     */
    public Body body;
    public ShapeRenderer renderer;
    public float positionY;
    public float width;

    /**
     *
     * Constructor
     *
     * @param world the world
     * @param camera the camera
     * @param positionY the y position
     * @param width the width
     */
    public Platform(World world, Camera camera, float positionY, float width){
        this.positionY = positionY;
        this.width = width;

        create(world, camera);
    }


    /**
     * Create all necessary objects and init body physics
     *
     * @param world the world
     * @param camera the camera
     */
    public void create(World world, Camera camera){

        // Used for drawing
        renderer = new ShapeRenderer();

        // Box2d body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Set friction
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        // Create 1 px polygon shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);
        fixtureDef.shape = shape;

        // Attach to world
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        // Set position
        body.setTransform(0, positionY, 0);

        // Restore
        shape.dispose();
    }

    /**
     * Cleanup
     */
    public void dispose() {
        if(renderer != null) {
            renderer.dispose();
            renderer = null;
        }

        body = null;
    }
}
