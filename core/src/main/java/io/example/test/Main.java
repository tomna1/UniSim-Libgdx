package io.example.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.example.test.GameMap.Buildable;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    ExtendViewport viewport;

    Sound dropSound;
    Music music;

    Vector2 touchPos;

    static final int gridWidth = 20;
    static final int gridHeight = 20;

    GameManager gameManager;

    GameMap.Buildable selectedBuilable = Buildable.Accommodation;

    @Override
    // Called once at the start of the game.
    public void create() {
        Assets.loadTextures();
        batch = new SpriteBatch();
        // TODO: Fix the viewport
        viewport = new ExtendViewport(gridWidth, gridHeight);

        // used for input.
        touchPos = new Vector2();

        // manager
        gameManager = new GameManager();
        gameManager.generateMap(gridWidth, gridHeight);

        // sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
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
