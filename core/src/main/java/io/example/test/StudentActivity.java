package io.example.test;

// This component contains information about an activity the student wants to do. It is created by
// a building and allow a student to start the activity of a specified builing using the 
// activity and event components of that builing
public class StudentActivity {
    private EnteringSystem enteringSystem = new EnteringSystem();
    
    // The status effect that the student will do once they have reached the place.
    private Student.Status toDo;
    
    // The amount of time the activity should take.
    private float duration;

    // The building this activity takes place in.
    private Building building;

    private int eventCount;


    public StudentActivity(Building building) {
        this.building = building;
        if (building.eventComponent != null) eventCount = building.eventComponent.getEventCount();
    }

    public Building getBuilding() { return building; }

    public int getEventCapacity() {
        return building.enterableComponent.getCapacity();
    }

    public boolean isValidStudent(Student student) {
        if (building.eventComponent == null) return false;
        if (building.eventComponent.validStudents.contains(student.getColour())) return true;
        return false;
    }

    public boolean canStartActivity() {
        if (hasEnded()) return false;
        if (building.eventComponent != null && building.eventComponent.timeUntilStarts > 0.0f) return false;
        return !building.enterableComponent.isFull();
    }

    // If the activity is based on a BuildingEventComponent, return true if it has
    // ended
    public boolean hasEnded() {
        if (building.eventComponent == null) return false;
        if (building.eventComponent.getEventCount() != eventCount) return true;
        return false;
    }

    public void startActivity(Student student) {
        if (canStartActivity() == false) return;
        
        toDo = building.activityComponent.toDo;
        if (building.eventComponent != null) duration = building.eventComponent.timeLeft;
        else duration = building.activityComponent.activityDuration;
        
        if (toDo == Student.Status.Eating) {
            student.eat(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else if (toDo == Student.Status.Sleeping) {
            student.sleep(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else if (toDo == Student.Status.InLecture) {
            student.attendLecture(duration);
            enteringSystem.addStudent(building.enterableComponent, student.getId());
            return;
        }
        else return;
    }

    public void endActivity(Student student) {
        student.setStatusFree();
        enteringSystem.removeStudent(building.enterableComponent, student.getId());
    }
}
