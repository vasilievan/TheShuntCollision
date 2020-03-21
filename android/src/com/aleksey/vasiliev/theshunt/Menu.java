package com.aleksey.vasiliev.theshunt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu implements Screen {
    Game parent;
    SpriteBatch drawingBatch;
    Texture background;
    AssetManager assets;

    Menu(Game game, AssetManager assets){
        this.parent = game;
        this.assets = assets;
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
        background = new Texture(Gdx.files.internal("background.png"));
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                parent.setScreen(new MyShuntGame(parent, assets));
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        serviceFunction();
        drawingBatch.begin();
        drawingBatch.draw(background, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
