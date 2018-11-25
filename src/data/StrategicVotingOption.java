package data;

import java.util.Arrays;

public class StrategicVotingOption {

	/*
	stores the strategic voting option tuples
	 */

	public char[] v;
	public Pair[] newO;
	public int[] newH;
	public String z;
	public int voterID;

	public StrategicVotingOption(char[] v, Pair[] newO, int[] newH, String z, int voterID) {

		this.v = v;
		this.newO = newO;
		this.newH = newH;
		this.z = z;
		this.voterID = voterID;
	}
}
