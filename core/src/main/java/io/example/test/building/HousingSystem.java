package io.example.test.building;

import io.example.test.student.StudentManager;

/**
 * This system is used to manipulate the {@link HousingComponent} of a building.
 * Will create new students and add them to the house and {@link StudentManager} and
 * will remove students from the house and {@link StudentManager}. 
 * @author Thomas Nash
 */
public class HousingSystem {
    // The student manager that will be used to add and remove students from.
    private StudentManager studentManager;

    // Added so the garbage collector has to do less work.
    private HousingComponent housingComponent;
    
    public HousingSystem(StudentManager studentManager) {
        this.studentManager = studentManager;
    }


    /**
     * This method should be called each time a new Building with a housing component is built.
     * It will add create new students and add set their home to the specified house 
     * @param building The building with the housing component.
     */
    public void fillHome(Building building) {
        if (building.housingComponent == null) return;
        int studentID;
        housingComponent = building.housingComponent;
        while (housingComponent.isFull() == false) {
            studentID = studentManager.addStudent(building);
            housingComponent.addStudentToHome(studentID);
        }
    }

    /**
     * This method should be called each time a Building with a housing component is destroyed.
     * It will automatically remove each student whose home is this building from the face
     * of the earth.
     * @param building The building with the housing component
     */
    public void killEmAll(Building building) {
        if (building.housingComponent == null) return;
        int studentID;
        housingComponent = building.housingComponent;
        while (housingComponent.count() > 0) {
            studentID = housingComponent.pop();
            studentManager.removeStudent(studentID);
        }
    }
}
