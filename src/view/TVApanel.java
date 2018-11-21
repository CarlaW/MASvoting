package view;
import static calculators.Calculator.calculateVotingOutcome;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.Pair;
import data.PreferenceMatrix;
import data.VotingScheme;

public class TVApanel extends JPanel {

	private final String[] schemes = { "Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)", "Borda" };
	private PreferenceMatrix preferenceMatrix;
	private String[][] stringMatrix;
	private int[] scoreVector;

	public TVApanel(VotingScheme scheme, PreferenceMatrix preferenceMatrix, int output) {
		this.preferenceMatrix = preferenceMatrix;

		// first, gather data
		stringMatrix = initMatrix();
		scoreVector = scheme.getScore(preferenceMatrix.numOfCandidates);
		Pair[] votingOutcome = calculateVotingOutcome(preferenceMatrix, scheme);
		addVotingOutcomeToMatrix(votingOutcome);
		int[] happiness = calculateHappiness(votingOutcome[0]);
		addHappinessToMatrix(happiness);

		if (output == 0) {
			displayInConsole(scheme);
		} else if (output == 1) {
			add(displaySchemeInfoInGUI(scheme));
			add(displayMatrixInGUI());
		} else {
			// then, display (either GUI or text)
			// GUI (I think it's easier to debug using this
			add(displaySchemeInfoInGUI(scheme));
			add(displayMatrixInGUI());
			// Console
			displayInConsole(scheme);
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
		int options = preferenceMatrix.numOfCandidates + 2;
		int voters = preferenceMatrix.numOfVoters + 2;
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
					text = Character.toString(preferenceMatrix.table[i - 1][j - 1]);
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

	private int[] calculateHappiness(Pair winner) {
		// calculate
		int[] happiness = new int[preferenceMatrix.numOfCandidates];
		for (int i = 0; i < preferenceMatrix.numOfVoters; i++) { // all voters
			for (int j = 0; j < preferenceMatrix.numOfCandidates; j++) { // respective preferences
				if (preferenceMatrix.table[j][i] == winner.option) {
					int temp = scoreVector.length - j - 1;
					happiness[i] = temp;
				}
			}
		}
		return happiness;
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

	private JPanel displaySchemeInfoInGUI(VotingScheme scheme) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		JLabel label1 = new JLabel("Current voting scheme: " + scheme.name);
		String v = "{";
		for (int i = 0; i < scoreVector.length; i++) {
			v = v + scoreVector[i];
		}
		v = v + "}";
		JLabel label2 = new JLabel("Respective vector: " + v);
		panel.add(label1);
		panel.add(label2);
		return panel;
	}

	private void displayInConsole(VotingScheme scheme) {
		String current = "Current voting scheme: " + scheme.name;
		String v = "{";
		for (int i = 0; i < scoreVector.length; i++) {
			v = v + scoreVector[i];
		}
		v = v + "}";
		String vector = "Respective vector: " + v;
		System.out.println(current);
		System.out.println(vector);
		System.out.println();

		String format = "%-25s";
		for (int i = 0; i < preferenceMatrix.numOfCandidates; i++) {
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
