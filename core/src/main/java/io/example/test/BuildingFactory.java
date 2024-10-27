package io.example.test;

import io.example.test.Building.BuildingType;
import io.example.test.Student.Status;

// This class is used to create Building objects. It will automatically assign
// the correct component to the building.
public class BuildingFactory {
    private Building building;
    // All different component types that can be added to a building
    private HousingComponent housingComponent;
    private EnterableComponent enterableComponent;
    private BuildingActivityComponent activityComponent;
    private BuildingEventComponent eventComponent;

    public BuildingFactory() { }

    // Creates and returns a building of the specified type. Will return null if it somehow
    // cannot create the building. Shouldnt return null but just in case.
    public Building createBuilding(BuildingType type, Vector2i pos) {
        if (type == BuildingType.Accommodation) return createAccommodation(pos);
        else if (type == BuildingType.LectureTheatre) return createLectureTheatre(pos);
        else if (type == BuildingType.Restaurant) return createRestaurant(pos);
        else return null;
    }

    // accommodation has a housing component to allow students to select it as a house
    private Building createAccommodation(Vector2i pos) {
        building = new Building(pos, BuildingType.Accommodation);
        
        enterableComponent = new EnterableComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        building.enterableComponent = enterableComponent;
        activityComponent = new BuildingActivityComponent(Status.Sleeping, Consts.ACCOMMODATION_SLEEPING_TIME_L1);
        building.activityComponent = activityComponent;
        housingComponent = new HousingComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        building.housingComponent = housingComponent;
        
        return building;
    }
    // Lecture theatre has a building event component to allow students to attend it lecture.
    private Building createLectureTheatre(Vector2i pos) {
        building = new Building(pos, BuildingType.LectureTheatre);
        
        enterableComponent = new EnterableComponent(Consts.LECTURE_THEATRE_CAPACITY_L1);
        building.enterableComponent = enterableComponent;
        activityComponent = new BuildingActivityComponent(Status.InLecture, Consts.LECTURE_DURATION_L1);
        building.activityComponent = activityComponent;
        eventComponent = new BuildingEventComponent(Consts.LECTURE_TIME_TO_START_L1, Consts.LECTURE_DURATION_L1, Consts.LECTURE_TIME_BETWEEN_EVENTS_L1);
        eventComponent.addValidColour(GameManager.getRandomColour());
        building.eventComponent = eventComponent;
        
        return building;
    }
    // Restaurant has nothing yet.
    private Building createRestaurant(Vector2i pos) {
        building = new Building(pos, BuildingType.Restaurant);
        
        enterableComponent = new EnterableComponent(Consts.RESTAURANT_CAPACITY_L1);
        building.enterableComponent = enterableComponent;
        activityComponent = new BuildingActivityComponent(Status.Eating, Consts.RESTAURANT_EATING_TIME_L1);
        building.activityComponent = activityComponent;

        return building;
    }
}
