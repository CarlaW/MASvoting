package calculators;

import static calculators.Calculator.GetIndexOf;
import static calculators.Calculator.MakeDeepCopy;
import static calculators.Calculator.SortTable;
import static calculators.Calculator.calculateHappiness;
import static calculators.Calculator.calculateVotingOutcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import data.Pair;
import data.PreferenceMatrix;
import data.StrategicVotingOption;
import data.VotingScheme;

public class StrategicalVoting {

	public StrategicalVoting(PreferenceMatrix preferenceMatrix, int voterID, VotingScheme scheme) {

		this.winner = calculateVotingOutcome(preferenceMatrix, votingScheme)[0];
		this.votingScheme = scheme;
		this.preferenceMatrix = preferenceMatrix;
		this.voterID = voterID;
		this.truePreference = preferenceMatrix.table[voterID];
		this.oldHapiness = calculateHappiness(preferenceMatrix, winner)[voterID];
		this.numOfCandidates = preferenceMatrix.numOfCandidates;

	}

	public char[] Swap(char first, char second) { // Swap two candidates
		char[] newPreference = MakeDeepCopy(truePreference);

		int firstIndex = GetIndexOf(this.truePreference, first);
		int secondIndex = GetIndexOf(this.truePreference, second);

		newPreference[secondIndex] = first;
		newPreference[firstIndex] = second;

		return newPreference;
	}

	public char[] LeaveOneCandidate(char choice) { // Clear Voting Vector
		char[] newPreference = new char[truePreference.length];
		newPreference[0] = choice;
		return newPreference;
	}

	/*
	 * 
	 * COMPROMISING
	 * 
	 */
	public ArrayList<StrategicVotingOption> TryCompromise() { // Iterate over possible compromising options and return
																// set
																// of StrategicVotingOptions
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();

		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = 0; j < i; j++) {
				String reasoning = "We should swap " + truePreference[j] + " and " + truePreference[i] + " \n "
						+ "now the outome is ...";// TODO Normal reasoning

				char[] newPreference = Swap(truePreference[j], truePreference[i]);
//				
				preferenceMatrix.table[voterID] = newPreference;

				Pair[] outcome = calculateVotingOutcome(preferenceMatrix, votingScheme);
				int[] newHapiness = calculateHappiness(preferenceMatrix, outcome[0]);

				if (newHapiness[voterID] > this.oldHapiness) {
					StrategicVotingOption newTuple = new StrategicVotingOption(voterID, newPreference, outcome, // Constructing
																												// a new
																												// Tuple
							IntStream.of(newHapiness).sum(), reasoning);
					setOfOptions.add(newTuple);
				}

				preferenceMatrix.table[voterID] = this.truePreference; // Returning table back to normal state
			}
		}

		return setOfOptions;
	}

	/*
	 * 
	 * BURYING: THIS PART IS DIFFERENT FROM COMPROMISING ONLY BY ITERATING ALGORITHM
	 * 
	 */
	public ArrayList<StrategicVotingOption> TryBury() { // Iterate over possible burying options and return set of
														// StrategicVotingOptions
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		String reasoning = "Bury"; // TO DO normal reasoning

		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = i; j < numOfCandidates; j++) {
				VotingVector newPreference = Swap(truePreference.get(j), truePreference.get(i));
				preferenceMatrix.table.put(voterID, newPreference);
				int newHapiness = CalculateSingleHapiness(preferenceMatrix, truePreference);

				if (newHapiness > this.oldHapiness) {
					StrategicVotingOption newTuple = ConstructTuple(preferenceMatrix, newPreference, reasoning);
					setOfOptions.add(newTuple);
				}

				preferenceMatrix.table.put(voterID, this.truePreference); // Returning table back to normal state
			}
		}

		return setOfOptions;
	}

	/*
	 * 
	 * BULLETVOTING
	 * 
	 */
	public ArrayList<StrategicVotingOption> TryBulletVoting() {
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		String reasoning = "BulletVoting";// TO DO normal reasoning

		for (int i = 1; i < numOfCandidates; i++) {
			VotingVector newPreference = LeaveOneCandidate(this.truePreference.get(i));
			preferenceMatrix.table.put(voterID, newPreference);
			int newHapiness = CalculateSingleHapiness(preferenceMatrix, truePreference);

			if (newHapiness > this.oldHapiness) {
				StrategicVotingOption newTuple = ConstructTuple(preferenceMatrix, newPreference, reasoning);
				setOfOptions.add(newTuple);
			}

			preferenceMatrix.table.put(voterID, this.truePreference); // Returning table back to normal state
		}

		return setOfOptions;
	}

	public StrategicVotingOption ConstructTupleWithPrint(PreferenceMatrix pm, VotingVector newVector,
			String reasoning) {
		System.out.println("New option created!");

		HashMap<Character, Integer> newOutcome = CalculateOutcome(pm, votingScheme);
		newOutcome = SortTable(newOutcome);
		int newHapiness = CalculateOverallHapiness(pm);

		System.out.println("new outcome");
		PrintOutcome(newOutcome);

		System.out.println("and the reason is... \n" + reasoning);
		return new StrategicVotingOption(newVector, newOutcome, newHapiness, reasoning);
	}

	public PreferenceMatrix preferenceMatrix;
	public Pair winner;
	public char[] truePreference;
	int numOfCandidates, voterID, oldHapiness;
	public VotingScheme votingScheme;

}
