package org.bob.core.item;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by jeff on 16/06/16.
 */
public class Item {

    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    public boolean projectionMatrixSet;

    public Body body;
    public float scale;
    public Vector2 position;

    public Item(float scale, Vector2 position) {
        this.scale = scale;
        this.position = position;
    }

    public void render(SpriteBatch batch) {

    }
}



