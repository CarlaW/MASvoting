package view;
import javax.swing.*;
import java.awt.*;

public class PopupSettings extends JPanel {

    private JComboBox schemeList;
    private JTextField voters;
    private JTextField options;
    private JComboBox output;

    public PopupSettings(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel votingScheme = makeVotingSchemePanel();
        JPanel matrixSize = makeMatrixSizePanel();
        JPanel outputStyle = makeOutputStylePanel();
        add(votingScheme);
        add(matrixSize);
        add(outputStyle);
    }

    private JPanel makeVotingSchemePanel(){
        JPanel panel = new JPanel();
        String[] schemes = {"Plurality", "Voting For Two", "AntiPlurality", "Borda Voting"};
        schemeList = new JComboBox(schemes);
        schemeList.setSelectedIndex(0);
        panel.add(new JLabel("Voting scheme"));
        panel.add(schemeList);
        return panel;
    }

    private JPanel makeMatrixSizePanel(){
        JPanel panel = new JPanel();
        voters = new JTextField(5);
        options = new JTextField(5);
        JLabel voteLabel = new JLabel("Enter # of voters");
        JLabel optionLabel = new JLabel("Enter # of voting options");
        panel.add(voteLabel);
        panel.add(voters);
        panel.add(optionLabel);
        panel.add(options);
        return panel;
    }

    private JPanel makeOutputStylePanel(){
        JPanel panel = new JPanel();
        String[] styles = {"Console", "GUI", "Console + GUI"};
        output = new JComboBox(styles);
        output.setSelectedIndex(0);
        panel.add(new JLabel("Select the way the output is displayed"));
        panel.add(output);
        return panel;
    }

    public JComboBox getVotingScheme(){
        return schemeList;
    }

    public JTextField getVoters(){
        return voters;
    }

    public JTextField getOptions(){
        return options;
    }

    public JComboBox getOutput() {
        return output;
    }
}
