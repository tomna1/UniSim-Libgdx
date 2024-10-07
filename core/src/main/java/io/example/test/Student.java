package io.example.test;

import java.util.ArrayList;

import io.example.test.GameManager.Colours;
/* */
public class Student {
    private static final float moveSpeed = 2.0f;
    private static final float learningMeterGain = 2.0f;
    private static final float hungerMeterGain = 2.0f;
    private static final float sleepMeterGain = 2.0f;
    private static final float learningMeterLoss = -1.0f;
    private static final float hungerMeterLoss = -1.0f;
    private static final float sleepMeterLoss = -1.0f;

    private Colours colour;
    private String name;

    // A students position can not be directly moved but rather they
    // are told the path to take and the update method will move the
    // student.
    private float posX;
    private float posY;

    private float homePosX;
    private float homePosY;

    // Whichever meter is lowest will determine what the student does next. All
    // meters decrease over time and increase when the student does a certain thing.
    // All meters should be between 0 and 100 as percentages.

    // Learning meter is increased when a student is in a lecture or library. 
    private float learningMeter;
    // Increase when the student eats.
    private float hungerMeter;
    // Increases when the student sleeps at their home.
    private float sleepMeter;


    // A student can only be moving to one building at a time.
    private DrawableBuilding movingTo;
    // The points that would move the student to the building.
    ArrayList<Vector2i> movePoints;

    // What the student is currently doing
    private Status status;
    // how long until the student finishes what they are currently doing.
    float timeUntilFree;

    public enum Status {
        Free,
        Eating,
        Sleeping,
        InLecture,
        Travelling
    }
    
    Student(String name, Colours colour, DrawableBuilding home) {
        this.name = name;
        this.colour = colour;
        this.posX = home.getPosX();
        this.posY = home.getPosY();
        status = Status.Free;
        homePosX = home.getPosX();
        homePosY = home.getPosY();
        movePoints = new ArrayList<Vector2i>();
    }

    // standard getters.

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public String getName() {
        return name;
    }
    public Colours getColour() {
        return colour;
    }

    public boolean isFree() {
        if (status == Status.Free) {
            return true;
        }
        return false;
    }

    public void setNewHome(int posX, int posY) {
        homePosX = posX;
        homePosY = posY;
    }

    public void moveTo(DrawableBuilding building) {
        movingTo = building;
    }
    public void setPath(ArrayList<Vector2i> path) {
        movePoints = path;
    }

    // Will make sure that all meters are within the 0-100 range inclusively.
    private void validateMeters() {
        if (learningMeter < 0) learningMeter = 0;
        else if (learningMeter > 100) learningMeter = 100;

        if (sleepMeter < 0) sleepMeter = 0;
        else if (sleepMeter > 100) sleepMeter = 100;

        if (hungerMeter < 0) hungerMeter = 0;
        else if (hungerMeter > 100) hungerMeter = 100;
    }

    // Will apply loss to the meters specified in the function. Will make sure that no meters go lower than 0.
    public void applyMeterLoss(boolean learningMeter, boolean sleepingMeter, boolean hungerMeter, float deltaTime) {
        if (learningMeter) {
            if (this.learningMeter > 0) this.learningMeter -= learningMeterLoss * deltaTime;
        }
        if (sleepingMeter) {
            if (this.sleepMeter > 0) this.sleepMeter -= sleepMeterLoss * deltaTime;
        }
        if (hungerMeter) {
            if (this.hungerMeter > 0) this.hungerMeter -= hungerMeterLoss * deltaTime;
        }

        validateMeters();
    }

    
    // update function will change the variables of the student depending status of the student.
    // EG if they are sleeping then the sleeping meter will go up.
    public void update(float deltaTime) {
        
        if (status == Status.Free) {
            applyMeterLoss(true, true, true, deltaTime);
            return;
        }

        else if (status == Status.InLecture) {
            applyMeterLoss(false, true, true, deltaTime);
            learningMeter += learningMeterGain * deltaTime;
        }

        else if (status == Status.Eating) {
            applyMeterLoss(true, true, false, deltaTime);
            hungerMeter += hungerMeterGain * deltaTime;
        }

        else if (status == Status.Sleeping) {
            applyMeterLoss(true, false, true, deltaTime);
            sleepMeter += sleepMeterGain * deltaTime;
        }
        
        
        if (movePoints.size() == 0) return;

        // moves the student in the direction of the building. Dont care about obstacles or path for now.
        float directionX = movePoints.get(0).x - posX;
        float directionY = movePoints.get(0).y - posY;
        status = Status.Travelling;
        posX += directionX * (deltaTime * moveSpeed);
        posY += directionY * (deltaTime * moveSpeed);
        
        // if they have reached their destination, remove.
        if (posX == movePoints.get(0).x && posY == movePoints.get(0).y) {
            movePoints.remove(0);

            if (movingTo.getType() == Buildable.Accommodation) {
                status = Status.Free;
            }
            else if (movingTo.getType() == Buildable.Accommodation) {
                status = Status.InLecture;
            }
            
        }


        validateMeters();
    }
    
    
}
