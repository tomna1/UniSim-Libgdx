package io.example.test;

import com.badlogic.gdx.graphics.Texture;

public class Assets {
    // Misc textures
    public static Texture couldNotLoad;
    public static Texture studentTexture;
    
    // Building textures used by gamemap.
    public static Texture accommodationTextureL1;
    public static Texture accommodationTextureL2;
    public static Texture accommodationTextureL3;
    
    public static Texture lectureTheatreTextureL1;
    public static Texture lectureTheatreTextureL2;
    public static Texture lectureTheatreTextureL3;
    
    public static Texture restaurantTextureL1;
    public static Texture restaurantTextureL2;
    public static Texture restaurantTextureL3;

    // Tile textures used by grid.
    public static Texture grassTile;
    public static Texture pathTile;

    private Assets() {};

    // Note that textured cannot be loaded before libgdx has called the create method in
    // Main.java
    public static void loadTextures() {
        couldNotLoad = new Texture("misc/couldnot_load.png");
        studentTexture = new Texture("misc/student.png");
        
        accommodationTextureL1 = new Texture("buildings/house.jpg");
        lectureTheatreTextureL1 = new Texture("buildings/lecture_theatre.png");
        restaurantTextureL1 = new Texture("misc/couldnot_load.png");

        grassTile = new Texture("tiles/grass.png");
        pathTile = new Texture("tiles/path.jpg"); 
    }
}
