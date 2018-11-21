package data;
import java.util.HashMap;

public class PreferenceMatrix {

	public PreferenceMatrix(int numOfVoters, int numOfCandidates) {

		this.table = new char[numOfCandidates][numOfVoters];
		this.numOfVoters = numOfVoters;
		this.numOfCandidates = numOfCandidates;
	
	}
	public PreferenceMatrix(char[][] table) {
		this.table = table;
		this.numOfVoters = table[0].length;
		this.numOfCandidates = table.length;
	}
	
	
	public void SetPreferences(char[][] table) {
		this.table = table;
	}
	


	
	public char[][] table;
	public HashMap<Character, Integer> outcome;
	public int numOfCandidates;
	public int numOfVoters;

}
