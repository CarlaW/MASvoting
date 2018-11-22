package data;

import static data.Helper.getVotingSchemeByName;
import static data.Helper.transposeMatrix;

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

	private final String[] schemes = { "Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)", "Borda" };
	private char[][] preferenceMatrix;
	private String[][] stringMatrix;
	private int[] votingVector;
	private int numOfVoters, numOfCandidates;

	public TVApanel(int votingScheme, char[][] preferenceMatrix, int output) {
		
		/*
		 * First transpose the preference matrix and create VotingScheme enum
		 * and assign variables
		 */

		this.preferenceMatrix = transposeMatrix(preferenceMatrix);
		VotingScheme scheme = getVotingSchemeByName(votingScheme);
		numOfVoters = preferenceMatrix.length;
		numOfCandidates = preferenceMatrix[0].length;
		ArrayList<ArrayList<StrategicVotingOption>> result = new ArrayList<ArrayList<StrategicVotingOption>>(
				numOfVoters);
		
		// Iterate over voters
		for (int i = 0; i < numOfVoters; i++) {

			StrategicVoting strategicVoting = new StrategicVoting(preferenceMatrix, i, scheme);

			int happiness = strategicVoting.calculateThisHappiness(strategicVoting.winner); // see the big comment below
			if (happiness < numOfCandidates - 1) {
				System.out.println("For agent # " + i);
				System.out.println("Happiness is: " + happiness + " and Max(happines) is : " + (numOfCandidates - 1));
				System.out.println();
				strategicVoting.tryCompromise();

			}
		}

		/*
		 *  Now I have problems: I created a class StrategicVoting.java", and I wanted to put
		 * all the calculations there. But I forgot that we will need
		 * also calculate result for Console and GUI
		 * So I made an extra object for this, but, apparently this is SHIT
		 * and couldn't come up with anything smarter that this. :/
		 * I see two solutions for this:
		 * 
		 * Bulat 
		 *  
		 */
		StrategicVoting strategicVoting = new StrategicVoting(preferenceMatrix, 0, scheme);
		stringMatrix = initMatrix();
		votingVector = scheme.getScore(numOfCandidates);
		Pair[] votingOutcome = strategicVoting.outcome;
		addVotingOutcomeToMatrix(votingOutcome);
		int[] happiness = strategicVoting.calculateHappiness(votingOutcome[0]);
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

		/*
		 * - display preference matrix (done) - Non-strategic voting outcome O (done) -
		 * Overall voter happiness level 𝐻=Σ𝐻𝑖 (done) - set of strategic-voting
		 * options 𝑆={𝑠𝑖} 𝑣 – is a tactically modified preference list of this voter
		 * 𝑂̃ – a voting outcome resulting from applying 𝑣 𝐻̃ – an overall voter
		 * happiness level resulting from applying 𝑣 𝑧 – briefly states why 𝑖 prefers
		 * 𝑂̃ over 𝑂 - Overall risk of strategic voting for this voting situation
		 * 𝑅=|𝑆|/𝑛
		 */
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
		for (int i = 0; i < (11 * stringMatrix[0].length + 35); i++) {
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
}
