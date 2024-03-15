package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Zeppelin extends Rectangle {
    private static final float width = 783;
    private static final float height = 109;
    private Texture zeppelinImage;
    private Sound engineSound;
    static float screenWidth = GameConfig.SCREEN_WIDTH;
    static float screenHeight = GameConfig.SCREEN_HEIGHT;


    public Zeppelin() {
        super(); // Call the default Rectangle constructor
        init();
    }
    private void init() {
        // Set default dimensions and position
        set(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, width, height);

        // Load textures and sounds
        //zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));
        zeppelinImage = new Texture(Gdx.files.internal("Dirigibile-Zeppelin-L59.png"));
        engineSound = Gdx.audio.newSound(Gdx.files.internal("ZeppelinEngine.mp3"));

        playEngineSound(2.2f); // Set the initial volume (you can change this value)
    }
   /* public static void setScreenDimensions(float width, float height) {
        screenWidth = width;
        screenHeight = height;
    }*/
    public void update() {
        handleInput();
    }

    public Texture getZeppelinImage() {
        return zeppelinImage;
    }


    public void playEngineSound(float volume) {
        engineSound.loop(volume);
        // You can add more parameters for pitch, pan, etc., based on your requirements.
    }

    private void handleInput() {
        // Handle user input for zeppelin movement
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // camera.unproject(touchPos); // If needed
            x = touchPos.x - width / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            x -= 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            x += 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (x < 0)
            x = 0;
        if (x > screenWidth - width)
            x = screenWidth - width;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            y += 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            y -= 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (y < 0)
            y = 0;
        if (y > screenHeight - height)
            y = screenHeight - height;
    }

    public void dispose() {
        zeppelinImage.dispose();
        engineSound.dispose();
    }
}
