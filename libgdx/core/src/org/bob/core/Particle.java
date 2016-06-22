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

    /**
     * Creates the Particlesystem from the File "gfx/Particles" but doesnt start yet
     */
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

    /**
     * Changes the Position of the current system
     * @param x New Position x
     * @param y New Position y
     */
    public void changePosition(float x, float y) {
        pe.getEmitters().first().setPosition(x, y);
    }

    /**
     * Starts the first emitter by setting the maxParticleCount-Attribute
     */
    public void startEmitting() {
        if(!running){
            pe.getEmitters().first().setMaxParticleCount(maxParticleCount);
            running = true;
        }
    }

    /**
     * Stops the first emitter by setting the maxParticleCount-Attribute
     */
    public void stopEmitting() {
        if(running){
            pe.getEmitters().first().setMaxParticleCount(0);
            running = false;
        }
    }

    /**
     * Renders the current particle-system
     */
    @Override
    public void render() {

        batch.begin();
        pe.draw(batch);
        batch.end();
        if (pe.isComplete()) {
            pe.reset();

        }
    }

    /**
     * Updates the Particles
     */
    public void updateParticles() {
        pe.update(Gdx.graphics.getDeltaTime());

    }


}
