package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Experiment {

	// TODO Put it somewhere else
	public static final String[] schemes = { "Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)",
			"Borda" };;
	private int numOfVoters, numOfCandidates;
	ArrayList<char[][]> matrixPermutation;
	float[] riskPerPermutation, happinessPerPermutation;
	float[][] riskPerScheme, happinessPerScheme;

	public Experiment(ArrayList<char[][]> matrixPermutation) {
		this.numOfVoters = matrixPermutation.get(0).length;
		this.numOfCandidates = matrixPermutation.get(0)[0].length;
		this.matrixPermutation = matrixPermutation;
		riskPerScheme = new float[schemes.length][];
		happinessPerScheme = new float[schemes.length][];
		for (int i = 0; i < schemes.length; i++) {
			run(matrixPermutation, i);
			riskPerScheme[i] = riskPerPermutation;
			happinessPerScheme[i] = happinessPerPermutation;
		}
		try {
			writeData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void run(ArrayList<char[][]> matrixPermutation, int votingScheme) {
		riskPerPermutation = new float[matrixPermutation.size()];
		happinessPerPermutation = new float[matrixPermutation.size()];
		System.out.println(matrixPermutation.size());
		for (int h = 0; h < matrixPermutation.size(); h++) {
			char[][] prefMatrix = matrixPermutation.get(h);
			TVA tva = new TVA(votingScheme, prefMatrix);
			riskPerPermutation[h] = tva.getRisk();
			happinessPerPermutation[h] = tva.getHappinessSum();
		}
	}

	public void writeData() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("risk_c" + numOfCandidates + "_v" + numOfVoters + ".csv"));
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < riskPerScheme[0].length; i++) {
			sb.append(i);
			sb.append(',');
		}
		sb.append("\n");

////		sb.append("STARTING ON RISK");
//		sb.append("\n");

		for (int j = 0; j < riskPerScheme.length; j++) {
			for (int i = 0; i < riskPerScheme[j].length; i++) {
				sb.append(riskPerScheme[j][i]);
				sb.append(',');
			}
			sb.append("\n");
		}
		sb.append("\n");
//		sb.append("STARTING ON HAPPINESS");
//		sb.append("\n");
		for (int j = 0; j < happinessPerScheme.length; j++) {
			for (int i = 0; i < happinessPerScheme[j].length; i++) {
				sb.append(happinessPerScheme[j][i]);
				sb.append(',');
			}
			sb.append("\n");
		}
		pw.write(sb.toString());
		pw.close();
	}
}
