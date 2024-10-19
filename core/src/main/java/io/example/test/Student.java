package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameManager.Colours;
/* */
public class Student {
    private static final float moveSpeed = 5.0f;
    // all losses and gains take place over 1 second.
    private static final float learningMeterGain = 5.0f;
    private static final float hungerMeterGain = 5.0f;
    private static final float sleepMeterGain = 5.0f;
    private static final float learningMeterLoss = -1.0f;
    private static final float hungerMeterLoss = -1.0f;
    private static final float sleepMeterLoss = -1.0f;


    private GameMap gameMap;

    private int ID;
    private Colours colour;
    private String name;
    private Sprite sprite;

    // A students position can not be directly moved but rather they
    // are told the path to take and the update method will move the
    // student.
    private Vector2f pos;

    // Building ID of the students home.
    private Vector2i homePos;

    // Whichever meter is lowest will determine what the student does next. All
    // meters decrease over time and increase when the student does a certain thing.
    // All meters should be between 0 and 200 inclusively.

    // Learning meter is increased when a student is in a lecture or library. 
    private float learningMeter = 200.0f;
    // Increase when the student eats.
    private float hungerMeter = 200.0f;
    // Increases when the student sleeps at their home.
    private float sleepMeter = 200.0f;


    // The points that would move the student to the building.
    private ArrayList<Vector2i> path = new ArrayList<Vector2i>();;

    // What the student is currently doing
    private Status status = Status.Free;
    // how long until the student finishes what they are currently doing.
    private float timeUntilFree;

    // what the student wants to do next. aka after travelling to a lecture hall
    // they want to learn.
    private Status next = Status.Free;


    public enum Status {
        Free,
        Eating,
        Sleeping,
        InLecture,
        Travelling
    }
    
    Student(GameMap gameMap, int ID, String name, Colours colour, Vector2i homePos) {
        this.ID = ID;
        this.name = name;
        this.colour = colour;
        pos = new Vector2f(homePos.x, homePos.y);
        this.homePos = homePos;
        sprite = new Sprite(Assets.studentTexture);
        sprite.setSize(1, 1);
        this.gameMap = gameMap;
    }

    // standard getters.

    public int getId() { return ID; }
    public Vector2i getHomePos() { return homePos; }
    public float getPosX() { return pos.x; }
    public float getPosY() { return pos.y; }
    public String getName() { return name; }
    public Colours getColour() { return colour; }

    public void setNewHome(Vector2i homePos) {
        this.homePos = homePos;
    }
    

    public boolean isFree() {
        if (status == Status.Free) {
            return true;
        }
        return false;
    }

    public boolean isHungry() {
        if (hungerMeter <= 100) return true;
        return false;
    }

    public boolean isSleepy() {
        if (sleepMeter <= 100) return true;
        return false;
    }

    public boolean wantsToLearn() {
        if (learningMeter <= 100) return true;
        return false;
    }

    public float getSatisfaction() {
        float hungerSatisfaction = hungerMeter;
        if (hungerSatisfaction > 100) hungerSatisfaction = 100;
        float sleepSatisfaction = sleepMeter;
        if (sleepSatisfaction > 100) sleepSatisfaction = 100;
        float learningSatisfaction = learningMeter;
        if (learningSatisfaction > 100) learningSatisfaction = 100;
        
        return (hungerSatisfaction + sleepSatisfaction + learningSatisfaction) / 3;
    }


