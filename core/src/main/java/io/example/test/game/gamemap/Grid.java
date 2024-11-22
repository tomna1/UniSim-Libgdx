package io.example.test.game.gamemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.game.building.Building;
import io.example.test.game.gamemap.Tile.TileType;
import io.example.test.game.util.Assets;
import io.example.test.game.util.Consts;
import io.example.test.game.util.Vector2i;

import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The grid contains information about each specific tile in the {@link GameMap}.
 * It is mainly used for pathfinding and checking if buildings can be placed in
 * certain places.
 * @author Thomas Nash
 */
public class Grid {
    private Tile[][] tiles;
    
    private int width;
    private int height;

    /**
     * Creates a new grid with the specified width and height.
     * @param width The width of the grid. Should be >= 1.
     * @param height The height of the grid. Should be >= 1.
     */
    public Grid(int width, int height) {
        tiles = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(TileType.Empty);
            }
        }
        this.width = width;
        this.height = height;
    }

    /**
     * @return The width of the grid.
     */
    public int getWidth() { return width; }
    /**
     * @return The height of the grid.
     */
    public int getHeight() { return height; }

    /**
     * Checks if there is a building at a point on the grid.
     * @param pos The point to check.
     * @return true if there is a building and false otherwise.
     */
    public boolean isBuildingAtPoint(Vector2i pos) {
        TileType tile = getTile(pos);
        if (tile == null) return false;
        if (tile == TileType.Building || tile == TileType.BuildingBL) return true;
        return false;
    }

    /**
     * Checks if the position is within the confines of the grid.
     * @param pos The point to check.
     * @return true if the position is within the grid and false otherwise.
     */
    public boolean posWithinGrid(Vector2i pos) {
        if (pos.x < 0 || pos.y < 0) return false;
        if (pos.x >= width || pos.y >= height) return false;

        return true;
    }

    /**
     * Checks if a building can fit within the dimensions of the grid. Does not
     * check if there is another anything blocking the builing from being built
     * in that position such as another building
     * @param building The building to check.
     * @return true if it can fit and false otherwise.
     */
    private boolean canBuildingFitInGrid(Building building) {
        if (building.mapObjectComponent.pos.x < 0 || building.mapObjectComponent.pos.y < 0) return false;
        
        if (
            building.mapObjectComponent.pos.x + building.getWidth() > width ||
            building.mapObjectComponent.pos.y + building.getHeight() > height
        ) return false;

        return true;
    }

    /**
     * Checks to see whether a building can be added to the grid. 
     * @param building The building.
     * @return true if it can be added and false otherwise.
     */
    public boolean canAddBuilding(Building building) {
        if (canBuildingFitInGrid(building) == false) return false;
        int posX = building.mapObjectComponent.pos.x;
        int posY = building.mapObjectComponent.pos.y;
        int width = building.getWidth();
        int height = building.getHeight();
        // Checks all tiles that the building work take up are empty.
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                if (tiles[i][j].getTileType() != TileType.Empty) return false;
            }
        }
        return true;
    }

    /**
     * Returns the TileType at a particular coordinate.
     * @param pos The coordinate.
     * @return TileType/null if out of range.
     */
    public TileType getTile(Vector2i pos) {
        if (posWithinGrid(pos) == false) return null;
        return tiles[pos.y][pos.x].getTileType();
    }

    /**
     * Returns true if a particular tile on the grid is walkable. A tile is 
     * walkable if there is a path or building already there.
     * @param pos The coordinate.
     * @return true if walkable and false otherwise.
     */
    public boolean isWalkable(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        TileType tile = tiles[pos.y][pos.x].getTileType();
        if (tile == TileType.Path || tile == TileType.Building || tile == TileType.BuildingBL || tile == TileType.BuildingB) return true;
        return false;
    }


    /**
     * Attempts to add a singular path tile to the grid.
     * @param pos The point at which to add the path.
     * @return true if successfully added and false otherwise.
     */
    public boolean addPath(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        if (tiles[pos.y][pos.x].getTileType() != TileType.Empty) return false;
        tiles[pos.y][pos.x].setTileType(TileType.Path);;
        if (Consts.GRID_PLACEMENT_DEBUG_MODE_ON) {
            Gdx.app.log("Grid", "Adding path at " + pos.toString());
        }
        return true;
    }


    /**
     * Attempts to remove a singular path tile from the grid.
     * @param pos The point at which to remove the path.
     * @return true is successfully removed and false otherwise.
     */
    public boolean removePath(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        if (tiles[pos.y][pos.x].getTileType() != TileType.Path) return false;
        tiles[pos.y][pos.x].setTileType(TileType.Empty);
        if (Consts.GRID_PLACEMENT_DEBUG_MODE_ON) {
            Gdx.app.log("Grid", "Removing path at " + pos.toString());
        }
        return true;
    }

    /**
     * Attempts to add a building to the grid.
     * @param building The building to add.
     * @return true if successfully added and false otherwise.
     */
    public boolean addBuilding(Building building) {
        if (canAddBuilding(building) == false) return false;

        int posX = building.mapObjectComponent.pos.x;
        int posY = building.mapObjectComponent.pos.y;
        int width = building.getWidth();
        int height = building.getHeight();
        // Adds the building
        int i = posY;
        for (int j = posX+1; j < posX + width; j++) {
            tiles[i][j].setTileType(TileType.BuildingB);
        }
        
        for (i = posY+1; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                tiles[i][j].setTileType(TileType.Building);
            }
        }
        
        tiles[posY][posX].setTileType(TileType.BuildingBL);
        return true;
    }

    // ISSUE: POSSIBLE ISSUE AS A USER MAY BE ABLE TO REMOVE A DIFFERENT TYPE
    // OF BUILDING FROM THE ONE THAT WAS PLACED. THE GAMEMAP SHOULD FIX THIS
    // AS A TILE SHOULD ONLY BE ASSOCIATED WITH A SINGLE BUILDING BUT ANYTHING
    // IS POSSIBLE. 
    
    /**
     * Attempts to remove a building from the grid. Note that this will not remove
     * the building from the buildings array in the {@link GameMap} class.
     * @param building The building to remove/
     * @return true is successfully removed and false otherwise.
     */
    public boolean removeBuilding(Building building) {
        int posX = building.mapObjectComponent.pos.x;
        int posY = building.mapObjectComponent.pos.y;

        if (tiles[posY][posX].getTileType() != TileType.BuildingBL) return false;

        int width = building.getWidth();
        int height = building.getHeight();

        // Removes all building tiles
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                tiles[i][j].setTileType(TileType.Empty);
            }
        }

        return true;
    }


    /**
     * Tries to find a valid path between 2 point. Note that both the
     * start and end points can be the bottom left corner of the building
     * but every other tile has to be a path tile.
     * @param start The start position.
     * @param end The end position.
     * @return An ArrayList containing each point on the path to the end point.
     * Returns null if path was not found. Note that if start an end are the same
     * the path will be of size=1 containing that node.
     */
    public ArrayList<Vector2i> findPath(Vector2i start, Vector2i end) {
        // TODO: OPTIMISE, A*.
        ArrayList<Vector2i> path = new ArrayList<>();
        
        if (isWalkable(start) == false) return null;
        if (isWalkable(end) == false) return null;

        if (start.equals(end)) {
            path.add(new Vector2i(start));
            return path;
        }
        
        ArrayList<Vector2i> validEndNodes = new ArrayList<>();
        if (isWalkable(end) == false) validEndNodes.add(new Vector2i(end));
        if (getTile(end) == TileType.BuildingBL) {
            validEndNodes.add(new Vector2i(end.x+1 ,end.y));
            validEndNodes.add(new Vector2i(end.x-1 ,end.y));
            validEndNodes.add(new Vector2i(end.x ,end.y+1));
            validEndNodes.add(new Vector2i(end.x ,end.y-1));
        }
        
        // Uses BFS algorithm. Optimisations can be made.
        // All path tiles in this algorithm are considered nodes.
       
        boolean[][] isVisited = new boolean[height][width];
        // contains the the tile of the previous node.
        Vector2i[][] prevNode = new Vector2i[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                isVisited[i][j] = false;
            }
        }

        // Gdx.app.log("Grid", "Finding path between " + start.toString() + " and " + end.toString());

        // This will be used to decide which path tile to visit next.
        Queue<Vector2i> nodeQueue = new LinkedList<>();
        nodeQueue.add(start);

        while (nodeQueue.size() > 0) {
           Vector2i node = nodeQueue.poll();
           Vector2i neighbour = new Vector2i(node);
            if (validEndNodes.contains(node)) {
                // construct the completed path.
                while (node.equals(start) == false) {
                    path.add(0, node);
                    node = prevNode[node.y][node.x];
                }
                path.add(0, node);
                if (path.contains(end) == false) path.add(end);
                if (Consts.PATHFINDING_DEBUG_MODE_ON) {
                    Gdx.app.log("Grid", "Found path between " + start.toString() + " and " + end);
                }
                return path;
            } 

            // Checks left neighbour
            neighbour.x--;
            if (getTile(neighbour) == TileType.Path) {
                if (isVisited[neighbour.y][neighbour.x] == false) {
                    nodeQueue.add(new Vector2i(neighbour));
                    prevNode[neighbour.y][neighbour.x] = node;
                    isVisited[neighbour.y][neighbour.x] = true;
                }
            }
            neighbour.x++;
            // Checks right neighbour
            neighbour.x++;
            if (getTile(neighbour) == TileType.Path) {
                if (isVisited[neighbour.y][neighbour.x] == false) {
                    nodeQueue.add(new Vector2i(neighbour));
                    prevNode[neighbour.y][neighbour.x] = node;
                    isVisited[neighbour.y][neighbour.x] = true;
                }
            }
            neighbour.x--;
            // Checks up neighbour
            neighbour.y++;
            if (getTile(neighbour) == TileType.Path) {
                if (isVisited[neighbour.y][neighbour.x] == false) {
                    nodeQueue.add(new Vector2i(neighbour));
                    prevNode[neighbour.y][neighbour.x] = node;
                    isVisited[neighbour.y][neighbour.x] = true;
                }
            }
            neighbour.y--;
            // Checks down neighbour
            neighbour.y--;
            if (getTile(neighbour) == TileType.Path) {
                if (isVisited[neighbour.y][neighbour.x] == false) {
                    nodeQueue.add(new Vector2i(neighbour));
                    prevNode[neighbour.y][neighbour.x] = node;
                    isVisited[neighbour.y][neighbour.x] = true;
                }
            }
            neighbour.y++;
        }

        // If here is reached, that means all valid nodes have been checked.
        if (Consts.PATHFINDING_DEBUG_MODE_ON) {
            Gdx.app.log("Grid", "Could not find path between " + start.toString() + " and " + end.toString());
        }
        return null;
    }


    /**
     * Draws all tiles that make up the grid.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        // Draw all tiles. If they are a building tile, will draw grass instead.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j].getTileType() == TileType.Path) {
                    batch.draw(Assets.pathTile, j, i, 1, 1);
                } else {
                    batch.draw(Assets.grassTile, j, i, 1, 1);
                }
            }
        }
    }


}
