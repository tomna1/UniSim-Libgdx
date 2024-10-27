package io.example.test;

// This component can be added to a building to specify how
// to create a StudentActivityComponent.
public class BuildingActivityComponent {
    // The status effect that the student will do once they have reached the place.
    // SHOULD EITHER BE EATING, SLEEPING OR IN LECTURE.
    public Student.Status toDo;

    // How long the activity takes. If the building also have an event component
    // then the event componet duration takes priority and so this does not matter.
    public float activityDuration;
 
    
    public BuildingActivityComponent(Student.Status toDo, float activityDuration) {
        this.toDo = toDo;
        this.activityDuration = activityDuration;
    }
}
