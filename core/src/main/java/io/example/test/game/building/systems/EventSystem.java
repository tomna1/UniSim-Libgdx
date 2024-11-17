package io.example.test.building.systems;

import com.badlogic.gdx.Gdx;

import io.example.test.building.Building;
import io.example.test.building.components.BuildingEventComponent;
import io.example.test.student.StudentActivity;
import io.example.test.student.StudentManager;
import io.example.test.util.Consts;

/** 
 * This system is used to manipulate the {@link BuildingEventComponent} of a building.
 * The {@link #updateBuilding(Building, float)} method will update the 
 * {@link BuildingEventComponent} and if the event is restarted, it will ping all
 * necessary students about the event by calling the {@link StudentManager#processEvent(StudentActivity)} 
 * method.
 * @author Thomas Nash
*/
public class EventSystem {
    StudentManager studentManager;
    
    public EventSystem(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    /**
     * Updates all buildings that have an event component. Will automatically call
     * necessary {@link StudentManager#processEvent(StudentActivity)} each time an event
     * gets restarted.
     * @param building The building with the event component.
     * @param deltaTime The time since the last frame.
     */
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
