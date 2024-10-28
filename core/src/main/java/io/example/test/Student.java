package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameManager.Colours;
/* */
public class Student {
    // The map the student uses for pathfinding.
    private GameMap gameMap;

    private int ID;
    private Colours colour;
    private String name;
    private Sprite sprite;

    // A students position can not be directly moved but rather they
    // are told the path to take and the update method will move the
    // student.
    private Vector2f pos;

    // same as pos but with integers instead of floats. Use instead of creating
    // new Vector2i all the time.
    private Vector2i posI = new Vector2i();

    // Students home.
    private Building home;

    // Whichever meter is lowest will determine what the student does next. All
    // meters decrease over time and increase when the student does a certain thing.
    // All meters should be between 0 and 200 inclusively.

    // Learning meter is increased when a student is in a lecture or library. 
    private float learningMeter = Consts.MAX_METER_AMOUNT;
    // Increase when the student eats.
    private float hungerMeter = Consts.MAX_METER_AMOUNT;
    // Increases when the student sleeps at their home.
    private float sleepMeter = Consts.MAX_METER_AMOUNT;


    // The points that would move the student to the building.
    private ArrayList<Vector2i> path = new ArrayList<Vector2i>();
    private StudentActivity activity;

    // What the student is currently doing
    private Status status = Status.Free;
    // how long until the student finishes what they are currently doing.
    private float timeUntilFree;

    private enum Status {
        Free, // they are not doing anything
        Waiting, // waiting for an building event to start
        Eating, 
        Sleeping,
        InLecture,
        Travelling // moving STUDENT ARE ONLY DRAWN WHEN THEY ARE MOVING.
    }

    // Activity is used by the studentActivity class and building activity component to define
    // what activity a student can do.
    public enum Activity {
        Eat,
        Sleep,
        AttendLecture
    }
    
    Student(GameMap gameMap, int ID, String name, Colours colour, Building home) {
        this.ID = ID;
        this.name = name;
        this.colour = colour;
        pos = new Vector2f(home.pos.x, home.pos.y);
        this.home = home;
        sprite = new Sprite(Assets.studentTexture);
        sprite.setSize(1, 1);
        this.gameMap = gameMap;
    }

    // standard getters.

    public int getId() { return ID; }
    public Vector2i getHomePos() { return home.pos; }
    public float getPosX() { return pos.x; }
    public float getPosY() { return pos.y; }
    public String getName() { return name; }
    public Colours getColour() { return colour; }

    public void setStatusFree() { status = Status.Free; }
    

    public boolean isFree() {
        if (status == Status.Free) {
            return true;
        }
        return false;
    }
    private boolean isHungry() {
        if (hungerMeter < Consts.SATISIFIED_METER_AMOUNT) return true;
        return false;
    }
    private boolean isSleepy() {
        if (sleepMeter < Consts.SATISIFIED_METER_AMOUNT) return true;
        return false;
    }
    private boolean wantsToLearn() {
        if (learningMeter < Consts.SATISIFIED_METER_AMOUNT) return true;
        return false;
    }

    public float getSatisfaction() {
        // Satisfaction can be 0-100 inclusively. A student is fully satisfied if
        // all their interal meters are about the satisfied meter amount.
        float hungerSatisfaction = (hungerMeter / Consts.SATISIFIED_METER_AMOUNT)*100;
        if (hungerSatisfaction > 100) hungerSatisfaction = 100;
        float sleepSatisfaction = (sleepMeter / Consts.SATISIFIED_METER_AMOUNT)*100;
        if (sleepSatisfaction > 100) sleepSatisfaction = 100;
        float learningSatisfaction = (learningMeter / Consts.SATISIFIED_METER_AMOUNT)*100;
        if (learningSatisfaction > 100) learningSatisfaction = 100;
        
        return (hungerSatisfaction + sleepSatisfaction + learningSatisfaction) / 3;
    }

    // Returns true is successfully processed and false if not
    public boolean processEvent(StudentActivity studentActivity) {
        // Will automatically override what the student is currently doing and
        // make them participate in the event.

        if (studentActivity.getType() == Student.Activity.AttendLecture && wantsToLearn() == false) return false;
        if (studentActivity.getType() == Student.Activity.Eat && isHungry() == false) return false;
        if (studentActivity.getType() == Student.Activity.Sleep && isSleepy() == false) return false;

        if (studentActivity.isValidStudent(this) == false) {
            return false;
        }
        if (canDoActivity(studentActivity)) {
            doActivity(studentActivity);
            return true;
        }
        return false;
    }


