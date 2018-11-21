package calculators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import data.Pair;
import data.PreferenceMatrix;
import data.VotingScheme;

public class Calculator {

	public Calculator(ArrayList<Character> votingVector) {

	}

	public static int[] calculateHappiness(PreferenceMatrix preferenceMatrix, Pair winner) {

		int numOfCandidates = preferenceMatrix.numOfCandidates;
		int numOfVoters = preferenceMatrix.numOfVoters;
		// calculate
		int[] happiness = new int[numOfCandidates];
		for (int i = 0; i < numOfVoters; i++) { // all voters
			for (int j = 0; j < numOfCandidates; j++) { // respective preferences
				if (preferenceMatrix.table[i][j] == winner.option) {
					int temp = numOfCandidates - j - 1;
					happiness[i] = temp;
				}
			}
		}
		return happiness;
	}

//	public static int CalculateOverallHapiness(PreferenceMatrix pm) {
//		int overallHapiness = 0;
//		for (int i = 0; i < pm.numOfVoters; i++) {
//			VotingVector tempPreference = pm.table.get(i);
//			overallHapiness += CalculateSingleHapiness(pm, tempPreference);
//		}
//		return overallHapiness;
//	}

	public static Pair[] calculateVotingOutcome(PreferenceMatrix preferenceMatrix, VotingScheme scheme) {
		Pair[] votingOutcome = initOutcomeVector(preferenceMatrix.numOfCandidates);
		int[] scoreVector = scheme.getScore(preferenceMatrix.numOfCandidates);
		// first calculate
		for (int i = 0; i < preferenceMatrix.numOfVoters; i++) { // all voters
			for (int j = 0; j < preferenceMatrix.numOfCandidates; j++) { // respective preferences
				if (scoreVector[j] != 0) {
					votingOutcome[(preferenceMatrix.table[i][j] - 'A')].count += scoreVector[j];
				}
			}
		}
		// then order
		Arrays.sort(votingOutcome);
		return votingOutcome;
	}

	private static Pair[] initOutcomeVector(int size) {
		Pair[] vector = new Pair[size];
		for (int i = 0; i < size; i++) {
			char option = (char) (i + 'A');
			vector[i] = new Pair(0, option);
		}
		return vector;
	}

	public static char[] MakeDeepCopy(char[] arr) {
		return Arrays.copyOf(arr, arr.length);
	}

	public static int GetIndexOf(char[] arr, char key) {

		int res = IntStream.range(0, arr.length).filter(i -> arr[i] == key).findFirst().orElse(-1);

		return res;

	}

	public static HashMap<Character, Integer> SortTable(HashMap<Character, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<Character, Integer>> list = new LinkedList<Map.Entry<Character, Integer>>(hm.entrySet());
		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
			public int compare(Map.Entry<Character, Integer> o2, Map.Entry<Character, Integer> o1) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<Character, Integer> newOutcome = new LinkedHashMap<Character, Integer>();

		for (Map.Entry<Character, Integer> aa : list) {
			newOutcome.put(aa.getKey(), aa.getValue());
		}

		return newOutcome;
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
