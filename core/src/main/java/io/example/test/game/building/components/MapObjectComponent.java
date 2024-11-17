package io.example.test.game.building.components;

import io.example.test.game.util.Vector2i;

/**
 * This component can be added to buildings to give them a position and a
 * width and a height which is necessary for adding the object to a map.
 * @author Thomas Nash
 */
public class MapObjectComponent {
    public int width;
    public int height;

    public Vector2i pos;

    public MapObjectComponent(Vector2i pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }
}
