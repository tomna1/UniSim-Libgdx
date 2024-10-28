package io.example.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

// This class is used to control how the camera moves based on user inputs.
public class CameraManager {
    private OrthographicCamera camera;

    public CameraManager(Camera camera) {
        this.camera = (OrthographicCamera)camera;
    }
    
    public void processCameraInput() {
        // WASD moves the camera in associated directions.
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-(Consts.CAMERA_SPEED * Gdx.graphics.getDeltaTime()), 0.0f, 0.0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate((Consts.CAMERA_SPEED * Gdx.graphics.getDeltaTime()), 0.0f, 0.0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0.0f, (Consts.CAMERA_SPEED * Gdx.graphics.getDeltaTime()), 0.0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0.0f, -(Consts.CAMERA_SPEED * Gdx.graphics.getDeltaTime()), 0.0f);
        }

        // Makes sure the camera is kept within the confines of the game map.
        if (camera.position.x < 0) camera.position.x = 0;
        else if (camera.position.x > Consts.GRID_WIDTH) camera.position.x = Consts.GRID_WIDTH;
        if (camera.position.y < 0) camera.position.y = 0;
        else if (camera.position.y > Consts.GRID_WIDTH) camera.position.y = Consts.GRID_WIDTH;

        // Zooms the camera in or out depending on whether Q or E is pressed.
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) zoomOut();
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) zoomIn();

        // Sets the input processes so that whenever the mouse wheel is scrolled, it either
        // zooms in or out. WARNING: IF A NEW INPUT PROCESSOR IS SET THEN THIS WILL MOST
        // LIKELY BREAK.
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
	        public boolean scrolled (float amountX, float amountY) {
		        if (amountY < 0) zoomIn();
                else if (amountY > 0) zoomOut();
                return true;
	        }
        });

        camera.update();
    }


    private void zoomOut() {
        if (camera.zoom < Consts.CAM_MAX_ZOOM) camera.zoom += Consts.CAM_ZOOM_PER_LEVEL;
    }
    private void zoomIn() {
        if (camera.zoom > Consts.CAM_MIN_ZOOM) camera.zoom -= Consts.CAM_ZOOM_PER_LEVEL;
    }
}
