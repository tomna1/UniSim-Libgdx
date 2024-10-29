package io.example.test.util;

import java.util.PriorityQueue;

// Used to give a unique ID to something. next() gets the unique
// ID and returnID returns the ID back to the ID giver.
public class UniqueIDGiver {
    private PriorityQueue<Integer> nums;

    public UniqueIDGiver() {
        nums = new PriorityQueue<Integer>();
        nums.add(1);
    }

    // Returns the next unique ID. If returns 0, no new unique ID
    // cam be given.
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

    // Returns an ID back to the IDgiver.
    public void returnID(int ID) {
        if (nums.contains(ID) == false && nums.peek() > ID) {
            nums.add(ID);
        }
    }

    // Returns true if at least one more valid ID can be given.
    public boolean canGiveID() {
        if (nums.size() == 1 && nums.peek() == Integer.MAX_VALUE) {
            return false;
        }
        return true;
    }
}
