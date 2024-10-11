package io.example.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameMap.BuildingType;

// This class should only be used in the gameMap class. It represents a drawable building.
public class DrawableBuilding {
    private int ID;
    
    // width of object sprite in tiles
    private int width;
    // height of object sprit in tiles.
    private int height;
    // The X Pos of the bottom left corner of the object.
    private int posX;
    // The Y pos of the bottom left corner of the object.
    private int posY;

    private BuildingType type;
    

    public DrawableBuilding(int ID, int posX, int posY, BuildingType type) {
        this.ID = ID;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        if (type == BuildingType.Accommodation) {
            width = 2;
            height = 2;
        }
        else if (type == BuildingType.LectureTheatre) {
            width = 3;
            height = 3;
        }
        else {
            width = 1;
            height = 1;
        }
       
    }

    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public BuildingType getType() {
        return type;
    }
    public int getId() {
        return ID;
    }
    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(SpriteBatch batch) {
        if (type == BuildingType.Accommodation) {
            batch.draw(Assets.accommodationTexture, posX, posY, width, height);
        }
        else if (type == BuildingType.LectureTheatre) {
            batch.draw(Assets.lectureTheatreTexture, posX, posY, width, height);
        }
        else if (type == BuildingType.Restaurant) {
            batch.draw(Assets.restaurantTexture, posX, posY, width, height);
        }
        
        
    }
}
