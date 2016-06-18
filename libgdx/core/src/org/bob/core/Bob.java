package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by jeff on 16/06/16.
 */
public class Bob extends Actor {

    private static final int FRAME_COLS = 6;         // #1
    private static final int FRAME_ROWS = 5;         // #2
    Animation walkAnimation;          // #3
    Texture walkSheet;              // #4
    TextureRegion[] walkFrames;             // #5
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
    public Bob() {
        currentAction = 1;
        position = new Vector2(0, 0);
        walkingSpeed = 3;
        createSheet();
        bodyDef = new BodyDef();
        timer = 0;
        timeToElapse = 1f;
    }

    public void changeAction(){
        currentAction = MathUtils.random(0,1);
    }

    private void setTimerRandom(){
        timeToElapse = MathUtils.random(minimumTime,maximumTime);
    }

    @Override
    public void act(float delta) {
        timer+=delta;
        if(timer >= timeToElapse){
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


    private void createSheet() {
        walkSheet = new Texture(Gdx.files.internal("gfx/animation_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        actorWidth = walkSheet.getWidth() / FRAME_COLS;
        actorHeight = walkSheet.getHeight() / FRAME_ROWS;
        Gdx.app.log("actorsize", "Width = " + actorWidth + ", Height = " + actorHeight);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);      // #11
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;

        // #13
    }

    @Override
    public void draw(Batch batch, float alpha) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, position.x, position.y);             // #17
        spriteBatch.end();
    }


}
