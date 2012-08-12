package sEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;
import javax.swing.undo.*;

/**
 * Class for the text area used in each tab
 * 
 **/
public class TextArea extends JPanel {
    private static final long serialVersionUID = 5342961201711426309L;
    private JScrollPane scrollPane = null;
    private JTextArea textArea = null;
    private JTextArea lineNumberBar;
    private JTextField cursorStatusBar;
    private boolean savedStatus = false;
    private String fileName = "";
    private String fileDir = "";
    final UndoManager undo = new UndoManager();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public boolean getSavedStatus() {
        return savedStatus;
    }

    public void setSavedStatus(boolean savedStatus) {
        this.savedStatus = savedStatus;
    }

    public JTextArea getTextArea() {
        return (textArea);
    }

    public JTextArea getLineNumberBarArea() {
        return (lineNumberBar);
    }

    public TextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        lineNumberBar = new JTextArea("1");
        lineNumberBar.setFont(textArea.getFont());
        lineNumberBar.setBackground(Color.LIGHT_GRAY);
        lineNumberBar.setEditable(false);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int cursorLocation = textArea.getDocument().getLength();
                Element rootElement = textArea.getDocument()
                        .getDefaultRootElement();
                String sideNumber = "1" + System.getProperty("line.separator");
                for (int i = 2; i < rootElement.getElementIndex(cursorLocation) + 2; i++) {
                    sideNumber += i + System.getProperty("line.separator");
                }
                return sideNumber;
            }

            public void changedUpdate(DocumentEvent e) {
                lineNumberBar.setText(getText());
                savedStatus = false;
            }

            public void insertUpdate(DocumentEvent e) {
                lineNumberBar.setText(getText());
                savedStatus = false;
            }

            public void removeUpdate(DocumentEvent e) {
                lineNumberBar.setText(getText());
                savedStatus = false;
            }
        });
        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(textArea);
        scrollPane.setRowHeaderView(lineNumberBar);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(111)),
                        BorderFactory.createEmptyBorder(6, 6, 6, 6)),
                scrollPane.getBorder()));
        textArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                JTextArea dArea = (JTextArea) e.getSource();
                int rowNum = 1;
                int colNum = 1;
                try {
                    int caretpos = dArea.getCaretPosition();
                    rowNum = dArea.getLineOfOffset(caretpos);
                    colNum = caretpos - dArea.getLineStartOffset(rowNum);
                    rowNum += 1;
                } catch (Exception ex) {
                }
                updatesBar(rowNum, colNum);
            }
        });
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        cursorStatusBar = new JTextField();
        this.add(cursorStatusBar, BorderLayout.SOUTH);
        updatesBar(1, 0);
        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent e) {
                        undo.addEdit(e.getEdit());
                    }
                });
        textArea.getActionMap().put("Undo", new AbstractAction("Undo") {
            private static final long serialVersionUID = 5346267885089932145L;

            public void actionPerformed(ActionEvent evt) {
                undo();
            }
        });
        textArea.getActionMap().put("Redo", new AbstractAction("Redo") {
            private static final long serialVersionUID = 1890793635382898796L;

            public void actionPerformed(ActionEvent evt) {
                redo();
            }
        });
    }

    public void undo() {
        try {
            if (undo.canUndo()) {
                undo.undo();
            }
        } catch (CannotUndoException e) {
        }
    }

    public void redo() {
        try {
            if (undo.canRedo()) {
                undo.redo();
            }
        } catch (CannotRedoException e) {
        }
    }

    private void updatesBar(int rowNum, int colNum) {
        cursorStatusBar.setText("Line: " + rowNum + " Column: " + colNum);
    }
}
