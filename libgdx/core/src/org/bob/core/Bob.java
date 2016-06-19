package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
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

    private static final int FRAME_COLS = 6;         // #1
    private static final int FRAME_ROWS = 20;         // #2
    Animation walkRightAnimation;          // #3
    Animation walkLeftAnimation;
    Animation idleAnimation;
    Animation smokeAnimation;          // #3


    Texture walkSheet;              // #4
    TextureRegion[] walkFramesRight;             // #5
    TextureRegion[] walkFramesLeft;             // #5
    TextureRegion[] idleFrames;
    TextureRegion[] smokeFrames;
    Particle p;

    SpriteBatch spriteBatch;            // #6
    TextureRegion currentFrame;           // #7
    float stateTime;                                        // #8
    float actorX = 0, actorY = 100, actorWidth, actorHeight;
    Vector2 position;
    float walkingSpeed;
    public boolean started = false;
    BodyDef bodyDef;
    int currentAction;
    float timer;
    float timeToElapse;
    float minimumTime = 1;
    float maximumTime = 10;
    float climbingSpeed;
    World world;
    PolygonShape shape;
    Body body;
    Camera camera;
    public Bob(World world,Camera camera) {
        p = new Particle();
        p.create();
        this.world = world;
        this.camera = camera;
        currentAction = 3;
        position = new Vector2(0, 0);
        walkingSpeed = 200;
        createSheet();
        createCollider();
        timer = 0;
        climbingSpeed = 1;
        timeToElapse = 1f;
    }

    private void createCollider(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;

        shape = new PolygonShape();
        shape.setAsBox(actorWidth,actorHeight);

        fixtureDef.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setTransform(100, 200, 0);
        shape.dispose();
    }

    public void jump(){
        body.applyForceToCenter(100,0,true);
    }

    public void changeAction() {
        currentAction = MathUtils.random(0, 3);
    }

    private void setTimerRandom() {
        timeToElapse = MathUtils.random(5, 5);
    }


    @Override
    public void act(float delta) {
        timer += delta;
        if (timer >= timeToElapse) {
            changeAction();
            setTimerRandom();
            timer = 0;
        }
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

    private void smoke() {
        p.changePosition((body.getPosition().x-actorWidth)/2+63,(body.getPosition().y-actorHeight)/2+85);
    }

    //Switch case nr 0
    public void run() {
        if (body.getPosition().x/2 > Gdx.graphics.getWidth()) {
            changeDirection();

        } else if (body.getPosition().x < 0) {
            changeDirection();


        }
        Gdx.app.log("rightleft", "X: " + body.getPosition().x/Gdx.graphics.getWidth()/2);

        moveX(walkingSpeed);
    }

    //switch case nr 1
    public void stand() {
        //TODO: Implement
    }

    //switch case nr 2
    public void climb(){

        moveY(climbingSpeed);
    }

    private void changeDirection() {
        walkingSpeed *= -1;

    }

    private Vector2 getPosition() {
        return position;
    }
    //most important
    private void setActorPosition(Vector2 position) {
        this.position = position;
    }

    private void setActorPosition(float x,float y){
        setActorPosition(new Vector2(x,y));
    }

    private void setPositionX(float x) {
        setActorPosition(x,getPosition().y);
    }

    private void setPositionY(float y) {
        setActorPosition(getPosition().x,y);
    }

    private void move(Vector2 delta) {
        //setActorPosition(getPosition().x + delta.x, getPosition().y + delta.y);
        //body.applyForceToCenter(delta,false);

    }

    private void moveX(float x) {
        move(new Vector2(x,0));
    }

    private void moveY(float y) {
        move(new Vector2(0,y));

    }


    private void createSheet() {
        walkSheet = new Texture(Gdx.files.internal("gfx/jeff-01.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        actorWidth = walkSheet.getWidth() / FRAME_COLS;
        actorHeight = walkSheet.getHeight() / FRAME_ROWS;
        Gdx.app.log("actorsize", "Width = " + actorWidth + ", Height = " + actorHeight);
        walkFramesRight = new TextureRegion[FRAME_COLS * 5];
        walkFramesLeft = new TextureRegion[FRAME_COLS * 5];
        idleFrames = new TextureRegion[6];
        smokeFrames = new TextureRegion[12];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFramesRight[index++] = tmp[i][j];
            }
        }
        index = 0;
        for (int i = 5; i < 10; i++) {
            for (int j = FRAME_COLS - 1; j > -1; j--) {
                walkFramesLeft[index++] = tmp[i][j];
            }
        }

        for (int i = 0; i < 6; i++) {
            idleFrames[i] = tmp[10][i];
        }

        for (int i = 0; i < 6; i++) {
            smokeFrames[i] = tmp[11][i];
        }
        for (int i = 0; i < 6; i++) {
            smokeFrames[i+6] = tmp[12][i];
        }




        walkRightAnimation = new Animation(0.025f, walkFramesRight);      // #11
        walkLeftAnimation = new Animation(0.025f, walkFramesLeft);
        idleAnimation = new Animation(0.1f, idleFrames);
        smokeAnimation = new Animation(0.4f, smokeFrames);

        //smokeAnimation = new Animation(0.1f,smokeFrames);
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;

        // #13
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
            case 3:
                return smokeAnimation.getKeyFrame(stateTime,true);
        }

        return smokeAnimation.getKeyFrame(stateTime, true);

    }


    private void drawChildObjects(){
        if(currentAction == 3) {
            p.render();
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        //Children Updates:
        p.updateParticles();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = returnSpriteSheet();  // #16
        spriteBatch.begin();

        spriteBatch.draw(currentFrame,(body.getPosition().x-actorWidth)/2,(body.getPosition().y-actorHeight)/2);
        spriteBatch.end();
        drawChildObjects();
    }


}
