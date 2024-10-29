package io.example.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. 
 * This is the main file of the game.
 * @author Thomas Nash
*/
public class Main extends Game {
    private SpriteBatch batch;
    ExtendViewport viewport;
    CameraManager camManager;

    Sound dropSound;
    Music music;

    // world coords of mouse pos this frame
    Vector2 worldMousePos;
    // screen pos of mouse this frame.
    Vector2 screenMousePos;

    GameManager gameManager;

    /**
     * Called once at the start of the game.
     */
    @Override
    public void create() {
        Assets.loadTextures();
        batch = new SpriteBatch();
        // TODO: Fix the viewport
        viewport = new ExtendViewport(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);
        camManager = new CameraManager(viewport.getCamera());

        // used for input.
        worldMousePos = new Vector2();
        screenMousePos = new Vector2();

        // manager
        gameManager = new GameManager();
        gameManager.generateMap(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);

        // sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        // Logging
        if (Consts.DEBUG_MODE_ON) {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * This method is called by libgdx each time the window for the game
     * is clicked off of. For example, this method would be called if the game
     * was minimised by the user.S
     */
    @Override
    public void pause() {
    }


    /**
     * The main loop of the game.
     */
    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        screenMousePos.set(Gdx.input.getX(), Gdx.input.getY());
        worldMousePos.set(screenMousePos);
        // converts screen coords to world coords.
        viewport.unproject(worldMousePos);

        camManager.processCameraInput();
        gameManager.processInput(worldMousePos);
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        gameManager.update(delta);
    }

    private void draw() {
        // Clears the screen to red.
        ScreenUtils.clear(Color.RED);
        viewport.apply();
        // updates the camera view.
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // all drawing should take place between begin and end.
        batch.begin();
        gameManager.draw(batch);
        batch.end();
    }
}
