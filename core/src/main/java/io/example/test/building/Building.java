package io.example.test.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.building.components.BuildingActivityComponent;
import io.example.test.building.components.BuildingEventComponent;
import io.example.test.building.components.EnterableComponent;
import io.example.test.building.components.HousingComponent;
import io.example.test.building.components.MapObjectComponent;
import io.example.test.building.components.RenderComponent;
import io.example.test.student.StudentActivity;
import io.example.test.util.Assets;

/**
 * A class which represents a building that can be placed on the map. The building
 * is made up of components which defines its functionality. A building must
 * have a {@link EnterableComponent} and a {@link BuildingActivityComponent}. The
 * other components are optional.
 * @author Thomas Nash
 */
public class Building {
    public enum BuildingType {
        LectureTheatre, 
        Accommodation,
        Restaurant,
    }

    // Only used for printing out the type of the building that may have been deleted.
    // Does not actually affect the functionality of the building.
    private BuildingType type;

    // Used to give a position, width and height.
    public MapObjectComponent mapObjectComponent;

    // Used to draw the building onto the screen. NECESSARY COMPONENT.
    private RenderComponent renderComponent;

    // Housing component is added to buildings that students can make their home
    // such as accommodation. OPTIONAL COMPONENT
    public HousingComponent housingComponent;

    // The enterable component is added to buildings to allow students to enter the building.
    // NECESSARY COMPONENT
    public EnterableComponent enterableComponent;

    // Allows the building to create a StudentActivityComponent. NECESSARY COMPONENT
    public BuildingActivityComponent activityComponent;

    // This is added to buildings to allow have an event (like a lecture hall would have a letcture
    // event). More information can be found in the BuildingEventComponent class. OPTIONAL COMPONENT
    public BuildingEventComponent eventComponent;

    /**
     * Creates a new building with the specified position. The type of the 
     * building defines which texture is used.
     * @param pos Position of the building.
     * @param type Type of the building.
     */
    public Building(
        BuildingType type,
        MapObjectComponent mapObjectComponent,
        RenderComponent renderComponent,
        EnterableComponent enterableComponent,
        BuildingActivityComponent activityComponent
    ) {
        this.type = type;
        this.mapObjectComponent = mapObjectComponent;
        this.renderComponent = renderComponent;
        this.enterableComponent = enterableComponent;
        this.activityComponent = activityComponent;
    }

    /**
     * Gets the width of the building based on its {@link MapObjectComponent}.
     * @return Width. Might return 0 if no width is found.
     */
    public int getWidth() {
        if (mapObjectComponent == null) return 0;
        return mapObjectComponent.width;
    }
    /**
     * Gets the height of the building based on its {@link MapObjectComponent}.
     * @return Might return 0 if no height is found.
     */
    public int getHeight() {
        if (mapObjectComponent == null) return 0;
        return mapObjectComponent.height;
    }

    /**
     * Each building has an activity that is defined by its 
     * {@link BuildingActivityComponent} and can also be defined by its
     * {@link BuildingEventComponent}. This returns a StudentActivity
     * based on those 2 components.
     * @return Activity of building.
     */
    public StudentActivity getActivity() {
        return new StudentActivity(this);
    }


    /**
     * Draws the building onto the screen based on its internal
     * {@link RenderComponent}.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        if (mapObjectComponent == null) return;
        if (renderComponent == null) return;
        if (renderComponent.texture == null) {
            batch.draw(
                Assets.couldNotLoad, mapObjectComponent.pos.x, mapObjectComponent.pos.y,
                mapObjectComponent.width, mapObjectComponent.height
            );
        }
        else {
            batch.draw(
                renderComponent.texture, mapObjectComponent.pos.x, mapObjectComponent.pos.y,
                mapObjectComponent.width, mapObjectComponent.height
            ); 
        }
    }


    @Override
    public String toString() {
        if (type == BuildingType.Accommodation) return "Accommodation" + mapObjectComponent.pos.toString();
        else if (type == BuildingType.LectureTheatre) return "Lecture Theatre" + mapObjectComponent.pos.toString();
        else if (type == BuildingType.Restaurant) return "Restaurant" + mapObjectComponent.pos.toString();
        else return "Building" + mapObjectComponent.pos.toString();
    }
}
