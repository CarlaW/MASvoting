package data;

import static data.Helper.makeDeepCopy;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Helper {

	public Helper() {

	}

	public static char[] makeDeepCopy(char[] arr) {
		return Arrays.copyOf(arr, arr.length);
	}

	public static int getIndexOf(char[] arr, char key) {
		int res = IntStream.range(0, arr.length).filter(i -> arr[i] == key).findFirst().orElse(-1);
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

	public static VotingScheme getVotingSchemeByName(int ID) {
		VotingScheme res = null;
		switch (ID) {
		case 0:
			res = VotingScheme.Plurality;
		case 1:
			res = VotingScheme.VotingForTwo;
		case 2:
			res = VotingScheme.AntiPlurality;
		case 3:
			res = VotingScheme.BordaVoting;
		}
		return res;
	}

//	public static int CalculateOverallHapiness(PreferenceMatrix pm, boolean ifPrintHapiness) {
//		int overallHapiness = 0;
//		System.out.println("Hapiness:");
//		for (int i = 0; i < pm.numOfVoters; i++) {
//			VotingVector tempPreference = pm.table.get(i);
//			int individualHapiness = CalculateSingleHapiness(pm, tempPreference);
//			overallHapiness += individualHapiness;
//			System.out.println(i + ": " + individualHapiness);
//		}
//		return overallHapiness;
//	}
}
