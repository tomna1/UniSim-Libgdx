package io.example.test;

// This class is used to add and remove students from a building using
// the entering component of a building.
public class EnteringSystem {
    public EnteringSystem() { }

    public boolean addStudent(EnterableComponent enterableComponent, int studentID) {
        if (enterableComponent.isFull()) return false;
        enterableComponent.enterStudent(studentID);
        return true;
    }
    public boolean removeStudent(EnterableComponent enterableComponent, int studentID) {
        return enterableComponent.exitStudent(studentID);
    }
}
