package io.example.test;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// This is the class where all the students are stored and updated. This
// is where all the logic for the students go. 
public class StudentManager {
    private Map<Integer, Student> students;
    private UniqueIDGiver IDGiver = new UniqueIDGiver();

    // used for managing deltatime between update calls.
    float totalDeltaTime = 0.0f;
    // How long the manager should wait before updating the meters of each
    // student.
    private static final float timeBetweenStudentMeterUpdates = 1.0f;

    GameMap gameMap;

    public StudentManager() {
        students = new HashMap<>();
    }
    // This method should be called before any other methods can be called.
    public void useMap(GameMap gameMap) { this.gameMap = gameMap;}

    // Returns the studentID if it has been successfully added and returns -1 if not.
    public int addStudent(Vector2i homePos) {
        int id = IDGiver.next();
        if (id == 0) {
            return -1;
        }
        Student student = new Student(id, "", GameManager.getRandomColour(), homePos);
        students.put(id, student);
        return id;
    }

    // Removes a student from the manager based on their id.
    public void removeStudent(int studentID) {
        if (students.containsKey(studentID) == false) {
            return;
        }
        IDGiver.returnID(studentID);
        students.remove(studentID);
    }

    // Returns the average satisfaction of a student.
    public float getOverallSatisfaction() {
        float satisfaction = 0.0f;
        float satisfactionMult = 100.0f / students.size();
        for (Student stu : students.values()) {
            satisfaction += satisfactionMult * stu.getSatisfaction();
        }
        return satisfaction;
    }

    // updates all students.
    public void update(float deltaTime) {
        totalDeltaTime += deltaTime;
        boolean updateMeters = false;
        if (totalDeltaTime > timeBetweenStudentMeterUpdates) {
            updateMeters = true;
            totalDeltaTime = 0.0f;
        }
        for (Student stu : students.values()) {
            stu.update(deltaTime, updateMeters);
        }
    }

    // Draws all the travelling students onto the screen.
    public void drawStudents(SpriteBatch batch) {
        for (Student stu : students.values()) {
            stu.draw(batch);
        }
    }
}
