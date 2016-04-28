package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jannis on 25.04.2016.
 */
public class Enemy {
    float posX;
    float posY;
    float thePosX;
    float width;
    float height;
    int currentStep;
    RectF rectShape;
    Paint a;
    int type;
    public Enemy(float windowSizeX, float windowSizeY,float width,float height, float percentY){
        this.posX = windowSizeX/2;
        this.thePosX = posX;
        this.posY = windowSizeY*percentY;
        this.width = width;
        this.height = height;
        rectShape = new RectF();
        a = new Paint();
        a.setARGB(255,0,255,255);

        update();
    }

    public void update(){
        move();
        float left = posX-width/2;
        float up = posY-height/2;
        float right = left+width;
        float down = up+height;
        rectShape.set(left,up,right,down);
        currentStep++;

    }

    public void move(){
        this.posX = thePosX + (float)(Math.sin(currentStep/10)*100);
    }
    void draw(Canvas c){
        c.drawRect(rectShape,a);
    }
}
