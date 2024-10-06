package io.example.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameMap.Buildable;

// This class should only be used in the gameMap class. It represents a drawable building.
public class DrawableBuilding {
    // width of object sprite in tiles
    private int width;
    // height of object sprit in tiles.
    private int height;
    // The X Pos of the bottom left corner of the object.
    private int posX;
    // THe Y pos of the bottom left corner of the object.
    private int posY;

    Buildable type;
    
    // The sprite used to draw the building.
    protected Sprite sprite;

    public DrawableBuilding(int posX, int posY, int width, int height, GameMap.Buildable type) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.type = type;
        if (type == Buildable.Accommodation) sprite = new Sprite(Assets.accommodationTexture);
        else if (type == Buildable.LectureTheatre) sprite = new Sprite(Assets.lectureTheatreTexture);
        else sprite = new Sprite(Assets.couldNotLoad);
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
    public Buildable getType() {
        return type;
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
