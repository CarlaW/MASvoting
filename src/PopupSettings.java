import javax.swing.*;
import java.awt.*;

public class PopupSettings extends JPanel {

    private JComboBox schemeList;
    private JTextField voters;
    private JTextField options;

    public PopupSettings(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel votingScheme = makeVotingSchemePanel();
        JPanel matrixSize = makeMatrixSizePanel();
        add(votingScheme);
        add(matrixSize);
    }

    private JPanel makeVotingSchemePanel(){
        JPanel panel = new JPanel();
        String[] schemes = {"Voting for 1 (Plurality)", "Voting for 2", "Anti-plurality (Veto)", "Borda"};
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

    public JComboBox getVotingScheme(){
        return schemeList;
    }

    public JTextField getVoters(){
        return voters;
    }

    public JTextField getOptions(){
        return options;
    }
}
