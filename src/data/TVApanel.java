package data;

import static data.Helper.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TVApanel extends JPanel {

	public TVApanel(int votingScheme, char[][] preferenceMatrix, int output) {

		/*
		 * First transpose the preference matrix and create VotingScheme enum and assign
		 * variables
		 */

		this.preferenceMatrix = preferenceMatrix;
		this.truePreferenceMatrix = this.preferenceMatrix.clone();
		this.numOfVoters = this.preferenceMatrix.length;
		this.numOfCandidates = this.preferenceMatrix[0].length;
		this.scheme = getVotingSchemeByName(votingScheme);
		this.outcome = calculateVotingOutcome(truePreferenceMatrix);
		this.winner = this.outcome[0];
		this.overallHappiness = calculateHappiness(winner, truePreferenceMatrix);

		ArrayList<ArrayList<StrategicVotingOption>> result = new ArrayList<ArrayList<StrategicVotingOption>>(
				numOfVoters);

		// Iterate over voters
		for (int i = 0; i < numOfVoters; i++) {
			int happiness = calculateHappiness(winner, truePreferenceMatrix)[i];
			if (happiness < numOfCandidates - 1) {
				System.out.println("For agent # " + i);
				System.out.println("Happiness is: " + happiness + " and Max(happines) is : " + (numOfCandidates - 1));
				System.out.println();
				result.add(tryCompromise(i));
			}
		}

		this.preferenceMatrix = transposeMatrix(truePreferenceMatrix);
		stringMatrix = initMatrix();
		votingVector = scheme.getScore(numOfCandidates);
		addVotingOutcomeToMatrix(this.outcome);
		int[] happiness = calculateHappiness(winner, truePreferenceMatrix);
		addHappinessToMatrix(happiness);

		if (output == 0) {
			displayInConsole(votingScheme);
		} else if (output == 1) {
			add(displaySchemeInfoInGUI(votingScheme));
			add(displayMatrixInGUI());
		} else {
			// then, display (either GUI or text)
			// GUI (I think it's easier to debug using this
			add(displaySchemeInfoInGUI(votingScheme));
			add(displayMatrixInGUI());
			// Console
			displayInConsole(votingScheme);
		}

	}

	/*
	 * 
	 * COMPROMISING Iterate over possible options and return set of
	 * StrategicVotingOptions
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryCompromise(int voterID) {
		char[] truePreference = preferenceMatrix[voterID];
		int oldHappiness = overallHappiness[voterID];
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = 0; j < i; j++) {
				String reasoning = "We should swap " + truePreference[j] + " and " + truePreference[i] + " \n "
						+ "now the outome is ...";// TODO Normal reasoning
				char[] newPreference = swap(j, i, truePreference);
				preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
				Pair[] newOutcome = calculateVotingOutcome(preferenceMatrix);
				int[] newHappiness = calculateHappiness(newOutcome[0], truePreferenceMatrix);
				if (newHappiness[voterID] > oldHappiness) {
					setOfOptions.add(new StrategicVotingOption(newPreference, newOutcome, newHappiness, reasoning));
				}
			}
		}
		return setOfOptions;
	}

	/*
	 * 
	 * Bury Iterate over possible options and return set of StrategicVotingOptions
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryBury(int voterID) {
		char[] truePreference = preferenceMatrix[voterID];
		int oldHappiness = overallHappiness[voterID];
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 1; i < numOfCandidates; i++) {
			for (int j = i + 1; j < numOfCandidates; j++) {
				String reasoning = "We should swap " + truePreference[j] + " and " + truePreference[i] + " \n "
						+ "now the outome is ...";// TODO Normal reasoning
				char[] newPreference = swap(j, i, truePreference);
				preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
				Pair[] newOutcome = calculateVotingOutcome(preferenceMatrix);
				int[] newHappiness = calculateHappiness(newOutcome[0], truePreferenceMatrix);
				if (newHappiness[voterID] > oldHappiness) {
					setOfOptions.add(new StrategicVotingOption(newPreference, newOutcome, newHappiness, reasoning));
				}
			}
		}

		return setOfOptions;
	}

	/*
	 * 
	 * BULLETVOTING
	 * 
	 */
	public ArrayList<StrategicVotingOption> tryBulletVoting(int voterID) {
		char[] truePreference = preferenceMatrix[voterID];
		int oldHappiness = overallHappiness[voterID];
		ArrayList<StrategicVotingOption> setOfOptions = new ArrayList<StrategicVotingOption>();
		for (int i = 0; i < numOfCandidates; i++) {

			String reasoning = "";
			char[] newPreference = leaveOneCandidate(i, truePreference);
			preferenceMatrix[voterID] = newPreference; // Put new Voting Vector in preference matrix
			Pair[] newOutcome = calculateVotingOutcome(preferenceMatrix);
			int[] newHappiness = calculateHappiness(newOutcome[0], truePreferenceMatrix);
			if (newHappiness[voterID] > oldHappiness) {
				setOfOptions.add(new StrategicVotingOption(newPreference, newOutcome, newHappiness, reasoning));
			}
		}

		return setOfOptions;
	}

	// Calculating Functions
	public int[] calculateHappiness(Pair winner, char[][] preferenceMatrix) {
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

	public Pair[] calculateVotingOutcome(char[][] preferenceMatrix) {
		Pair[] votingOutcome = new Pair[numOfCandidates];
		int[] scoreVector = scheme.getScore(numOfCandidates);
		// Initialize the vector
		for (int i = 0; i < numOfCandidates; i++) {
			char option = (char) (i + 'A');
			votingOutcome[i] = new Pair(0, option);
		}
		// Calculate the outcome
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

	public char[] swap(int firstIndex, int secondIndex, char[] truePreference) { // Swap two candidates
		char[] newPreference = makeDeepCopy(truePreference);
		newPreference[secondIndex] = truePreference[firstIndex];
		newPreference[firstIndex] = truePreference[secondIndex];
		return newPreference;
	}

	public char[] leaveOneCandidate(int index, char[] truePreference) { // Clear Voting Vector
		char[] newPreference = new char[truePreference.length];
		newPreference[0] = truePreference[index];
		return newPreference;
	}

	private String[][] initMatrix() {
		int options = preferenceMatrix.length + 2;
		int voters = preferenceMatrix[0].length + 2;
		String[][] matrix = new String[options][voters];

		String text;
		for (int i = 0; i < options; i++) {
			for (int j = 0; j < voters; j++) {
				if ((i == 0 && j == 0) || (i != 0 && i != (options - 1) && j == (voters - 1))
						|| (i == (options - 1) && j != 0 && j != (voters - 1))) {
					// System.out.println("top left corner and happiness and voting outcome");
					text = " ";
					matrix[i][j] = text;
				} else if (i == 0 && j != (voters - 1)) {
					// System.out.println("top row");
					text = "Voter #" + j;
					matrix[i][j] = text;
				} else if (i == 0 && j == (voters - 1)) {
					// System.out.println("top right corner");
					text = "Voting outcome O";
					matrix[i][j] = text;
				} else if (j == 0 && i != 0 && i != (options - 1)) {
					// System.out.println("left column");
					text = "Preference #" + i;
					matrix[i][j] = text;
				} else if (j == 0 && i == (options - 1)) {
					// System.out.println("left bottom corner");
					text = "Voter happiness H";
					matrix[i][j] = text;
				} else if (i == (options - 1) && j == (voters - 1)) {
					// System.out.println("right bottom corner");
					text = "Risk for strategic voting R";
					matrix[i][j] = text;
				} else {
					// System.out.println("actual info");
					text = Character.toString(preferenceMatrix[i - 1][j - 1]);
					matrix[i][j] = text;
				}
			}
		}
		return matrix;
	}

	private void addVotingOutcomeToMatrix(Pair[] votingOutcome) {
		for (int i = 0; i < stringMatrix.length; i++) {
			if (i != 0 && i != (stringMatrix.length - 1)) {
				String text = Character.toString(votingOutcome[i - 1].option) + ": "
						+ Integer.toString(votingOutcome[i - 1].count);
				stringMatrix[i][stringMatrix[0].length - 1] = text;
			}
		}
	}

	private void addHappinessToMatrix(int[] happiness) {
		// display single H's
		for (int j = 0; j < stringMatrix[0].length; j++) {
			if (j != 0 && j != (stringMatrix[0].length - 1)) {
				stringMatrix[stringMatrix.length - 1][j] = Integer.toString(happiness[j - 1]);
			}
		}
		// display sum of H
		stringMatrix[(stringMatrix.length - 1)][0] = "Voter happiness H = " + IntStream.of(happiness).sum();
	}

	private JPanel displaySchemeInfoInGUI(int votingScheme) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		JLabel label1 = new JLabel("Current voting scheme: " + schemes[votingScheme]);
		String v = "{";
		for (int i = 0; i < votingVector.length; i++) {
			v = v + votingVector[i];
		}
		v = v + "}";
		JLabel label2 = new JLabel("Respective vector: " + v);
		panel.add(label1);
		panel.add(label2);
		return panel;
	}

	private void displayInConsole(int votingScheme) {
		String current = "Current voting scheme: " + schemes[votingScheme];
		String v = "{";
		for (int i = 0; i < votingVector.length; i++) {
			v = v + votingVector[i];
		}
		v = v + "}";
		String vector = "Respective vector: " + v;
		System.out.println(current);
		System.out.println(vector);
		System.out.println();

		String format = "%-25s";
		for (int i = 0; i < preferenceMatrix[0].length; i++) {
			format += "%-11s";
		}
		format += "%30s\n";

		String separator = "";
		for (int i = 0; i < (11 * preferenceMatrix[0].length + 35); i++) {
			separator += "-";
		}

		for (String[] row : stringMatrix) {
			System.out.format(format, row);
			System.out.println(separator);
		}
	}

	private JPanel displayMatrixInGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(stringMatrix.length, stringMatrix[0].length, 1, 1));

		for (int i = 0; i < stringMatrix.length; i++) {
			for (int j = 0; j < stringMatrix[0].length; j++) {
				JLabel label = new JLabel(stringMatrix[i][j], SwingConstants.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				panel.add(label);
			}
		}
		return panel;
	}

	private final String[] schemes = { "Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)", "Borda" };
	private char[][] preferenceMatrix, truePreferenceMatrix;
	private String[][] stringMatrix;
	private int[] votingVector, overallHappiness;
	private int numOfVoters, numOfCandidates;
	public Pair winner;
	public Pair[] outcome;
	public VotingScheme scheme;
}
