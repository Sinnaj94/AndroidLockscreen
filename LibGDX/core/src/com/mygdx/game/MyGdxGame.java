package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture orange;
	Texture banana;
	Texture blueberry;
	Texture cherry;
	Texture strawberry;
	Texture coconut;
	Texture pair;
	Texture kiwi;
	Texture peach;
	Texture apple;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		orange = new Texture("orange.png");
		banana = new Texture("banana.png");
		blueberry = new Texture("blueberry.png");
		cherry = new Texture("cherry.png");
		strawberry = new Texture("strawberry.png");
		coconut = new Texture("coconut.png");
		pair = new Texture("pair.png");
		kiwi = new Texture("kiwi.png");
		peach = new Texture("peach.png");
		apple = new Texture("apple.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(orange, 960, 540);
		batch.draw(banana, 860, 440);
		batch.draw(blueberry, 1060, 340);
		batch.draw(cherry, 360, 500);
		batch.draw(strawberry, 1160, 840);
		batch.draw(coconut, 1560, 240);
		batch.draw(pair, 1090, 840);
		batch.draw(kiwi, 960, 940);
		batch.draw(peach, 7760, 640);
		batch.draw(apple, 160, 340);
		batch.end();
	}
}
