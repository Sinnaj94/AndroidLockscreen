package org.livewallpaper;

/**
 * Created by Johannes on 27.04.2016.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Represents a bullet in the game.
 */
public class Bullet extends GameObject {

    float posX;
    float posY;
    float speed;
    float width;
    float height;
    RectF rectShape;
    float damage;
    Paint a;
    boolean active;

    public Bullet(){
        init();
    }

    private void init() {
        active = false;
        speed = Constants.BULLET_SPEED;
        width = 10;
        height = 10;
        rectShape = new RectF();
        a = new Paint();
        //damage: 0 = 0%, 1 = 100%
        damage = 1;
        a.setARGB(255, 255, 255, 255);
    }

    /**
     * Determine if bullet is outside the screen
     * @return true if inside the screen, false if not.
     */
    public boolean outOfScreen(){
        if(posY <= 0){
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        posY -= speed;

        float left = posX - width / 2;
        float up = posY - height / 2;
        float right = left + width;
        float down = up + height;
        rectShape.set(left, up, right, down);

        if(!outOfScreen()) {
            Log.v(Bullet.class.getSimpleName(), "Update: posY:" + posY);
        }else{
            reset();
            Log.v(Bullet.class.getSimpleName(), "Bullet out of screen.");
        }
    }

    /**
     *  Fire a bullet
     * @param poxX start position x
     * @param posY start position y
     */
    public void fire(float poxX, float posY){
        active = true;
        this.posX = poxX;
        this.posY = posY;
    }

    /**
     * Reset the bullet for the next usage.
     */
    private void reset() {
        active = false;
        this.posX = -1;
        this.posY = -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas c) {
        c.drawRect(rectShape, a);
    }
}
