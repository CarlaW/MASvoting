package util;

import java.util.Arrays;
import java.util.stream.IntStream;

import data.Pair;
import data.VotingScheme;

public class Helper {

	public Helper() {

	}

	public static char[] makeDeepCopy(char[] arr) {
		return Arrays.copyOf(arr, arr.length);
	}

	public static String[][] makeDeepCopy(String[][] arr) {
		if (arr == null) {
			return null;
		}
		final String[][] result = new String[arr.length][];
		for (int i = 0; i < arr.length; i++) {
			result[i] = Arrays.copyOf(arr[i], arr[i].length);
		}
		return result;
	}
	
	public static char[][] makeDeepCopy(char[][] arr) {
		if (arr == null) {
			return null;
		}
		final char[][] result = new char[arr.length][];
		for (int i = 0; i < arr.length; i++) {
			result[i] = arr[i].clone();
		}
		return result;
	}

	public static int getIndexOf(char[] arr, char key) {
		int res = IntStream.range(0, arr.length).filter(i -> arr[i] == key).findFirst().orElse(-1);
		return res;
	}

	public static int getIndexOf(Pair[] outcome, char key) {
		int res = 0;
		for (int i = 0; i < outcome.length; i++) {
			if (outcome[i].option == key) {
				return i;
			}
		}
		return res;
	}

	public static char[][] transposeMatrix(char[][] arr) {
		int height = arr.length;
		int width = arr[0].length;
		char[][] res = new char[width][height];
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				res[y][x] = arr[x][y];
			}
		}
		return res;
	}

	public static int factorial(int n) {
		if (n == 0)
			return 1;
		else
			return (n * factorial(n - 1));
	}

	public static VotingScheme getVotingSchemeByName(int ID) {
		VotingScheme res = null;
		switch (ID) {
		case 0:
			return res = VotingScheme.Plurality;
		case 1:
			return res = VotingScheme.VotingForTwo;
		case 2:
			return res = VotingScheme.AntiPlurality;
		case 3:
			return res = VotingScheme.BordaVoting;
		}
		return res;
	}

	public static void printBoard(char[][] board) {
		for (char[] c : board) {
			System.out.print(new String(c) + "|");
			System.out.println();
		}
		System.out.println();
	}

}

//	public static int CalculateOverallHapiness(PreferenceMatrix pm, boolean ifPrintHapiness) 
