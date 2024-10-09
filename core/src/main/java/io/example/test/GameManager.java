package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.UnweightedMeshSpawnShapeValue;
import com.badlogic.gdx.math.Vector2;

import io.example.test.GameMap.Buildable;
import io.example.test.GameMap.BuildingType;

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
    private ArrayList<Colours> activatedColours;

    private BuildingType selectedBuilding = BuildingType.Accommodation;
    private Buildable selectedBuildable = Buildable.Building;
    // How long the game manager should wait before updating the meters of each
    // student.
    private static final float timeBetweenStudentMeterUpdates = 1.0f;

    // how much money the player has.
    private int money;

    // the satisfaction a student gain from learning. more learning = more satisfaction.
    public float studentLearningSatisfaction;
    // the satisfaction a student gains from travelling. shorts journeys = more satisfaction,
    // long journeys = less satisfaction.
    public float studentDistanceTravelledSatisfaction;


    private GameMap gameMap;

    // could be implemented as a hashmap instead
    private ArrayList<Student> students;
    
    // could be implemented as a hashmap instead.
    private ArrayList<Building> buildings = new ArrayList<>();

    private UniqueIDGiver studentIDGiver = new UniqueIDGiver();
    

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
    }

    // This method should be called directly after the game manager is instantiated. 
    // Can only be called once.
    public void generateMap(int width, int height) {
        if (gameMap == null) gameMap = new GameMap(width, height);
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
            selectedBuildable = Buildable.Building;
            selectedBuilding = BuildingType.Accommodation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            selectedBuildable = Buildable.Path;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            selectedBuildable = Buildable.Building;
            selectedBuilding = BuildingType.LectureTheatre;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            if (students.size() != 0) {
                Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
                students.get(0).travelTo(gameMap, pos);
            } 
        }
        
        // If left click, add the selected buildable.
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (selectedBuildable == Buildable.Path) {
                gameMap.addPath((int)touchPos.x, (int)touchPos.y);
            } 
            else {
                int buildID = gameMap.addBuilding(selectedBuilding, (int)touchPos.x, (int)touchPos.y);
                
                if (selectedBuilding == BuildingType.Accommodation && buildID != -1) {
                    Vector2i homePos = new Vector2i((int)touchPos.x, (int)touchPos.y);
                    Accommodation build = new Accommodation(buildID);
                    int studentID;
                    for (int i = 0; i < Accommodation.getStudentSize(); i++) {
                        studentID = studentIDGiver.next();
                        Student stu = new Student(studentID,"", getRandomColour(), buildID, homePos.x, homePos.y);
                        students.add(stu);
                        build.addStudent(studentID);
                    }
                    buildings.add(build);
                } 
                
                else if (selectedBuilding == BuildingType.LectureTheatre && buildID != 0) {

                }
            }
        }
        // If right click, remove building.
        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2i pos = new Vector2i((int)touchPos.x, (int)touchPos.y);
            if (gameMap.isBuildingAtPoint(pos.x, pos.y) == false) {
                // removes that path at the point
                gameMap.removeBuildableAtPoint(pos.x, pos.y);
                return;
            } 
            
            BuildingType type = gameMap.getBuildTypeAtPoint(pos.x, pos.y);
            int buildId = gameMap.removeBuildableAtPoint(pos.x, pos.y);
            // System.out.println("buildID = " + buildId);
            if (type == BuildingType.Accommodation) {
                for (int i = 0; i < students.size(); i++) {
                    // System.out.println("homeID = " + students.get(i).getHomeId());
                    if (students.get(i).getHomeId() == buildId) {
                        studentIDGiver.returnID(students.get(i).getId());
                        students.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    // updates all the necessary stats.
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


    private void drawStudents(SpriteBatch batch) {
        if (students.size() == 0) return;
        for (Student stu : students) {
            stu.draw(batch);
        }
    }

    public void draw(SpriteBatch batch) {
        gameMap.draw(batch);
        drawStudents(batch);
    }



}
