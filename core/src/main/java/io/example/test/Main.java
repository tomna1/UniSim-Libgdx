package io.example.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    ExtendViewport viewport;

    Sound dropSound;
    Music music;

    Vector2 touchPos;

    GameManager gameManager;

    // Called once at the start of the game.
    @Override
    public void create() {
        Assets.loadTextures();
        batch = new SpriteBatch();
        // TODO: Fix the viewport
        viewport = new ExtendViewport(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);

        // used for input.
        touchPos = new Vector2();

        // manager
        gameManager = new GameManager();
        gameManager.generateMap(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);

        // sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        if (Consts.DEBUG_MODE_ON) {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
         
    }

    @Override
    // The main loop of the game.
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }



    private void input() {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        gameManager.processInput(touchPos);
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        gameManager.update(delta);
    }

    private void draw() {
        // Clears the screen to red.
        ScreenUtils.clear(Color.RED);
        // Only one viewport so unnecessary but ill keep it here.
        viewport.apply();
        // updates the camera view.
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // all drawing should take place between begin and end.
        batch.begin();
        gameManager.draw(batch);
        batch.end();
    }
}
