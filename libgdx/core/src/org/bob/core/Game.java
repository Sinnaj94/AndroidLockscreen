package org.bob.core;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.Console;
import java.util.logging.Logger;





/**
 * Created by jeff on 16/06/16.
 */
public class Game extends InputAdapter implements ApplicationListener {
    SpriteBatch batch;
    Texture img;
    Viewport viewport;
    Camera camera;
    private Array<Sprite> bears;
    @Override
    public void create() {
        bears = new TextureAtlas("gfx/bear.sheet").createSprites();

        camera = new PerspectiveCamera();
        viewport = new FitViewport(1080, 1920, camera);
        batch = new SpriteBatch();
        img = new Texture("gfx/baer.png");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img,0,100);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
