package io.example.test.building.components;

import com.badlogic.gdx.graphics.Texture;

/**
 * This component can be added to a building and it allows the building to
 * be drawn onto the screen using the texture associated with this
 * component. 
 * @author Thomas Nash.
 */
public class RenderComponent {
    public Texture texture;

    public RenderComponent(Texture texture) {
        this.texture = texture;
    }
}
