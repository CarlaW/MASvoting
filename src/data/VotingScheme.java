package data;

import java.util.Arrays;

public enum VotingScheme {

	Plurality(0, "Plurality"), VotingForTwo(1, "Voting For Two"), AntiPlurality(2, "AntiPlurality"),
	BordaVoting(3, "Borda Voting");

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
		for (int i = 0; i < numOfCandidates - 1; i++) {
			res[i] = 1;
		}
		res[numOfCandidates - 1] = 0;
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
		case 0:
			return res = PluralityScore(numOfCandidates);
		case 1:
			return res = VotingForTwoScore(numOfCandidates);
		case 2:
			return res = AntiPluralityScore(numOfCandidates);
		case 3:
			return res = BordaVotingScore(numOfCandidates);
		}

		return res;
	}

	public int ID;
	public String name;
}
