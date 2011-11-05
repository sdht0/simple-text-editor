package sEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FindReplaceDialog extends JDialog {
	private static final long serialVersionUID = 3766698499692046178L;
	private static boolean sessionActive = false;
	private JTextArea textArea;
	private int currentPos = 0;
	private int pos = -1;
	private JPanel parentPanel;
	JTextField replaceTextField = new JTextField();
	JTextField findTextField = new JTextField();
	JButton findNext = new JButton("Find Next", null);
	JButton findPrevious = new JButton("Find Previous", null);
	JLabel findLabel = new JLabel("Find Text:");
	JButton replaceAll = new JButton("Replace All", null);
	JButton replace = new JButton("Replace", null);
	JLabel notFound = new JLabel("Text not Found");
	public FindReplaceDialog(Frame parent, boolean modal, JTextArea textArea) {
		super(parent, modal);
		this.textArea = textArea;
		initComponents();
	}
	private void initComponents() {
		if (sessionActive == true) {
			dispose();
			return;
		}
		sessionActive = true;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Find/Replace");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width / 2.6);
		int height = (int) (screenSize.height / 4.5);
		setSize(width, height);
		setLocation((screenSize.width / 2) - (width / 2),
				(screenSize.height / 2) - (height / 2));
		setResizable(false);
		setVisible(true);
		setModal(false);
		notFound.setForeground(Color.RED);
		notFound.setVisible(false);
		createDialog();
		add(parentPanel);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sessionActive = false;
				dispose();
			}
		});
	}
	private void createDialog() {
		JLabel replaceLabel = new JLabel("Replace with:");
		findNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				findNext();
			}
		});
		findPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				findPrevious();
			}
		});
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				replace();
			}
		});
		replaceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				replaceAll();
			}
		});
		findTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				notFound.setVisible(false);
			}
		});
		replaceTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				notFound.setVisible(false);
			}
		});
		parentPanel = new JPanel();
		GroupLayout layout = new GroupLayout(parentPanel);
		parentPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		int buttonSize = 130;
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING)
												.addComponent(findLabel)
												.addComponent(replaceLabel))
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(findTextField)
												.addComponent(replaceTextField)))
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(notFound)
								.addComponent(findNext, buttonSize, buttonSize,
										buttonSize)
								.addComponent(findPrevious, buttonSize,
										buttonSize, buttonSize))
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(replace, buttonSize, buttonSize,
										buttonSize)
								.addComponent(replaceAll, buttonSize,
										buttonSize, buttonSize)));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(findLabel)
												.addGap(12)
												.addComponent(replaceLabel))
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(findTextField)
												.addComponent(replaceTextField)))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(notFound).addComponent(findNext)
								.addComponent(findPrevious))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(replace).addComponent(replaceAll)));
	}

	private void replaceAll() {
		if (!findTextField.getText().isEmpty()) {
			String oldContent = textArea.getText();

			String newContent = oldContent.replaceAll(findTextField.getText(),
					replaceTextField.getText());
			textArea.setText(newContent);
		}
	}

	private void replace() {
		String replaceWord = replaceTextField.getText();
		if (textArea.getSelectedText() != null)
			textArea.replaceSelection(replaceWord);

	}

	private void findNext() {
		if (findTextField.getText().isEmpty())
			return;
		String context = textArea.getText();
		String wordToFind = findTextField.getText();
		pos = context.indexOf(wordToFind, currentPos);
		if (pos == -1)
			currentPos = 0;
		pos = context.indexOf(wordToFind, currentPos);
		currentPos = pos;
		if (pos != -1) {
			textArea.setSelectionStart(pos);
			textArea.setSelectionEnd(pos + wordToFind.length());
			textArea.requestFocusInWindow();
			currentPos = (currentPos + 1);
		} else {
			currentPos = 0;
			notFound.setVisible(true);
		}
	}
	private void findPrevious() {
		boolean found = false;
		if (findTextField.getText().isEmpty())
			return;
		String context = textArea.getText();
		String wordToFind = findTextField.getText();
		if (wordToFind.length() > context.length())
			return;
		pos = currentPos - wordToFind.length();
		while (pos != currentPos) {
			if (pos < 0) {
				pos = context.length() - wordToFind.length();
			}
			if (context.subSequence(pos, pos + wordToFind.length()).equals(
					wordToFind)) {
				found = true;
				currentPos = pos + 1;
				textArea.setSelectionStart(pos);
				textArea.setSelectionEnd(pos + wordToFind.length());
				textArea.requestFocusInWindow();
				break;
			}
			pos--;
		}
		if (found == false)
			notFound.setVisible(true);
	}
}
