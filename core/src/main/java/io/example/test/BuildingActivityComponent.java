package io.example.test;

/** 
 * This component can be added to a building to specify what activity can
 * be completed at this building. 
 * @author Thomas Nash
 * @see io.example.test.BuildingEventComponent
 * @see io.example.test.StudentActivity
 */
public class BuildingActivityComponent {
    /**
     * The activity the student will do once they reached this building.
     */
    public Student.Activity activityType;

    /**
     * How long the activity takes. If a building also has an event component then that
     * will take priority in which can this does not matter.
     */
    public float activityDuration;

    /**
     * Some activities may be WalkIn activties which means that students are able to
     * walk in and do them at any time in which case this variable is set to true.
     * If this variable is set to false. The only way to allow a student to do this
     * activity is if the same building has a {@link BuildingEventComponent} which
     * triggers an Event and allows the students to do the activity.
     */
    public boolean isWalkIn;
 
    
    public BuildingActivityComponent(Student.Activity activityType, float activityDuration, boolean isWalkIn) {
        this.activityType = activityType;
        this.activityDuration = activityDuration;
        this.isWalkIn = isWalkIn;
    }
}
