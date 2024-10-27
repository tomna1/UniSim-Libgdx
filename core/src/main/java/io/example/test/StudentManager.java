package io.example.test;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.test.GameManager.Colours;

import com.badlogic.gdx.Gdx;

// This is the class where all the students are stored and updated. This
// is where all the logic for the students go. 
public class StudentManager {
    private Map<Integer, Student> students;
    private UniqueIDGiver IDGiver = new UniqueIDGiver();

    // used for managing deltatime between update calls.
    float totalDeltaTime = 0.0f;

    GameMap gameMap;

    public StudentManager() {
        students = new HashMap<>();
    }
    // This method should be called before any other methods can be called.
    public void useMap(GameMap gameMap) { this.gameMap = gameMap;}

    public int count() { return students.size(); }

    // Returns the studentID if it has been successfully added and returns -1 if not.
    public int addStudent(Building home) {
        int id = IDGiver.next();
        if (id == 0) {
            return -1;
        }
        if (Consts.STUDENT_LIFE_DEBUG_MODE_ON) {
            Gdx.app.log("StudentManager", "Adding new Student " + id);
        }
        Student student = new Student(gameMap, id, "", GameManager.getRandomColour(), home);
        GameManager.addMoney(Consts.MONEY_PER_STUDENT_JOINING);
        students.put(id, student);
        return id;
    }

    // Removes a student from the manager based on their id.
    public void removeStudent(int studentID) {
        if (students.containsKey(studentID) == false) {
            return;
        }
        IDGiver.returnID(studentID);
        if (Consts.STUDENT_LIFE_DEBUG_MODE_ON) {
            Gdx.app.log("StudentManager", "removing  Student " + studentID);
        }
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
        if (totalDeltaTime > Consts.TIME_BETWEEN_STUDENT_UPDATES) {
            updateMeters = true;
            totalDeltaTime = 0.0f;
        }
        for (Student stu : students.values()) {
            stu.update(deltaTime, updateMeters);
        }
    }

    // Building should have an event component which is used to ping all valid
    // students.
    public void processEvent(StudentActivity studentActivity) {
        int max = studentActivity.getEventCapacity();
        int count = 0;

        for (Student stu : students.values()) {
            if (stu.processEvent(studentActivity)) count++;
            if (count > max) return;
        }
    }

    // Draws all the travelling students onto the screen.
    public void drawStudents(SpriteBatch batch) {
        for (Student stu : students.values()) {
            stu.draw(batch);
        }
    }
}
