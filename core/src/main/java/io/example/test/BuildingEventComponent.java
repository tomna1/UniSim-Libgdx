package io.example.test;

import java.util.ArrayList;
import io.example.test.GameManager.Colours;

// A building Event is a building activity but with a time limit and when the event is first
// activated by the building, it will ping all valid students to come to the building. This
// will give all valid students a StudentActivityComponent which they can use to enter the event.
// Events override the current activity a student is doing. 
public class BuildingEventComponent {
    
    // The colours of various students this event is for. If empty,
    // the event is for all students
    public ArrayList<Colours> validStudents = new ArrayList<>();

    // This value should be incremented every time a new event is started.
    // The StudentActivity class uses it to check if an event has ended.
    private int eventCount = 0;

    // Each event has a timeToStart, an event duration and a timeBetweenEvents.
    // timeToStart is the amount of time before an events starts
    // eventDuration is the amount of time an event lasts
    // timeBetweenEvents is the amount of time after an event has finished for the
    // event to be restarted

    private float timeToStart;
    public float timeUntilStarts;

    private float eventDuration;
    public float timeLeft;

    private float timeBetweenEvents;
    public float timeBeforeRestart;

    public BuildingEventComponent(float timeToStart, float eventDuration, float timeBetweenEvents) {
        this.timeToStart = timeToStart;
        this.eventDuration = eventDuration;
        timeUntilStarts = timeToStart;
        timeLeft = eventDuration;
        this.timeBetweenEvents = timeBetweenEvents;
        timeBeforeRestart = timeBetweenEvents;
    }

    public int getEventCount() { return eventCount; }

    public void restartEvent() {
        timeUntilStarts = timeToStart;
        timeLeft = eventDuration;
        timeBeforeRestart = timeBetweenEvents;
        eventCount++;
    }

    public void addValidColour(Colours colour) {
        if (validStudents.contains(colour) == false) {
            validStudents.add(colour);
        }
    }
}
