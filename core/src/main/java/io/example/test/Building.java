package io.example.test;

import com.badlogic.gdx.graphics.Texture;
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

    // used for drawing the accommodation. Texture depends on the type 
    // of the building and the level of the building.
    private Texture texture;

    // Each building has a level. Lowest level is 1 and highest level is 3.
    protected short level;

    // how many students can fit in building
    private short studentCapacity;
    // The IDs of students that are in the building.
    private int[] students;
    // The current amount of students in the building
    private short studentCount = 0;

    protected Building(Vector2i pos, short studentCapacity, BuildingType type) {
        this.pos = pos;
        this.studentCapacity = studentCapacity;
        students = new int[studentCapacity];
        this.type = type;
        if (type == BuildingType.Accommodation) {
            texture = Assets.accommodationTextureL1;
        } else if (type == BuildingType.LectureTheatre) {
            texture = Assets.lectureTheatreTextureL1;
        } else if (type == BuildingType.Restaurant) {

        }
        level = 1;
    }


    public BuildingType getType() { return type; }  

    public int getHeight() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_HEIGHT; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_HEIGHT; }
        else if (type == BuildingType.Restaurant) { return 3; }
        else { return 1; }
    }
    public int getWidth() {
        if (type == BuildingType.Accommodation) { return Consts.ACCOMMODATION_WIDTH; }
        else if (type == BuildingType.LectureTheatre) { return Consts.LECTURE_THEATRE_WIDTH; }
        else if (type == BuildingType.Restaurant) { return 2; }
        else { return 1; }
    }

    public abstract void upgrade();
    public abstract void downgrade();
    public short getLevel() { return level; }

    // Each building type should have its own capcity.
    public short getCapacity() { return studentCapacity; }
    
    // Sets the new capacity of the building. Will keep the same students in the building,
    // removing some if the capacity is set lower than the current amount of student. Only called
    // by upgrade and downgrade.
    protected void setCapacity(short newCapacity) {
        this.studentCapacity = newCapacity;
        int[] new_students = new int[newCapacity];

        for (int i = 0; i < studentCount; i++) {
            if (i+1 > newCapacity) break;
            new_students[i] = students[i];
        }
        this.students = new_students;
    }

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
        if (texture != null) {
            batch.draw(texture, pos.x, pos.y, getWidth(), getHeight());
        }
        else {
            batch.draw(Assets.couldNotLoad, pos.x, pos.y, getWidth(), getHeight());
        }
    }
}
