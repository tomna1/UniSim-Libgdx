package io.example.test;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class StartScreen implements Screen {
    private Main main;
    private ExtendViewport viewport;

    public StartScreen(Main main) {
        this.main = main;
        viewport = main.getViewport();
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

    }
}
