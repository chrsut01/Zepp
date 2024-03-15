package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {
    final Plane game;
    Texture planeImage;
    Texture zeppelinImage;

    Texture scrollSkyImage1;
    Texture scrollSkyImage2;
    int planesHit;
    Sound planeFlyingSound;
    Sound planeCrashSound;
    Music zepEngineSound;

    OrthographicCamera camera;
    Rectangle zeppelin;
    Array<Rectangle> planes;
    long lastPlaneTime;
    private float backgroundVelocity = 0.5f;
    private float backgroundX = 0;
    private float backgroundY = 0;
    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;
   // int screenWidth = 800;
    //int screenHeight = 480;
    private static final float zeppelinImageWidth = 1567;
    private static final float zeppelinImageHeight = 218;
    private static final float width = zeppelinImageWidth / 3;
    private static final float height = zeppelinImageHeight / 3;

    public GameScreen(final Plane game) {
        this.game = game;

        // load the images for the plane
        planeImage = new Texture(Gdx.files.internal("sopwith_camel_small.png"));
        //zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));
        zeppelinImage = new Texture(Gdx.files.internal("Dirigible-Zeppelin-L59.png"));

        zepEngineSound = Gdx.audio.newMusic(Gdx.files.internal("ZeppelinEngine.mp3"));

        scrollSkyImage1 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));
        scrollSkyImage2 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));

        planeFlyingSound = Gdx.audio.newSound(Gdx.files.internal("plane_flying.mp3"));
        planeCrashSound = Gdx.audio.newSound(Gdx.files.internal("plane_crash.mp3"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        // create a Rectangle to logically represent the zeppelin
        zeppelin = new Rectangle();
        zeppelin.width = width;
        zeppelin.height = height;
        // the bottom screen edge
        zeppelin.x = screenWidth / 2 - width / 1.5f; // center the zeppelin horizontally
        zeppelin.y = screenHeight / 2 - height / 2; // center the zeppelin vertically

        planes = new Array<>();
        spawnPlane();

    }

    private void spawnPlane() {
        Rectangle plane = new Rectangle();
        plane.y = MathUtils.random(0, screenHeight - 44);
        plane.x = screenWidth;
        plane.width = 44;
        plane.height = 44;
        planes.add(plane);
        planeFlyingSound.play();
       // lastPlaneTime = TimeUtils.nanoTime();
        lastPlaneTime = TimeUtils.millis() + 4000;


     /*   float x = screenWidth;
        float y = random((screenHeight/4), screenHeight - (screenHeight/4)); // ensures planes enter screen at least 1/4 screen-height from top and bottom
        int yAngle = random.nextInt(120) - 60;

        Plane plane = new Plane();
        planeFlyingSound.play();
        planes.add(plane);
        lastPlaneTime = TimeUtils.millis() + 4000; // sets the delay time to 4 seconds*/

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch...
        game.batch.begin();

        game.batch.draw(scrollSkyImage1,  backgroundX, backgroundY, 16384, 1856);
        game.batch.draw(scrollSkyImage2,  backgroundX + 16384, backgroundY, 16384, 1856);

        // Set the font size
        game.font.getData().setScale(2f);
        // draw the Drops Collected score
        game.font.draw(game.batch, "Planes hit: " + planesHit, 5, 475);
        // shows backgroundX
        game.font.draw(game.batch, "backgroundX: " + backgroundX, 5, 450);

        // draw the bucket and all drops
        game.batch.draw(zeppelinImage, zeppelin.x, zeppelin.y, zeppelin.width, zeppelin.height);
        for (Rectangle plane : planes) {
            game.batch.draw(planeImage, plane.x, plane.y, plane.width, plane.height);
        }



        game.batch.end();

        backgroundX -= backgroundVelocity;
        if (backgroundX <= -32768.0 + screenWidth){ //this number comes from 2 X 16384(image width) - screenWidth (screen width)
            backgroundX = 0;
        }



        // Handle user input for zeppelin movement
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); // If needed
            zeppelin.x = touchPos.x - width / 2;
        }
    /*
     //  moves zeppelin up and down - probably not needed for our game
     if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            zeppelin.x -= 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            zeppelin.x += 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (zeppelin.x < 0)
            zeppelin.x = 0;
        if (zeppelin.x > screenWidth - width)
            zeppelin.x = screenWidth - width;*/

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            zeppelin.y += 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            zeppelin.y -= 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (zeppelin.y < 0)
            zeppelin.y = 0;
        if (zeppelin.y > screenHeight - height)
            zeppelin.y = screenHeight - height;

        if (TimeUtils.timeSinceMillis(lastPlaneTime) > 4000)
            spawnPlane();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Rectangle> iter = planes.iterator();
        while (iter.hasNext()) {
            Rectangle plane = iter.next();
            plane.x -= 200 * Gdx.graphics.getDeltaTime();
            plane.y -= 30 * Gdx.graphics.getDeltaTime();
            if (plane.y + 44 < 0)
                iter.remove();
            if (plane.overlaps(zeppelin)) {
                planesHit++;
                planeCrashSound.play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        planeFlyingSound.stop();
                    }
                }, 0.25F); // 1 second delay
                iter.remove();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        // Adjust the viewport to the new screen size
        camera.setToOrtho(false, screenWidth, screenHeight);
    }

    @Override
    public void show() {
        zepEngineSound.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        zepEngineSound.dispose();
        zeppelinImage.dispose();
        planeImage.dispose();
        planeFlyingSound.dispose();
        planeCrashSound.dispose();
        scrollSkyImage1.dispose();
        scrollSkyImage2.dispose();
    }

}
