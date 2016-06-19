package org.bob.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.Disposable;

/**
 * Created by jeff on 16/06/16.
 */
public class Particle extends ApplicationAdapter {
    ParticleEffect pe;
    SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("Particles.p"), Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pe.start();
    }

    public void changePosition(){

    }


    @Override
    public void render() {

        pe.update(Gdx.graphics.getDeltaTime());
        batch.begin();
        pe.draw(batch);
        batch.end();
        if (pe.isComplete()) {
            pe.reset();

        }
    }


}
