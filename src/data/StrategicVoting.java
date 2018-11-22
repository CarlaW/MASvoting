package data;

import static data.Helper.getIndexOf;
import static data.Helper.makeDeepCopy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class StrategicVoting {

	public StrategicVoting(char[][] preferenceMatrix, int voterID, VotingScheme scheme) {

		this.numOfCandidates = preferenceMatrix[0].length;
		this.numOfVoters = preferenceMatrix.length;
		this.votingScheme = scheme;
		this.preferenceMatrix = preferenceMatrix;
		this.outcome = calculateVotingOutcome();
		this.winner = outcome[0];
		this.voterID = voterID;
		this.truePreference = preferenceMatrix[voterID];
		this.oldHappiness = calculateHappiness(winner)[voterID];

	}

	/*
	 * 
	 * COMPROMISING Iterate over possible options and return set of
	 * StrategicVotingOptions
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryCompromise() {
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = 0; j < i; j++) {

				String reasoning = "We should swap " + truePreference[j] + " and " + truePreference[i] + " \n "
						+ "now the outome is ...";// TODO Normal reasoning
				char[] newPreference = swap(truePreference[j], truePreference[i]);
				preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
				Pair[] outcome = calculateVotingOutcome();
				newHappiness = calculateThisHappiness(outcome[0]);

				if (shouldManipulate()) {
					addNewOption(newPreference, outcome, newHappiness, reasoning);
				}

				preferenceMatrix[voterID] = truePreference; // Returning table back to normal state
			}
		}

		return setOfOptions;
	}

	/*
	 * 
	 * Bury Iterate over possible options and return set of StrategicVotingOptions
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryBury() {
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = i; j < numOfCandidates; j++) {

				String reasoning = "We should swap " + truePreference[j] + " and " + truePreference[i] + " \n "
						+ "now the outome is ...";// TODO Normal reasoning
				char[] newPreference = swap(truePreference[j], truePreference[i]);
				preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
				Pair[] outcome = calculateVotingOutcome();
				newHappiness = calculateThisHappiness(outcome[0]);

				if (shouldManipulate()) {
					addNewOption(newPreference, outcome, newHappiness, reasoning);
				}

				preferenceMatrix[voterID] = truePreference; // Returning table back to normal state
			}
		}

		return setOfOptions;
	}

	/*
	 * 
	 * BULLETVOTING
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryBulletVoting() {
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 0; i < numOfCandidates; i++) {

			String reasoning = "";

			char[] newPreference = leaveOneCandidate(truePreference[i]);
			preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
			Pair[] outcome = calculateVotingOutcome();
			newHappiness = calculateThisHappiness(outcome[0]);

			if (shouldManipulate()) {
				addNewOption(newPreference, outcome, newHappiness, reasoning);
			}

			preferenceMatrix[voterID] = truePreference; // Returning table back to normal state
		}

		return setOfOptions;

	}

	// Calculating Functions
	public int[] calculateHappiness(Pair winner) {
		int[] happiness = new int[numOfVoters];
		for (int i = 0; i < numOfVoters; i++) { // all voters
			for (int j = 0; j < numOfCandidates; j++) { // respective preferences
				if (preferenceMatrix[i][j] == winner.option) { //
					happiness[i] = numOfCandidates - j - 1;
				}
			}
		}
		return happiness;
	}

	public int calculateThisHappiness(Pair winner) {
		int happiness = 0;
		for (int j = 0; j < numOfCandidates; j++) { // respective preferences
			if (truePreference[j] == winner.option) { //
				happiness = numOfCandidates - j - 1;
			}
		}
		return happiness;
	}

	public Pair[] calculateVotingOutcome() {
		Pair[] votingOutcome = new Pair[numOfCandidates];
		int[] scoreVector = votingScheme.getScore(numOfCandidates);
		// Initialize the vector
		for (int i = 0; i < numOfCandidates; i++) {
			char option = (char) (i + 'A');
			votingOutcome[i] = new Pair(0, option);
		}
		// Calcuate the outcome
		for (int i = 0; i < numOfVoters; i++) { // all voters
			for (int j = 0; j < numOfCandidates; j++) { // respective preferences
				if (scoreVector[j] != 0 && preferenceMatrix[i][j] != '\u0000') {
					votingOutcome[(preferenceMatrix[i][j]) - 'A'].count += scoreVector[j];
				}
			}
		}
		// Order it
		Arrays.sort(votingOutcome);
		return votingOutcome;
	}

	public char[] swap(char first, char second) { // Swap two candidates
		char[] newPreference = makeDeepCopy(truePreference);
		int firstIndex = getIndexOf(this.truePreference, first);
		int secondIndex = getIndexOf(this.truePreference, second);
		newPreference[secondIndex] = first;
		newPreference[firstIndex] = second;
		return newPreference;
	}

	public char[] leaveOneCandidate(char choice) { // Clear Voting Vector
		char[] newPreference = new char[truePreference.length];
		newPreference[0] = choice;
		return newPreference;
	}

	public boolean shouldManipulate() {
		boolean shouldManipulate = false;
		if (newHappiness >= oldHappiness) {
			shouldManipulate = true;
		}
		return shouldManipulate;
	}

	public void addNewOption(char[] v, Pair[] newO, int newH, String z) {
		StrategicVotingOption temp = new StrategicVotingOption(v, newO, newH, z);
		if (temp != null) {

			setOfOptions.add(temp);
		}

	}

	public char[][] preferenceMatrix;
	public Pair winner;
	public Pair[] outcome;
	public char[] truePreference;
	int numOfCandidates, numOfVoters, voterID, oldHappiness, newHappiness;
	public VotingScheme votingScheme;
	public ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
}
