package io.example.test.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.example.test.Main;
import io.example.test.game.util.Consts;

public class GameScreen implements Screen {
    private Main main;
    private ExtendViewport viewport;

    private SpriteBatch batch;

    private CameraManager camManager;

    private GameManager gameManager;

    // world coords of mouse pos this frame
    private Vector2 worldMousePos;
    // screen pos of mouse this frame.
    private Vector2 screenMousePos;

    public GameScreen(Main main) {
        this.main = main;
        viewport = main.getViewport();

        camManager = new CameraManager(viewport.getCamera());

        // used for input.
        worldMousePos = new Vector2();
        screenMousePos = new Vector2();

        // manager
        gameManager = new GameManager();
        gameManager.generateMap(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);

        batch = new SpriteBatch();
    }
    
    @Override
    public void show() {

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
    public void render(float deltaTime) {
        input();
        logic();
        draw();
    }

    /**
     * All input stuff goes into this method.
     */
    private void input() {
        screenMousePos.set(Gdx.input.getX(), Gdx.input.getY());
        worldMousePos.set(screenMousePos);
        // converts screen coords to world coords.
        viewport.unproject(worldMousePos);

        camManager.processCameraInput();
        gameManager.processInput(worldMousePos);
    }

    /**
     * All logic should happen in this method.
     */
    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        gameManager.update(delta);
    }

    /**
     * All drawing should take place in this method.
     */
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



    /**
     * This method is overriden by {@link Main#resize(int, int)}.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
