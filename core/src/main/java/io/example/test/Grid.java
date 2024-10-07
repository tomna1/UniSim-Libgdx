package io.example.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// A grid contains informtion about each specific tile in the game.
// Will be used to check if a building can be added to the map or not.
public class Grid {
    private TileType[][] tiles;
    
    private int width;
    private int height;

    public enum TileType {
        Empty,
        River,
        Road,
        Path,
        BuildingBL, // the bottom left corner of the building.
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

    // Returns true if the coordinates are located within the grid.
    public boolean posWithinGrid(int posX, int posY) {
        if (posX < 0 || posY < 0) return false;
        if (posX >= width || posY >= height) return false;

        return true;
    }

    // Checks if a building can fit within the dimension of the grid. Does not
    // check if there is actual space in the grid.
    public boolean canBuildingFitInGrid(DrawableBuilding b) {
        if (b.getPosX() < 0 || b.getPosY() < 0) return false;
        
        if (
        b.getPosX() + b.getWidth() > width ||
        b.getPosY() + b.getHeight() > height
        ) return false;

        return true;
    }

    // Returns true if there is enough space in the grid to fit a building at a point.
    public boolean containsSpace(DrawableBuilding b) {
        if (canBuildingFitInGrid(b) == false) return false;
        int posX = b.getPosX();
        int posY = b.getPosY();
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
    public TileType getTile(int posX, int posY) {
        if (posWithinGrid(posX, posY) == false) return null;
        return tiles[posY][posX];
    }


    // Adds a singular path tile to the grid.
    public boolean addPath(int posX, int posY) {
        Vector2i pos = new Vector2i(posX, posY);
        if (posWithinGrid(pos.x, pos.y) == false) return false;
        if (tiles[pos.y][pos.x] != TileType.Empty) return false;
        tiles[pos.y][pos.x] = TileType.Path;
        return true;
    }

    // Adds a straight path to the grid from start to end.
    public boolean addStraightPath(Vector2i start, Vector2i end) {
        if (posWithinGrid(start.x, start.y) == false) return false;
        if (posWithinGrid(end.x, end.y) == false) return false;
        if (start.x != end.x && start.y != end.y) return false;
        
        
        return true;
    }

    // Removes a singular path file from the grid.
    public boolean removePath(int posX, int posY) {
        if (posWithinGrid(posX, posY) == false) return false;
        if (tiles[posY][posX] != TileType.Path) return false;
        tiles[posY][posX] = TileType.Empty;
        return true;
    }

    // Adds a building to the grid. Returns true if successfully added and false
    // otherwise.
    public boolean addBuilding(DrawableBuilding b) {
        if (containsSpace(b) == false) return false;

        int posX = b.getPosX();
        int posY = b.getPosY();
        int width = b.getWidth();
        int height = b.getHeight();
        // Adds the building
        for (int i = posY; i < posY + height; i++) {
            for (int j = posX; j < posX + width; j++) {
                tiles[i][j] = TileType.Building;
            }
        }
        tiles[posY][posX] = TileType.BuildingBL;
        return true;
    }

    // ISSUE: POSSIBLE ISSUE AS A USER MAY BE ABLE TO REMOVE A DIFFERENT TYPE
    // OF BUILDING FROM THE ONE THAT WAS PLACED. THE GAMEMAP SHOULD FIX THIS
    // AS A TILE SHOULD ONLY BE ASSOCIATED WITH A SINGLE BUILDING BUT ANYTHING
    // IS POSSIBLE. Removes a building from the grid.
    public boolean removeBuilding(DrawableBuilding b) {
        int posX = b.getPosX();
        int posY = b.getPosY();

        if (tiles[posY][posX] != TileType.BuildingBL) return false;

        int width = b.getWidth();
        int height = b.getHeight();
        if (width == 1 && height == 1) {
            tiles[posY][posX] = TileType.Empty;
            return true;
        }

        // Checks Building dimensions are correct
        for (int i = posX+1; i < posX + width; i++) {
            if (tiles[posY][i] != TileType.Building) return false;
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
