package org.bob.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hi5dev.box2d_pexml.PEXML;

import org.bob.core.item.Grape;

import java.util.HashMap;
import java.util.Random;


/**
 * Created by jeff on 16/06/16.
 */
public class Game extends InputAdapter implements ApplicationListener {

    private float accumulator = 0;
    public static final float TIME_STEP = 1/300f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    public static final int width = 1080/2;
    public static final int height = 1920/2;

    /**
     * Adjust this value to change the amount of fruit that falls from the sky.
     */

    static final int COUNT = 25;
    Body[] fruitBodies = new Body[COUNT];
    public static final float SCALE = 1.05f;
    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    Sprite[] fruitSprites = new Sprite[COUNT];
    TextureAtlas textureAtlas;

    public SpriteBatch batch;
    public Viewport viewport;
    public Camera camera;

    public Stage stage;
    public World world;
    public Box2DDebugRenderer debugRenderer;
    private PEXML physicsBodies;

    private Array<Sprite> bears;

    private SpriteFactory spriteFactory;



    // Game Objects
    public Bob bob;
    public Platform platform;
    public Grape grape;


    @Override
    public void create() {

        SpriteFactory spriteFactory = new SpriteFactory(SCALE);

        // Physics
        Box2D.init();
        physicsBodies = new PEXML(Gdx.files.internal("physics_old.xml").file());
        world = new World(new Vector2(0, -120), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera();

        // Viewport
        viewport = new StretchViewport(width, height, camera);

        // Stage
        stage = new Stage(viewport);

        // Create game objects

        bob = new Bob();
        stage.addActor(bob);
        platform = new Platform(world,camera);
        grape = new Grape(world,spriteFactory.get(Grape.SPRITE_ID),SCALE);

        // Batch
        batch = new SpriteBatch();

        textureAtlas = new TextureAtlas("sprites.txt");

        loadSprites();

        generateFruit();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        debugRenderer.render(world, camera.combined);


        batch.begin();

        grape.render(batch);

        // iterate through each of the fruits
        for (int i = 0; i < fruitBodies.length; i++) {

            // get the physics body of the fruit
            Body body = fruitBodies[i];

            // get the position of the fruit from Box2D
            Vector2 position = body.getPosition();

            // get the degrees of rotation by converting from radians
            float degrees = (float) Math.toDegrees(body.getAngle());

            // draw the fruit on the screen
            drawSprite(fruitSprites[i], position.x, position.y, degrees);
        }

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
        textureAtlas.dispose();
        sprites.clear();
        world.dispose();
        debugRenderer.dispose();
    }


    private void loadSprites() {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    private void drawSprite(Sprite sprite, float x, float y, float degrees) {
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }

    private void generateFruit() {
        String[] fruitNames = new String[]{"banana", "cherries", "orange"};

        Random random = new Random();

        for (int i = 0; i < fruitBodies.length; i++) {
            String name = fruitNames[random.nextInt(fruitNames.length)];

            fruitSprites[i] = sprites.get(name);

            float x = random.nextFloat() * Game.width;
            float y = random.nextFloat() * Game.height + Game.height;

            fruitBodies[i] = createBody(name, x, y, 0);
        }
    }

    private Body createBody(String name, float x, float y, float rotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = physicsBodies.createBody(name, world, bodyDef, SCALE, SCALE);
        body.setTransform(x, y, rotation);

        return body;
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
