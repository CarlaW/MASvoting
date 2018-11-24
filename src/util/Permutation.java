package util;

import java.util.ArrayList;
import static util.Helper.*;

public class Permutation {

	public ArrayList<char[]> finalVector;
	public ArrayList<char[][]> finalMatrix;

	public Permutation(char[] arr) {
		finalVector = new ArrayList<char[]>();
		permuteVector(new String(arr), 0, arr.length - 1);

	}

	public Permutation(ArrayList<char[]> arrList, int numOfVoters) {
		finalMatrix = new ArrayList<char[][]>();
		char[][] perm = new char[numOfVoters][arrList.get(0).length];
		permuteVertorsWithRepeats(perm, 0, arrList);
		printMatrix(finalMatrix);

	}

	public void permuteVertorsWithRepeats(char[][] perm, int pos, ArrayList<char[]> start) {
		if (pos == perm.length) {
			char[][] temp = makeDeepCopy(perm);
			this.finalMatrix.add(temp);
		} else {
			for (int i = 0; i < start.size(); i++) {
				perm[pos] = start.get(i);
				permuteVertorsWithRepeats(perm, pos + 1, start);
			}
		}
	}

	public ArrayList<char[]> getVectorPermutations() {
		return finalVector;
	}

	public ArrayList<char[][]> getMatrixPermutations() {
		return finalMatrix;
	}

	public void printMatrix(ArrayList<char[][]> matrix) {
		for (char[][] c : matrix) {
			printBoard(c);
		}
	}

	/**
	 * permutation function §
	 * 
	 * @param str string to calculate permutation for
	 * @param l   starting index
	 * @param r   end index
	 */

	private void permuteVector(String str, int l, int r) {
		if (l == r) {
			this.finalVector.add(str.toCharArray());
		} else {
			for (int i = l; i <= r; i++) {
				str = swap(str, l, i);
				permuteVector(str, l + 1, r);
				str = swap(str, l, i);
			}
		}
	}

	/**
	 * Swap Characters at position
	 * 
	 * @param a string value
	 * @param i position 1
	 * @param j position 2
	 * @return swapped string
	 */
	private String swap(String a, int i, int j) {
		char temp;
		char[] charArray = a.toCharArray();
		temp = charArray[i];
		charArray[i] = charArray[j];
		charArray[j] = temp;
		return String.valueOf(charArray);
	}

}