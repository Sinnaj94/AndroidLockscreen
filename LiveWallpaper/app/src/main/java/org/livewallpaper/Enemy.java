package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jannis on 25.04.2016.
 */
public class Enemy extends GameObject {

    float posX;
    float posY;
    float thePosX;
    float width;
    float height;
    int currentStep;
    RectF rectShape;
    Paint a;
    /**
     * The type is used to define strength, color and speed of the Enemy
     */
    int type;

    /**
     * @param windowSizeX The X Screen size of the android device
     * @param windowSizeY The Y screen size of the android device
     * @param width       the width of the enemy
     * @param height      the height of the enemy
     * @param percentY    defines, how many percent the enemy is drawn from top
     */
    public Enemy(float windowSizeX, float windowSizeY, float width, float height, float percentY) {
        this.posX = windowSizeX / 2;
        this.thePosX = posX;
        this.posY = windowSizeY * percentY;
        this.width = width;
        this.height = height;
        rectShape = new RectF();
        a = new Paint();
        a.setARGB(255, 0, 255, 255);

        update();
    }

    /**
     * updates the enemy (moves it then plots it to the rectangle)
     */
    @Override
    public void update() {
        move();
        float left = posX - width / 2;
        float up = posY - height / 2;
        float right = left + width;
        float down = up + height;
        rectShape.set(left, up, right, down);
        currentStep++;

    }

    /**
     * Randomly moves the Enemy
     */
    public void move() {
        this.posX = thePosX + (float) (Math.sin(currentStep / 10) * 100);
    }

    /**
     * draws the Enemy
     *
     * @param c The Canvas
     */
    @Override
    public void draw(Canvas c) {
        c.drawRect(rectShape, a);
    }
}
