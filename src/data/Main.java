package data;

import static util.Helper.transposeMatrix;

import java.util.ArrayList;

import javax.swing.JOptionPane;

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
		/*uncomment to run experiments*/
//		runExperiment();
//		runExperiment(6, 4, 100);
	}

	private void initialize() {
		askForSettings();
		askForPreferenceMatrix();
		/*
		for easier testing, uncomment line below and comment line above to get a random preference matrix instead of manually inputting one
	 	*/

		//askForRandomPreferenceMatrix();
		TVA tva = new TVA(votingScheme, preferenceMatrix);
	}

	private void runExperiment() {

		char[] temp = { 'A', 'B', 'C' };
		int numOfVoters = 5;

		/*create a new Permutation object
		based on the inital preference vector "temp", all the permutations of this vector are created
		Using these permutations, all possible matrices with "numOfVoters" voters are created
		* */
		Permutation vectorPermutations = new Permutation(temp);
		Permutation matrixPermutations = new Permutation(vectorPermutations.finalVector, numOfVoters);

		ArrayList<char[][]> testSample = matrixPermutations.getMatrixPermutations();
		/*
		create a new instance of the experiment class using the formerly created permutations
		 */
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
		/*
		this method creates a popup window asking for three different inputs
		- voting scheme
		- number of voters
		- number ofvting options/candidates
		 */
		PopupSettings settings = new PopupSettings();
		int out = JOptionPane.showConfirmDialog(null, settings, "Enter preferences", JOptionPane.OK_CANCEL_OPTION);
		/*
		if input is not confirmed by clicking "OK", close the window and stop the program
		 */
		if (out != 0) {
			System.exit(0);
		}
		votingScheme = settings.getVotingScheme().getSelectedIndex();
		try {
			/*
			see if a number is actually entered
			 */
			numOfVoters = Integer.parseInt(settings.getVoters().getText());
			numOfCandidates = Integer.parseInt(settings.getOptions().getText());
			/*
			check if the entered numbers are higher than the assignments requirements of 3
			also check if there are no more candidates than letters in the alphabet
			 */
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
			/*
			if nothing is entered, throw exception
			 */
			JOptionPane.showMessageDialog(null, "Please enter a number.");
			askForSettings();
		}
	}

	private void askForPreferenceMatrix() {
		/*
		after first inputs are entered, use these to create another popup in which the preference matrix can be entered
		 */
		PopupInput input = new PopupInput(numOfVoters, numOfCandidates);
		int out = JOptionPane.showConfirmDialog(null, input, "Enter preference matrix", JOptionPane.OK_CANCEL_OPTION);
		if (out != 0) {
			System.exit(0);
		}
		preferenceMatrix = input.getPreferenceMatrix();
		/*
		check if voters actually adhered to normal voting behaviour
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
		/*
		checks if a voter lists the same option twice in his preference vector
		 */
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
		/*
		creates a new object of RandomInput, which creates a random preference matrix
		 */
		RandomInput input = new RandomInput(numOfVoters, numOfCandidates);
		preferenceMatrix = input.getRandomPreferenceMatrix();
		return preferenceMatrix;
	}

	private boolean noVotesPerVoter() {
		/*
		checks if a voter actually entered a true favourite
		 */
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
