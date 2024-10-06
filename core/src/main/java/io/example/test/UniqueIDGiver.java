package io.example.test;

import java.util.PriorityQueue;

// Gives a unique ID
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
    public void returnID(Integer ID) {
        nums.add(ID);
    }
}
