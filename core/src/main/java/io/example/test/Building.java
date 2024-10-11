package io.example.test;

// A building that students should be able to enter and leave.
public abstract class Building {
    private int ID;

    // how many students can fit in building
    private short studentCapacity;
    // The IDs of students that are in the building.
    private int[] studentsInBuilding;
    // The current amount of students in the building
    private short studentCount = 0;

    public Building(int buidlingID, short studentCapacity) {
        ID = buidlingID;
        this.studentCapacity = studentCapacity;
        studentsInBuilding = new int[studentCapacity];
    }

    // Each accommodation type should have its own capcity.
    public short getCapacity() {
        return studentCapacity;
    }

    int getId() {
        return ID;
    }

    // Returns true if the max amount of students are already in the building.
    public boolean isFull() {
        return studentCount >= studentCapacity;
    }

    public boolean contains(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (studentsInBuilding[i] == studentID) return true;
        }
        return false;
    }

    public void addStudent(int studentID) {
        if (isFull()) return;
        studentsInBuilding[studentCount] = studentID;
        studentCount++;
    }

    public void removeStudent(int studentID) {
        boolean foundStudent = false;
        for (int i = 0; i < studentCapacity; i++) {
            if (foundStudent) {
                studentsInBuilding[i-1] = studentsInBuilding[i];
                continue;
            } 
            if (studentsInBuilding[i] == studentID) {
                foundStudent = true;
                studentsInBuilding[i] = 0;
                studentCount--;
            }
            
        }
    }
}
