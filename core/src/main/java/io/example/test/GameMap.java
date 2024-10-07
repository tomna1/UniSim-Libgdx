package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.Grid.TileType;

// A GameMap is used to draw all of the buildings to the screen as well as
// be able to using pathing algorithms to find paths for students. ALL LOGIC GOES
// INTO THE GAMEMANAGER CLASS
public class GameMap {
    // A gamemap needs to store:
    //      The buildings on the gamemap
    //      Whether a specific 

    // Used to draw all buildings.
    private ArrayList<DrawableBuilding> buildings;

    // Used to store what each individual tile contains and where most checks and
    // algorithms take place.
    private Grid grid;
    
    private int selectedBuildingPosX;
    private int selectedBuildingPosY;

    // Everything that can be built in the map.
    public enum Buildable {
        LectureTheatre, 
        Accommodation,
        Restaurant,
        Path
    }

    public GameMap(int width, int height) {
        buildings = new ArrayList<DrawableBuilding>();
        grid = new Grid(width, height);
    }

    public TileType getTile(int posX, int posY) {
        return grid.getTile(posX, posY);
    }

    // returns the index of a building in buildings at a particular point
    // on the map. Will return -1 if there is not building at that point on the map.
    private int indexOfBuildingAtPoint(int posX, int posY) {
        if (getTile(posX, posY) != TileType.Building && getTile(posX, posY) != TileType.BuildingBL) {
            return -1;
        }

        Vector2i pos = new Vector2i();
        for (int i = 0; i < buildings.size(); i++) {
            pos.x = buildings.get(i).getPosX();
            pos.y = buildings.get(i).getPosY();
            int height = buildings.get(i).getHeight();
            int width = buildings.get(i).getWidth();
            if (
            posX >= pos.x &&
            posX <= pos.x + width &&
            posY >= pos.y &&
            posY <= pos.y + height
            ) {
                return i;
            }

        }
        return -1;
    }

    // adds a buildable to the map. Returns true if successfully added and false
    // otherwise.
    boolean addBuildable(Buildable type, int posX, int posY) {
        if (type == Buildable.Path) {
            Vector2i pos = new Vector2i(posX, posY);
            return grid.addPath(pos.x, pos.y);
        }
        
        if (type == Buildable.Accommodation) {
            DrawableBuilding building = new DrawableBuilding(posX, posY, type);
            if (grid.containsSpace(building) == false) return false;
            if (grid.addBuilding(building) == false) return false;
            buildings.add(building);
            return true;
        }
        else if (type == Buildable.LectureTheatre) {
            DrawableBuilding building = new DrawableBuilding(posX, posY, type);
            if (grid.addBuilding(building) == false) return false;
            buildings.add(building);
            return true;
        }
        
        return false;
    }

    boolean selectBuilding(int posX, int posY) {
        selectedBuildingPosX = posX;
        selectedBuildingPosY = posY;
        return false;
    }

    boolean moveSelectedBuilding(int posX, int posY) {
        // TODO: this
        return true;
    }


    boolean removeSelectedBuilding() {
        int index = indexOfBuildingAtPoint(selectedBuildingPosX, selectedBuildingPosY);
        if (index == -1) return false;

        if (grid.removeBuilding(buildings.get(index)) == false) return false;
        buildings.remove(index);
        return true;
    }

    // Removes a buildable from the map where the building is at the point pos.
    public boolean removeBuildableAtPoint(Vector2i pos) {
        // Checks if the tile contains a non removeable.
        TileType tile = grid.getTile(pos.x, pos.y);
        if (tile == TileType.Empty || tile == TileType.River || tile == TileType.Road) {
            return false;
        }
        // If the tile contains a path, remove it.
        if (tile == TileType.Path) {
            return grid.removePath(pos.x, pos.y);
        }
        
        
        // Removes the building
        int index = indexOfBuildingAtPoint(pos.x, pos.y);
        if (index == -1) return false;

        if (grid.removeBuilding(buildings.get(index)) == false) return false;
        buildings.remove(index);
        return true;
    }

    public void draw(SpriteBatch batch) {
        grid.draw(batch);
        // Draws the buildings
        for (DrawableBuilding b : buildings) {
            b.draw(batch);
        }
    }
}
