package io.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.Grid.TileType;
import io.example.test.Building.BuildingType;

// The gamemap stores all the buildings as well as uses an internal grid for pathfinding.
public class GameMap {
    private Map<Vector2i, Building> buildings;

    // Used to store what each individual tile contains and where most checks and
    // algorithms take place.
    private Grid grid;

    Random rand = new Random();

    // Used to create new students when accommodation is built or destroyed.
    private StudentManager studentManager;
    

    public GameMap(StudentManager studentManager, int width, int height) {
        grid = new Grid(width, height);
        buildings = new HashMap<>();
        this.studentManager = studentManager;
    }

    public TileType getTile(int posX, int posY) { return grid.getTile(new Vector2i(posX, posY)); }
    public boolean isTileWalkable(int posX, int posY) { return grid.isWalkable(new Vector2i(posX, posY)); }
    public boolean isBuildingAtPoint(int posX, int posY) { return grid.isBuildingAtPoint(new Vector2i(posX, posY)); }
    public boolean posWithinMap(int posX, int posY) { return grid.posWithinGrid(new Vector2i(posX, posY)); }
    public ArrayList<Vector2i> findPath(Vector2i start, Vector2i end) { return grid.findPath(start, end); }
    
    public boolean addPath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.addPath(pos);
    }
    public boolean removePath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.removePath(pos);
    }

    // DELETE LATER.
    public Vector2i getRandomPathPoint() {
        for (int i = 0; i < 5; i++) {
            int posX = rand.nextInt(Consts.GRID_WIDTH+1);
            int posY = rand.nextInt(Consts.GRID_HEIGHT+1);
            Vector2i pos = new Vector2i(posX, posY);
            if (grid.getTile(pos) == TileType.Path) return pos;
            // System.out.println("random pos = " + pos);
        }
        return null;
    }

    public BuildingType getBuildTypeAtPoint(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        if (buildings.containsKey(pos) == false) return null;
        return buildings.get(pos).getType();
    }


    // adds a buildable to the map. Returns -1 if not successfully added. Returns
    // the building ID if successfully added.
    public boolean addBuilding(BuildingType type, int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        
        if (type == BuildingType.Accommodation) {
            Accommodation building = new Accommodation(pos);
            if (grid.addBuilding(building) == false) return false;
            building.makeStudentsHome(studentManager);
            buildings.put(pos, building);
            if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                Gdx.app.log("GameMap", "Adding " + building.getType() + " at " + pos.toString());
            }
            return true;
        }
        else if (type == BuildingType.LectureTheatre) {
            LectureTheatre building = new LectureTheatre(pos);
            if (grid.addBuilding(building) == false) return false;
            buildings.put(pos, building);
            if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                Gdx.app.log("GameMap", "Adding " + building.getType() + " at " + pos.toString());
            }
            return true;
        }
        
        return false;
    }

    // Removes a buildable from the map where the building is at the point pos. Returns true
    // if successful and false otherwise
    public boolean removeBuildingAtPoint(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        TileType tile = grid.getTile(pos);

        // All the 3 if statements work to find the bottom left tile of the building
        // which can be used to remove the building.
        if (tile == TileType.Building) {
            while (tile == TileType.Building) {
                pos.y--;
                tile = grid.getTile(pos);
            }
        }
        
        if (tile == TileType.BuildingB) {
            while (tile == TileType.BuildingB) {
                pos.x--;
                tile = grid.getTile(pos);
            }
        }
        if (tile == TileType.BuildingBL) {
            if (buildings.containsKey(pos)) {
                Building building = buildings.get(pos);
                if (building.getType() == BuildingType.Accommodation) {
                    Accommodation acc = (Accommodation)building;
                    acc.killEmAll(studentManager);
                }
                grid.removeBuilding(buildings.get(pos));
                buildings.remove(pos);
                if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                    Gdx.app.log("GameMap", "Removing " + building.getType() + " at " + pos.toString());
                }
                return true;
            }
            return false;
        }

        return false;
    }


    public boolean removeBuildableAtPoint(int posX, int posY) {
        if (removePath(posX, posY)) return true;
        return removeBuildingAtPoint(posX, posY);
    }


    public void draw(SpriteBatch batch) {
        // draws the map. Does not draw buildings.
        grid.draw(batch);
        // Draws the dBuildings
        for (Building b : buildings.values()) {
            b.draw(batch);
        }
    }
}
