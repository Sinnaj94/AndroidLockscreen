package org.bob.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created by jeff on 16/06/16.
 */
public class Game extends InputAdapter implements ApplicationListener {

    private float accumulator = 0;
    public static final float TIME_STEP = 1/300f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    public static final int width = 1080;
    public static final int height = 1920;


    SpriteBatch batch;
    Viewport viewport;
    Camera camera;
    Bob bob;
    Stage stage;
    World world;
    Box2DDebugRenderer debugRenderer;

    private Array<Sprite> bears;

    @Override
    public void create() {

        // Box2d
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new PerspectiveCamera();
        viewport = new StretchViewport(1080, 1920, camera);
        stage = new Stage(viewport);
        //INPUT

        //ACTOR BOB **
        bob = new Bob();
        //INPUT
        //** ACTOR BOB


        stage.addActor(bob);
        batch = new SpriteBatch();


    }




    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
