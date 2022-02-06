package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {
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
        if (list != null) {
            for (Object num : list) {
                if (num == null)
                    return false;
            }
            return true;
        }
        return false;
    }
}
