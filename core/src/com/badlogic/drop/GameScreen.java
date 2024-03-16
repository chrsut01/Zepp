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
    Texture mountainImage;
    int planesHit;
    Sound planeFlyingSound;
    Sound planeCrashSound;
    Music zepEngineSound;

    OrthographicCamera camera;
    Rectangle zeppelin;
    Rectangle zeppelinHitBox;
    Rectangle mountain;
    Array<Rectangle> planes;
    long lastPlaneTime;
    private float backgroundVelocity = 0.5f;
    private float mountainVelocity = 1f;
    private float backgroundX = 0;
    private float backgroundY = 0;
    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;
   // int screenWidth = 800;
    //int screenHeight = 480;
    int paddingX = 5;
    int paddingY = 5;
    private static final float zeppelinImageWidth = 1567;
    private static final float zeppelinImageHeight = 218;
    private static final float width = zeppelinImageWidth / 2.75f;
    private static final float height = zeppelinImageHeight / 2.75f;

    public GameScreen(final Plane game) {
        this.game = game;

        // load the images for the plane
        planeImage = new Texture(Gdx.files.internal("sopwith_camel_small.png"));
        //zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));
        zeppelinImage = new Texture(Gdx.files.internal("Dirigible-Zeppelin-L59.png"));

        zepEngineSound = Gdx.audio.newMusic(Gdx.files.internal("ZeppelinEngine.mp3"));

        scrollSkyImage1 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));
        scrollSkyImage2 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));

        mountainImage = new Texture(Gdx.files.internal("mountain-transparency.png"));

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

        zeppelinHitBox = new Rectangle(zeppelin.x + paddingX, zeppelin.y + paddingY, zeppelin.width - 2 * paddingX, zeppelin.height - 2 * paddingY);

        planes = new Array<>();
        spawnPlane();
        spawnMountain();

    }

    private void spawnMountain() {
        mountain = new Rectangle();
        mountain.y = 0;
        mountain.x = screenWidth;
        mountain.width = 1050;
        mountain.height = 443;
       // planes.add(mountain);
    }

    private void spawnPlane() {
        Rectangle plane = new Rectangle();
        plane.y = MathUtils.random(0, screenHeight - 44);
        plane.x = screenWidth;
        plane.width = 44;
        plane.height = 44;
        planes.add(plane);
        planeFlyingSound.play();

        // Randomize the delay before the next plane spawn
        float randomSpawnDelay = MathUtils.random(0.5f, 4f); // Adjust the range as needed
        lastPlaneTime = TimeUtils.millis() + (long) (randomSpawnDelay * 1000); // Convert to milliseconds
        //lastPlaneTime = TimeUtils.millis() + 4000;
       // lastPlaneTime = TimeUtils.nanoTime();

        // Occasionally create a cluster (e.g., 10% chance)
        if (MathUtils.randomBoolean(0.1f)) {
            spawnCluster();
        }


     /*   float x = screenWidth;
        float y = random((screenHeight/4), screenHeight - (screenHeight/4)); // ensures planes enter screen at least 1/4 screen-height from top and bottom
        int yAngle = random.nextInt(120) - 60;

        Plane plane = new Plane();
        planeFlyingSound.play();
        planes.add(plane);
        lastPlaneTime = TimeUtils.millis() + 4000; // sets the delay time to 4 seconds*/

    }
    private void spawnCluster() {
        // Create additional planes within a short time frame
        for (int i = 0; i < 3; i++) {
            spawnPlane();
        }
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

        game.batch.draw(mountainImage,  mountain.x, mountain.y, mountain.width, mountain.height);

        // Set the font size
        game.font.getData().setScale(2f);
        // draw the Drops Collected score
        game.font.draw(game.batch, "Planes hit: " + planesHit, 5, 475);
        // shows backgroundX
       // game.font.draw(game.batch, "backgroundX: " + backgroundX, 5, 450);

        // draw the bucket and all drops
        game.batch.draw(zeppelinImage, zeppelin.x, zeppelin.y, zeppelin.width, zeppelin.height);
        for (Rectangle plane : planes) {
            game.batch.draw(planeImage, plane.x, plane.y, plane.width, plane.height);
        }


        // Move the mountain across the screen
        mountain.x -= (backgroundVelocity * 2); // Move the mountain faster than the sky

        // Check if the mountain has moved off the screen
     /*   if (mountain.x + mountain.width < 0) {
            // Respawn the mountain at the right edge of the screen
            mountain.x = screenWidth;
        }*/

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
            zeppelinHitBox.setX(zeppelin.x - paddingX);
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
            zeppelinHitBox.setY(zeppelin.y - paddingY);

        if (TimeUtils.timeSinceMillis(lastPlaneTime) > 4000)
            spawnPlane();

        // Generate a random y-axis angle for each plane
        int[] yAngles = new int[planes.size];
        for (int i = 0; i < planes.size; i++) {
            yAngles[i] = random.nextInt(120) - 60; // Random angle between -60 and 60 degrees
        }
        Iterator<Rectangle> iter = planes.iterator();
        int index = 0;
        while (iter.hasNext()) {
            Rectangle plane = iter.next();
            // Get the random y-axis angle for this plane
            int yAngle = yAngles[index];

            // Calculate the random y-axis angle
            //int yAngle = random.nextInt(120) - 60;// Random angle between -60 and 60 degrees

            // Calculate the y-axis movement based on the angle
            //float yMovement = 30 * MathUtils.sinDeg(yAngle) * Gdx.graphics.getDeltaTime();
            float yMovement = 30 * MathUtils.sinDeg(yAngle);

            plane.x -= 250 * Gdx.graphics.getDeltaTime();
            //plane.y -= yAngle * Gdx.graphics.getDeltaTime();
            plane.y -= yMovement * Gdx.graphics.getDeltaTime();

            if (plane.y + 44 < 0)
                iter.remove();
            if (plane.overlaps(zeppelinHitBox)) {
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
            index++;
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
        mountainImage.dispose();
    }

}
