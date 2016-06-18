package org.bob.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hi5dev.box2d_pexml.PEXML;

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

    public static final int width = 1080;
    public static final int height = 1920;

    /**
     * Adjust this value to change the amount of fruit that falls from the sky.
     */
    static final int COUNT = 25;
    Body[] fruitBodies = new Body[COUNT];
    static final float SCALE = 0.05f;
    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    Sprite[] fruitSprites = new Sprite[COUNT];
    TextureAtlas textureAtlas;
    Body ground;


    public SpriteBatch batch;
    public Viewport viewport;
    public Camera camera;
    public Bob bob;
    public Stage stage;
    public World world;
    public Box2DDebugRenderer debugRenderer;
    private PEXML physicsBodies;

    private Array<Sprite> bears;

    @Override
    public void create() {

        // Physics
        Box2D.init();
        physicsBodies = new PEXML(Gdx.files.internal("physics.xml").file());
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new PerspectiveCamera();

        // Viewport
        viewport = new StretchViewport(1080, 1920, camera);

        // Stage
        stage = new Stage(viewport);

        //ACTOR BOB **
        bob = new Bob();
        stage.addActor(bob);

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
        createGround();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());


        batch.begin();

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

            float x = random.nextFloat() * 50;
            float y = random.nextFloat() * 50 + 50;

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

    private void createGround() {
        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(camera.viewportWidth, 1);

        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        ground.setTransform(0, 0, 0);

        shape.dispose();
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
