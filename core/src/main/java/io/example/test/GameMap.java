package io.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.Tile.TileType;
import io.example.test.Building.BuildingType;
import io.example.test.Student.Status;

// The gamemap stores all the buildings as well as uses an internal grid for pathfinding.
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
    
    public boolean addPath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.addPath(pos);
    }
    public boolean removePath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        return grid.removePath(pos);
    }


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


    // adds a buildable to the map. Returns -1 if not successfully added. Returns
    // the building ID if successfully added.
    public boolean addBuilding(BuildingType type, int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        
        if (type == BuildingType.Accommodation) {
            Building building = buildingFactory.createBuilding(BuildingType.Accommodation, pos);
            if (grid.addBuilding(building) == false) return false;
            buildings.put(pos, building);
            housingSystem.onBuildingBuilt(building.housingComponent, building);
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
            if (buildings.containsKey(pos) == false) return false;
            
            Building building = buildings.get(pos);
            if (building.getType() == BuildingType.Accommodation) {
                housingSystem.onBuildingDestroyed(building.housingComponent);
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
    // Removes paths and buildings.
    public boolean removeBuildableAtPoint(int posX, int posY) {
        if (removePath(posX, posY)) return true;
        return removeBuildingAtPoint(posX, posY);
    }


    // Finds a building with the closest activity. TODO: VERY INEFFCIENT SINCE MANY findPath() CALLS
    // DEFINETELY OPTIMISE IF PERFORMANCE ISSUES.
    public ArrayList<Vector2i> findBuildingPathWithActivity(Status status, int posX, int posY) {
        ArrayList<Vector2i> closestBuilding = null;
        ArrayList<Vector2i> currPath;
        int distance = Integer.MAX_VALUE;
        int currDistance;
        Vector2i pos = new Vector2i(posX, posY);

        for (Building b : buildings.values()) {
            if (b.activityComponent.toDo == status) {
                currPath = findPath(pos, b.pos);
                if (currPath == null) continue;
                currDistance = currPath.size();
                if (currDistance < distance) {
                    distance = currDistance;
                    closestBuilding = currPath;
                }
            }
        }
        return closestBuilding;
    }


    // updates all buildings with events
    public void update(float deltaTime) {
        for (Building b : buildings.values()) {
            eventSystem.updateBuilding(b, deltaTime);
        }
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
