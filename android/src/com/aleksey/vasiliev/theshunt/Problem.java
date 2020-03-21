package com.aleksey.vasiliev.theshunt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Problem implements Screen {
	Game parent;
	SpriteBatch drawingBatch;
	Texture mine;

	public Problem(Game game){
		this.parent = game;
	}

	private void serviceFunction() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void dispose() {
		drawingBatch.dispose();
	}

	@Override
	public void show() {
		drawingBatch = new SpriteBatch();
		mine = new Texture(Gdx.files.internal("problem.png"));
	}

	@Override
	public void render(float delta) {
		serviceFunction();
		drawingBatch.begin();
		drawingBatch.draw(mine, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		drawingBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {

	}
}
