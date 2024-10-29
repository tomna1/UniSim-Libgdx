package io.example.test.building;

/** 
 * This class is used to add and remove students from a building by manipulating
 * the {@link EnterableComponent} of that Building.
 * @author Thomas Nash
 */
public class EnteringSystem {
    public EnteringSystem() { }

    /**
     * Attempts to add a student to the component. Think of it like a student
     * entering a building. 
     * @param enterableComponent The component.
     * @param studentID ID of a student.
     * @return true if successfully removed and false otherwise.
     */
    public boolean addStudent(EnterableComponent enterableComponent, int studentID) {
        if (enterableComponent.isFull()) return false;
        enterableComponent.enterStudent(studentID);
        return true;
    }
    /**
     * Attempts to remove a student from the component. Think of it like a student
     * leaving a building. 
     * @param enterableComponent The component.
     * @param studentID ID of a student.
     * @return true if successfully removed and false otherwise.
     */
    public boolean removeStudent(EnterableComponent enterableComponent, int studentID) {
        return enterableComponent.exitStudent(studentID);
    }
}
