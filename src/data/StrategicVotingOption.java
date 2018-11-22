package data;

import java.util.Arrays;

public class StrategicVotingOption {

	public char[] v;
	public Pair[] newO;
	public int newH;
	public String z;

	public StrategicVotingOption(char[] v, Pair[] newO, int newH, String z) {

		this.v = v;
		this.newO = newO;
		this.newH = newH;
		this.z = z;

		System.out.println("- - - - - - - ");
		System.out.println("New tuple!");
		System.out.println("V is: " + new String(v));
		System.out.println("New outcome is ....");
		for (int i = 0; i < newO.length; i++) {
			System.out.println(newO[i].option + ": " + newO[i].count);
		}
		System.out.println("NewH is " + newH);
		System.out.println("Reason is ");
		System.out.println("- - - - - - - ");

	}
}
