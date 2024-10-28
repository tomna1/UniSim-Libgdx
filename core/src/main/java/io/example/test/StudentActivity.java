package io.example.test;

/**
 * This class contains information about an activity offered by a building.
 * Activties allow students to replenish the internal meters. Students can use the
 * {@link #getPos()} method to get the position of the activity and use the {@link #startActivity(Student)}
 * method when they have reached the place of the activity will which call the necessary
 * methods on the Student class to start the activity. This treats events and activities
 * similarly and so the student should call {@link #hasEnded()} to check whether the
 * possible event has ended.
 * @author Thomas Nash
 */
public class StudentActivity {
    private EnteringSystem enteringSystem = new EnteringSystem();
    
    // The status effect that the student will do once they have reached the place.
    private Student.Activity activity;
    
    // The amount of time the activity should take.
    private float duration;

    // The building this activity takes place in.
    private Building building;

    private int eventCount;


    /**
     * Creates a new object based on a building. The building must have an
     * {@link BuildingActivityComponent}  and optionally could have a
     * {@link BuildingEventComponent}.
     * @param building The building where the activity takes place.
     */
    public StudentActivity(Building building) {
        this.building = building;
        if (building.eventComponent != null) eventCount = building.eventComponent.getEventCount();
    }

    // standard getters.

    public int getActivityCapacity() { return building.enterableComponent.getCapacity(); }
    public Vector2i getPos() { return new Vector2i(building.pos); }
    public Student.Activity getType() { return activity; }

    /**
     * If the activity is based on an event, checks whether the student is 
     * allowed to attend or not.
     * @param student student.
     * @return true if valid and false if not.
     */
    public boolean isValidStudent(Student student) {
        if (building.eventComponent == null) return false;
        if (building.eventComponent.validStudents.contains(student.getColour())) return true;
        return false;
    }

    /**
     * If the activity is based on a {@link BuildingEventComponent} then it
     * will check if the event has ended and return true if it has and false if not.
     * If the building has no event component then it will return false.
     * @return true/false.
     */
    public boolean hasEnded() {
        if (building.eventComponent == null) return false;
        if (building.eventComponent.getEventCount() != eventCount) return true;
        return false;
    }
    /**
     * Checks to see whether the activity can start. If the activity is based on a
     * {@link BuildingEventComponent}, it will first check if the event has ended
     * and then check if the event has started. This always check if the building
     * is full.
     * @return true/false.
     */
    public boolean canStartActivity() {
        if (hasEnded()) return false;
        if (building.eventComponent != null && building.eventComponent.timeUntilStarts > 0.0f) return false;
        return !building.enterableComponent.isFull();
    }

    /**
     * Will start the activity by making the student do whatever the activity
     * is for the set duration. For example, if the activity is eating them
     * it will call {@link Student#eat(float)}. Will also add the student
     * to the {@link EnterableComponent} of the building.
     * @param student The student who wants to do the activity.
     */
    public void startActivity(Student student) {
        if (canStartActivity() == false) return;
        
        activity = building.activityComponent.activityType;
        if (building.eventComponent != null) duration = building.eventComponent.timeLeft;
        else duration = building.activityComponent.activityDuration;
        
        if (activity == Student.Activity.Eat) {
            student.eat(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else if (activity == Student.Activity.Sleep) {
            student.sleep(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else if (activity == Student.Activity.AttendLecture) {
            student.attendLecture(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else return;
    }

    /**
     * Will end the activity by removing the student from the {@link EnterableComponent}
     * of the building where the activity takes place and setting the student
     * status to free by calling {@link Student#setStatusFree()}.
     * @param student The student who did the activity.
     */
    public void endActivity(Student student) {
        student.setStatusFree();
        enteringSystem.removeStudent(building.enterableComponent, student.getId());
    }

    @Override
    public String toString() {
        return building.toString();
    }
}
