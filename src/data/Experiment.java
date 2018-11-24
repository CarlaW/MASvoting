package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Experiment {

	// TODO Put it somewhere else
	public static final String[] schemes = { "Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)",
			"Borda" };;
	private int votingScheme;
	ArrayList<char[][]> matrixPermutation;
	float[] riskPerPermutation;
	float[][] riskPerScheme;
	String outputInfo;

	public Experiment(int votingScheme, ArrayList<char[][]> matrixPermutation, String oututInfo) {
		this.outputInfo = oututInfo;
		this.votingScheme = votingScheme;
		this.matrixPermutation = matrixPermutation;
		riskPerPermutation = getRiskArray(matrixPermutation, votingScheme);
		try {
			writeRisk(riskPerPermutation);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Experiment(ArrayList<char[][]> matrixPermutation) {
		this.matrixPermutation = matrixPermutation;
		for (int i = 0; i < schemes.length; i++) {
			riskPerScheme[i] = getRiskArray(matrixPermutation, i);
			try {
				writeRisk(riskPerPermutation);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private float[] getRiskArray(ArrayList<char[][]> matrixPermutation, int votingScheme) {
		float[] riskPerPermutation = new float[matrixPermutation.size()];
		for (int h = 0; h < matrixPermutation.size(); h++) {
			char[][] prefMatrix = matrixPermutation.get(h);
			TVA tva = new TVA(votingScheme, prefMatrix);
			riskPerPermutation[h] = tva.getRisk();
		}
		return riskPerPermutation;
	}

	public float[] getRiskPerPermutation() {
		return riskPerPermutation;
	}

	public void writeRisk(float[] riskPerPermutation) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("risk" + outputInfo + ".csv"));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < riskPerPermutation.length; i++) {
			sb.append(riskPerPermutation[i]);
			System.out.println(riskPerPermutation[i]);
			sb.append(',');
		}
		pw.write(sb.toString());
		pw.close();
	}
}
