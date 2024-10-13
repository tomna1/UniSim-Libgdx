package io.example.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

// A grid contains informtion about each specific tile in the game.
// Will be used to check if a building can be added to the map or not.
public class Grid {
    // defines the max amount of nodes a pathfinding algorithm should check.
    private static final int MAX_NODES_CHECKED = 200;
    
    private TileType[][] tiles;
    
    private int width;
    private int height;

    public enum TileType {
        Empty,
        River,
        Road,
        Path,
        BuildingBL, // the bottom left corner of the building.
        BuildingB, // The bottom row of a building.
        Building
    }

    public Grid(int width, int height) {
        tiles = new TileType[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = TileType.Empty;
            }
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public boolean isBuildingAtPoint(Vector2i pos) {
        TileType tile = getTile(pos);
        if (tile == null) return false;
        if (tile == TileType.Building || tile == TileType.BuildingBL) return true;
        return false;
    }

    // Returns true if the coordinates are located within the grid.
    public boolean posWithinGrid(Vector2i pos) {
        if (pos.x < 0 || pos.y < 0) return false;
        if (pos.x >= width || pos.y >= height) return false;

        return true;
    }

    // Checks if a building can fit within the dimension of the grid. Does not
    // check if there is actual space in the grid.
    public boolean canBuildingFitInGrid(Building b) {
        if (b.pos.x < 0 || b.pos.y < 0) return false;
        
        if (
        b.pos.x + b.getWidth() > width ||
        b.pos.y + b.getHeight() > height
        ) return false;

        return true;
    }

    // Returns true if there is enough space in the grid to fit a building at a point.
    public boolean canAddBuilding(Building b) {
        if (canBuildingFitInGrid(b) == false) return false;
        int posX = b.pos.x;
        int posY = b.pos.y;
        int width = b.getWidth();
        int height = b.getHeight();
        // Checks all tiles that the building work take up are empty.
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                if (tiles[i][j] != TileType.Empty) return false;
            }
        }
        return true;
    }

    // Returns the tiletupe at a particular coorindate. Returns null if outside range.
    public TileType getTile(Vector2i pos) {
        if (posWithinGrid(pos) == false) return null;
        return tiles[pos.y][pos.x];
    }

    // Returns true if a particular tile on the grid is walkable. A tile is 
    // walkable if there is a path or building there.
    public boolean isWalkable(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        TileType tile = tiles[pos.y][pos.x];
        if (tile == TileType.Path || tile == TileType.Building || tile == TileType.BuildingBL) return true;
        return false;
    }


    // Adds a singular path tile to the grid.
    public boolean addPath(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        if (tiles[pos.y][pos.x] != TileType.Empty) return false;
        tiles[pos.y][pos.x] = TileType.Path;
        return true;
    }

    // Adds a straight path to the grid from start to end.
    public boolean addStraightPath(Vector2i start, Vector2i end) {
        // TODO: IMPLEMENT
        return true;
    }

    // Removes a singular path tile from the grid.
    public boolean removePath(Vector2i pos) {
        if (posWithinGrid(pos) == false) return false;
        if (tiles[pos.y][pos.x] != TileType.Path) return false;
        tiles[pos.y][pos.x] = TileType.Empty;
        return true;
    }

    // Adds a building to the grid. Returns true if successfully added and false
    // otherwise.
    public boolean addBuilding(Building b) {
        if (canAddBuilding(b) == false) return false;

        int posX = b.pos.x;
        int posY = b.pos.y;
        int width = b.getWidth();
        int height = b.getHeight();
        // Adds the building
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                if (i == (posY + height - 1)) {
                    tiles[i][j] = TileType.BuildingB;
                } else {
                    tiles[i][j] = TileType.Building;
                }
            }
        }
        tiles[posY][posX] = TileType.BuildingBL;
        return true;
    }

    // ISSUE: POSSIBLE ISSUE AS A USER MAY BE ABLE TO REMOVE A DIFFERENT TYPE
    // OF BUILDING FROM THE ONE THAT WAS PLACED. THE GAMEMAP SHOULD FIX THIS
    // AS A TILE SHOULD ONLY BE ASSOCIATED WITH A SINGLE BUILDING BUT ANYTHING
    // IS POSSIBLE. Removes a building from the grid.
    public boolean removeBuilding(Building b) {
        int posX = b.pos.x;
        int posY = b.pos.y;

        if (tiles[posY][posX] != TileType.BuildingBL) return false;

        int width = b.getWidth();
        int height = b.getHeight();
        if (width == 1 && height == 1) {
            tiles[posY][posX] = TileType.Empty;
            return true;
        }

        // Checks Building dimensions are correct
        for (int i = posX+1; i < posX + width; i++) {
            if (tiles[posY][i] != TileType.Building || tiles[posY][i] != TileType.BuildingB) return false;
        }
        for (int i = posY+1; i < posY + height; i++) {
            if (tiles[i][posX] != TileType.Building) return false;
        }

        // Removes all building tiles
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                tiles[i][j] = TileType.Empty;
            }
        }

        return true;
    }


    // Finds a valid path between a start point and an end point. Will return null if
    // the path is not possible.
    public ArrayList<Vector2i> findPath(Vector2i start, Vector2i end) {
        // Uses BFS algorithm. Optimisations can be made.
        if (isWalkable(start) == false) return null;
        if (isWalkable(end) == false) return null;
        if (start.equals(end)) return null;

        // All path tiles in this algorithm are considered nodes.
       
        boolean[][] isVisited = new boolean[height][width];
        // contains the the tile of the previous node.
        Vector2i[][] prevNode = new Vector2i[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                isVisited[i][j] = false;
            }
        }

        // This will be used to decide which path tile to visit next.
        Queue<Vector2i> nodeQueue = new LinkedList<>();
        nodeQueue.add(start);

        while (nodeQueue.size() > 0) {
           Vector2i node = nodeQueue.poll();
            if (node.equals(end)) {
                // construct the completed path.
                ArrayList<Vector2i> path = new ArrayList<>();
                while (node.equals(start) == false) {
                    path.add(0, node);
                    node = prevNode[node.y][node.x];
                }
                path.add(0, node);
                return path;
            } 

            // Checks left neighbour
            node.x--;
            if (getTile(node) == TileType.Path) {
                if (isVisited[node.y][node.x] == false) {
                    nodeQueue.add(new Vector2i(node));
                    prevNode[node.y][node.x] = node;
                    isVisited[node.y][node.x] = true;
                }
            }
            node.x++;
            // Checks right neighbour
            node.x++;
            if (getTile(node) == TileType.Path) {
                if (isVisited[node.y][node.x] == false) {
                    nodeQueue.add(new Vector2i(node));
                    prevNode[node.y][node.x] = node;
                    isVisited[node.y][node.x] = true;
                }
            }
            node.x--;
            // Checks up neighbour
            node.y--;
            if (getTile(node) == TileType.Path) {
                if (isVisited[node.y][node.x] == false) {
                    nodeQueue.add(new Vector2i(node));
                    prevNode[node.y][node.x] = node;
                    isVisited[node.y][node.x] = true;
                }
            }
            node.y--;
            // Checks down neighbour
            node.y--;
            if (getTile(node) == TileType.Path) {
                if (isVisited[node.y][node.x] == false) {
                    nodeQueue.add(new Vector2i(node));
                    prevNode[node.y][node.x] = node;
                    isVisited[node.y][node.x] = true;
                }
            }
            node.y++;
        }

        // If here is reached, that means all valid nodes have been checked.
        return null;
    }


    // Draws all the tiles that make up the grid.
    public void draw(SpriteBatch batch) {
        // Draw all tiles. If they are a building tile, will draw grass instead.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == TileType.Path) {
                    batch.draw(Assets.pathTile, j, i, 1, 1);
                } else {
                    batch.draw(Assets.grassTile, j, i, 1, 1);
                }
            }
        }
    }


}
