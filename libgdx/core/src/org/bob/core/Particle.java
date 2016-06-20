package org.bob.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Particle extends ApplicationAdapter {
    ParticleEffect pe;
    SpriteBatch batch;
    boolean startedSystem;
    int maxParticleCount;
    boolean running;


    @Override
    public void create() {
        running = true;

        batch = new SpriteBatch();
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("gfx/Particles"), Gdx.files.internal("gfx"));

        maxParticleCount = pe.getEmitters().first().getMaxParticleCount();

        pe.start();
        stopEmitting();

    }

    public void changePosition(float x, float y) {
        pe.getEmitters().first().setPosition(x, y);
    }

    public void startEmitting() {
        if(!running){
            pe.getEmitters().first().setMaxParticleCount(maxParticleCount);
            running = true;
        }
    }

    public void stopEmitting() {
        if(running){
            pe.getEmitters().first().setMaxParticleCount(0);
            running = false;
        }
    }

    @Override
    public void render() {

        batch.begin();
        pe.draw(batch);
        batch.end();
        if (pe.isComplete()) {
            pe.reset();

        }
    }

    public void updateParticles() {
        pe.update(Gdx.graphics.getDeltaTime());

    }


}
