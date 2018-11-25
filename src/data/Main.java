package data;

import static util.Helper.transposeMatrix;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.Permutation;
import view.PopupInput;
import view.PopupSettings;
import view.RandomInput;

public class Main {

	private int numOfVoters;
	private int numOfCandidates;

	private int votingScheme;
	private char[][] preferenceMatrix;

	public static void main(String[] args) {
		new Main();
	}

	private Main() {

		initialize();
//		runExperiment();
//		runExperiment(6, 4, 100);
	}

	private char[][] getTestMatrix() {
		char[][] matrix = { { 'C', 'B', 'C', 'B', 'B' }, { 'A', 'D', 'D', 'D', 'C' }, { 'D', 'C', 'A', 'C', 'D' },
				{ 'B', 'A', 'B', 'A', 'A' } };
		return matrix;

	}

	private void initialize() {
		askForSettings();
		preferenceMatrix = transposeMatrix(getTestMatrix());
//		 askForPreferenceMatrix();
//		askForRandomPreferenceMatrix();

		JFrame frame = new JFrame("Tactical Voting Analyst");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		TVA tva = new TVA(votingScheme, preferenceMatrix);
	}

	private void runExperiment() {

		char[] temp = { 'A', 'B', 'C', 'D' };
		int numOfVoters = 5;

		Permutation vectorPermutations = new Permutation(temp);
		Permutation matrixPermutations = new Permutation(vectorPermutations.finalVector, numOfVoters);

		ArrayList<char[][]> testSample = matrixPermutations.getMatrixPermutations();
		Experiment firstExperiment = new Experiment(testSample);

	}

	private void runExperiment(int numOfVoters, int numOfCandidates, int numOfRuns) {
		this.numOfVoters = numOfVoters;
		this.numOfCandidates = numOfCandidates;

		ArrayList<char[][]> testSample = new ArrayList<char[][]>();

		for (int i = 0; i < numOfRuns; i++) {
			testSample.add(askForRandomPreferenceMatrix());
		}

		Experiment firstExperiment = new Experiment(testSample);

	}

	private void askForSettings() {
		PopupSettings settings = new PopupSettings();
		int out = JOptionPane.showConfirmDialog(null, settings, "Enter preferences", JOptionPane.OK_CANCEL_OPTION);
		if (out != 0) {
			System.exit(0);
		}
		votingScheme = settings.getVotingScheme().getSelectedIndex();
		try {
			numOfVoters = Integer.parseInt(settings.getVoters().getText());
			numOfCandidates = Integer.parseInt(settings.getOptions().getText());
			if (numOfCandidates > 26) {
				JOptionPane.showMessageDialog(null, "There can be no more than 26 preferences.");
				askForSettings();
			} else if (numOfCandidates < 3) {
				JOptionPane.showMessageDialog(null, "There have to be more than 2 preferences.");
				askForSettings();
			} else if (numOfVoters < 3) {
				JOptionPane.showMessageDialog(null, "There have to be more than 2 voters.");
				askForSettings();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a number.");
			askForSettings();
		}
	}

	private void askForPreferenceMatrix() {
		PopupInput input = new PopupInput(numOfVoters, numOfCandidates);
		int out = JOptionPane.showConfirmDialog(null, input, "Enter preference matrix", JOptionPane.OK_CANCEL_OPTION);
		if (out != 0) {
			System.exit(0);
		}
		preferenceMatrix = input.getPreferenceMatrix();
		/*
		 * for (int i=0; i<noOptions; i++){ for (int j=0; j<noVoters; j++){
		 * System.out.print("i = " + i + " j = " + j + " ");
		 * if(preferenceMatrix[i][j].isEmpty()){ System.out.print(" "); }
		 * System.out.print(preferenceMatrix[i][j] + "   "); } System.out.println(); }
		 */
		if (doubleVotePerVoter()) {
			JOptionPane.showMessageDialog(null, "A voter cannot list the same option twice.");
			askForPreferenceMatrix();
		}
		if (noVotesPerVoter()) {
			JOptionPane.showMessageDialog(null, "A voter must have a first preference.");
			askForPreferenceMatrix();
		}
		preferenceMatrix = transposeMatrix(preferenceMatrix);
	}

	private boolean doubleVotePerVoter() {
		for (int i = 0; i < numOfVoters; i++) {
			for (int j = 0; j < numOfCandidates - 1; j++) {
				if (preferenceMatrix[j][i] == preferenceMatrix[j + 1][i]) {
					if (preferenceMatrix[j][i] == ' ') {
						continue;
					}
					return true;
				}
			}
		}
		return false;
	}

	private char[][] askForRandomPreferenceMatrix() {
		RandomInput input = new RandomInput(numOfVoters, numOfCandidates);
		preferenceMatrix = input.getRandomPreferenceMatrix();
		return preferenceMatrix;
	}

	private boolean noVotesPerVoter() {
		for (int i = 0; i < numOfVoters; i++) {
			if (preferenceMatrix[0][i] == preferenceMatrix[0][i]) {
				if (preferenceMatrix[0][i] == ' ') {
					return true;
				}
			}
		}
		return false;
	}
}
