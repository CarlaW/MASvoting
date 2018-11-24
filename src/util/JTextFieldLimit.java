package util;
import javax.swing.text.*;

public class JTextFieldLimit extends PlainDocument {

    public static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private int limit;
    private String options = "";
	

    public JTextFieldLimit(int limit, int noOptions) {
        super();
        this.limit = limit;
        for (int i=0; i<noOptions; i++){
            options = options + ALPHABET[i];
        }
    }

    private boolean correctLetter(String s){
        if (options.contains(s)) {
            return true;
        } else {
            return false;
        }
    }

    public void insertString(int offset, String  str, AttributeSet attr ) throws BadLocationException {
        if (str == null) {
            return;
        } else {
            str = str.toUpperCase();
        }

        //check if you can actually vote for that letter, if not, remove it immediately again
        if (!correctLetter(str)){
            super.remove(offset, limit);
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        } else if ((getLength() + str.length()) == limit+1){ //if you try to enter 2 letters, only keep newest letter
            super.remove(0, limit);
            super.insertString(0, str, attr);
        }
    }
}