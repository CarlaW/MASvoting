package data;

import static data.Helper.makeDeepCopy;

import java.util.Arrays;
import java.util.Random;

public class RandomInput {

	private int voters;
	private int options;
	

	public RandomInput(int voters, int options) {
		this.voters = voters;
		this.options = options;

	}

	public char[][] getRandomPreferenceMatrix() {
		char[][] preferenceMatrix = new char[voters][options];
		char[] newAlphabet = new char[options];

		for (int i = 0; i < options; i++) {
			newAlphabet[i] = JTextFieldLimit.ALPHABET[i];
		}

		for (int i = 0; i < voters; i++) {
			Random r = new Random();
			char[] tempNewAlphabet = makeDeepCopy(newAlphabet);
			for (int j = options - 1; j > 0; j--) {
				int k = r.nextInt(j + 1);
				char temp = tempNewAlphabet[j];
				tempNewAlphabet[j] = tempNewAlphabet[k];
				tempNewAlphabet[k] = temp;
			}

			preferenceMatrix[i] = tempNewAlphabet;
		}
		
		return preferenceMatrix;
	}

}
