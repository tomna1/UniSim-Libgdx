package io.example.test;

import java.util.ArrayList;

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

    // how much money the player has.
    private int money;

    // the overall satisfaction, an average of all satisfactions.
    
    // the satisfaction a student gain from learning. more learning = more satisfaction.
    public float studentLearningSatisfaction;
    // the satisfaction a student gains from travelling. shorts journeys = more satisfaction,
    // long journeys = less satisfaction.
    public float studentDistanceTravelledSatisfaction;

    private ArrayList<Student> students;

    // Used to store only the accomoodation. A copy is in the buildings array.
    private ArrayList<Accommodation> accommodation;
    // Used to store only the lecture theatres. A copy is in the buildinds arrray.
    private ArrayList<LectureTheatre> lectureTheatres;

    private GameMap gameMap;


    public GameManager() {
        activatedColours = new ArrayList<Colours>();
        activatedColours.add(Colours.RED);
        activatedColours.add(Colours.BLUE);
        money = 1000;
        studentDistanceTravelledSatisfaction = 100.0f;
        studentLearningSatisfaction = 100.0f;
        students = new ArrayList<Student>();
        accommodation = new ArrayList<Accommodation>();
        lectureTheatres = new ArrayList<LectureTheatre>();
    }
    // This must be called immediately after initialisation or bad things will occur.
    public void useGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
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



}
