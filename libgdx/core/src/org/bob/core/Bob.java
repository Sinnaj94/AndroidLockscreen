package org.bob.core;

import com.badlogic.gdx.Gdx;
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
    World world;
    PolygonShape shape;
    Body body;
    Camera camera;

    /**
     * Constructor
     *
     * @param world  current World
     * @param camera Camera
     */
    public Bob(World world, Camera camera) {

        //Create Particle System
        p = new Particle();
        p.create();

        this.world = world;
        this.camera = camera;

        //Attributes for the player
        currentAction = 10;
        position = new Vector2(0, 0);
        walkingSpeed = 200;
        climbingSpeed = 1;

        //Create the sheet and Collision box
        createSheet();
        createCollider();

        //Initiate timer
        resetTimer();

    }

    private void createCollider() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;

        fixtureDef.density = 1f;

        shape = new PolygonShape();
        shape.setAsBox(actorWidth, actorHeight);

        fixtureDef.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setTransform(100, 200, 0);
        shape.dispose();
    }

    public void jump() {
        body.applyForceToCenter(100, 0, true);
    }

    public void changeAction() {
        currentAction = MathUtils.random(0, 3);
    }

    private void resetTimer() {
        timer = 0;
        timeToElapse = 1f;
        timeToElapse = MathUtils.random(5, 5);
    }

    private boolean timeOver(float delta) {
        timer += delta;
        if (timer >= timeToElapse) {
            return true;
        }
        return false;
    }

    @Override
    public void act(float delta) {
        if (timeOver(delta)) {
            resetTimer();
            changeAction();
        }
        decideAction();
    }

    private void decideAction() {
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


    //Switch case nr 0
    public void run() {
        if (body.getPosition().x / 2 > Gdx.graphics.getWidth()) {
            changeDirection();

        } else if (body.getPosition().x < 0) {
            changeDirection();


        }


        moveX(walkingSpeed);
    }

    //switch case nr 1
    public void stand() {
        //TODO: Implement
    }

    //switch case nr 2
    public void climb() {

        moveY(climbingSpeed);
    }

    //switch case nr 3
    private void smoke() {
        p.changePosition((body.getPosition().x - actorWidth) / 2 + 63, (body.getPosition().y - actorHeight) / 2 + 85);
    }

    private void changeDirection() {
        walkingSpeed *= -1;
    }

    private Vector2 getPosition() {
        return position;
    }

    //Move the Actor.
    private void move(Vector2 delta) {
        //setActorPosition(getPosition().x + delta.x, getPosition().y + delta.y);
        body.applyForceToCenter(delta,false);

    }

    private void moveX(float x) {
        move(new Vector2(x, 0));
    }

    private void moveY(float y) {
        move(new Vector2(0, y));
    }

    //Create the Sheet
    private void createSheet() {
        spriteSheet = new Texture(Gdx.files.internal(path));
        //Define the actor width & height
        actorWidth = spriteSheet.getWidth() / FRAME_COLS;
        actorHeight = spriteSheet.getHeight() / FRAME_ROWS;

        //SPECIAL CASES *****
        walkFramesLeft = new TextureRegion[FRAME_COLS * 5];
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;
        for (int i = 5; i < 10; i++) {
            for (int j = FRAME_COLS - 1; j > -1; j--) {
                walkFramesLeft[index++] = tmp[i][j];
            }
        }
        //******** SPECIAL CASES

        walkFramesRight = getFrames(0,5,0,4);
        idleFrames = getFrames(0,5,10,10);
        smokeFrames = getFrames(0,5,11,12);

        walkRightAnimation = new Animation(0.025f, walkFramesRight);      // #11
        walkLeftAnimation = new Animation(0.025f, walkFramesLeft);
        idleAnimation = new Animation(0.1f, idleFrames);
        smokeAnimation = new Animation(0.4f, smokeFrames);
        climbAnimation = new Animation(0.1f,getFrames(0,5,13,14));
        listenAnimation = new Animation(.045f,getFramesReverted(0,5,15,15));
        crouchAnimation = new Animation(.045f,getFramesReverted(0,5,16,16));

        //smokeAnimation = new Animation(0.1f,smokeFrames);
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;

        // #13
    }

    private TextureRegion[] getFrames( int xS,int xE,int yS, int yE){
        yE+=1;
        xE+=1;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;

        TextureRegion[] ret = new TextureRegion[(yE-yS)*(xE-xS)];

        for (int x = yS; x < yE; x++) {
            for (int y = xS; y < xE; y++) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        return ret;
    }

    private TextureRegion[] getFramesReverted( int xS,int xE,int yS, int yE){
        yE+=1;
        xE+=1;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        int index = 0;

        TextureRegion[] ret = new TextureRegion[(yE-yS)*(xE-xS)*2];

        for (int x = yS; x < yE; x++) {
            for (int y = xS; y < xE; y++) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        for (int x = yE-1; x > yS-1; x--) {
            for (int y = xE-1; y > xS-1; y--) {
                //x and y are swifted??
                ret[index++] = tmp[x][y];
            }
        }
        return ret;
    }



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
                return climbAnimation.getKeyFrame(stateTime,true);
            case 3:
                return smokeAnimation.getKeyFrame(stateTime, true);
        }

        return listenAnimation.getKeyFrame(stateTime, true);

    }


    private void drawChildObjects() {

        p.render();

    }

    @Override
    public void draw(Batch batch, float alpha) {
        //Children Updates:
        p.updateParticles();

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = returnSpriteSheet();  // #16
        spriteBatch.begin();

        spriteBatch.draw(currentFrame, (body.getPosition().x - actorWidth) / 2, (body.getPosition().y - actorHeight) / 2);
        spriteBatch.end();
        drawChildObjects();
    }


    public boolean isWaitingForAction() {
        return false;
    }
}
