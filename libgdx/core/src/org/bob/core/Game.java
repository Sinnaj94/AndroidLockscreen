package org.bob.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.bob.core.item.Coconut;
import org.bob.core.item.Grape;
import org.bob.core.item.Item;
import org.bob.core.item.Wheel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Created by jeff on 16/06/16.
 */
public class Game extends InputAdapter implements ApplicationListener {

    private float accumulator = 0;
    public static final float TIME_STEP = 1 / 300f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    public static final int width = 1080 / 2;
    public static final int height = 1920 / 2;

    public static final float SCALE = 1.05f;

    public Viewport viewport;
    public Camera camera;

    public World world;
    public Box2DDebugRenderer debugRenderer;

    private SpriteFactory spriteFactory;
    public SpriteBatch batch;

    // Game Objects
    public Stage stage;
    public Bob bob;
    public Platform platform;
    public List<Item> items;


    @Override
    public void create() {

        spriteFactory = new SpriteFactory(SCALE);

        items = new LinkedList<Item>();

        // Physics
        Box2D.init();

        world = new World(new Vector2(0, -120), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera();

        // Viewport
        viewport = new ExtendViewport(width, height, camera);

        // Stage
        stage = new Stage(viewport);

        // Create game objects
        bob = new Bob(world, camera);
        stage.addActor(bob);
        platform = new Platform(world, camera);

        // Batch
        batch = new SpriteBatch();

        doSpawnItems(10);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        if(bob.isWaitingForAction()){

        }


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        debugRenderer.render(world, camera.combined);

        batch.begin();

        // Items
        for (Item item : items)
            item.render(batch);

        batch.end();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
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
        world.dispose();
        debugRenderer.dispose();
    }

    public void doSpawnItems(int count) {

        Random random = new Random();

        Item item;

        for (int i = 0; i <= count; i++) {

            float x = random.nextFloat() * Game.width;
            float y = ((random.nextFloat() * Game.height) / 2) + Game.height;

            Vector2 position = new Vector2(x, y);

            switch (random.nextInt(3)) {
                case (0):
                    item = new Grape(world, spriteFactory, position, SCALE);
                    break;
                case (1):
                    item = new Coconut(world, spriteFactory, position, SCALE);
                    break;
                case (2):
                    item = new Wheel(world, spriteFactory, position, SCALE);
                    break;
                default:
                    item = null;

            }
            if (item != null)
                items.add(item);
        }
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

}
