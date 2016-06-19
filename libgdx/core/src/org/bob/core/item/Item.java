package org.bob.core.item;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by jeff on 16/06/16.
 */
public class Item {

    public Body body;
    public float scale;

    public Item(float scale) {
        this.scale = scale;
    }
}