    // Will make the student sleep for that set amount of time.
    public void sleep(float time) {
        if (status == Status.Free) {
            status = Status.Sleeping;
            timeUntilFree = time;
            if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
                Gdx.app.log("Student", "Student " + ID + " is now sleeping for " + time + " seconds.");
            }
        }
    }
    // Will make the student eat for that amount of time.
    public void eat(float time) {
        if (status == Status.Free) {
            status = Status.Eating;
            timeUntilFree = time;
            if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
                Gdx.app.log("Student", "Student " + ID + " is now eating for " + time + " seconds.");
            }
        }
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
    // This function should be called every 10 seconds. 
    public void applyMeterLoss(boolean learningMeter, boolean sleepMeter, boolean hungerMeter) {
        if (learningMeter) {
            this.learningMeter += learningMeterLoss;
        }
        if (sleepMeter) {
            this.sleepMeter += sleepMeterLoss;
        }
        if (hungerMeter) {
            this.hungerMeter += hungerMeterLoss;
        }
        validateMeters();
    }


    // update function will update all the meter associated with the student and move
    // the student if they are travelling.
    public void update(float deltaTime, boolean updateMeters) {
        // updates the internal meters of the student
        if (updateMeters) {
            applyMeterLoss(true, true, true);
            if (status == Status.Eating) hungerMeter += hungerMeterGain;
            else if (status == Status.Sleeping) sleepMeter += sleepMeterGain;
            else if (status == Status.InLecture) learningMeter += learningMeterGain;
            validateMeters();
        }

        if (status == Status.Free) {
            // decide what to do next.
            if (isHungry()) {
                // NEEDS TO FIND THE NEAREST PLACE TO EAT THAT ISNT FULL
            }
            else if (isSleepy()) {
                // NEEDS TO FIND A PATH HOME AND GO SLEEP
            }
            else if (wantsToLearn()) {
                //
            }
            Vector2i point = gameMap.getRandomPathPoint();
            if (point != null) {
                travelTo(point);
            }
        }

        // moves the student.
        if (status == Status.Travelling) {
            move(deltaTime);
        }

        // updates the time until free if a student is doing an activity.
        if (status == Status.Eating || status == Status.Sleeping) {
            timeUntilFree -= deltaTime;
            if (timeUntilFree <= 0.0f) status = Status.Free;
        }
    }

    
    // Moves the student based on the path. Should only be called by the update method.
    private void move(float deltaTime) {
        if (path.size() == 0) {
            if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
                Gdx.app.log("Student", "Student " + ID + " is now free.");
            }
            status = Status.Free;
            return;
        }

        Vector2i movingTo = path.get(0);
        boolean isPositive = false;
        float distance;
        
        distance = Math.abs(movingTo.x - pos.x);
        if (movingTo.x - pos.x > 0) isPositive = true;
        // Will only move the character in a single direction at a time.
        // AKA character cannot go diagonal.
        if (distance != 0) {
            distance -= moveSpeed * deltaTime;
            
            // if the distance has flipped, that means the student went past their target.
            if (distance < 0) distance = 0;
            
            // calculates the new pos based on the shorter distances.
            if (isPositive) pos.x = movingTo.x - distance;
            else pos.x = movingTo.x + distance;
            return;
        }

        distance = Math.abs(movingTo.y - pos.y);
        isPositive = false;
        if (movingTo.y - pos.y > 0) isPositive = true;
        // Will only move the character in a single direction at a time.
        // AKA character cannot go diagonal.
        if (distance != 0) {
            distance -= moveSpeed * deltaTime;
            
            // if the distance has flipped, that means the student went past their target.
            if (distance < 0) distance = 0;
            
            // calculates the new pos based on the shorter distances.
            if (isPositive) pos.y = movingTo.y - distance;
            else pos.y = movingTo.y + distance;
            return;
        }
        
        // If there is no x or y distance to the point, it has been reached.
        path.remove(0);
    }

    // Tells the student to travel to a specific point on a map. A student can only
    // start travelling when they are free.
    public boolean travelTo(Vector2i point) {
        if (status != Status.Free) return false;
        Vector2i charPos = new Vector2i((int)pos.x, (int)pos.y);
        ArrayList<Vector2i> path = gameMap.findPath(charPos, point);
        if (path == null) return false;
        
        this.path = path;
        status = Status.Travelling;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now travelling to " + point.toString());
        }
        return true;
    }

    // Draws student
    public void draw(SpriteBatch batch) {
        sprite.setX(pos.x);
        sprite.setY(pos.y);
        sprite.draw(batch);
    }
}
