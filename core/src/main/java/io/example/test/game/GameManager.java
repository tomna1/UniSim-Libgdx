package io.example.test.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.example.test.game.building.Building.BuildingType;
import io.example.test.game.gamemap.GameMap;
import io.example.test.game.student.StudentManager;
import io.example.test.game.util.Consts;
import io.example.test.game.util.Vector2i;

/**
 * The game manager holds all information about the game.
 * @author Thomas Nash
 */
public class GameManager {
    public enum Colours {
        GREEN,
        RED, 
        BLUE,
        ORANGE,
        PURPLE
    }

    // Each colour represents a university course/department. A student which has
    // the colour red can only attend lectures which also have the colour red.
    private static ArrayList<Colours> activatedColours;

    // used to choose which building to build.
    private BuildingType selectedBuilding = BuildingType.Accommodation;
    // used to build paths
    private boolean isPathSelected = false;

    // how much money the player has.
    private static int _money = Consts.STARTING_MONEY;

    // The map that the game takse place on.
    private GameMap gameMap;

    // Used to manage all students.
    private StudentManager studentManager;
    

    // used for managing deltatime between update calls.
    float totalDeltaTime = 0;


    public GameManager() {
        activatedColours = new ArrayList<Colours>();
        activatedColours.add(Colours.RED);
        activatedColours.add(Colours.BLUE);
        studentManager = new StudentManager();
    }

    /**
     * This methods generates a map with the specified width and height. This
     * should be called after the game manager is instantiated. This method
     * can only be called once.
     * @param width Width of the map.
     * @param height Height of the map.
     */
    public void generateMap(int width, int height) {
        if (gameMap == null) gameMap = new GameMap(studentManager, width, height);
        studentManager.useMap(gameMap);
    }

    /**
     * Activates a new colour which allows that colour to be generated with the
     * {@link #getRandomColour()} method.
     * @param colour The colour to activate.
     */
    public static void activateColour(Colours colour) {
        if (activatedColours.contains(colour)) return;
        activatedColours.add(colour);
    }
    /**
     * Checks if a colour has already been activated.
     * @param colour The colour to check.
     * @return true if the colour has been activated and false if not.
     */
    public static boolean isColourActivated(Colours colour) {
        if (activatedColours.contains(colour)) return true;
        return false;
    }
    public static Colours getRandomColour() {
        return Colours.RED;
    }

    /**
     * Adds the specified amount of money to the player. Does this by doing
     * _money += money.
     * @param money The amount of money to add. Should be positive.
     * @return The new total amount of money.
     */
    public static int addMoney(int money) {
        _money += money;
        return _money;
    }
    /**
     * Removes the specified amount of money from the player. Does thi by doing
     * _money -= money.
     * @param money The amount of money to remove. Should be positive.
     * @return The new total amount of money.
     */
    public static int removeMoney(int money) {
        _money -= money;
        return _money;
    }
    public boolean isBankrupt() {
        if (_money < 0) return true;
        return false;
    }

    /**
     * All input related stuff goes into this method.
     * @param touchPos The point of the grid that is being touched. (World)
     * coords not screen coords).
     */
    public void processInput(Vector2 touchPos) {
        // touch pos should be the world coordinates, not screen coordinates. 
        
        // use 1 to select accommodation and 2 to select path, 3 to select lecture
        // theatre and 4 to select restaurant.
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            isPathSelected = false;
            selectedBuilding = BuildingType.Accommodation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            isPathSelected = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            isPathSelected = false;
            selectedBuilding = BuildingType.LectureTheatre;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            isPathSelected = false;
            selectedBuilding = BuildingType.Restaurant;
        }
        
        // If left click, add the selected buildable.
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (isPathSelected) {
                gameMap.addPath((int)touchPos.x, (int)touchPos.y);
            } 
            else {
                gameMap.addBuilding(selectedBuilding, (int)touchPos.x, (int)touchPos.y);
            }
        }
        // If right click, remove building.
        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
            gameMap.removeBuildableAtPoint(pos.x, pos.y);
        }
    }

    /**
     * Calls {@link StudentManager#update(float)} and {@link GameMap#update(float)}.
     * @param deltaTime The time since the last frame.
     */
    public void update(float deltaTime) {
        studentManager.update(deltaTime);
        gameMap.update(deltaTime);
    }

    /**
     * Draws the game map and the students onto the screen.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        gameMap.draw(batch);
        studentManager.drawStudents(batch);
    }
}
