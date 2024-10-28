package io.example.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    
    /**
     * The position of the building. Also works as an ID since no 2 buildings
     * can have the same position.
     */
    public Vector2i pos;

    // Used for drawing and pathfinding.
    private BuildingType type;

    // used for drawing the accommodation. Texture depends on the type 
    // of the building and the level of the building.
    private Texture texture;

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
    public Building(Vector2i pos, BuildingType type) {
        this.pos = pos;
        this.type = type;
        if (type == BuildingType.Accommodation) {
            texture = Assets.accommodationTextureL1;
        } else if (type == BuildingType.LectureTheatre) {
            texture = Assets.lectureTheatreTextureL1;
        } else if (type == BuildingType.Restaurant) {
            texture = Assets.restaurantTextureL1;
        } else texture = Assets.couldNotLoad;
    }

    /**
     * @return The type of the building.
     */
    public BuildingType getType() { return type; }  

    /**
     * @return The width of the building.
     */
    public int getWidth() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_WIDTH; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_WIDTH; }
        else if (type == BuildingType.Restaurant) { return Consts.RESTAURANT_WIDTH; }
        else { return 1; }
    }
    /**
     * @return The height of the building.
     */
    public int getHeight() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_HEIGHT; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_HEIGHT; }
        else if (type == BuildingType.Restaurant) { return Consts.RESTAURANT_HEIGHT; }
        else { return 1; }
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
     * Draws the building onto the screen.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, pos.x, pos.y, getWidth(), getHeight());
        }
        else {
            batch.draw(Assets.couldNotLoad, pos.x, pos.y, getWidth(), getHeight());
        }
    }


    @Override
    public String toString() {
        if (type == BuildingType.Accommodation) return "Accommodation" + pos.toString();
        else if (type == BuildingType.LectureTheatre) return "Lecture Theatre" + pos.toString();
        else if (type == BuildingType.Restaurant) return "Restaurant" + pos.toString();
        else return "Building" + pos.toString();
    }
}
