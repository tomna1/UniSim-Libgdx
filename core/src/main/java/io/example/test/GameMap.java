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
    Grid grid;
    
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

    // adds a buildable to the map. Returns true if successfully added and false
    // otherwise.
    boolean addBuildable(Buildable type, int posX, int posY) {
        if (type == Buildable.Path) {
            Vector2i pos = new Vector2i(posX, posY);
            return grid.addPath(pos);
        }
        
        if (type == Buildable.Accommodation) {
            DrawableBuilding building = new DrawableBuilding(posX, posY, Accommodation.getWidth(), Accommodation.getHeight(), type);
            if (grid.containsSpace(building) == false) return false;
            if (grid.addBuilding(building) == false) return false;
            buildings.add(building);
            return true;
        }
        else if (type == Buildable.LectureTheatre) {
            DrawableBuilding building = new DrawableBuilding(posX, posY, LectureTheatre.getWidth(), LectureTheatre.getHeight(), type);
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
        Vector2i pos = new Vector2i(selectedBuildingPosX, selectedBuildingPosY);
        for (int i = 0; i < buildings.size(); i++) {
            int posX = buildings.get(i).getPosX();
            int posY = buildings.get(i).getPosY();
            int height = buildings.get(i).getHeight();
            int width = buildings.get(i).getWidth();
            if (
            pos.x >= posX &&
            pos.x <= posX + width &&
            pos.y >= posY &&
            pos.y <= posY + height
            ) {
                if (grid.removeBuilding(buildings.get(i)) == false) return false;
                buildings.remove(i);
                return true;
            }

        }
        return false;
    }

    boolean removeBuildableAtPoint(Vector2i pos) {
        // Checks if the tile contains a non removeable.
        TileType tile = grid.getTile(pos.x, pos.y);
        if (tile == TileType.Empty || tile == TileType.River || tile == TileType.Road) {
            return false;
        }
        // If the tile contains a path, remove it.
        if (tile == TileType.Path) {
            return grid.removePath(pos);
        }
        
        // Removes the building
        for (int i = 0; i < buildings.size(); i++) {
            int posX = buildings.get(i).getPosX();
            int posY = buildings.get(i).getPosY();
            int height = buildings.get(i).getHeight();
            int width = buildings.get(i).getWidth();
            if (
            pos.x >= posX &&
            pos.x <= posX + width &&
            pos.y >= posY &&
            pos.y <= posY + height
            ) {
                if (grid.removeBuilding(buildings.get(i)) == false) return false;
                buildings.remove(i);
                return true;
            }

        }
        return false;
    }

    public void draw(SpriteBatch batch) {
        grid.draw(batch);
        // Draws the buildings
        // System.out.println(buildings.size());
        for (DrawableBuilding b : buildings) {
            // System.out.println(b.getType());
            b.draw(batch);
        }
    }

    public void drawGrid(SpriteBatch batch) {
        grid.draw(batch);
    }


}
