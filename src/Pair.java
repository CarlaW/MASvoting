public class Pair implements Comparable<Pair> {

    public int count;
    public char option;

    public Pair(int count, char option){
        this.count = count;
        this.option = option;
    }

    @Override
    public int compareTo(Pair o) {
        return -1 * Integer.valueOf(this.count).compareTo(o.count);
    }
}
