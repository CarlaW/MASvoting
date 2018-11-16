public class StrategicVotingOption {

    public final int voterIndex;
    public int[] v;
    public int[] newO;
    public int newH;
    public String z;

    public StrategicVotingOption(int voterIndex, int[] v, int[] newO, int newH, String z){
        this.voterIndex = voterIndex;
        this.v = v;
        this.newO = newO;
        this.newH = newH;
        this.z = z;
    }
}
