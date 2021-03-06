package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jannis on 25.04.2016.
 */
public class Player extends GameObject {
    /**
     * rectangle Attributes!
     */
    float posX;
    float posY;
    float width;
    float height;

    BulletManager bulletManager;


    /**
     * The Bounds
     */
    float boundX;
    float boundY;
    float maxSpeed;
    //the Rectangle Shape is defined by the position X Y and width and height
    RectF rectShape;
    //The Paint
    Paint a;


    /**
     * The Constructor.
     *
     * @param windowSizeX The window Size  X of the screen
     * @param windowSizeY Window Size Y of the Screen
     * @param width       width of the player
     * @param height      height of the player
     * @param percentDown Percentage: defines how many percent the posY is from the top
     */
    public Player(float windowSizeX, float windowSizeY, float width, float height, float percentDown) {
        this.posX = windowSizeX / 2;
        this.posY = windowSizeY * percentDown;
        this.width = width;
        this.height = height;
        maxSpeed = 10f;
        rectShape = new RectF();
        a = new Paint();
        a.setARGB(255, 255, 0, 0);
        boundX = windowSizeX;
        boundY = windowSizeY;
        bulletManager = new BulletManager(Constants.BULLET_MAX_ELEMENTS);

        update();
    }

    /**
     * This method shoots a bullet
     */
    public void shoot() {
        bulletManager.shootBullet(posX,posY);
    }

    /**
     * This method changes the X-Position of the Player. To make it smooth, a delta value is calculated.
     *
     * @param newPosX the new X Position of the Player / the Spaceship.
     */
    public void changePosX(float newPosX) {
        float delta = Math.abs(newPosX - posX);
        if (newPosX > posX) {
            posX += delta * maxSpeed;
        } else if (newPosX < posX) {
            posX -= delta * maxSpeed;
        }
        //Here we check if the player is outside the area
        collideWithWalls();
    }

    /**
     * method to move the player with a given maxSpeed factor (use for accelerometer)
     *
     * @param speed the speed the player can move
     */
    public void moveX(float speed) {
        if (Math.abs(speed) > .5) {
            float delta = this.maxSpeed * speed;
            posX -= delta;
            collideWithWalls();
        }

    }

    /**
     * Method used to detect, whether the player collides with a wall. Also handles the collision
     */
    private void collideWithWalls() {
        if (posX - width / 2 < 0) {
            posX = width / 2;
        } else if (posX + width / 2 > boundX) {
            posX = boundX - width / 2;
        }
    }


    /**
     * The update method builds the rectShape and automatically puts the posX and posY attribute in the middle of the player.
     */
    @Override
    public void update() {

        bulletManager.update();

        float left = posX - width / 2;
        float up = posY - height / 2;
        float right = left + width;
        float down = up + height;
        rectShape.set(left, up, right, down);
    }

    /**
     * The draw method draws the player.
     *
     * @param c The Canvas
     */
    @Override
    public void draw(Canvas c) {
        c.drawRect(rectShape, a);

        for(Bullet b : bulletManager) {
            b.draw(c);
        }
    }
}
