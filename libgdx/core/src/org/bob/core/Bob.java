package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
    float actorX = 0, actorY = 100, actorWidth,actorHeight;
    float walkingSpeed;
    public boolean started = false;

    public Bob() {
        walkingSpeed = 3;
        createSheet();


    }

    @Override
    public void act(float delta) {
        run();

    }

    public void run(){
        if(actorX +actorWidth> Gdx.graphics.getWidth()){
            walkingSpeed*=-1;
            actorX = Gdx.graphics.getWidth() - actorWidth;
        }else if(actorX < 0){
            walkingSpeed*=-1;
            actorX = 0;

        }
        actorX += walkingSpeed;
    }

    public void stand(){

    }

    private void createSheet() {
        walkSheet = new Texture(Gdx.files.internal("gfx/animation_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        actorWidth = walkSheet.getWidth() / FRAME_COLS;
        actorHeight = walkSheet.getHeight()/FRAME_ROWS;
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
        spriteBatch.draw(currentFrame, actorX, actorY);             // #17
        spriteBatch.end();
    }


}
