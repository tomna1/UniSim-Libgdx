package io.example.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// A building that students should be able to enter and leave.
public abstract class Building {
    public enum BuildingType {
        LectureTheatre, 
        Accommodation,
        Restaurant,
    }
    
    // The position works as an ID as 2 buildings cannot have the same
    // position.
    public Vector2i pos;

    // Used for drawing and casting to the appropriate type.
    protected BuildingType type;

    // how many students can fit in building
    private short studentCapacity;
    // The IDs of students that are in the building.
    private int[] students;
    // The current amount of students in the building
    private short studentCount = 0;

    public Building(Vector2i pos, short studentCapacity) {
        this.pos = pos;
        this.studentCapacity = studentCapacity;
        students = new int[studentCapacity];
    }

    public int getHeight() {
        if (type == BuildingType.Accommodation) { return 2; }
        else if (type == BuildingType.LectureTheatre) { return 3; }
        else if (type == BuildingType.Restaurant) { return 3; }
        else { return 1; }
    }
    public int getWidth() {
        if (type == BuildingType.Accommodation) { return 2; }
        else if (type == BuildingType.LectureTheatre) { return 3; }
        else if (type == BuildingType.Restaurant) { return 2; }
        else { return 1; }
    }

    // Each building type should have its own capcity.
    public short getCapacity() { return studentCapacity; }
    public BuildingType getType() { return type; }   

    // Returns true if the max amount of students are already in the building.
    public boolean isFull() {
        return studentCount >= studentCapacity;
    }

    public boolean contains(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) return true;
        }
        return false;
    }

    // A students enters the building.
    public void enterStudent(int studentID) {
        if (isFull()) return;
        students[studentCount] = studentID;
        studentCount++;
    }

    // A student exits the building.
    public void exitStudent(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) {
                int temp = students[studentCount];
                students[i] = temp;
                studentCount--;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (type == BuildingType.Accommodation) {
            batch.draw(Assets.accommodationTexture, pos.x, pos.y, getWidth(), getHeight());
        }
        else if (type == BuildingType.LectureTheatre) {
            batch.draw(Assets.lectureTheatreTexture, pos.x, pos.y, getWidth(), getHeight());
        }
        else if (type == BuildingType.Restaurant) {
            batch.draw(Assets.restaurantTexture, pos.x, pos.y, getWidth(), getHeight());
        }
        else {
            batch.draw(Assets.couldNotLoad, pos.x, pos.y, getWidth(), getHeight());
        }
    }
}
