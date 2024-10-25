package io.example.test;

import io.example.test.Building.BuildingType;

// This class is used to create Building objects. It will automatically assign
// various components and assign those components to the correct system.
public class BuildingFactory {
    private Building building;
    private HousingComponent housingComponent;
    private EnterableComponent enterableComponent;

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
        housingComponent = new HousingComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        enterableComponent = new EnterableComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        building = new Building(pos, BuildingType.Accommodation, housingComponent, enterableComponent);
        return building;
    }
    // Lecture theatre has a building event component to allow students to attend it lecture.
    private Building createLectureTheatre(Vector2i pos) {
        enterableComponent = new EnterableComponent(Consts.LECTURE_THEATRE_CAPACITY_L1);
        building = new Building(pos, BuildingType.LectureTheatre, null, enterableComponent);
        return building;
    }
    // Restaurant has nothing yet.
    private Building createRestaurant(Vector2i pos) {
        enterableComponent = new EnterableComponent(Consts.RESTAURANT_CAPACITY_L1);
        building = new Building(pos, BuildingType.Restaurant, null, enterableComponent);
        return building;
    }
}
