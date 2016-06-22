package org.bob.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.bob.core.item.Coconut;
import org.bob.core.item.Grape;
import org.bob.core.item.Item;
import org.bob.core.item.Strawberry;
import org.bob.core.item.Weinflasche;
import org.bob.core.item.Wheel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Main Application class.
 * Manage all the game logic and the drawing.
 * <p/>
 * <p/>
 * Created by jeff on 16/06/16.
 */
public class Game extends InputAdapter implements ApplicationListener {

    /*
     * Fields
     */

    private float accumulator = 0;
    public static final float TIME_STEP = 1 / 300f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    public float width;
    public float height;
    Texture backgroundImage;
    Sprite backgroundSprite;
    public Viewport viewport;
    public Camera camera;
    public World world;
    public Box2DDebugRenderer debugRenderer;
    private SpriteFactory spriteFactory;
    private BodyEditorLoader physicsLoader;
    public SpriteBatch batch;
    public Stage stage;
    public Bob bob;
    public Platform platform;
    public List<Item> items;
    public GestureDetector gestureDetector;


    /*
     * Overriden Methods
     */

    @Override
    public void create() {

        // Physics
        Box2D.init();

        // Load background image and set as sprite
        backgroundImage = new Texture(Gdx.files.internal("gfx/background.png"));
        backgroundSprite = new Sprite(backgroundImage);

        // Create sprite factory, used for creating all spawned items.
        spriteFactory = new SpriteFactory();

        // Instantiate list for spawned items
        items = new LinkedList<Item>();

        // Load physics body information from json
        physicsLoader = new BodyEditorLoader(Gdx.files.internal("data/physic_bodies.json"));

        // Create libgdx world
        world = new World(new Vector2(0, -320), true);

        // Create box2d debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // Create camera
        camera = new OrthographicCamera();

        // Create viewport
        viewport = new ScreenViewport(camera);

        // Create stage
        stage = new Stage(viewport);

        // Create SpriteBatch
        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {

        // Set screen dimensions
        viewport.update(width, height);
        batch.setProjectionMatrix(camera.combined);
        this.width = width;
        this.height = height;


        // Create static platform where bob walks
        if (platform == null) {
            this.platform = new Platform(world, camera, ((float) height) * 0.13f, width);
        }

        // Create bob
        if (bob == null) {
            bob = new Bob(this, world, camera);
            stage.addActor(bob);
        }


        // Set input listener
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(new MyGestureListener(bob, this));
            Gdx.input.setInputProcessor(gestureDetector);
        }
    }

    @Override
    public void render() {

        // Update logic
        update();

        // Clean screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update scene2d
        stage.act(Gdx.graphics.getDeltaTime());

        // Prepare patch for drawing
        batch.begin();

        // Draw items
        batch.draw(backgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        for (Item item : items)
            item.render(batch);
        batch.end();
        stage.draw();

        //debugRenderer.render(world, camera.combined);

        // Update box2d world
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

        /*
         * Cleanup resources
         */

        stage.dispose();
        stage = null;
        world.dispose();
        world = null;
        platform.dispose();
        platform = null;
        debugRenderer.dispose();
        debugRenderer = null;
        bob.dispose();
        spriteFactory.dispose();
        spriteFactory = null;
        viewport = null;
        camera = null;
        physicsLoader = null;
        gestureDetector = null;
    }

    /**
     * Update the logic of the game and
     * cleanup unused objects.
     */
    private void update() {

        // Check if item is outside the screen
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {

            Item item = iterator.next();

            float leftEdge = item.body.getPosition().x + (item.sprite.getWidth() / 2);
            float rightEdge = item.body.getPosition().x - (item.sprite.getWidth() / 2);

            boolean remove = false;
            if (leftEdge < 0) {
                remove = true; // left from the screen
            } else if (rightEdge > camera.viewportWidth) {
                remove = true; // right from the screen
            }

            // Item outside?
            if (remove) {
                // Remove item from world
                world.destroyBody(item.body);
                // Remove from list
                iterator.remove();
            }

        }
    }

    /**
     * Spawn random objects. Use count to specify the number of items to spawn
     * and postion to choose a postition to spawn. Keep positon null to use a random position
     *
     * @param count    number of items to spawn
     * @param position the position to spawn the objects, null for random position above the screen.
     */
    public void doSpawnItems(int count, Vector2 position) {

        // Create a random
        Random random = new Random();

        Item item;

        for (int i = 0; i < count; i++) {

            // if position not set use random positon
            if (position == null) {
                float x = random.nextFloat() * width;
                float y = ((random.nextFloat() * height) / 2) + height;

                position = new Vector2(x, y);
            }

            // set random item
            switch (random.nextInt(5)) {
                case (0):
                    item = new Coconut(world, spriteFactory, position);
                    break;
                case (1):
                    item = new Strawberry(world, spriteFactory, physicsLoader, position);
                    break;
                case (2):
                    item = new Wheel(world, spriteFactory, position);
                    break;
                case (3):
                    item = new Weinflasche(world, spriteFactory, physicsLoader, position);
                    break;
                case (4):
                    item = new Grape(world, spriteFactory, position);
                    break;
                default:
                    item = null;

            }
            if (item != null) {
                // add item
                items.add(item);
            }
        }
    }

    /**
     * Update box2d world.
     *
     * @param deltaTime elapsed time sind last draw.
     */
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
