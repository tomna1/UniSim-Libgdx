package io.example.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.example.test.game.GameScreen;
import io.example.test.game.util.Assets;
import io.example.test.game.util.Consts;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. 
 * This is the main file of the game where the game loop takes place.
 * @author Thomas Nash
*/
public class Main extends Game {
    private ExtendViewport viewport;

    private StartScreen startScreen;
    private GameScreen gameScreen;
    private EndScreen endScreen;

    public ExtendViewport getViewport() { return viewport; }

    /**
     * Called once at the start of the game.
     */
    @Override
    public void create() {
        Assets.loadTextures();
        // TODO: Fix the viewport
        viewport = new ExtendViewport(Consts.GRID_WIDTH, Consts.GRID_HEIGHT);

        // Logging
        if (Consts.DEBUG_MODE_ON) {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }

        startScreen = new StartScreen(this);
        gameScreen = new GameScreen(this);
        this.setScreen(startScreen);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        super.render();
    }

    /**
     * This method is called by libgdx each time the window for the game
     * is clicked off of. For example, this method would be called if the game
     * was minimised by the user.
     */
    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }

    public void startGame() {
        gameScreen = new GameScreen(this);
        this.setScreen(gameScreen);
        startScreen.dispose();
    }

    public void endGame() {
        endScreen = new EndScreen(this);
        this.setScreen(endScreen);
        gameScreen.dispose();
    }
}
