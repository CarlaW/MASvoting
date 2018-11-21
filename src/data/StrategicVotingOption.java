package data;
public class StrategicVotingOption {

    public final int voterIndex;
    public char[] newPreference;
    public Pair[]  newOutcome;
    public int newHappiness;
    public String reason;

    public StrategicVotingOption(int voterIndex, char[] newPreference, Pair[] newOutcome, int newHapiness, String reason){
        this.voterIndex = voterIndex;
        this.newPreference = newPreference;
        this.newOutcome = newOutcome;
        this.newHappiness = newHapiness;
        this.newHappiness = newHappiness;
    }
}
