package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.example.test.GameMap.Buildable;

// Game manager holds all the logic of the game i guess idk.
public class GameManager {
    public enum Colours {
        GREEN,
        RED, 
        BLUE,
        ORANGE,
        PURPLE
    }
    // How long the game manager should wait before updating the meters of each
    // student.
    private static final float timeBetweenStudentMeterUpdates = 1.0f;

    // Each colour represents a university course/department. A student which has
    // the colour red can only attend lectures which also have the colour red.
    private ArrayList<Colours> activatedColours;

    // how much money the player has.
    private int money;

    // the satisfaction a student gain from learning. more learning = more satisfaction.
    public float studentLearningSatisfaction;
    // the satisfaction a student gains from travelling. shorts journeys = more satisfaction,
    // long journeys = less satisfaction.
    public float studentDistanceTravelledSatisfaction;

    private ArrayList<Student> students;


    private GameMap gameMap;

    GameMap.Buildable selectedBuilable;

    // used for managing deltatime between update calls.
    float totalDeltaTime = 0;


    public GameManager() {
        activatedColours = new ArrayList<Colours>();
        activatedColours.add(Colours.RED);
        activatedColours.add(Colours.BLUE);
        money = 1000;
        studentDistanceTravelledSatisfaction = 100.0f;
        studentLearningSatisfaction = 100.0f;
        students = new ArrayList<Student>();
        selectedBuilable = Buildable.Accommodation;
    }

    // This method should be called directly after the game manager is instantiated. 
    // Can only be called once.
    public void generateMap(int width, int height) {
        if (gameMap == null) gameMap = new GameMap(width, height);
    }
    // adds a buildable to the map. Returns true if successfully added and false
    // otherwise.
    boolean addBuildable(Buildable type, int posX, int posY) {
        if (gameMap == null) return false;
        return gameMap.addBuildable(type, posX, posY);
    }
    // Removes a buildable from the map where the building is at the point pos.
    public boolean removeBuildableAtPoint(int posX, int posY) {
        if (gameMap == null) return false;
        return gameMap.removeBuildableAtPoint(posX, posY);
    }
    

    public void activateColour(Colours colour) {
        if (activatedColours.contains(colour)) return;
        activatedColours.add(colour);
    }
    public boolean isColourActivated(Colours colour) {
        if (activatedColours.contains(colour)) return true;
        return false;
    }
    public Colours getRandomColour() {
        return Colours.RED;
    }


    public int addMoney(int money) {
        this.money += money;
        return this.money;
    }
    public int removeMoney(int money) {
        this.money -= money;
        return this.money;
    }
    public boolean isBankrupt() {
        if (money < 0) return true;
        return false;
    }

    // returns the overall satisfaction.
    public float getSatisfaction() {
        return (studentLearningSatisfaction + studentDistanceTravelledSatisfaction) / 2;
    }


    // ALL INPUT STUFF GOES HERE
    public void processInput(Vector2 touchPos) {
        // touch pos should be the world coordinates, not screen coordinates. 
        
        // use 1 to select accommodation and 2 to select path, 3 to select lecture
        // theatre.
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            selectedBuilable = Buildable.Accommodation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            selectedBuilable = Buildable.Path;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            selectedBuilable = Buildable.LectureTheatre;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            if (students.size() != 0) {
                Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
                students.get(0).travelTo(gameMap, pos);
            } 
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            if (students.size() != 0) {
                System.out.println("Learning Meter = " + students.get(0).getLearningMeter());
            } 
        } 
        
        // If left click, add the selected buildable.
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            boolean isBuilt = addBuildable(selectedBuilable, (int)touchPos.x, (int)touchPos.y);
            if (selectedBuilable == Buildable.Accommodation && isBuilt) {
                Vector2i homePos = new Vector2i((int)touchPos.x, (int)touchPos.y);
                for (int i = 0; i < Accommodation.getStudentSize(); i++) {
                    Student stu = new Student("", getRandomColour(), homePos);
                    students.add(stu);
                }
            }
        }
        // If right click, remove building.
        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
            removeBuildableAtPoint(pos.x, pos.y);
        }
    }


    public void update(float deltaTime) {
        totalDeltaTime += deltaTime;
        boolean updateMeters = false;
        if (totalDeltaTime > timeBetweenStudentMeterUpdates) {
            updateMeters = true;
            totalDeltaTime = 0.0f;
        }
        for (Student stu : students) {
            stu.update(deltaTime, updateMeters);
        }
    }


    private void drawMap(SpriteBatch batch) {
        if (gameMap == null) return;
        gameMap.draw(batch);
    }
    private void drawStudents(SpriteBatch batch) {
        if (students.size() == 0) return;
        for (Student stu : students) {
            batch.draw(Assets.studentTexture, stu.getPosX(), stu.getPosY(), 1, 1);
        }
    }

    public void draw(SpriteBatch batch) {
        drawMap(batch);
        drawStudents(batch);
    }



}
