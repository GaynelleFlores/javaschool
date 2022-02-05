package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class PyramidBuilder {
    private int countLevelsNum(List<Integer> inputNumbers) throws CannotBuildPyramidException {
        int i = 1;
        int size;
        size = inputNumbers.size();
        while(size > 0) {
            size -= i;
            i++;
        }
        if (size < 0)
            throw new CannotBuildPyramidException("Not enough numbers");
        return i - 1;
    }
    private void fillPyramid(int levels, int len, List<Integer> inputNumbers, int [][] res) {
        int i = len / 2;
        int j = 0;
        int numsOnString = 1;
        ListIterator<Integer> iter = inputNumbers.listIterator();
        while (j < levels) {
            while (i < len && numsOnString > 0) {
                if (((levels - j) % 2 == 0 && i%2 != 0) || ((levels - j) % 2 != 0 && i % 2 == 0)) {
                    if (iter.hasNext()) {
                        res[j][i] = iter.next();
                        numsOnString--;
                    }
                }
                else
                    res[j][i] = 0;
                i++;
            }
            j++;
            i = len / 2 - j;
            if (i < 0)
                i = 0;
            numsOnString = j + 1;
        }
    }
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        int [][] res;
        int len;
        checkInputList(inputNumbers);
        int levelsNum = countLevelsNum(inputNumbers);
        Collections.sort(inputNumbers);
        len = levelsNum * 2 - 1;
        res = new int[levelsNum][len];
        fillPyramid(levelsNum, len, inputNumbers, res);
        return res;
    }
    private void checkInputList(List<Integer> inputNumbers) {
        if (inputNumbers == null || inputNumbers.isEmpty())
            throw new CannotBuildPyramidException("List is empty or null");
        for (Integer num : inputNumbers) {
            if (num == null)
                throw new CannotBuildPyramidException("List contains null");
        }
    }
}
