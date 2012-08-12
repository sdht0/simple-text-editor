package sEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AboutDialog {
	public static void showAboutDialog() {
		final JFrame frame = new JFrame();
		frame.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width / 2);
		int height = (int) (screenSize.height / 1.7);
		frame.setSize(width, height);
		frame.setLocation((screenSize.width / 2) - (width / 2),
				(screenSize.height / 2) - (height / 2));
		frame.setTitle("About");
		JPanel parentPanel = new JPanel();
		JPanel childPanel = new JPanel();
		JLabel titleLabel = new JLabel("sEditor");
		titleLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		JLabel descLabel = new JLabel(
				"A simple text editor using Java Swing technlogies.");
		JLabel versionLabel = new JLabel("Version: 1.0");
		JLabel authorLabel = new JLabel(
				"Created by: Siddhartha Sahu <sh.siddhartha@gmail.com>");
		JLabel creditLabel = new JLabel("Credits:");
		JTextPane creditsText = new JTextPane();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				frame.dispose();
			}
		});
		creditsText.setMaximumSize(new Dimension((int) (width / 1.2), 100));
		creditsText.setEditable(false);
		creditsText
				.setText("This java program would not have been possible without the generous tutorials, help"
						+ " and code examples from the following sources:\n\n"
						+ "* The Oracle Tutorials at http://download.oracle.com/javase/tutorial\n\n"
						+ "* Code examples at http://www.java2s.com/\n\n"
						+ "* Questions and Answers at http://stackoverflow.com\n\n"
						+ "* The jEdit text editor at https://github.com/finbrein/jedit-text-editor\n\n"
						+ "and also the various sites visited when googling for key concepts!");
		childPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		GroupLayout layout = new GroupLayout(childPanel);
		childPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(titleLabel).addComponent(versionLabel)
				.addComponent(descLabel).addComponent(authorLabel)
				.addComponent(creditLabel, GroupLayout.Alignment.LEADING)
				.addComponent(creditsText).addComponent(okButton));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(titleLabel).addComponent(versionLabel)
				.addComponent(descLabel).addComponent(authorLabel)
				.addComponent(creditLabel).addComponent(creditsText).addGap(20)
				.addComponent(okButton));
		parentPanel.add(childPanel);
		frame.add(parentPanel);
	}
}
