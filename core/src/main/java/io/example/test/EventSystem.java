package io.example.test;

import com.badlogic.gdx.Gdx;

// This system is used to manipulated the building event component of 
// a building.
public class EventSystem {
    StudentManager studentManager;
    
    public EventSystem(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    // updates all buildings that have the event component
    public void updateBuilding(Building building, float deltaTime) {
        if (building.eventComponent == null) return;
        if (building.activityComponent == null) return;
        
        float extraTime;
        if (building.eventComponent.timeUntilStarts > 0.0f) {
            extraTime = Math.abs(building.eventComponent.timeUntilStarts - deltaTime);
            building.eventComponent.timeUntilStarts -= deltaTime;
            if (building.eventComponent.timeUntilStarts <= 0.0f) {
                building.eventComponent.timeUntilStarts = 0.0f;
                building.eventComponent.timeLeft -= extraTime;
                if (Consts.EVENT_START_DEBUG_MODE_ON) {
                    Gdx.app.log("EventSystem", "Event at building "+building.toString()+" has started.");
                }
                return;
            }
        } else if (building.eventComponent.timeLeft > 0.0f){
            extraTime = Math.abs(building.eventComponent.timeLeft - deltaTime);
            building.eventComponent.timeLeft -= deltaTime;
            if (building.eventComponent.timeLeft <= 0.0f) {
                if (Consts.EVENT_START_DEBUG_MODE_ON) {
                    Gdx.app.log("EventSystem", "Event at building "+building.toString()+" has ended.");
                }
                building.eventComponent.timeLeft = 0.0f;
                building.eventComponent.timeBeforeRestart -= extraTime;
            }
            return;
        } else if (building.eventComponent.timeBeforeRestart > 0.0f) {
            building.eventComponent.timeBeforeRestart -= deltaTime;
            if (building.eventComponent.timeBeforeRestart <= 0.0f) {
                building.eventComponent.restartEvent();
                if (Consts.EVENT_START_DEBUG_MODE_ON) {
                    Gdx.app.log("EventSystem", "Event at building "+building.toString()+" has been restarted.");
                }
                studentManager.processEvent(building.getActivity());
            }
            
        }
    }
}
