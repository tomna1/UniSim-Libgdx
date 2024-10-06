package io.example.test;

import io.example.test.GameManager.Colours;

// Non drawable object.
public class LectureTheatre {
    int studentSize;
    int studentCount;

    Colours currentLecture;
    Colours nextLecture;

    public float timeUntilNextLectureStarts;
    public float timeUntilCurrentLectureEnds;

    // Used to distinguish this building in the map.
    int posX;
    int posY;
    
    public LectureTheatre(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

        studentSize = 10;
        studentCount = 0;
    }

    public void update(float deltaTime) {

    }




    static int getWidth() {
        return 3;
    }
    static int getHeight() {
        return 3;
    }
}
