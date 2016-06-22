package org.bob.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.lang.ref.WeakReference;

/**
 * Main game gesture listener. Class listens for
 * touch and swipe events.
 *
 * Created by Jannis on 20.06.2016.
 */
public class MyGestureListener implements GestureDetector.GestureListener{

    /*
     * Fields
     */

    final int THRESHOLD_X = 1000;
    WeakReference <Bob> bob;
    WeakReference <Game> game;

    /**
     * Constructor
     * @param bob our bob
     * @param game game object
     */
    public MyGestureListener(Bob bob,Game game){
        this.bob = new WeakReference<Bob>(bob);
        this.game = new WeakReference<Game>(game);
    }


    /*
     * Overridden methods.
     */

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // Spawn item on tap
        game.get().doSpawnItems(1,new Vector2(x, Gdx.graphics.getHeight()-y));
        if(bob.get().betweenMyX(x)){
            bob.get().changeAction(5);
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(velocityX > THRESHOLD_X){
            // Swipe from left to right
            bob.get().changeAction(0);
            // Let bob walk to the right
            bob.get().setDirection(1);
        }else if(velocityX < -THRESHOLD_X){
            // Swipe from right to left
            bob.get().changeAction(0);
            // Let bob walk to the left
            bob.get().setDirection(-1);

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
