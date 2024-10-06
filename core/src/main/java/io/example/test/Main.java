package io.example.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.example.test.Accommodation;
import io.example.test.GameMap.Buildable;
import io.example.test.Grid.TileType;
import io.example.test.GameMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    ExtendViewport viewport;

    Sound dropSound;
    Music music;

    Vector2 touchPos;

    static final int gridWidth = 20;
    static final int gridHeight = 20;

    GameMap gameMap;
    GameManager gameManager;

    GameMap.Buildable selectedBuilable = Buildable.Accommodation;

    int i = 0;

    @Override
    // Called once at the start of the game.
    public void create() {
        Assets.loadTextures();
        batch = new SpriteBatch();
        // TODO: Fix the viewport
        viewport = new ExtendViewport(gridWidth, gridHeight);

        // used for input.
        touchPos = new Vector2();

        // map
        gameMap = new GameMap(gridWidth, gridHeight);

        // manager
        gameManager = new GameManager();
        gameManager.useGameMap(gameMap);

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
        if (i == 0) {
            gameMap.addBuildable(Buildable.Accommodation, 0, 0);
            i++;
        }
        
        float delta = Gdx.graphics.getDeltaTime();

        // use 1 to select accommodation and 2 to select path, 3 to select lecture
        // theatre.
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            selectedBuilable = Buildable.Accommodation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            selectedBuilable = Buildable.Path;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            selectedBuilable = Buildable.LectureTheatre;
        }
        
        // If left click, add the selected buildable.
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            gameMap.addBuildable(selectedBuilable, (int)touchPos.x, (int)touchPos.y);
        }
        // If right click, remove building.
        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
            gameMap.removeBuildableAtPoint(pos);
        }

        
    }

    private void logic() {

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
        gameMap.draw(batch);
        batch.end();
    }
}
