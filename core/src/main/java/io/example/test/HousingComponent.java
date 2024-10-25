package io.example.test;

// A component that can be added to the building class. It stores the studentIDs
// of students who house this is. This component is manipulated by the HousingSystem.
public class HousingComponent {
    // The IDS of students this building is home to.
    private int[] homeTo;
    // maximum amount of students that can be added.
    private int maxCount;
    // Current amount of students in it.
    private int homeToCount = 0;

    // houseMaxStudentCount is the maximum amount of students
    // that can make this building their house.
    public HousingComponent(int houseMaxStudentCount) {
        homeTo = new int[houseMaxStudentCount];
        maxCount = houseMaxStudentCount;
    }

    public int count() { return homeToCount; }
    public boolean isFull() { return homeToCount >= maxCount; }

    public boolean containsStudent(int studentID) {
        for (int i = 0; i < homeToCount; i++) {
            if (homeTo[i] == studentID) return true;
        }
        return false;
    }

    // returns true if successfully added and false if not.
    public boolean addStudentToHome(int studentID) {
        if (homeToCount >= maxCount) return false;
        homeTo[homeToCount] = studentID;
        homeToCount++;
        return true;
    }

    public boolean removeStudentFromHome(int studentID) {
        for (int i = 0; i < homeToCount; i++) {
            if (homeTo[i] == studentID) {
                // swap this studentID with the last studentID so I dont have
                // to move all students after this to the left when i remove it.
                homeTo[i] = homeTo[homeToCount-1];
                homeToCount--;
                return true;
            }
        }
        return false;
    }

    // removes the last studentID from the component and returns it. Returns -1
    // if no valid student to remove. 
    public int pop() {
        if (homeToCount <= 0) return -1;
        homeToCount--;
        return homeTo[homeToCount];
    }
}
