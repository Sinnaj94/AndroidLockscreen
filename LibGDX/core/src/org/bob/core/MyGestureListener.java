package org.bob.core;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Jannis on 20.06.2016.
 */
public class MyGestureListener implements GestureDetector.GestureListener{
    final int THRESHOLD_X = 1000;
    Bob bob;
    Game game;
    public MyGestureListener(Bob bob,Game game){
        this.bob = bob;
        this.game = game;
    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.doSpawnItems(1,null);

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(velocityX > THRESHOLD_X){
            bob.changeAction(0);
            bob.setDirection(1);
        }else if(velocityX < -THRESHOLD_X){
            bob.changeAction(0);
            bob.setDirection(-1);

        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
