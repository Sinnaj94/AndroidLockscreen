package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.lang.ref.WeakReference;

/**
 * Created by jeff on 16/06/16.
 */
public class Bob extends Actor {

    //Define How many frames the Spritesheet has.
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 20;
    //Path of the spritesheet
    final String path = "gfx/jeffso1857.png";

    //Texture Regions
    Texture spriteSheet;              // #4
    TextureRegion[] walkFramesRight;             // #5
    TextureRegion[] walkFramesLeft;             // #5
    TextureRegion[] idleFrames;
    TextureRegion[] smokeFrames;

    //Animations
    Animation walkRightAnimation;
    Animation walkLeftAnimation;
    Animation idleAnimation;
    Animation smokeAnimation;
    Animation climbAnimation;
    Animation listenAnimation;
    Animation crouchAnimation;
    //Particle world, smoke
    Particle p;

    //Draw Objects
    SpriteBatch spriteBatch;
    TextureRegion currentFrame;
    float stateTime;

    //Actor attributes
    float actorWidth, actorHeight;
    Vector2 position;
    float walkingSpeed;
    int currentAction;
    float climbingSpeed;

    //Timed events
    float timer;
    float timeToElapse;
    float minimumTime;
    float maximumTime;

    //Physic Objects
    WeakReference <World> world;
    PolygonShape shape;
    Body body;
    WeakReference<Camera> camera;
    float directionTimer;
    float maxDirectionTimer;
    //HIT LAST: 'n' -> null, 'l' -> left, 'r' -> right
    char hitLast;

    //Game contains Item list to check
    WeakReference <Game> game;

    //Music
    Music music;

    /**
     * Constructor
     *
     * @param world  current World
     * @param camera Camera
     */
    public Bob(Game game, World world, Camera camera) {

        this.game = new WeakReference<Game>(game);

        hitLast = 'n';
        //Create Particle System
        p = new Particle();
        p.create();

        this.world = new WeakReference<World>(world);
        this.camera = new WeakReference<Camera>(camera);

        //Attributes for the player
        currentAction = 3;
        position = new Vector2(0, 0);
        walkingSpeed = 200;
        climbingSpeed = 1;

        //Create the sheet and Collision box
        createSheet();
        createCollider();

        //Initiate timer
        resetTimer();
        maxDirectionTimer = .1f;
        directionTimer = maxDirectionTimer;

        //Initiate Music
        music = Gdx.audio.newMusic(Gdx.files.internal("data/weekend.mp3"));
    }



    /**
     * Creates a Collider for Bob
     *      ^
     *    ´   `
     *    |   |
     *    |   |
     *    |___|
     *
     *
     */
    private void createCollider() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;

        Vector2[] vertices = new Vector2[]{new Vector2(-actorWidth / 2, -actorHeight), new Vector2(-actorWidth / 2, actorHeight / 2), new Vector2(0, actorHeight), new Vector2(actorWidth / 2, actorHeight / 2), new Vector2(actorWidth / 2, -actorHeight)};

        shape = new PolygonShape();

        shape.set(vertices);
        //shape.setAsBox(actorWidth, actorHeight);

        fixtureDef.shape = shape;

