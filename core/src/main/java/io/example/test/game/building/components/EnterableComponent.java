package io.example.test.building.components;

/**
 * A component that can be added to the building class. It stores the studentIDs
 * of students who have entered this building. It is manipulated by the
 * {@link io.example.test.building.systems.EnteringSystem}.
 * @author Thomas Nash
 */
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

    /**
     * @return The amount of people this building can fit in it.
     */
    public int getCapacity() { return studentCapacity; }
    /**
     * @return true if the max amount of people are already inside.
     */
    public boolean isFull() { return studentCount >= studentCapacity; }

    /**
     * Checks whether the component contains a studentID. 
     * @param studentID ID of a student
     * @return true if component contains ID and false otherwise.
     */
    public boolean contains(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) return true;
        }
        return false;
    }

    /**
     * Attempts to add a student to the component. Think of it like a student entering
     * a building.
     * @param studentID ID of a student.
     * @return true if successfully added and false otherwise.
     */
    public boolean enterStudent(int studentID) {
        if (isFull()) return false;
        students[studentCount] = studentID;
        studentCount++;
        return true;
    }

    /**
     * Attempts to remove a student from the component Think of it like a student leaving
     * a building.
     * @param studentID ID of a student.
     * @return true if successfully removed and false otherwise.
     */
    public boolean exitStudent(int studentID) {
        for (int i = 0; i < studentCapacity; i++) {
            if (students[i] == studentID) {
                students[i] = students[studentCount-1];
                studentCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a copy of the data this component is storing which are the IDs
     * of students which are currently inside this component.
     * @return Array of ints which represent the IDs of student.
     */
    public int[] data() {
        int[] data = new int[studentCapacity];
        data = students.clone();
        return data;
    }
}
