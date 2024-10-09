package io.example.test;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
    
    // The sprite used to draw the building.
    private Sprite sprite;

    public DrawableBuilding(int ID, int posX, int posY, BuildingType type) {
        this.ID = ID;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        if (type == BuildingType.Accommodation) {
            sprite = new Sprite(Assets.accommodationTexture);
            width = 2;
            height = 2;
        }
        else if (type == BuildingType.LectureTheatre) {
            sprite = new Sprite(Assets.lectureTheatreTexture);
            width = 3;
            height = 3;
        }
        else {
            sprite = new Sprite(Assets.couldNotLoad);
            width = 1;
            height = 1;
        }
        sprite.setSize(width, height);
        sprite.setX(posX);
        sprite.setY(posY);
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
    public Sprite getSprite() {
        return sprite;
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
        sprite.setX(posX);
        sprite.setY(posY);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
