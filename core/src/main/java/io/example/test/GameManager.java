package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.example.test.Building.BuildingType;

// Game manager holds all the logic of the game i guess idk.
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

    // This method should be called directly after the game manager is instantiated. 
    // Can only be called once.
    public void generateMap(int width, int height) {
        if (gameMap == null) gameMap = new GameMap(studentManager, width, height);
        studentManager.useMap(gameMap);
    }

    public static void activateColour(Colours colour) {
        if (activatedColours.contains(colour)) return;
        activatedColours.add(colour);
    }
    public static boolean isColourActivated(Colours colour) {
        if (activatedColours.contains(colour)) return true;
        return false;
    }
    public static Colours getRandomColour() {
        return Colours.RED;
    }


    public static int addMoney(int money) {
        _money += money;
        return _money;
    }
    public static int removeMoney(int money) {
        _money -= money;
        return _money;
    }
    public boolean isBankrupt() {
        if (_money < 0) return true;
        return false;
    }

    // ALL INPUT STUFF GOES HERE
    public void processInput(Vector2 touchPos) {
        // touch pos should be the world coordinates, not screen coordinates. 
        
        // use 1 to select accommodation and 2 to select path, 3 to select lecture
        // theatre.
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            isPathSelected = false;
            selectedBuilding = BuildingType.Accommodation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            isPathSelected = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            isPathSelected = false;
            selectedBuilding = BuildingType.LectureTheatre;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            
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

    
    public void update(float deltaTime) {
        studentManager.update(deltaTime);
    }


    public void draw(SpriteBatch batch) {
        gameMap.draw(batch);
        studentManager.drawStudents(batch);
    }
}
