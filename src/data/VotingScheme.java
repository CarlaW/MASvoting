package data;

import java.util.ArrayList;
import java.util.Collections;

public enum VotingScheme {

	Plurality(1, "Plurality"), VotingForTwo(2, "Voting For Two"), AntiPlurality(3, "AntiPlurality"),
	BordaVoting(4, "Borda Voting");
	
	VotingScheme(int ID, String name) {

		this.ID = ID;
		this.name = name;

	}

	public int[] PluralityScore(int numOfCandidates) {
		int[] res = new int[numOfCandidates];
		res[0] = 1;
		return res;
	}

	public int[] VotingForTwoScore(int numOfCandidates) {
		int[] res = new int[numOfCandidates];
		res[0] = 1;
		res[1] = 1;
		return res;
	}

	public int[] AntiPluralityScore(int numOfCandidates) {
		int[] res = new int[numOfCandidates];
		res[numOfCandidates] = 1;
		return res;
	}

	public int[] BordaVotingScore(int numOfCandidates) {
		int[] res = new int[numOfCandidates];
		for (int i = 0; i < numOfCandidates; i++) {
			res[i] = numOfCandidates - i - 1;
		}
		return res;
	}

	public int[] getScore(int numOfCandidates) {
		int[] res = new int[numOfCandidates];
		switch (this.ID) {
		case 1:
			res = PluralityScore(numOfCandidates);
		case 2:
			res = VotingForTwoScore(numOfCandidates);
		case 3:
			res = AntiPluralityScore(numOfCandidates);
		case 4:
			res = BordaVotingScore(numOfCandidates);
		}

		return res;
	}
	

	public int ID;
	public String name;
}
