package io.example.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.Grid.TileType;

// A GameMap is used to draw all of the buildings to the screen as well as
// be able to using pathing algorithms to find paths for students. ALL LOGIC GOES
// INTO THE GAMEMANAGER CLASS. CHANGE THIS CLASS TO INHERIT GRID CLASS.
public class GameMap {
    // Used to draw all buildings.
    private ArrayList<DrawableBuilding> buildings;

    // Used to store what each individual tile contains and where most checks and
    // algorithms take place.
    private Grid grid;
    

    private UniqueIDGiver buildingIDGiver = new UniqueIDGiver();

    // Everything that can be built in the map.
    public enum BuildingType {
        LectureTheatre, 
        Accommodation,
        Restaurant,
    }
    public enum Buildable {
        Building,
        Path
    }

    public GameMap(int width, int height) {
        buildings = new ArrayList<DrawableBuilding>();
        grid = new Grid(width, height);
    }

    public TileType getTile(int posX, int posY) {
        return grid.getTile(posX, posY);
    }
    public boolean isTileWalkable(int posX, int posY) {
        return grid.isWalkable(posX, posY);
    }
    public boolean isBuildingAtPoint(int posX, int posY) {
        return grid.isBuildingAtPoint(posX, posY);
    }
    public boolean posWithinMap(int posX, int posY) {
        return grid.posWithinGrid(posX, posY);
    }
    public ArrayList<Vector2i> findPath(Vector2i start, Vector2i end) {
        return grid.findPath(start, end);
    }

    public boolean addPath(int posX, int posY) {
        return grid.addPath(posX, posY);
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

    // adds a buildable to the map. Returns -1 if not successfully added. Returns
    // the building ID if successfully added.
    int addBuilding(BuildingType type, int posX, int posY) {
        int newID = buildingIDGiver.next();
        if (type == BuildingType.Accommodation) {
            DrawableBuilding building = new DrawableBuilding(newID, posX, posY, type);
            if (grid.addBuilding(building) == false) {
                buildingIDGiver.returnID(newID);
                return -1;
            }
            buildings.add(building);
            return newID;
        }
        else if (type == BuildingType.LectureTheatre) {
            DrawableBuilding building = new DrawableBuilding(newID, posX, posY, type);
            if (grid.addBuilding(building) == false) {
                buildingIDGiver.returnID(newID);
                return -1;
            }
            buildings.add(building);
            return newID;
        }
        
        return -1;
    }

    // Removes a buildable from the map where the building is at the point pos. Returns -1
    // if unsuccessful and the buildingID of the removed building if successful.
    public int removeBuildableAtPoint(int posX, int posY) {
        // Checks if the tile contains a non removeable.
        TileType tile = grid.getTile(posX, posY);
        if (tile == TileType.Empty || tile == TileType.River || tile == TileType.Road) {
            return -1;
        }
        // If the tile contains a path, remove it.
        if (tile == TileType.Path) {
            if (grid.removePath(posX, posY) == false) return -1;
        }
        
        
        // Removes the building
        int index = indexOfBuildingAtPoint(posX, posY);
        if (index == -1) return -1;

        int buildID = buildings.get(index).getId();
        if (grid.removeBuilding(buildings.get(index)) == false) return -1;
        buildings.remove(index);
        buildingIDGiver.returnID(buildID);
        return buildID;
    }

    // Returns -1 if there is no building at point. Returns the building ID of whatever is
    // at the point (posX, posY).
    public int getBuildIdAtPoint(int posX, int posY) {
        int index = indexOfBuildingAtPoint(posX, posY);
        if (index == -1) return -1;
        else return buildings.get(index).getId();
    }

    public BuildingType getBuildTypeAtPoint(int posX, int posY) {
        int index = indexOfBuildingAtPoint(posX, posY);
        if (index == -1) return null;
        return buildings.get(index).getType();
    }

    public void draw(SpriteBatch batch) {
        grid.draw(batch);
        // Draws the buildings
        for (DrawableBuilding b : buildings) {
            b.draw(batch);
        }
    }
}
