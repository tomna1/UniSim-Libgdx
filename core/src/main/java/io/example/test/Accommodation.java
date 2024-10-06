package io.example.test;

// Non drawable object
public class Accommodation {
    private int studentSize;
    private int studentCount;

    // Used to distinguish the building on the map.
    int posX;
    int posY;
    
    public Accommodation(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        
        studentSize = 2;
        studentCount = 0;

        // texture.dispose();
    }
   

    public int getStudentSize() {
        return studentSize;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public void addStudent() {
        if (studentCount < studentSize) {
            studentCount++;
        }
    }

    public int getStudentCount() {
        return studentCount;
    }

    
    static int getWidth() {
        return 2;
    }
    static int getHeight() {
        return 2;
    }
}
