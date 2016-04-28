package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * This method draws a cross in the middle
 */
public class Grid {
    float windowSizeX;
    float windowSizeY;
    float top;
    float left;
    float bottom;
    float right;
    RectF senkrechte;
    RectF waagerechte;
    Paint a;

    /**
     * The Constructor.
     * @param windowSizeX Android screen size X
     * @param windowSizeY Android screen size Y
     */
    public Grid(float windowSizeX, float windowSizeY){
        this.windowSizeX = windowSizeX;
        this.windowSizeY = windowSizeY;
        a = new Paint();
        a.setColor(0xffffffff);
        a.setAntiAlias(true);
        a.setStrokeWidth(2);
        a.setStrokeCap(Paint.Cap.ROUND);
        a.setStyle(Paint.Style.STROKE);
        update();
    }

    /**
     * defines a cross (waagerechte & senkrechte). is only run once (in the constructor)
     */
    public void update(){
        top = 0;
        left = 0;
        bottom = windowSizeY/2;
        right = windowSizeX;
        senkrechte = new RectF(left,top,right,bottom);
        top = 0;
        left = 0;
        bottom = windowSizeY;
        right = windowSizeX/2;
        waagerechte = new RectF(left,top,right,bottom);
    }
    public void draw(Canvas c){
        c.drawRect(senkrechte,a);
        c.drawRect(waagerechte,a);
    }
}
