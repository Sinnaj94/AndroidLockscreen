package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by jeff on 16/06/16.
 */
public class Item {

    public Body body;
    public float scale;
    public Vector2 position;

    public Item(float scale, Vector2 position) {
        this.scale = scale;
        this.position = position;
    }

    public static void main (String [] args){
        Random r = new Random();
        for(int i=0; i<50; i++)
            System.out.println(r.nextInt(5));

    }

    public void render(SpriteBatch batch) {

    }
}