        body = world.get().createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setTransform(100, this.game.get().platform.positionY + 100, 0);
        shape.dispose();
    }

    /**
     * Adds a force to Bob, to make him jump
     */
    public void jump() {
        body.applyForceToCenter(100, 0, true);
    }

    /**
     * Checks whether the given X-Position is overlapping with bob.
     * @param x X-Position
     * @return True if overlaps, false if not.
     */
    public boolean betweenMyX(float x) {

        if (x > position.x && x < position.x + actorWidth * 2) {
            return true;
        }

        return false;
    }

    /**
     * Changes the Action of Bob, accordingly to a simple automat.
     */
    public void changeAction() {
        //Mechanism:
        int newAction;
        if (currentAction == 0) {
            newAction = MathUtils.random(1, 4);
        } else{
            newAction = 0;
        }

        changeAction(newAction);
    }

    /**
     * Changes Bobs Action and resets the timer. Also adjusts some special cases.
     * @param newAction The number of the new Action
     */
    public void changeAction(int newAction) {
        if (newAction == 0) {
            hitLast = 'n';
        } else if(newAction == 4){
            music.play();
        } else{
            body.setLinearVelocity(0, 0);
        }
        if(newAction!=4){
            music.stop();
        }
        currentAction = newAction;
        resetTimer();

    }

    /**
     * Changes the direction
     * @param direction Negative -> left, positive -> right
     */
    public void setDirection(int direction) {
        if (direction < 0) {
            walkingSpeed = -Math.abs(walkingSpeed);
        } else {
            walkingSpeed = Math.abs(walkingSpeed);
        }
    }

    /**
     *  Resets the Timer
     */
    private void resetTimer() {
        timer = 0;
        timeToElapse = getNewTime();
    }

    /**
     *
     * @return The time for the specific action, for the timer. Partly randomly generated.
     */
    private float getNewTime() {
        if (currentAction == 0) {
            return MathUtils.random(5f, 12f);

        } else if (currentAction == 5) {
            return 1;
        }
        return MathUtils.random(5f, 10f);

    }

    /**
     * @param delta The game-time-milliseconds
     * @return Time to jump from one frame to another
     */
    private boolean timeOver(float delta) {
        timer += delta;
        if (timer >= timeToElapse) {
            return true;
        }
        return false;
    }

    /**
     * The act method from bob
     * @param delta Time to jump from one frame to another
     */
    @Override
    public void act(float delta) {
        if (timeOver(delta)) {
            changeAction();
        }
        decideAction(delta);
    }

    /**
     * Performs the move (like running or climbing)
     * @param delta Time to jump from one frame to another
     */
    private void decideAction(float delta) {

        switch (currentAction) {
            case 0:
                run();
                break;
            case 1:
                stand();
                break;
            case 2:
                climb();
                break;
            case 3:
                smoke();
                break;
        }
    }


    /**
     * First State
     */
    public void run() {
        if (position.x + actorWidth * 2 > Gdx.graphics.getWidth()) {
            if (hitLast != 'r') {
                changeDirection();
                hitLast = 'r';
            }
        } else if (position.x < 0) {
            if (hitLast != 'l') {
                changeDirection();
                hitLast = 'l';
            }
        }
        moveX(walkingSpeed);
    }

    /**
     * Second State
     */
    public void stand() {

    }

    /**
     * Third State
     */
    public void climb() {
        moveX(0f); // force stop
        moveY(climbingSpeed);
    }

    /**
     * Fourth State
     */
    private void smoke() {
        moveX(0f); // force stop
        p.changePosition((position.x + actorWidth * 2 * .75f), (position.y + actorHeight * 2 * .8f));
    }

    /**
     * Changes the direction
     */
    private void changeDirection() {

        walkingSpeed *= -1;

    }


    /**
     * Move the actor on x & y axis
     * @param delta Speed Vector
     */
    private void move(Vector2 delta) {
        //setActorPosition(getPosition().x + delta.x, getPosition().y + delta.y);
        body.setLinearVelocity(delta);

    }

    /**
     * Moves the actor on x axis
     * @param x Speed
     */
    private void moveX(float x) {
        move(new Vector2(x, body.getLinearVelocity().y));
    }

    /**
     * Moves the actor on y axis
     * @param y Speed
     */
    private void moveY(float y) {
        move(new Vector2(body.getLinearVelocity().x, y));
    }

    /**
     * Creates the Spritesheet from the PNG
     * Contains Parts from https://github.com/libgdx/libgdx/wiki/2D-Animation
     */
    private void createSheet() {
        spriteSheet = new Texture(Gdx.files.internal(path));
        //Define the actor width & height
        actorWidth = spriteSheet.getWidth() / FRAME_COLS;
        actorHeight = spriteSheet.getHeight() / FRAME_ROWS;

        //Define The Frames
        //SPECIAL CASES *****
        //We use special cases, because we want some Animations from the Spritesheet go backwards and forward, not repeat
        walkFramesLeft = new TextureRegion[FRAME_COLS * 5];
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;
        for (int i = 5; i < 10; i++) {
            for (int j = FRAME_COLS - 1; j > -1; j--) {
                walkFramesLeft[index++] = tmp[i][j];
            }
        }
        //******** SPECIAL CASES

        walkFramesRight = getFrames(0, 5, 0, 4);
        idleFrames = getFrames(0, 5, 10, 10);
        smokeFrames = getFrames(0, 5, 11, 12);

        //Here we build the Animations
        walkRightAnimation = new Animation(0.025f, walkFramesRight);
        walkLeftAnimation = new Animation(0.025f, walkFramesLeft);
        idleAnimation = new Animation(0.1f, idleFrames);
        smokeAnimation = new Animation(0.4f, smokeFrames);
        climbAnimation = new Animation(0.1f, getFrames(0, 5, 13, 14));
        listenAnimation = new Animation(.045f, getFramesReverted(0, 5, 15, 15));
        crouchAnimation = new Animation(.045f, getFramesReverted(0, 5, 16, 16));


        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    /**
     * The start X and Y values define the current array number
     * @param xS Start X
     * @param xE End X
     * @param yS Start Y
     * @param yE End Y
     * @return An Array of Frames in the given range
     */
    private TextureRegion[] getFrames(int xS, int xE, int yS, int yE) {
        yE += 1;
        xE += 1;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;

        TextureRegion[] ret = new TextureRegion[(yE - yS) * (xE - xS)];

        for (int x = yS; x < yE; x++) {
            for (int y = xS; y < xE; y++) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        return ret;
    }

    /**
     * The start X and Y values define the current array number
     * @param xS Start X
     * @param xE End X
     * @param yS Start Y
     * @param yE End Y
     * @return An Array of Frames in the given range, also backwards
     */
    private TextureRegion[] getFramesReverted(int xS, int xE, int yS, int yE) {
        yE += 1;
        xE += 1;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;

        TextureRegion[] ret = new TextureRegion[(yE - yS) * (xE - xS) * 2];

        for (int x = yS; x < yE; x++) {
            for (int y = xS; y < xE; y++) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        for (int x = yE - 1; x > yS - 1; x--) {
            for (int y = xE - 1; y > xS - 1; y--) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        return ret;
    }

    /**
     *
     * @return Returns the Spritearrays of the current Action
     */
    private TextureRegion returnSpriteSheet() {

        switch (currentAction) {
            case 0:
                if (walkingSpeed < 0) {
                    return walkLeftAnimation.getKeyFrame(stateTime, true);

                } else {
                    return walkRightAnimation.getKeyFrame(stateTime, true);

                }
            case 1:
                return idleAnimation.getKeyFrame(stateTime, true);
            case 2:
                return climbAnimation.getKeyFrame(stateTime, true);
            case 3:
                return smokeAnimation.getKeyFrame(stateTime, true);
            case 4:
                return listenAnimation.getKeyFrame(stateTime, true);
            case 5:
                return crouchAnimation.getKeyFrame(stateTime, true);

        }

        return crouchAnimation.getKeyFrame(stateTime, true);

    }

    /**
     * Draws child objects, for example Particles eg. Also starts the Particles, when the smoke case is on.
     */
    private void drawChildObjects() {
        if (currentAction == 3) {
            p.startEmitting();
        } else {
            p.stopEmitting();
        }
        p.render();

    }

    /**
     * Draws Bob and his child elements
     * @param batch current Batch
     * @param alpha Opacity of the drawn elements
     */
    @Override
    public void draw(Batch batch, float alpha) {

        //Children Updates:
        p.updateParticles();

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = returnSpriteSheet();
        position.x = (body.getPosition().x - actorWidth);
        position.y = (body.getPosition().y - actorHeight);
        spriteBatch.begin();

        spriteBatch.draw(currentFrame, position.x, position.y, actorWidth * 2, actorHeight * 2);

        spriteBatch.end();
        drawChildObjects();
    }


    /**
     * Delete all the Elements (by garbage collector, setting them to null)
     */
    public void dispose() {

        if(spriteSheet != null) {
            spriteSheet.dispose();
            spriteSheet = null;
        }

        walkFramesRight = null;
        walkFramesLeft = null;
        idleFrames = null;
        smokeFrames = null;

        walkRightAnimation = null;
        walkLeftAnimation = null;
        idleAnimation = null;
        smokeAnimation = null;
        climbAnimation = null;
        listenAnimation = null;
        crouchAnimation = null;

        if(p!= null) {
            p.dispose();
            p = null;
        }

        if(spriteBatch != null) {
            spriteBatch.dispose();
            spriteBatch = null;
        }

        currentFrame = null;
        position = null;
        world = null;
        shape = null;
        body = null;
        camera = null;
        game = null;
        music = null;
    }
}
