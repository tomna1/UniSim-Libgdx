package io.example.test.game.building.components;

import java.util.ArrayList;

import io.example.test.game.GameManager.Colours;
import io.example.test.game.building.systems.EventSystem;

/** A building Event is a building activity but with a time limit. Events are split
 * into 3 time phases. The {@link #timeToStart} which indicates how long until the event starts.
 * The {@link #eventDuration} which indicates how long the event will last and the 
 * {@link #timeBeforeRestart} which indicates how long to wait after the event has ended 
 * before it restarts. When an event restarts, valid students are pinged about it. 
 * Events override the current activity a student is doing.
 * @author Thomas Nash
 * @see EventSystem
 */ 
public class BuildingEventComponent {
    
    // The colours of various students this event is for. If empty,
    // the event is for all students
    public ArrayList<Colours> validStudents = new ArrayList<>();

    // This value should be incremented every time a new event is started.
    // The StudentActivity class uses it to check if an event has ended.
    private int eventCount = 0;


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

    /**
     * The event count value is incremented each time this event is restarted. In that
     * way it essentially acts as a way to check if the event has ended.
     * @return int
     */
    public int getEventCount() { return eventCount; }

    /**
     * Restarts the event which resets {@link #timeUntilStarts}, {@link #timeLeft}
     * and {@link #timeBeforeRestart}. Will also increment {@link #eventCount}.
     */
    public void restartEvent() {
        timeUntilStarts = timeToStart;
        timeLeft = eventDuration;
        timeBeforeRestart = timeBetweenEvents;
        eventCount++;
    }

    /**
     * Adds a valid colour to the event. Only students with the same colour
     * as one of the valid colours of this event can attend.
     * @param colour The new colour.
     */
    public void addValidColour(Colours colour) {
        if (validStudents.contains(colour) == false) {
            validStudents.add(colour);
        }
    }
}
