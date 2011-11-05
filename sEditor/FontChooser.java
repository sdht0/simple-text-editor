package sEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;

public class FontChooser extends JDialog {
	private static final long serialVersionUID = -814377347358613089L;
	private static boolean sessionActive = false;
	String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getAvailableFontFamilyNames();
	String[] fontSizes = {"9", "10", "11", "12", "14", "16", "18", "20", "22",
			"24", "28", "36", "48", "72"};
	JPanel parentPanel;
	JList<String> fontlist = new JList<String>(fonts);
	JList<String> sizelist = new JList<String>(fontSizes);
	JCheckBox bold = new JCheckBox("Bold");
	JCheckBox italic = new JCheckBox("Italic");
	JTextArea textArea;
	Font font;
	public FontChooser(Frame frame, JTextArea t) {
		super(frame, "Choose Font", false);
		if (sessionActive == true) {
			dispose();
			return;
		}
		sessionActive = true;
		this.textArea = t;
		font = textArea.getFont();
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width / 3);
		int height = (int) (screenSize.height / 1.5);
		setSize(width, height);
		setLocation((screenSize.width / 2) - (width / 2),
				(screenSize.height / 2) - (height / 2));
		parentPanel = new JPanel(new BorderLayout());
		add(parentPanel);
		createFontList();
		createButtons();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sessionActive = false;
				dispose();
			}
		});
	}

	private void createFontList() {
		JPanel content = new JPanel(new BorderLayout());
		JPanel fontPanel = new JPanel(new BorderLayout());
		fontPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Font"));
		JScrollPane fontpane = new JScrollPane(fontlist);
		fontpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fontpane.setBorder(BorderFactory.createEtchedBorder());
		fontPanel.add(fontpane);
		JPanel sizePanel = new JPanel(new BorderLayout());
		sizePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Size"));
		JScrollPane sizepane = new JScrollPane(sizelist);
		sizepane.setBorder(BorderFactory.createEtchedBorder());
		sizePanel.add(sizepane);
		JPanel attributes = new JPanel(new GridLayout(1, 2));
		attributes.add(bold);
		attributes.add(italic);
		attributes.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Attributes"));
		JPanel right = new JPanel(new BorderLayout());
		right.add(sizePanel, BorderLayout.CENTER);
		right.add(attributes, BorderLayout.SOUTH);
		content.add(fontPanel);
		content.add(right, BorderLayout.EAST);
		final String fontName = font.getName();
		ListModel<?> model = fontlist.getModel();
		fontlist.clearSelection();
		for (int i = 0; i < model.getSize(); i++) {
			if (fontName.equals(model.getElementAt(i))) {
				fontlist.setSelectedIndex(i);
				break;
			}
		}
		final String fontSize = String.valueOf(font.getSize());
		model = sizelist.getModel();
		sizelist.clearSelection();
		for (int i = 0; i < model.getSize(); i++) {
			if (fontSize.equals(model.getElementAt(i))) {
				sizelist.setSelectedIndex(i);
				break;
			}
		}
		if (font.isBold())
			bold.setSelected(true);
		if (font.isItalic())
			italic.setSelected(true);
		parentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		parentPanel.add(content);
	}

	public void createButtons() {
		JButton okButton;
		JButton cancelButton;
		JPanel childPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textArea.setFont(getSelectedFont());
				sessionActive = false;
				dispose();
			}
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sessionActive = false;
				dispose();
			}
		});
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		childPanel.add(buttonPanel, BorderLayout.EAST);
		childPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		parentPanel.add(childPanel, BorderLayout.SOUTH);
	}

	private Font getSelectedFont() {
		String name = (String) this.fontlist.getSelectedValue();
		int s = Font.PLAIN;
		if (this.bold.isSelected() && this.italic.isSelected()) {
			s = Font.BOLD | Font.ITALIC;
		}
		if (this.bold.isSelected()) {
			s = Font.BOLD;
		}
		if (this.italic.isSelected()) {
			s = Font.ITALIC;
		}
		int size;
		final String selected = (String) this.sizelist.getSelectedValue();
		if (selected != null) {
			size = Integer.parseInt(selected);
		} else {
			size = 12;
		}
		Font f = new Font(name, s, size);
		return f;
	}
}
