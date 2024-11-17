package io.example.test.gamemap;

/**
 * Each tile is a 1x1 block on the grid.
 * @author Thomas Nash
 * @see Grid
 */
public class Tile {
    public enum TileType {
        Empty,
        River,
        Road,
        Path,
        BuildingBL, // the bottom left corner of the building.
        BuildingB, // The bottom row of a building.
        Building
    }

    private TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    public TileType getTileType() { return type; }
    public void setTileType(TileType type) { this.type = type; }
}