    // update function will update all the meter associated with the student and move
    // the student if they are travelling. Basically where the student AI is located.
    public void update(float deltaTime, boolean updateMeters) {
        // updates the internal meters of the student
        if (updateMeters) {
            if (status == Status.Eating) {
                hungerMeter += Consts.HUNGER_METER_GAIN;
                applyMeterLoss(true, true, false);
            }
            else if (status == Status.Sleeping) {
                sleepMeter += Consts.SLEEP_METER_GAIN;
                applyMeterLoss(true, false, true);
            }
            else if (status == Status.InLecture) {
                learningMeter += Consts.LEARNING_METER_GAIN;
                applyMeterLoss(false, true, true);
            }
            else {
                applyMeterLoss(true, true, true);
            }
            validateMeters();
        }

        if (status == Status.Waiting) {
            if (activity == null) {
                setStatusFree();
            }
            if (activity.hasEnded()) {
                setStatusFree();
                activity = null;
            }
            if (activity.canStartActivity()) activity.startActivity(this);
        }

        // moves the student.
        if (status == Status.Travelling) {
            move(deltaTime);
            return;
        }
        // updates the time until free if a student is doing an activity.
        if (status == Status.Eating || status == Status.Sleeping || status == Status.InLecture) {
            timeUntilFree -= deltaTime;
            if (timeUntilFree <= 0.0f) onActivityFinished();
        }

        // If they are free, they should decide what to do based on their internal meters.
        // This means finding a building that will improve the appropriate meter, travelling
        // to that building and completing the student activity gained from the building.
        if (status == Status.Free) {
            posI.x = (int)pos.x;
            posI.y = (int)pos.y;
            if (isHungry()) {
                activity = gameMap.findNearestActivity(posI.x, posI.y, Student.Activity.Eat);
                if (canDoActivity(this.activity)) {
                    doActivity(this.activity);
                    return;
                }
            }
            if (isSleepy()) {
                activity = home.getActivity();
                if (canDoActivity(this.activity)) {
                    doActivity(this.activity);
                    return;
                }
            }
            if (wantsToLearn()) {
                // go to nearest library. TODO: IMPLEMENT
            }
        }
    }

    // Will make sure that all meters are within the min and max value range inclusively. ONLY CALLED
    // BY UPDATE METHOD.
    private void validateMeters() {
        if (learningMeter < Consts.MIN_METER_AMOUNT) learningMeter = Consts.MIN_METER_AMOUNT;
        else if (learningMeter > Consts.MAX_METER_AMOUNT) learningMeter = Consts.MAX_METER_AMOUNT;

        if (sleepMeter < Consts.MIN_METER_AMOUNT) sleepMeter = Consts.MIN_METER_AMOUNT;
        else if (sleepMeter > Consts.MAX_METER_AMOUNT) sleepMeter = Consts.MAX_METER_AMOUNT;

        if (hungerMeter < Consts.MIN_METER_AMOUNT) hungerMeter = Consts.MIN_METER_AMOUNT;
        else if (hungerMeter > Consts.MAX_METER_AMOUNT) hungerMeter = Consts.MAX_METER_AMOUNT;
    }
    // Will apply loss to the meters specified in the function. Does not guarantee
    // the meters will be between min and max values. ONLY CALLED BY UPDATE METHOD.
    private void applyMeterLoss(boolean learningMeter, boolean sleepMeter, boolean hungerMeter) {
        if (learningMeter) this.learningMeter += Consts.LEARNING_METER_LOSS;
        if (sleepMeter) this.sleepMeter += Consts.SLEEP_METER_LOSS;
        if (hungerMeter) this.hungerMeter += Consts.HUNGER_METER_LOSS;
    }


    
    /**
     * Moves the students based on the currently set {@link #path} variable.
     * @param deltaTime The time since last frame.
     */
    private void move(float deltaTime) {
        if (path.size() == 0) {
            onDestinationReached();
        }

        Vector2i movingTo = path.get(0);
        boolean isPositive = false;
        float distance;
        
        distance = Math.abs(movingTo.x - pos.x);
        if (movingTo.x - pos.x > 0) isPositive = true;
        // Will only move the character in a single direction at a time.
        // AKA character cannot go diagonal.
        if (distance != 0) {
            distance -= Consts.MOVE_SPEED * deltaTime;
            
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
            distance -= Consts.MOVE_SPEED * deltaTime;
            
            // if the distance has flipped, that means the student went past their target.
            if (distance < 0) distance = 0;
            
            // calculates the new pos based on the shorter distances.
            if (isPositive) pos.y = movingTo.y - distance;
            else pos.y = movingTo.y + distance;
            return;
        }
        
        // If there is no x or y distance to the point, it has been reached.
        path.remove(0);
        if (path.size() == 0) {
            onDestinationReached();
        }
    }

    // This method should be called whenever the student reaches its target destinatiom.
    // This method requires the activity variable should already be set.
    private void onDestinationReached() {
        if (activity.canStartActivity()) activity.startActivity(this);   
        else status = Status.Waiting;
    }

    // When an activity is finshed, this method should be called.
    private void onActivityFinished() {
        activity.endActivity(this);
        activity = null;
    }

    // Checks whether the activity at the building can be done.
    private boolean canDoActivity(StudentActivity activity) {
        if (activity == null) return false;
        posI.x = (int)pos.x;
        posI.y = (int)pos.y;
        path = gameMap.findPath(posI, activity.getPos());
        if (path == null) return false;
        return true;
    }
    private void doActivity(StudentActivity activity) {
        if (canDoActivity(activity) == false) return;
        this.activity = activity;
        status = Status.Travelling;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student "+ID+" is now travelling to " + activity.toString());
        }  
    }


    // Will make the student sleep for that set amount of time. Should only be called
    // by StudentActivity.
    protected void sleep(float time) {
        status = Status.Sleeping;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now sleeping for " + time + " seconds.");
        }
    }
    // Will make the student eat for that amount of time. Should only be called
    // by StudentActivity.
    protected void eat(float time) {
        status = Status.Eating;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now eating for " + time + " seconds.");
        }
    
    }
    // Will make the student attend the lecture for that amoumt of time. Should only be called
    // by StudentActivity.
    protected void attendLecture(float time) {
        status = Status.InLecture;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now attending lecture for " + time + " seconds.");
        }
    }

    // Draws student
    public void draw(SpriteBatch batch) {
        sprite.setX(pos.x);
        sprite.setY(pos.y);
        sprite.draw(batch);
    }
}
