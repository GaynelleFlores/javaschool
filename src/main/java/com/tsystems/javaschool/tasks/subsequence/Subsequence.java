package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {
    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (!checkList(x) || !checkList(y))
            throw new IllegalArgumentException();
        if (x.size() > y.size())
            return false;
        if (x.isEmpty())
            return true;
        return isSubsequence(x, y);
    }
    private boolean isSubsequence(List x, List y) {
        int i = 0;
        for (Object currY : y) {
            if (x.get(i).equals(currY)) {
                i++;
                if (i >= x.size())
                    return true;
            }
        }
        return false;
    }
    private boolean checkList(List list) {
        if (list == null)
            return false;
        for (Object num : list) {
            if (num == null)
                return false;
        }
        return true;
    }
}
