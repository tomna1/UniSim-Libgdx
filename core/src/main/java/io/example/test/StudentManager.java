package io.example.test;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

/**
 * This is the class where all students are stored and updated.
 * @author Thomas Nash
 */
public class StudentManager {
    private Map<Integer, Student> students;
    private UniqueIDGiver IDGiver = new UniqueIDGiver();

    // used for managing deltatime between update calls.
    private float totalDeltaTime = 0.0f;

    private GameMap gameMap;

    /**
     * Creates a new manager with no students.
     */
    public StudentManager() {
        students = new HashMap<>();
    }
    /**
     * Allows the student manager the assign the correct map to each student which
     * allows them to do pathfinding. SHOULD BE CALLED BEFORE ANY OTHER METHOD.
     * @param gameMap The map the students are on.
     */
    public void useMap(GameMap gameMap) { this.gameMap = gameMap;}

    /**
     * @return The amount of students stored by the manager.
     */
    public int count() { return students.size(); }

    /**
     * Attempts to add a student to the student manager.
     * @param home The home of the student.
     * @return The ID of the student or -1 if not successful.
     */
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

    /**
     * Attempts to remove a student from the manager based on their ID.
     * @param studentID The ID of a student.
     * @return true if successfully removed and false if not.
     */
    public boolean removeStudent(int studentID) {
        if (students.containsKey(studentID) == false) {
            return false;
        }
        IDGiver.returnID(studentID);
        if (Consts.STUDENT_LIFE_DEBUG_MODE_ON) {
            Gdx.app.log("StudentManager", "removing  Student " + studentID);
        }
        students.remove(studentID);
        return true;
    }

    /**
     * Gets the average satisfaction of every student stored by the manager.
     * @return Between 0-100 inclusively. Represents percentage.
     */
    public float getOverallSatisfaction() {
        float satisfaction = 0.0f;
        float satisfactionMult = 100.0f / students.size();
        for (Student stu : students.values()) {
            satisfaction += satisfactionMult * stu.getSatisfaction();
        }
        return satisfaction;
    }

    /**
     * Updates all students. This work by calling the {@link Student#update(float, boolean)}
     * method. Will only update the student meter as specified in 
     * {@link Consts#TIME_BETWEEN_STUDENT_UPDATES}.
     * @param deltaTime The time since the last frame.
     */
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

    /**
     * Will call the {@link Student#processEvent(StudentActivity)} method on all students.
     * Will never allow more students than the capacity of the event to successfully process
     * the event.
     * @param studentActivity The event to process.
     */
    public void processEvent(StudentActivity studentActivity) {
        int max = studentActivity.getActivityCapacity();
        int count = 0;

        for (Student stu : students.values()) {
            if (stu.processEvent(studentActivity)) count++;
            if (count > max) return;
        }
    }

    /**
     * Draws all students onto the screen.
     * @param batch
     */
    public void drawStudents(SpriteBatch batch) {
        for (Student stu : students.values()) {
            stu.draw(batch);
        }
    }
}
