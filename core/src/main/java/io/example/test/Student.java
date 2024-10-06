package io.example.test;

import java.util.ArrayList;

import io.example.test.GameManager.Colours;
/* */
public class Student {
    private Colours colour;
    private String name;

    private float posX;
    private float posY;

    private float homePosX;
    private float homePosY;

    // A student can only be moving to one building at a time.
    private DrawableBuilding movingTo;
    // The points that would move the student to the building.
    ArrayList<Vector2i> movePoints;

    private static final float moveSpeed = 2.0f;

    private Status status;
    // 
    float timeUntilFree;

    public enum Status {
        Free,
        InLecture,
        Travelling
    }
    
    Student(String name, Colours colour, DrawableBuilding home) {
        this.name = name;
        this.colour = colour;
        this.posX = home.getPosX();
        this.posY = home.getPosY();
        status = Status.Free;
        homePosX = home.getPosX();
        homePosY = home.getPosY();
        movePoints = new ArrayList<Vector2i>();
    }

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }

    public String getName() {
        return name;
    }
    public Colours getColour() {
        return colour;
    }

    public void setNewHome(int posX, int posY) {
        homePosX = posX;
        homePosY = posY;
    }

    public void moveTo(DrawableBuilding building) {
        movingTo = building;
    }
    public void setPath(ArrayList<Vector2i> path) {
        movePoints = path;
    }

    /* 
    public void update(float deltaTime) {
        if (movePoints.size() == 0) return;

        // moves the student in the direction of the building. Dont care about obstacles or path for now.
        float directionX = movePoints.get(0).x - posX;
        float directionY = movePoints.get(0).y - posY;
        status = Status.Travelling;
        posX += directionX * (deltaTime * moveSpeed);
        posY += directionY * (deltaTime * moveSpeed);
        
        // if they have reached their destination, remove.
        if (posX == movePoints.get(0).x && posY == movePoints.get(0).y) {
            movePoints.remove(0);

            if (movingTo.getType() == Buildable.Accommodation) {
                status = Status.Free;
            }
            else if (movingTo.getType() == Buildable.Accommodation) {
                status = Status.InLecture;
            }
            
        }
    }
    */
}
