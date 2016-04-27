package org.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.RectF;

/**
 * Created by Jannis on 25.04.2016.
 */
public class Player {
    //posX and posY define the Middle Point of the rectangle
    float posX;
    float posY;
    float width;
    float height;
    float speed;
    RectF rectShape;
    Paint a;

    public Player(float windowSizeX, float windowSizeY,float width, float height,float percentDown){
        this.posX = windowSizeX/2;
        this.posY = windowSizeY*percentDown;
        this.width = width;
        this.height = height;
        speed = 20f;
        rectShape = new RectF();
        a = new Paint();
        a.setARGB(255,255,0,0);

        update();
    }

    public void shoot(){

    }

    public void changePosX(float newPosX){
        float delta = Math.abs(newPosX-posX);
        if(newPosX>posX){

            posX+=delta*.1;
        }else if(newPosX<posX){
            posX-=delta*.1;
        }

    }

    public void update(){
        //if(bullet!=null){
            //bullet.update();
        //}
        float left = posX-width/2;
        float up = posY-height/2;
        float right = left+width;
        float down = up+height;
        rectShape.set(left,up,right,down);

    }
    void draw(Canvas c){
        c.drawRect(rectShape,a);

    }
}
