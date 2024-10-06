package io.example.test;

import com.badlogic.gdx.graphics.Texture;

public class Assets {
    // Misc textures
    public static Texture couldNotLoad;
    
    // Building textures used by gamemap.
    public static Texture accommodationTexture;
    public static Texture lectureTheatreTexture;

    // Tile textures used by grid.
    public static Texture grassTile;
    public static Texture pathTile;


    public static void loadTextures() {
        couldNotLoad = new Texture("misc/couldnot_load.png");
        
        accommodationTexture = new Texture("buildings/house.jpg");
        lectureTheatreTexture = new Texture("buildings/lecture_theatre.png");

        grassTile = new Texture("tiles/grass.png");
        pathTile = new Texture("tiles/path.jpg"); 
    }

    
}
