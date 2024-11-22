package io.example.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.test.game.util.Assets;
import io.example.test.game.util.Consts;

public class StartScreen implements Screen {
    private Main main;
    private StretchViewport viewport;
    private Stage stage;
    private ImageButton startButton;


    public StartScreen(Main main) {
        this.main = main;
        viewport = main.getViewport();
        stage = new Stage(viewport);
        setupButton();
        stage.addActor(startButton);
    }

    public void setupButton() {
        Drawable startButtonUp = new TextureRegionDrawable(Assets.startButtonUp);
        Drawable startButtonDown = new TextureRegionDrawable(Assets.startButtonDown);
        startButton = new ImageButton(startButtonUp, startButtonDown, startButtonDown);
        startButton.setSize(5, 5);
        startButton.setPosition(Consts.GRID_WIDTH / 2.0f, Consts.GRID_HEIGHT / 2.0f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.startGame();
            } 
        });
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        ScreenUtils.clear(Color.GREEN);
        stage.act(deltaTime);
        stage.draw();
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
        stage.dispose();
    }
}
