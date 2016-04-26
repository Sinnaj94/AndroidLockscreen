package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jannis on 25.04.2016.
 */
public class Grid {
    float windowSizeX;
    float windowSizeY;
    float top;
    float left;
    float bottom;
    float right;
    RectF senkrechte;
    Paint a;
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
    public void update(){
        top = 0;
        left = 0;
        bottom = windowSizeY/2;
        right = windowSizeX;
        senkrechte = new RectF(left,top,right,bottom);
    }
    public void draw(Canvas c){
        c.drawRect(senkrechte,a);
    }
}
