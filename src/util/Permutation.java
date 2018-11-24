package util;

import java.util.ArrayList;

public class Permutation {

	ArrayList<char[]> vectorPermutations;
	ArrayList<ArrayList<char[]>> matrixPermutations;

	public Permutation(char[] arr) {
		vectorPermutations = new ArrayList<>();
		permuteVector(new String(arr), 0, arr.length - 1);

	}

	public Permutation(char[][] arr) {
		matrixPermutations = new ArrayList<ArrayList<char[]>>();
		vectorPermutations = new ArrayList<char[]>();
		permuteVector(new String(arr[0]), 0, arr[0].length - 1);

	}

	public ArrayList<char[]> getVectorPermutations() {
		return vectorPermutations;
	}

	public ArrayList<ArrayList<char[]>> getMatrixPermutations() {
		return matrixPermutations;
	}

	/**
	 * permutation function
	 * 
	 * @param str string to calculate permutation for
	 * @param l   starting index
	 * @param r   end index
	 */

	public void permuteMatrix(char[][] matrix, int n, ArrayList<char[]> start) {
		if (start.size() >= n) {
			this.vectorPermutations.addAll(start);
		} else {
			for (char x[] : matrix) { // not a valid syntax in Java
				start.add(x);
				permuteMatrix(matrix, n, start);
			}
		}
	}

	private void permuteVector(String str, int l, int r) {
		if (l == r) {
			this.vectorPermutations.add(str.toCharArray());
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

	public void print() {
		for (char[] c : vectorPermutations) {
			System.out.println(new String(c));
		}

	}

}