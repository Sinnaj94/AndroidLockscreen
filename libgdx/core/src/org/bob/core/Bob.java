package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
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

    Texture walkSheet;              // #4
    TextureRegion[] walkFramesRight;             // #5
    TextureRegion[] walkFramesLeft;             // #5
    TextureRegion[] idleFrames;

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


    public Bob() {
        currentAction = 0;
        position = new Vector2(0, 0);
        walkingSpeed = 3;
        createSheet();
        bodyDef = new BodyDef();
        timer = 0;
        climbingSpeed = 1;
        timeToElapse = 1f;
    }

    public void changeAction() {
        //currentAction = MathUtils.random(0, 2);
    }

    private void setTimerRandom() {
        timeToElapse = MathUtils.random(minimumTime, maximumTime);
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
        }

    }

    //Switch case nr 0
    public void run() {
        if (getPosition().x + actorWidth > Gdx.graphics.getWidth()) {
            changeDirection();
            setPositionX(Gdx.graphics.getWidth() - actorWidth);
        } else if (getPosition().x < 0) {
            changeDirection();
            setPositionX(0);

        }

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

    private void setPosition(Vector2 position) {
        this.position = position;
    }

    private void setPositionX(float x) {
        this.position.x = x;
    }

    private void setPositionY(float y) {
        this.position.y = y;
    }

    private void move(Vector2 delta) {
        this.position.x += delta.x;
        this.position.y += delta.y;
    }

    private void moveX(float x) {
        this.position.x += x;
    }

    private void moveY(float y) {
        this.position.y += y;
    }


    private void createSheet() {
        walkSheet = new Texture(Gdx.files.internal("gfx/jeff.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        actorWidth = walkSheet.getWidth() / FRAME_COLS;
        actorHeight = walkSheet.getHeight() / FRAME_ROWS;
        Gdx.app.log("actorsize", "Width = " + actorWidth + ", Height = " + actorHeight);
        walkFramesRight = new TextureRegion[FRAME_COLS * 5];
        walkFramesLeft = new TextureRegion[FRAME_COLS * 5];
        idleFrames = new TextureRegion[6];
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
        index = 0;
        for (int i = 0; i < 6; i++) {
            idleFrames[index++] = tmp[10][i];

        }


        walkRightAnimation = new Animation(0.025f, walkFramesRight);      // #11
        walkLeftAnimation = new Animation(0.025f, walkFramesLeft);
        idleAnimation = new Animation(0.15f, idleFrames);
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


        }

        return idleAnimation.getKeyFrame(stateTime, true);

    }

    @Override
    public void draw(Batch batch, float alpha) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = returnSpriteSheet();  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, position.x, position.y);             // #17
        spriteBatch.end();
    }


}
