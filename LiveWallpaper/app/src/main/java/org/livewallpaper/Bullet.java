package org.livewallpaper;

/**
 * Created by Johannes on 27.04.2016.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

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

    /**
     * Default constructor
     * @param posX
     * @param posY
     */
    public Bullet(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        speed = 30f;
        width = 10;
        height = 10;
        rectShape = new RectF();
        a = new Paint();
        //damage: 0 = 0%, 1 = 100%
        damage = 1;
        a.setARGB(255, 255, 255, 255);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas c) {
        c.drawRect(rectShape, a);
    }
}
