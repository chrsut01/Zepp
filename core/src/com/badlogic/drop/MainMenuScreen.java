package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Plane game;

    Texture backgroundImage;
    OrthographicCamera camera;
    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;
    private float backgroundX = 0;
    private float backgroundY = 0;

    public MainMenuScreen(final Plane game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        backgroundImage = new Texture(Gdx.files.internal("main-menu-background.jpg"));

    }

    @Override
    public void show() {

    }

     @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 0);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);

        game.font.draw(game.batch, "Velkommen til Arika-Schiff!!! ", screenWidth / 2 -200, screenHeight / 2 + 25);
        game.font.draw(game.batch, "Tryk på mellemrumstasten for at begynde!", screenWidth / 2 - 200, screenHeight / 2 - 50);

        // Set the font size
        game.font.getData().setScale(2f);

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
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

    @Override
    public void dispose() {
    backgroundImage.dispose();
    }

}
