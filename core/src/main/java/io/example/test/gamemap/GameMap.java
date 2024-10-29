package io.example.test.gamemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.building.Building;
import io.example.test.building.BuildingActivityComponent;
import io.example.test.building.BuildingFactory;
import io.example.test.building.EventSystem;
import io.example.test.building.HousingSystem;
import io.example.test.building.Building.BuildingType;
import io.example.test.gamemap.Tile.TileType;
import io.example.test.student.Student;
import io.example.test.student.StudentActivity;
import io.example.test.student.StudentManager;
import io.example.test.util.Consts;
import io.example.test.util.Vector2i;

/**
 * Stores all the buildings and the grid of the gameMap. Mainly
 * used for adding building to the map and pathfinding between buildings.
 * @author Thomas Nash
 */
public class GameMap {
    private Map<Vector2i, Building> buildings;

    // Used to store what each individual tile contains and where most checks and
    // algorithms take place.
    private Grid grid;

    // used to create new buildings
    private BuildingFactory buildingFactory = new BuildingFactory();

    // used to add students to a building with a housing component.
    private HousingSystem housingSystem;

    private EventSystem eventSystem;
    

    public GameMap(StudentManager studentManager, int width, int height) {
        grid = new Grid(width, height);
        buildings = new HashMap<>();
        housingSystem = new HousingSystem(studentManager);
        eventSystem = new EventSystem(studentManager);
    }

    public TileType getTile(int posX, int posY) { return grid.getTile(new Vector2i(posX, posY)); }
    public boolean isTileWalkable(int posX, int posY) { return grid.isWalkable(new Vector2i(posX, posY)); }
    public boolean isBuildingAtPoint(int posX, int posY) { return grid.isBuildingAtPoint(new Vector2i(posX, posY)); }
    public boolean posWithinMap(int posX, int posY) { return grid.posWithinGrid(new Vector2i(posX, posY)); }
    public ArrayList<Vector2i> findPath(Vector2i start, Vector2i end) { return grid.findPath(start, end); }
    public ArrayList<Vector2i> findPath(Vector2i start, Building end) { return grid.findPath(start, end.pos); }
    
    /**
     * Attempts to adds a single path at the point.
     * @param posX The x coord of the point.
     * @param posY The y coord of the point.S
     * @return true if successfully added and false if not.
     */
    public boolean addPath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.addPath(pos);
    }
    /**
     * Attempts to remove a single path at the point.
     * @param posX The x coord of the point.
     * @param posY The y coord of the point.
     * @return true is successfully removed and false otherwise.
     */
    public boolean removePath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.removePath(pos);
    }


    /**
     * Checks to find if there is a specific building occupying a point in the map
     * and returns it if it is.
     * @param posX The x coord of the point.
     * @param posY The y coord of the point.
     * @return The building at the point or null if no building is found.
     */
    public Building getBuildingAtPoint(int posX, int posY) {
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
            if (buildings.containsKey(pos) == false) return null;
            return buildings.get(pos);
        }
        return null;
    }


    /**
     * Tries to add a building to the map at that point. 
     * @param type The type of the building to add.
     * @param posX The x coord of the building.
     * @param posY The y coord of the building.
     * @return true if successfully added and false if not.
     */
    public boolean addBuilding(BuildingType type, int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        
        if (type == BuildingType.Accommodation) {
            Building building = buildingFactory.createBuilding(BuildingType.Accommodation, pos);
            if (grid.addBuilding(building) == false) return false;
            buildings.put(pos, building);
            housingSystem.fillHome(building);
            if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                Gdx.app.log("GameMap", "Adding " + building.getType() + " at " + pos.toString());
            }
            return true;
        }
        else if (type == BuildingType.LectureTheatre) {
            Building building = buildingFactory.createBuilding(BuildingType.LectureTheatre, pos);
            if (grid.addBuilding(building) == false) return false;
            buildings.put(pos, building);
            if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                Gdx.app.log("GameMap", "Adding " + building.getType() + " at " + pos.toString());
            }
            return true;
        }
        
        return false;
    }

    /**
     * Tries to remove a building from the map at the specified point. 
     * @param posX The x coord of the point.
     * @param posY The y coord of the point.
     * @return true if successfully removed and false if not.
     */
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
            if (buildings.containsKey(pos) == false) return false;
            
            Building building = buildings.get(pos);
            if (building.housingComponent != null) {
                housingSystem.killEmAll(building);
            }
            grid.removeBuilding(building);
            buildings.remove(pos);
            if (Consts.BUILDING_PLACEMENT_DEBUG_MODE_ON) {
                Gdx.app.log("GameMap", "Removing " + building.getType() + " at " + pos.toString());
            }
            return true;
        }

        return false;
    }
    
    /**
     * Attempts to remove either a path or a building at the point specified.
     * @param posX The x coord of the point.
     * @param posY The y coord of the point.
     * @return true if a path or building was removed and false otherwise.
     */
    public boolean removeBuildableAtPoint(int posX, int posY) {
        if (removePath(posX, posY)) return true;
        return removeBuildingAtPoint(posX, posY);
    }


    /**
     * Finds the nearest building with a {@link BuildingActivityComponent} that
     * has the same activity as specified and where the ActivityComponent allows
     * walkins and the building is not full.
     * @param startPosX The x coord to start the search at.
     * @param startPosY The y coord to start the search at.
     * @param activity The type of activity to find.
     * @return The activity of the building. Can be null if no activity found.
     */
    public StudentActivity findNearestActivity(int startPosX, int startPosY, Student.Activity activity) {
        // TODO: definitely optimise.
        Building closestBuilding = null;
        ArrayList<Vector2i> currPath;

        int distance = Integer.MAX_VALUE;
        int currDistance;
        Vector2i pos = new Vector2i(startPosX, startPosY);

        for (Building b : buildings.values()) {
            if (
            b.activityComponent.activityType == activity && 
            b.activityComponent.isWalkIn == true && 
            b.enterableComponent.isFull() == false
            ) {
                currPath = findPath(pos, b.pos);
                if (currPath == null) continue;
                currDistance = currPath.size();
                if (currDistance < distance) {
                    distance = currDistance;
                    closestBuilding = b;
                }
            }
        }

        if (closestBuilding == null) return null;
        return closestBuilding.getActivity();
    }


    /**
     * Updates all the buildings with events.
     * @param deltaTime Time since last frame.
     */
    public void update(float deltaTime) {
        for (Building b : buildings.values()) {
            eventSystem.updateBuilding(b, deltaTime);
        }
    }

    /**
     * Draws the grid and then all buildings in that order.
     * @param batch 
     */
    public void draw(SpriteBatch batch) {
        // draws the map. Does not draw buildings.
        grid.draw(batch);
        // Draws the dBuildings
        for (Building b : buildings.values()) {
            b.draw(batch);
        }
    }
}
