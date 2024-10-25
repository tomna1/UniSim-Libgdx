package io.example.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// A building that students should be able to enter and leave.
public class Building {
    public enum BuildingType {
        LectureTheatre, 
        Accommodation,
        Restaurant,
    }
    
    // The position works as an ID as 2 buildings cannot have the same
    // position.
    public Vector2i pos;

    // Used for drawing and casting to the appropriate type.
    private BuildingType type;

    // used for drawing the accommodation. Texture depends on the type 
    // of the building and the level of the building.
    private Texture texture;

    // Housing component is added to buildings that students can make their home
    // such as accommodation. THIS SHOULD NOT BE CHANGED ONCE SET.
    public HousingComponent housingComponent;

    // The enterable component is added to buildings to allow students to enter the building.
    // THIS SHOULD NOT BE CHANGED ONCE SET.
    public EnterableComponent enterableComponent;

    public Building(Vector2i pos, BuildingType type, HousingComponent housingComponent, EnterableComponent enterableComponent) {
        this.pos = pos;
        this.type = type;
        if (type == BuildingType.Accommodation) {
            texture = Assets.accommodationTextureL1;
        } else if (type == BuildingType.LectureTheatre) {
            texture = Assets.lectureTheatreTextureL1;
        } else if (type == BuildingType.Restaurant) {

        }
        this.housingComponent = housingComponent;
        this.enterableComponent = enterableComponent;
    }


    public BuildingType getType() { return type; }  

    public int getWidth() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_WIDTH; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_WIDTH; }
        else if (type == BuildingType.Restaurant) { return Consts.RESTAURANT_WIDTH; }
        else { return 1; }
    }
    public int getHeight() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_HEIGHT; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_HEIGHT; }
        else if (type == BuildingType.Restaurant) { return Consts.RESTAURANT_HEIGHT; }
        else { return 1; }
    }


    public void draw(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, pos.x, pos.y, getWidth(), getHeight());
        }
        else {
            batch.draw(Assets.couldNotLoad, pos.x, pos.y, getWidth(), getHeight());
        }
    }
}
