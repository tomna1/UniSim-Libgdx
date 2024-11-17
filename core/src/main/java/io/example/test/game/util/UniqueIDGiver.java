package io.example.test.game.util;

import java.util.PriorityQueue;

/**
 * Used to generate a unique ID.
 * @author Thomas Nash
 */
public class UniqueIDGiver {
    private PriorityQueue<Integer> nums;

    public UniqueIDGiver() {
        nums = new PriorityQueue<Integer>();
        nums.add(1);
    }

    /**
     * Returns the next unique ID
     * @return A unique ID. Will return 0 if no ID can be given.
     */
    public int next() {
        if (nums.size() > 1) {
            int num = nums.remove();
            return num;
        }
        if (nums.size() == 1 && nums.peek() < Integer.MAX_VALUE) {
            int num = nums.remove();
            nums.add(num+1);
            return num;
        }
        if (nums.size() == 1 && nums.peek() == Integer.MAX_VALUE) {
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * Returns an ID back to the IDgiver which allows it to produce that ID again
     * if {@link #next()} is called.
     * @param ID The id to give back.
     */
    public void returnID(int ID) {
        if (nums.contains(ID) == false && nums.peek() > ID) {
            nums.add(ID);
        }
    }

    /**
     * @return true if at least on more unique ID can be given.
     */
    public boolean canGiveID() {
        if (nums.size() == 1 && nums.peek() == Integer.MAX_VALUE) {
            return false;
        }
        return true;
    }
}
