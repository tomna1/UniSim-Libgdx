package io.example.test;

import io.example.test.Building.BuildingType;
import io.example.test.Student.Activity;


/**
 * This class is used to create Building objects. It will automatically assign
 * the correct component to the building. All buildings will have an {@link EnterableComponent}
 * and an {@link BuildingActivityComponent} and certain buildings (depending on the type) might have
 * a {@link HousingComponent} and {@link BuildingEventComponent}.
 * @author Thomas Nash
 */
public class BuildingFactory {
    private Building building;
    // All different component types that can be added to a building
    private HousingComponent housingComponent;
    private EnterableComponent enterableComponent;
    private BuildingActivityComponent activityComponent;
    private BuildingEventComponent eventComponent;

    public BuildingFactory() { }

    /**
     * Creates and returns a building object based on the BuildingType
     * specified.
     * @param type The type of the building which determines the texture used
     * and the components added to the building.
     * @param pos The position of the building to be created.
     * @return Building object. WARNING: Some components may be null.
     * @author Thomas Nash
     */
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
        activityComponent = new BuildingActivityComponent(Activity.Sleep, Consts.ACCOMMODATION_SLEEPING_TIME_L1, true);
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
        activityComponent = new BuildingActivityComponent(Activity.AttendLecture, Consts.LECTURE_DURATION_L1, false);
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
        activityComponent = new BuildingActivityComponent(Activity.Eat, Consts.RESTAURANT_EATING_TIME_L1, true);
        building.activityComponent = activityComponent;

        return building;
    }
}
