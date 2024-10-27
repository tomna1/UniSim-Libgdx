package io.example.test;

// This system is used to affect the housing component of a building.
public class HousingSystem {
    // The student manager that will be used to add and remove students from.
    private StudentManager studentManager;
    
    public HousingSystem(StudentManager studentManager) {
        this.studentManager = studentManager;
    }


    public void onBuildingBuilt(HousingComponent housingComponent, Building home) {
        fillHome(housingComponent, home);
    }

    public void onBuildingDestroyed(HousingComponent housingComponent) {
        killEmAll(housingComponent);
    }

    // Fills the housing component with students and adds students to the map based on homePos. This
    // should be called each time a new building is built.
    private void fillHome(HousingComponent housingComponent, Building home) {
        int studentID;
        while (housingComponent.isFull() == false) {
            studentID = studentManager.addStudent(home);
            housingComponent.addStudentToHome(studentID);
        }
    }

    // Removes all student from the HousingComponent and removes them from the student manager.
    // This should be called before a new building is destroyed.
    private void killEmAll(HousingComponent housingComponent) {
        int studentID;
        while (housingComponent.count() > 0) {
            studentID = housingComponent.pop();
            studentManager.removeStudent(studentID);
        }
    }

}
