package io.example.test;

public class Accommodation extends Building {
    // The IDS of students this building is home to.
    private int[] homeTo;
    private int homeToCount = 0;
    
    public Accommodation(Vector2i pos) {
        super(pos, (short)2);
        homeTo = new int[2];
        for (int i = 0; i < 2; i++) {
            homeTo[i] = -1;
        }
        type = BuildingType.Accommodation;
    }


    private boolean makeHomeOfStudent(int studentId) {
        if (homeToCount < 2) {
            for (int i = 0 ; i < 2; i++) {
                if(homeTo[i] == -1) {
                    homeTo[i] = studentId;
                    homeToCount++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean removeStudentFromHome(int studentID) {
        if (homeToCount <= 0) return false;
        for (int i = 0; i < 2; i++) {
            if (homeTo[i] == studentID) {
                homeTo[i] = -1;
                homeToCount--;
                return true;
            }
        }
        return false;
    }

    public void makeStudentsHome(StudentManager studentManager) {
        int studentID;
        while (homeToCount < (int)getCapacity()) {
            studentID = studentManager.addStudent(pos);
            homeTo[homeToCount] = studentID;
            homeToCount++;
        }
    }

    public void killEmAll(StudentManager studentManager) {
        int studentID;
        while (homeToCount > 0) {
            studentID = homeTo[homeToCount-1];
            studentManager.removeStudent(studentID);
            homeToCount--;
        }
    }



    // Returns the amount of students currently using this accommodation as a home.
    public int getStudentHouseCount() {
        return homeToCount;
    }

    // Removes a random student from the house and returns the ID of the student removed.
    // Returns -1 if no student was removed.
    public int pop() {
        if (homeToCount <= 0) return -1;
        for (int i = 0; i < 2; i++) {
            if (homeTo[i] != -1) {
                int output = homeTo[i];
                homeTo[i] = -1;
                homeToCount--;
                return output;
            }
        }
        return -1;
    }
}
