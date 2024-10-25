package io.example.test;
// A component that can be added to the building class. It stores the studentIDs
// of students who have entered this building.
public class EnterableComponent {
    // how many students can fit in building
    private int studentCapacity;
    // The IDs of students that are in the building.
    private int[] students;
    // The current amount of students in the building
    private int studentCount = 0;

    public EnterableComponent(int studentCapacity) {
        this.studentCapacity = studentCapacity;
        this.students = new int[studentCapacity];
    }

    // Each building type should have its own capcity.
    public int getCapacity() { return studentCapacity; }
    // Returns true if the max amount of students are already in the building.
    public boolean isFull() { return studentCount >= studentCapacity; }

    public boolean contains(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) {
                return true;
            }
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
    public boolean exitStudent(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) {
                students[i] = students[studentCount];
                studentCount--;
                return true;
            }
        }
        return false;
    }
}
