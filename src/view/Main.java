package view;
import javax.swing.*;

import data.PreferenceMatrix;
import data.VotingScheme;

import java.awt.*;
import java.util.ArrayList;


public class Main {

	private int noVoters;
	private int noOptions;
	private int output;

	private VotingScheme scheme = VotingScheme.Plurality;
	private PreferenceMatrix preferenceMatrix;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

	private Main() {
		initialize();
	}

	private void initialize() {
		askForSettings();
		askForPreferenceMatrix();

		JFrame frame = new JFrame("Tactical Voting Analyst");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		TVApanel tva = new TVApanel(scheme, preferenceMatrix, output);
		if (output != 0) {
			panel.add(tva);
			frame.add(panel);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds(0, 0, screenSize.width, screenSize.height);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setVisible(true);
		}
	}

	private void askForSettings() {
		PopupSettings settings = new PopupSettings();
		int out = JOptionPane.showConfirmDialog(null, settings, "Enter preferences", JOptionPane.OK_CANCEL_OPTION);
		if (out != 0) {
			System.exit(0);
		}
		
		scheme = scheme.getByName((String)settings.getVotingScheme().getSelectedItem());
		output = settings.getOutput().getSelectedIndex();
		try {
			noVoters = Integer.parseInt(settings.getVoters().getText());
			noOptions = Integer.parseInt(settings.getOptions().getText());
			if (noOptions > 26) {
				JOptionPane.showMessageDialog(null, "There can be no more than 26 preferences.");
				askForSettings();
			} else if (noOptions < 3) {
				JOptionPane.showMessageDialog(null, "There have to be more than 2 preferences.");
				askForSettings();
			} else if (noVoters < 3) {
				JOptionPane.showMessageDialog(null, "There have to be more than 2 voters.");
				askForSettings();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a number.");
			askForSettings();
		}
	}

	private void askForPreferenceMatrix() {
		PopupInput input = new PopupInput(noVoters, noOptions);
		int out = JOptionPane.showConfirmDialog(null, input, "Enter preference matrix", JOptionPane.OK_CANCEL_OPTION);
		if (out != 0) {
			System.exit(0);
		}
		preferenceMatrix= input.getPreferenceMatrix();
		/*
		 * for (int i=0; i<noOptions; i++){ for (int j=0; j<noVoters; j++){
		 * System.out.print("i = " + i + " j = " + j + " ");
		 * if(preferenceMatrix[i][j].isEmpty()){ System.out.print(" "); }
		 * System.out.print(preferenceMatrix[i][j] + "   "); } System.out.println(); }
		 */
		if (doubleVotePerVoter()) {
			JOptionPane.showMessageDialog(null, "A voter cannot list the same option twice.");
			askForPreferenceMatrix();
		}
		if (noVotesPerVoter()) {
			JOptionPane.showMessageDialog(null, "A voter must have a first preference.");
			askForPreferenceMatrix();
		}
	}

	private boolean doubleVotePerVoter() {
		for (int i = 0; i < noVoters; i++) {
			for (int j = 0; j < noOptions - 1; j++) {
				if (preferenceMatrix.table[i][j] == preferenceMatrix.table[i + 1][j]) {
					if (preferenceMatrix.table[i][j] == ' ') {
						continue;
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean noVotesPerVoter() {
		for (int i = 0; i < noVoters; i++) {
			if (preferenceMatrix.table[i][0] == preferenceMatrix.table[i][0]) {
				if (preferenceMatrix.table[i][0] == ' ') {
					return true;
				}
			}
		}
		return false;
	}
}
