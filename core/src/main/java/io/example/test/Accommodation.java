package io.example.test;

// Non drawable object
public class Accommodation {
   static final int width = 2;
   static final int height = 2;
   
    // The number of students this accommodation can hold.
    private int studentSize;
    // The number of students currently living in this accommodation.
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
   
    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
    public void addStudent() {
        if (studentCount < studentSize) {
            studentCount++;
        }
    }
    public int getStudentSize() {
        return studentSize;
    }
    public int getStudentCount() {
        return studentCount;
    }

    
    static int getWidth() {
        return width;
    }
    static int getHeight() {
        return height;
    }
}
