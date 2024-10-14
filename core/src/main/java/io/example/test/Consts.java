package io.example.test;

// This class is to manage all constants that might appear
// in weird places which can be hard to track. Not all consts
// appear in this class such as the student meter gains const
// which are kept entirely in the student class.
public class Consts {
    private Consts() { }
    // =================================
    // ALL GENERAL CONSTANTS
    // =================================

    // Turns this make this false or true to see debug messages. If
    // turned off, no debug messages will show regardless of where the
    // individual class debug boolean are true or false.
    public static final boolean DEBUG_MODE_ON = true;
    
    // The results of pathfinding algorithms between points will be logged.
    public static final boolean PATHFINDING_DEBUG_MODE_ON = false;
    // Buildings being added and removed will be logged.
    public static final boolean BUILDING_PLACEMENT_DEBUG_MODE_ON = false;
    // Tiles being added and removed from the grid will be logged.
    public static final boolean GRID_PLACEMENT_DEBUG_MODE_ON = false;
    // Students being created and destroyed will be logged.
    public static final boolean STUDENT_LIFE_DEBUG_MODE_ON = false;
    // The tasks the students do will be logged.
    public static final boolean STUDENT_ACTIVITY_DEBUG_MODE_ON = false;

    // =================================
    // ALL MAP RELATED CONSTANTS
    // =================================

    public static final int GRID_WIDTH = 20;
    public static final int GRID_HEIGHT = 20;
    // The amount of nodes a pathfinding algorithm can check before quitting.
    public static final int MAX_NODES_CHECKED = 200;
    
    // =================================
    // ALL STUDENT RELATED CONSTANTS
    // =================================

    // Game manager gains this amount of money everytime a new student joins.
    public static final int MONEY_PER_STUDENT_JOINING = 100;
    // Game manager gains periodically gains MONEY_PER_STUDENT * student count.
    public static final int MONEY_PER_STUDENT = 50;
    // How long the manager should wait before updating the meters of each
    // student in seconds.
    public static final float TIME_BETWEEN_STUDENT_UPDATES = 1.0f;
}
