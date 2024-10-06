package io.example.test;

import java.util.ArrayList;

// Gives a unique ID
public class UniqueIDGiver {
    private ArrayList<Integer> nums;

    public UniqueIDGiver() {
        nums = new ArrayList<Integer>();
        nums.add(1);
    }

    // Returns the next unique ID. If returns 0, no new unique ID
    // cam be given.
    public int next() {
        if (nums.size() > 1) {
            int num = nums.get(0);
            nums.remove(0);
            return num;
        }
        if (nums.size() == 1 && nums.get(0) < Integer.MAX_VALUE) {
            int num = nums.get(0);
            nums.remove(0);
            nums.add(num+1);
            return num;
        }
    }

    // Returns an ID back to the IDgiver.
    public void return(Integer ID) {
        nums.push(0, ID);
    }
}
