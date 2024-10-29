package io.example.test.util;

import com.badlogic.gdx.graphics.Texture;

import io.example.test.Main;
/**
 * This class is used to store all textures that will be used in the game.
 * All textures are made public static variable and should not be changed
 * by another other class.
 * @author Thomas Nash
 */
public class Assets {
    // Misc textures
    public static Texture couldNotLoad;
    public static Texture studentTexture;
    
    // Building textures used by gamemap.
    public static Texture accommodationTextureL1;
    public static Texture lectureTheatreTextureL1;
    public static Texture restaurantTextureL1;

    // Tile textures used by grid.
    public static Texture grassTile;
    public static Texture pathTile;

    private Assets() {};

    /**
     * This method loads all the textures that might be used.
     * Note that this method should not be called before libgdx has called the
     * {@link Main#create()} method. 
     */
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
