package io.example.test;

// This class is to manage all constants that might appear
// in weird places which can be hard to track and to put all
// constants together to balance things easier.
public class Consts {
    private Consts() { }
    
    
    // =================================
    // ALL GENERAL CONSTANTS
    // =================================

    public static final int STARTING_MONEY = 1000;

    // =================================
    // ALL CAMERA CONSTANTS
    // =================================

    // How fast the camera moves.
    public static final float CAMERA_SPEED = 15.0f;
    // The MIN a cam can zoom in. MUST BE A MULTIPLE OF CAM_ZOOM_PER_LEVEL
    public static final float CAM_MIN_ZOOM = 0.5f;
    // The MAX a cam can zoom in. MUST BE A MULTIPLE OF CAM_ZOOM_PER_LEVEL
    public static final float CAM_MAX_ZOOM = 2.5f;
    // How much the camera should be zoomed in or out each time Q or E is pressed.
    public static final float CAM_ZOOM_PER_LEVEL = 0.5f;

    // =================================
    // ALL DEBUGGING CONSTANTS
    // =================================

    // Turns this make this false or true to see debug messages. If
    // turned off, no debug messages will show regardless of where the
    // individual class debug boolean are true or false.
    public static final boolean DEBUG_MODE_ON = true;
    
    // The results of pathfinding algorithms between points will be logged.
    public static final boolean PATHFINDING_DEBUG_MODE_ON = true;
    // Buildings being added and removed will be logged.
    public static final boolean BUILDING_PLACEMENT_DEBUG_MODE_ON = false;
    // When a building changes level (gets upgraded or downgraded).
    public static final boolean BUILDING_LEVEL_CHANGE_MODE_ON = false;
    // Tiles being added and removed from the grid will be logged.
    public static final boolean GRID_PLACEMENT_DEBUG_MODE_ON = false;
    // Students being created and destroyed will be logged.
    public static final boolean STUDENT_LIFE_DEBUG_MODE_ON = false;
    // The tasks the students do will be logged.
    public static final boolean STUDENT_ACTIVITY_DEBUG_MODE_ON = true;
    // When an event starts, it will be logged.
    public static final boolean EVENT_START_DEBUG_MODE_ON = true;

    // =================================
    // ALL MAP RELATED CONSTANTS
    // =================================

    public static final int GRID_WIDTH = 20;
    public static final int GRID_HEIGHT = 20;
    // The amount of nodes a pathfinding algorithm can check before quitting. CURRENTLY NOT IMPLEMENTED.
    public static final int MAX_NODES_CHECKED = 300;
    
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
    public static final float MOVE_SPEED = 5.0f;
    // all losses and gains take place over 1 second.
    public static final float LEARNING_METER_GAIN = 12.0f;
    public static final float HUNGER_METER_GAIN = 12.0f;
    public static final float SLEEP_METER_GAIN = 12.0f;
    public static final float LEARNING_METER_LOSS = -4.0f;
    public static final float HUNGER_METER_LOSS = -4.0f;
    public static final float SLEEP_METER_LOSS = -4.0f;

    // The minimum value the meter can go to.
    public static final float MIN_METER_AMOUNT = 0.0f;
    // The maximum value a meter can go to.
    public static final float MAX_METER_AMOUNT = 200.0f;
    // The value at which a student is fully satisfied. The meter can go higher
    // than this but a student wont gain additional satisfaction.
    public static final float SATISIFIED_METER_AMOUNT = 100.0f;

    // =================================
    // ALL BUILDING RELATED CONSTANTS
    // =================================

    // The width of the accommodatoin texture.
    public static final int ACCOMMODATION_WIDTH = 2;
    // The height of the accommodation texture.
    public static final int ACCOMMODATION_HEIGHT = 2;
    // the amount of students a building can have in it.
    public static final int ACCOMMODATION_CAPACITY_L1 = 2;
    // MAINTENANCE is the cost of maintaining.
    public static final int ACCOMMODATION_MAINTENANCE_L1 = 50;
    // The initial cost of building.
    public static final int ACCOMMODATION_COST_L1 = 300;
    // How long the building allow a student to sleep for in seconds.
    public static final float ACCOMMODATION_SLEEPING_TIME_L1 = 7.0f;

    public static final int LECTURE_THEATRE_WIDTH = 3;
    public static final int LECTURE_THEATRE_HEIGHT = 3;
    public static final int LECTURE_THEATRE_CAPACITY_L1 = 20;
    public static final int LECTURE_THEATRE_MAINTENANCE_L1 = 300;
    public static final int LECTURE_THEATRE_COST_L1 = 1000;
    // How long it takes a lecture to start once it has been announced.
    public static final float LECTURE_TIME_TO_START_L1 = 5.0f;
    // How long the lecture last once it has started
    public static final float LECTURE_DURATION_L1 = 10.0f;
    // How long it takes for the event to be restarted once it has ended.
    public static final float LECTURE_TIME_BETWEEN_EVENTS_L1 = 20.0f;


    public static final int RESTAURANT_WIDTH = 3;
    public static final int RESTAURANT_HEIGHT = 3;
    public static final int RESTAURANT_CAPACITY_L1 = 20;
    public static final int RESTAURANT_MAINTENANCE_L1 = 300;
    public static final int RESTAURANT_COST_L1 = 1000;
    // How long students should eat for
    public static final float RESTAURANT_EATING_TIME_L1 = 7.0f;
}
