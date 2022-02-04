import java.util.*;

public class Pyramid {
	/**
	 * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
	 * from left to right). All vacant positions in the array are zeros.
	 *
	 * @param inputNumbers to be used in the pyramid
	 * @return 2d array with pyramid inside
	 * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
	 */
	private int countLevelsNum(int size){
		int i = 1;
		while(size > 0) {
			size -= i;
			i++;
		}
		if (size < 0)
			return -1;
		return i - 1;
	}
	private void fillPyramid(int levels, int len, List<Integer> inputNumbers, int [][] res) {
		int i = levels / 2;
		int j = 0;
		int numsOnString = 1;
		ListIterator<Integer> iter = inputNumbers.listIterator();
		while (j < levels) {
			//System.out.println("i on begin = " + (levels / 2 - j));
			//System.out.println(Arrays.deepToString(res));

			while (i < len && numsOnString > 0) {
//				System.out.println("In str i=" + i + " j=" + j + " res = " + ((levels - j) % 2));
//				System.out.println(((levels - j) % 2 != 0) + " " + (i % 2 == 0));
				if (((levels - j) % 2 == 0 && i%2 != 0) || ((levels - j) % 2 != 0 && i % 2 == 0)) {
					if (iter.hasNext()) { // todo эта проверка не нужна при грамотной реализации
						res[j][i] = iter.next();
						numsOnString--;
					}
				}
				else
					res[j][i] = 0;
				i++;
			}
			j++;
			i = levels / 2 - j;
			if (i < 0)
				i = 0;
			numsOnString = j + 1;
		}

	}

	public int[][] buildPyramid(List<Integer> inputNumbers) {
		// TODO : Implement your solution here
		int [][] res;
		int len;
		int levelsNum = countLevelsNum(inputNumbers.size());
		if (levelsNum == -1) {
			System.out.println("Cannot build pyramid");
			System.exit(1);
		}
		Collections.sort(inputNumbers);
		len = levelsNum * 2 - 1;
		res = new int[levelsNum][len];
		System.out.println(Arrays.deepToString(res));
		System.out.println("levels " + levelsNum);
		System.out.println("len    " + len);
		fillPyramid(levelsNum, len, inputNumbers, res);

		System.out.println(Arrays.deepToString(res));
		return res;
	}
}
