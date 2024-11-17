package io.example.test.building;

import io.example.test.GameManager;
import io.example.test.building.Building.BuildingType;
import io.example.test.building.components.BuildingActivityComponent;
import io.example.test.building.components.BuildingEventComponent;
import io.example.test.building.components.EnterableComponent;
import io.example.test.building.components.HousingComponent;
import io.example.test.building.components.MapObjectComponent;
import io.example.test.building.components.RenderComponent;
import io.example.test.student.Student.Activity;
import io.example.test.util.Assets;
import io.example.test.util.Consts;
import io.example.test.util.Vector2i;


/**
 * This class is used to create Building objects. It will automatically assign
 * the correct component to the building. All buildings will have an {@link EnterableComponent}
 * and an {@link BuildingActivityComponent} and certain buildings (depending on the type) might have
 * a {@link HousingComponent} and {@link BuildingEventComponent}.
 * @author Thomas Nash
 */
public class BuildingFactory {
    // All different component types that can be added to a building
    private MapObjectComponent mapObjectComponent;
    private RenderComponent renderComponent;
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
     */
    public Building createBuilding(BuildingType type, Vector2i pos) {
        if (type == BuildingType.Accommodation) return createAccommodation(pos);
        else if (type == BuildingType.LectureTheatre) return createLectureTheatre(pos);
        else if (type == BuildingType.Restaurant) return createRestaurant(pos);
        else return null;
    }

    // accommodation has a housing component to allow students to select it as a house
    private Building createAccommodation(Vector2i pos) {
        mapObjectComponent = new MapObjectComponent(new Vector2i(pos), Consts.ACCOMMODATION_WIDTH, Consts.ACCOMMODATION_HEIGHT);
        renderComponent = new RenderComponent(Assets.accommodationTextureL1);
        enterableComponent = new EnterableComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        activityComponent = new BuildingActivityComponent(Activity.Sleep, Consts.ACCOMMODATION_SLEEPING_TIME_L1, true);

        Building building = new Building(
            BuildingType.Accommodation,
            mapObjectComponent, renderComponent,
            enterableComponent, activityComponent
        );

        housingComponent = new HousingComponent(Consts.ACCOMMODATION_CAPACITY_L1);
        building.housingComponent = housingComponent;
        
        return building;
    }
    // Lecture theatre has a building event component to allow students to attend it lecture.
    private Building createLectureTheatre(Vector2i pos) {
        mapObjectComponent = new MapObjectComponent(new Vector2i(pos), Consts.LECTURE_THEATRE_WIDTH, Consts.LECTURE_THEATRE_HEIGHT);
        renderComponent = new RenderComponent(Assets.lectureTheatreTextureL1);
        enterableComponent = new EnterableComponent(Consts.LECTURE_THEATRE_CAPACITY_L1);
        activityComponent = new BuildingActivityComponent(Activity.AttendLecture, Consts.LECTURE_DURATION_L1, false);
        
        Building building = new Building(
            BuildingType.LectureTheatre,
            mapObjectComponent, renderComponent,
            enterableComponent, activityComponent
        );

        eventComponent = new BuildingEventComponent(Consts.LECTURE_TIME_TO_START_L1, Consts.LECTURE_DURATION_L1, Consts.LECTURE_TIME_BETWEEN_EVENTS_L1);
        eventComponent.addValidColour(GameManager.getRandomColour());
        building.eventComponent = eventComponent;

        return building;
    }
    // Restaurant has nothing yet.
    private Building createRestaurant(Vector2i pos) {
        mapObjectComponent = new MapObjectComponent(new Vector2i(pos), Consts.RESTAURANT_WIDTH, Consts.RESTAURANT_HEIGHT);
        renderComponent = new RenderComponent(Assets.foodHallTextureL1);
        enterableComponent = new EnterableComponent(Consts.RESTAURANT_CAPACITY_L1);
        activityComponent = new BuildingActivityComponent(Activity.Eat, Consts.RESTAURANT_EATING_TIME_L1, true);

        Building building = new Building(
            BuildingType.Restaurant,
            mapObjectComponent, renderComponent,
            enterableComponent, activityComponent
        );

        return building;
    }
}
