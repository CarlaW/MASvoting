import javax.swing.*;
import java.awt.*;

public class PopupInput extends JPanel {

    private int voters;
    private int options;
    private JTextField[][] inputMatrix;

    public PopupInput(int voters, int options){
        this.voters = voters;
        this.options = options;
        JPanel inputMatrix = createInputMatrix();
        add(inputMatrix);
    }

    private JPanel createInputMatrix(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(options+1, voters+1));

        inputMatrix = new JTextField[options][voters];

        for (int i=0; i<options+1; i++){
            for (int j=0; j<voters+1; j++){
                if (i==0 &&j==0){
                    JLabel empty = new JLabel();
                    panel.add(empty);
                } else if(i==0){
                    JLabel label = new JLabel("Voter #" + j, SwingConstants.CENTER);
                    panel.add(label);
                } else if (j==0){
                    JLabel label = new JLabel("Preference #" + i);
                    panel.add(label);
                } else {
                    JTextField text = new JTextField(2);
                    text.setDocument(new JTextFieldLimit(1, options));
                    inputMatrix[i-1][j-1] = text;
                    panel.add(text);
                }
            }
        }
        return panel;
    }

    public char[][] getPreferenceMatrix(){
        char[][] preferenceMatrix = new char[options][voters];
        for (int i=0; i<options; i++){
            for (int j=0; j<voters; j++){
                if (inputMatrix[i][j].getText().isEmpty()){
                    preferenceMatrix[i][j] = ' ';
                } else {
                    preferenceMatrix[i][j] = inputMatrix[i][j].getText().charAt(0);
                }
            }
        }
        return preferenceMatrix;
    }
}
