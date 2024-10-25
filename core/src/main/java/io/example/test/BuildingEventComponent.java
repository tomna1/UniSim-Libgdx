package io.example.test;

import java.util.ArrayList;
import io.example.test.GameManager.Colours;

// A component which can be added to a building which specifies an event
// (such as a lecture for a lecture theatre) which will attract students
// to enter the building. This component is altered by the [ENTER SYSTEM NAME HERE].
public class BuildingEventComponent {
    // The colours of various students this event is for. If empty,
    // the event is for all students
    public ArrayList<Colours> validStudents;

    // The amount of time it takes for an event to start
    private float timeToStart;
    // The amount of time until an event starts
    public float timeUntilStarts;

    // The amount of time it takes for an event to be completed once it has started
    private float eventDuration;
    // The amount of time left before the event ends
    public float timeLeft;

    public BuildingEventComponent(ArrayList<Colours> validStudents, float timeToStart, float eventDuration) {
        this.validStudents = validStudents;
        this.timeToStart = timeToStart;
        this.eventDuration = eventDuration;
    }

    // Resets the timeUntilStarts variable to the amount of time it takes the event 
    // to start.
    public void resetTimeToStart() {
        timeUntilStarts = timeToStart;
    }
    // Resets the timeLeft variable to the amount of time it takes an event to take.
    public void resetTimeLeft() {
        timeLeft = eventDuration;
    }

    public void addValidColour(Colours colour) {
        if (validStudents.contains(colour) == false) {
            validStudents.add(colour);
        }
    }
}
