package io.example.test.student;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameManager.Colours;
import io.example.test.building.Building;
import io.example.test.gamemap.GameMap;
import io.example.test.util.Assets;
import io.example.test.util.Consts;
import io.example.test.util.Vector2f;
import io.example.test.util.Vector2i;
/**
 * This class represents a student at university. It is able to be move around
 * the university and complete tasks based on the buildings at placed on the
 * map. The student has internal meters which represent its satisfaction.
 * @author Thomas Nash
 */
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
        Eating, // Doing an eat activity.
        Sleeping, // Doing a sleep activity.
        InLecture, // Doing an AttendLecture activity.
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
        pos = new Vector2f(home.mapObjectComponent.pos.x, home.mapObjectComponent.pos.y);
        this.home = home;
        sprite = new Sprite(Assets.studentTexture);
        sprite.setSize(1, 1);
        this.gameMap = gameMap;
    }

    // standard getters.

    public int getId() { return ID; }
    public Vector2i getHomePos() { return new Vector2i(home.mapObjectComponent.pos); }
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

    /**
     * Gets the satisfaction of the student based on the value of the internal meters
     * and the set {@link Consts#SATISIFIED_METER_AMOUNT}.
     * @return 0-100 inclusively. Represents a percentage.
     */
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

    /**
     * Checks whether a student can complete the activity. A student cannot
     * complete an activity if the internal meter associated with that activity
     * is not low enough, the student is not a colour that is allowed to do
     * the activity or if the student cannot find a path to the activity.
     * @param studentActivity The activity to complete.
     * @return true if they are going to do the event and false if not.
     */
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


    /**
     * This function will update all meters associated with the student,
     * move the student if they are travelling and continue doing an activity
     * if the student is doing an activity. It is basically where the student
     * AI is located.
     * @param deltaTime The time since the last frame.
     * @param updateMeters true if the internal meters of the student should be updated.
     */
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

    /**
     * Will make sure that all meters are within the range set by the 
     * {@link Consts#MIN_METER_AMOUNT} and {@link Consts#MAX_METER_AMOUNT}
     * inclusively.
     */
    private void validateMeters() {
        if (learningMeter < Consts.MIN_METER_AMOUNT) learningMeter = Consts.MIN_METER_AMOUNT;
        else if (learningMeter > Consts.MAX_METER_AMOUNT) learningMeter = Consts.MAX_METER_AMOUNT;

        if (sleepMeter < Consts.MIN_METER_AMOUNT) sleepMeter = Consts.MIN_METER_AMOUNT;
        else if (sleepMeter > Consts.MAX_METER_AMOUNT) sleepMeter = Consts.MAX_METER_AMOUNT;

        if (hungerMeter < Consts.MIN_METER_AMOUNT) hungerMeter = Consts.MIN_METER_AMOUNT;
        else if (hungerMeter > Consts.MAX_METER_AMOUNT) hungerMeter = Consts.MAX_METER_AMOUNT;
    }
    /**
     * Will apply meter loss to the meters specified in the method. Should only be called
     * by the update method.
     * @param learningMeter 
     * @param sleepMeter
     * @param hungerMeter
     */
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

    /**
     * This method should be called whenever a students reaches its target destination.
     * It will start the activity based on the {@link #activity} set.
     */
    private void onDestinationReached() {
        if (activity.canStartActivity()) activity.startActivity(this);   
        else status = Status.Waiting;
    }

    /**
     * When a student finishes an activity this method should be called. 
     * It wiull call {@link StudentActivity#endActivity(Student)}.
     */
    private void onActivityFinished() {
        activity.endActivity(this);
        activity = null;
    }

    /**
     * Checks whether there is a valid path to an activity.
     * @param activity The specified activity.
     * @return true if valid path and false if not.
     */
    private boolean canDoActivity(StudentActivity activity) {
        if (activity == null) return false;
        posI.x = (int)pos.x;
        posI.y = (int)pos.y;
        path = gameMap.findPath(posI, activity.getPos());
        if (path == null) return false;
        return true;
    }
    /**
     * Starts moving towards the activity based on the location of the 
     * activity.
     * @param activity The activity to complete.
     */
    private void doActivity(StudentActivity activity) {
        if (canDoActivity(activity) == false) return;
        this.activity = activity;
        status = Status.Travelling;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student "+ID+" is now travelling to " + activity.toString());
        }  
    }


    /**
     * Will make the student sleep for that amount of time. Should ony be called
     * by {@link StudentActivity}.
     * @param time The amount of time to sleep for.
     */
    protected void sleep(float time) {
        status = Status.Sleeping;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now sleeping for " + time + " seconds.");
        }
    }
    /**
     * Will make the student eat for that amount of time. Should ony be called
     * by {@link StudentActivity}.
     * @param time The amount of time to eat for.
     */
    protected void eat(float time) {
        status = Status.Eating;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now eating for " + time + " seconds.");
        }
    
    }
    /**
     * Will make the student attend a lecture for that amount of time. Should ony be called
     * by {@link StudentActivity}.
     * @param time The amount of time to attend lecture for.
     */
    protected void attendLecture(float time) {
        status = Status.InLecture;
        timeUntilFree = time;
        if (Consts.STUDENT_ACTIVITY_DEBUG_MODE_ON) {
            Gdx.app.log("Student", "Student " + ID + " is now attending lecture for " + time + " seconds.");
        }
    }

    /**
     * Draws the student onto the screen.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        sprite.setX(pos.x);
        sprite.setY(pos.y);
        sprite.draw(batch);
    }
}
