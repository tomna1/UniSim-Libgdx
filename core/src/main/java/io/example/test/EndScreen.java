package io.example.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.test.game.util.Consts;

public class EndScreen implements Screen {
    private Main main;
    private StretchViewport viewport;
    private int score;
    private Stage stage;
    private Label scoreLabel;


    public EndScreen(Main main, int score) {
        this.main = main;
        viewport = main.getViewport();
        this.score = score;
        stage = new Stage(viewport);
        
        // Score label just contains the score that the player go at the end of the game.
        Skin scoreLabelSkin = new Skin(Gdx.files.internal("textures/ui/uiskin.json"));
        scoreLabel = new Label("Score : " + score, scoreLabelSkin);
        scoreLabel.setFontScale(3);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(Consts.GRID_WIDTH/2.0f, Consts.GRID_HEIGHT/2.0f, Align.center);
        stage.addActor(scoreLabel);
        scoreLabelSkin.dispose();
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
        viewport.apply();
        ScreenUtils.clear(Color.BLUE);
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
