package io.example.test;

/**
 * A component that can be added to the {@link Building} class. It stores the studentIDs
 * of students who house this is. This component is manipulated by the {@link HousingSystem}.
 * @author Thomas Nash
*/
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

    /**
     * @return The amount of students this houses.
     */
    public int count() { return homeToCount; }
    /**
     * @return True if full and false if not.
     */
    public boolean isFull() { return homeToCount >= maxCount; }

    /**
     * Checks to see whether this studentID is in the data.
     * @param studentID ID of the student.
     * @return true if it is and false if not.
     */
    public boolean containsStudent(int studentID) {
        for (int i = 0; i < homeToCount; i++) {
            if (homeTo[i] == studentID) return true;
        }
        return false;
    }

    /**
     * Attempts to add a studentID to the home.
     * @param studentID ID of the student.
     * @return true if successfully added and false otherwise.
     */
    public boolean addStudentToHome(int studentID) {
        if (homeToCount >= maxCount) return false;
        homeTo[homeToCount] = studentID;
        homeToCount++;
        return true;
    }

    /**
     * Attempts to remove a student from the home.
     * @param studentID ID of the student.
     * @return true if successfully removed and false if not.
     */
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

    /**
     * Removes a random studentID from the home and returns it. 
     * @return ID of a student or -1 if not studentID to return.
     */
    public int pop() {
        if (homeToCount <= 0) return -1;
        homeToCount--;
        return homeTo[homeToCount];
    }
}
