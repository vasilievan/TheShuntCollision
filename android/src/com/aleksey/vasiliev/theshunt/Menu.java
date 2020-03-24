package com.aleksey.vasiliev.theshunt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Menu implements Screen {
    private Game parent;
    private SpriteBatch drawingBatch;
    private Texture background;
    private AssetManager assets;
    private BitmapFont font24;
    private int totalScore;
    private float scaleCoeffwidth;

    Menu(Game game, AssetManager assets, int totalScore){
        this.parent = game;
        this.assets = assets;
        this.totalScore = totalScore;
    }

    private void scoreMaker() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("CASTELAR.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(130 / scaleCoeffwidth);
        parameter.color = Color.BLACK;
        font24 = generator.generateFont(parameter);
        generator.dispose();
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
        int width = 2130;
        scaleCoeffwidth = (float) width / Gdx.graphics.getWidth();
        drawingBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("background.png"));
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                parent.setScreen(new MyShuntGame(parent, assets));
                return true;
            }
        });
        scoreMaker();
    }

    @Override
    public void render(float delta) {
        serviceFunction();
        drawingBatch.begin();
        drawingBatch.draw(background, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font24.draw(drawingBatch, "TOTAL SCORE  " + totalScore, Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 2.5f);
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
