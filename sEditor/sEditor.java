package sEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

/**
 * @author Siddhartha Sahu
 * 
 *         A simple text editor built using Swing technologies
 * 
 **/
public class sEditor extends JFrame
        implements
        ActionListener {

    private static final long serialVersionUID = 8365312704591336309L;
    private int openDocumentsCounter = 0;
    private int sessionCounter = 0;
    /** The main panel containing the tabbed pane **/
    private JPanel mainEditorPanel = new JPanel();
    ;
	/** Tabbed pane for MDI interface **/
	private JTabbedPane tabbedPane = new JTabbedPane();
    ;
	/** Container for the text area of all open tabs **/
	private ArrayList<TextArea> textAreaContent = new ArrayList<TextArea>();
    /** Container for the internal frame of all open tabs **/
    private ArrayList<JInternalFrame> internalFrames = new ArrayList<JInternalFrame>();
    private String lastOpenedDir = ".";
    /** Constants for result of file actions **/
    private final int STATUS_ERROR = -1;
    private final int STATUS_OK = 0;
    private final int STATUS_NO = 1;
    private final int STATUS_CANCEL = 2;

    public static void main(String[] args) {
        new sEditor();
    }

    /**
     * Constructor for the sEditor main window
     * 
     **/
    public sEditor() {
        setVisible(true);
        setTitle("sEditor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension((int) (screenSize.width / 4),
                (int) (screenSize.height / 4)));
        int width = (int) (screenSize.width / 1.2);
        int height = (int) (screenSize.height / 1.2);
        setSize(width, height);
        setLocation((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2));
        createMenuBar();
        initiateEditor();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitEditor();
            }
        });
    }

    /**
     * Creates the menubar of sEditor
     * 
     **/
    private void createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = null;
        JMenuItem mi = null;

        menu = new JMenu("File");
        menu.setMnemonic('f');
        ArrayList<MenuDetail> file = new ArrayList<MenuDetail>();
        file.add(new MenuDetail("New...", "new", 'n', KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.CTRL_MASK), false));
        file.add(new MenuDetail("Open...", "open", 'o', KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_MASK), false));
        file.add(new MenuDetail("Save", "save", 's', KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_MASK), false));
        file.add(new MenuDetail("Save As...", "saveas", 'a', KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK
                | InputEvent.SHIFT_MASK), true));
        file.add(new MenuDetail("Close", "close", 'c', KeyStroke.getKeyStroke(
                KeyEvent.VK_W, InputEvent.CTRL_MASK), false));
        file.add(new MenuDetail("Exit", "exit", 'x', KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, InputEvent.CTRL_MASK), false));
        for (MenuDetail m : file) {
            mi = new JMenuItem(m.menuName, m.ac);
            mi.setAccelerator(m.keystroke);
            mi.setActionCommand(m.actionLabel);
            mi.addActionListener(this);
            menu.add(mi);
            if (m.seperatorBelow == true) {
                menu.addSeparator();
            }
        }
        menubar.add(menu);

        menu = new JMenu("Documents");
        menu.setMnemonic('d');
        ArrayList<MenuDetail> doc = new ArrayList<MenuDetail>();
        doc.add(new MenuDetail("Save All", "saveall", 's', KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK
                | InputEvent.SHIFT_MASK), false));
        doc.add(new MenuDetail("Close All", "closeall", 'c', KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK
                | InputEvent.SHIFT_MASK), false));
        doc.add(new MenuDetail("Close All but active document", "closeallba",
                'l', null, false));
        for (MenuDetail m : doc) {
            mi = new JMenuItem(m.menuName, m.ac);
            mi.setAccelerator(m.keystroke);
            mi.setActionCommand(m.actionLabel);
            mi.addActionListener(this);
            menu.add(mi);
            if (m.seperatorBelow == true) {
                menu.addSeparator();
            }
        }
        menubar.add(menu);

        menu = new JMenu("Edit");
        menu.setMnemonic('e');
        ArrayList<MenuDetail> edit = new ArrayList<MenuDetail>();
        edit.add(new MenuDetail("Undo", "undo", 'u', KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_MASK), false));
        edit.add(new MenuDetail("Redo", "redo", 'r', KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, InputEvent.CTRL_MASK), true));
        edit.add(new MenuDetail("Cut", "cut", 'c', KeyStroke.getKeyStroke(
                KeyEvent.VK_X, InputEvent.CTRL_MASK), false));
        edit.add(new MenuDetail("Copy", "copy", 'o', KeyStroke.getKeyStroke(
                KeyEvent.VK_C, InputEvent.CTRL_MASK), false));
        edit.add(new MenuDetail("Paste", "paste", 'p', KeyStroke.getKeyStroke(
                KeyEvent.VK_V, InputEvent.CTRL_MASK), false));
        edit.add(new MenuDetail("Select All", "selectall", 's', KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK), false));
        edit.add(new MenuDetail("Delete selected", "delete", 'd', null, true));
        edit.add(new MenuDetail("Find/Replace...", "find", 'f', KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), false));
        for (MenuDetail m : edit) {
            mi = new JMenuItem(m.menuName, m.ac);
            mi.setAccelerator(m.keystroke);
            mi.setActionCommand(m.actionLabel);
            mi.addActionListener(this);
            menu.add(mi);
            if (m.seperatorBelow == true) {
                menu.addSeparator();
            }
        }
        menubar.add(menu);

        menu = new JMenu("Format");
        menu.setMnemonic('m');
        ArrayList<MenuDetail> format = new ArrayList<MenuDetail>();
        format.add(new MenuDetail("Format...", "format", 'f', null, true));
        for (MenuDetail m : format) {
            mi = new JMenuItem(m.menuName, m.ac);
            mi.setAccelerator(m.keystroke);
            mi.setActionCommand(m.actionLabel);
            mi.addActionListener(this);
            menu.add(mi);
            if (m.seperatorBelow == true) {
                menu.addSeparator();
            }
        }
        mi = new JCheckBoxMenuItem("Wrap Text", true);
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().getLineWrap() == false) {
                    textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().setLineWrap(true);
                } else {
                    textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().setLineWrap(false);
                }
            }
        });
        menu.add(mi);
        mi = new JCheckBoxMenuItem("Show Line Number", true);
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (textAreaContent.get(tabbedPane.getSelectedIndex()).getLineNumberBarArea().isVisible()) {
                    textAreaContent.get(tabbedPane.getSelectedIndex()).getLineNumberBarArea().setVisible(false);
                } else {
                    textAreaContent.get(tabbedPane.getSelectedIndex()).getLineNumberBarArea().setVisible(true);
                }
            }
        });
        menu.add(mi);
        menubar.add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic('h');
        ArrayList<MenuDetail> help = new ArrayList<MenuDetail>();
        help.add(new MenuDetail("About...", "about", 'a', null, false));
        for (MenuDetail m : help) {
            mi = new JMenuItem(m.menuName, m.ac);
            mi.setAccelerator(m.keystroke);
            mi.setActionCommand(m.actionLabel);
            mi.addActionListener(this);
            menu.add(mi);
            if (m.seperatorBelow == true) {
                menu.addSeparator();
            }
        }
        menubar.add(menu);
        setJMenuBar(menubar);
    }

    /**
     * Initializes the main window and creates a new file
     * 
     **/
    private void initiateEditor() {
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainEditorPanel.setLayout(new GridLayout(1, 1));
        getContentPane().add(mainEditorPanel, BorderLayout.CENTER);
        createNewFile("", "", "");
    }

    /**
     * Actionlistener for menubar commands
     * 
     **/
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("new".equals(cmd)) {
            createNewFile("", "", "");
        }
        if ("open".equals(cmd)) {
            openFile();
        }
        if ("save".equals(cmd)) {
            saveFile(tabbedPane.getSelectedIndex());
        }
        if ("saveas".equals(cmd)) {
            saveFileAs(tabbedPane.getSelectedIndex());
        }
        if ("close".equals(cmd)) {
            closeFile(tabbedPane.getSelectedIndex());
        }
        if ("exit".equals(cmd)) {
            exitEditor();
        }
        if ("saveall".equals(cmd)) {
            saveAllFiles();
        }
        if ("closeall".equals(cmd)) {
            closeAllFiles();
        }
        if ("closeallba".equals(cmd)) {
            closeAllButActiveFile();
        }
        if ("undo".equals(cmd)) {
            undo();
        }
        if ("redo".equals(cmd)) {
            redo();
        }
        if ("cut".equals(cmd)) {
            cut();
        }
        if ("copy".equals(cmd)) {
            copy();
        }
        if ("paste".equals(cmd)) {
            paste();
        }
        if ("selectall".equals(cmd)) {
            selectAll();
        }
        if ("delete".equals(cmd)) {
            delete();
        }
        if ("find".equals(cmd)) {
            findDialog();
        }
        if ("format".equals(cmd)) {
            formatDialog();
        }
        if ("about".equals(cmd)) {
            showAbout();
        }
    }

    /**
     * Creates a new tab with blank text area
     * 
     * @param fileName
     *            the name of the new file
     * @param fileDir
     *            the directory in which file is to be created
     * @param fileContent
     *            the default text in text area
     * 
     **/
    private void createNewFile(String fileName, String fileDir,
            String fileContent) {
        TextArea ta = new TextArea();
        ta.setSavedStatus(true);
        ta.setFileName(fileName);
        ta.setFileDir(fileDir);
        ta.getTextArea().setText(fileContent);
        textAreaContent.add(ta);
        JInternalFrame jif = new JInternalFrame("", false, false, false);
        jif.setTitle(fileDir);
        jif.add(textAreaContent.get(openDocumentsCounter), BorderLayout.CENTER);
        jif.pack();
        jif.setVisible(true);
        internalFrames.add(jif);
        JDesktopPane dp = new JDesktopPane();
        dp.add(jif, BorderLayout.CENTER);
        try {
            jif.setMaximum(true);
        } catch (PropertyVetoException e) {
            Dimension screenSize = getSize();
            jif.setSize(screenSize.width, screenSize.height);
        }
        if ("".equals(fileName)) {
            fileName = "File" + sessionCounter;
        }
        tabbedPane.add(fileName, dp);
        tabbedPane.setTabComponentAt(openDocumentsCounter,
                new CustomTabButton(tabbedPane, this));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        mainEditorPanel.add(tabbedPane);
        openDocumentsCounter++;
        sessionCounter++;
    }

    /**
     * Opens a new file
     * 
     **/
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser(lastOpenedDir);
        int status = fileChooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fileChooser.getSelectedFile();
                BufferedReader r = new BufferedReader(new FileReader(f));
                String fileContent = "";
                int c = 0;
                while ((c = r.read()) != -1) {
                    fileContent += (char) c;
                }
                lastOpenedDir = f.getPath();
                createNewFile(f.getName(), lastOpenedDir, fileContent);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error opening file!");
            }
        }
    }

    /**
     * Saves to current file or prompts for filename
     * 
     * @param selectedIndex
     *            the index of tab to be saved
     * 
     * @return status of the result of the attempt to save file
     * 
     **/
    private int saveFile(int selectedIndex) {
        tabbedPane.setSelectedIndex(selectedIndex);
        String filePath = textAreaContent.get(selectedIndex).getFileDir();
        if (textAreaContent.isEmpty()) {
            return this.STATUS_ERROR;
        }
        if (textAreaContent.get(selectedIndex) == null) {
            return this.STATUS_ERROR;
        }
        int retVal = this.STATUS_ERROR;
        if ("".equals(filePath)) {
            retVal = saveFileAs(selectedIndex);
        } else {
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(filePath,
                        false));
                bufferedWriter.write(textAreaContent.get(selectedIndex).getTextArea().getText());
                textAreaContent.get(selectedIndex).setSavedStatus(true);
                retVal = this.STATUS_OK;
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file"
                        + selectedIndex);
                retVal = this.STATUS_ERROR;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file"
                        + selectedIndex);
                retVal = this.STATUS_ERROR;
            } finally {
                try {
                    if (bufferedWriter != null) {
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    }
                } catch (IOException ex) {
                    ex.getMessage();
                }
            }
        }
        return retVal;
    }

    /**
     * Saves file with a new name
     * 
     * @param selectedIndex
     *            the index of tab to be saved
     * 
     * @return status of the result of the attempt to save file
     * 
     **/
    private int saveFileAs(int selectedIndex) {
        String fileDir = "", fileName = "";
        if (textAreaContent.isEmpty()) {
            return this.STATUS_ERROR;
        }
        if (textAreaContent.get(selectedIndex) == null) {
            return this.STATUS_ERROR;
        }
        int retVal;
        int retnVal = this.STATUS_ERROR;
        do {
            retVal = this.STATUS_CANCEL;
            JFileChooser chooser = new JFileChooser(lastOpenedDir);
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                retnVal = this.STATUS_CANCEL;
            } else if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                retVal = this.STATUS_OK;
                if (f.exists()) {
                    retVal = JOptionPane.showConfirmDialog(this,
                            "File already exists!\nReplace?");
                    if (retVal == JOptionPane.YES_OPTION) {
                    } else if (retVal == JOptionPane.NO_OPTION) {
                        retVal = this.STATUS_NO;
                    } else if (retVal == JOptionPane.CANCEL_OPTION) {
                        retVal = this.STATUS_CANCEL;
                    }
                }
                if (retVal == this.STATUS_OK) {
                    fileName = chooser.getSelectedFile().getName();
                    fileDir = chooser.getSelectedFile().getPath();
                    try {
                        FileWriter writer = new FileWriter(f);
                        textAreaContent.get(selectedIndex).getTextArea().write(writer);
                        textAreaContent.get(selectedIndex).setSavedStatus(true);
                        textAreaContent.get(selectedIndex).setFileName(fileName);
                        textAreaContent.get(selectedIndex).setFileDir(fileDir);
                        retnVal = this.STATUS_OK;
                        writer.close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                "Failed to save file" + selectedIndex);
                        retVal = this.STATUS_ERROR;
                    }
                }
            }
        } while (retVal == this.STATUS_NO);
        if (retnVal == this.STATUS_OK) {
            tabbedPane.setTitleAt(selectedIndex, fileName);
            internalFrames.get(selectedIndex).setTitle(fileDir);
            lastOpenedDir = fileDir;
        }
        return retnVal;
    }

    /**
     * Closes a file
     * 
     * @param selectedIndex
     *            the index of tab to be closed
     * 
     * @return status of the result of the attempt to save file
     * 
     **/
    public int closeFile(int selectedIndex) {
        tabbedPane.setSelectedIndex(selectedIndex);
        int retVal = this.STATUS_ERROR;
        if (textAreaContent.get(selectedIndex).getSavedStatus() == false
                && textAreaContent.get(selectedIndex).getTextArea().getText().length() != 0) {
            int retvalue = JOptionPane.showConfirmDialog(this,
                    "File not Saved.\nSave now?");
            if (retvalue == JOptionPane.CANCEL_OPTION) {
                retVal = this.STATUS_CANCEL;
            } else if (retvalue == JOptionPane.YES_OPTION) {
                retVal = saveFile(selectedIndex);
            }
        }
        if (retVal != this.STATUS_CANCEL) {
            tabbedPane.remove(selectedIndex);
            textAreaContent.remove(selectedIndex);
            internalFrames.remove(selectedIndex);
            openDocumentsCounter--;
        }
        if (tabbedPane.getTabCount() == 0) {
            sessionCounter = 0;
            createNewFile("", "", "");
        }
        return retVal;
    }

    /**
     * Exits the editor
     * 
     **/
    private void exitEditor() {
        int r = closeAllFiles();
        if (r != this.STATUS_CANCEL) {
            System.exit(0);
        }
    }

    /**
     * Saves all open file tabs
     * 
     **/
    private void saveAllFiles() {
        int tabs = tabbedPane.getTabCount();
        for (int i = 0; i < tabs; i++) {
            int t = saveFile(i);
            if (t == this.STATUS_CANCEL) {
                break;
            }
        }
    }

    /**
     * Closes all open file tabs after attempting to save them
     * 
     **/
    private int closeAllFiles() {
        int tabs = tabbedPane.getTabCount();
        int retVal = this.STATUS_ERROR;
        for (int i = tabs - 1; i >= 0; i--) {
            retVal = closeFile(i);
            if (retVal == this.STATUS_CANCEL) {
                break;
            }
        }
        return retVal;
    }

    /**
     * Closes all open file tabs except the tab in focus
     * 
     **/
    private void closeAllButActiveFile() {
        int current = tabbedPane.getSelectedIndex();
        int tabs = tabbedPane.getTabCount();
        for (int i = tabs - 1; i >= 0; i--) {
            if (i != current) {
                closeFile(i);
            }
        }
    }

    /**
     * Undo action
     * 
     **/
    private void undo() {
        if (tabbedPane.getTabCount() != 0) {
            textAreaContent.get(tabbedPane.getSelectedIndex()).undo();
        }
    }

    /**
     * Redo action
     * 
     **/
    private void redo() {
        if (tabbedPane.getTabCount() != 0) {
            textAreaContent.get(tabbedPane.getSelectedIndex()).redo();
        }
    }

    /**
     * Cut action
     * 
     **/
    private void cut() {
        textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().cut();
    }

    /**
     * Copy action
     * 
     **/
    private void copy() {
        textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().copy();
    }

    /**
     * Paste action
     * 
     **/
    private void paste() {
        textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().paste();
    }

    /**
     * Deletes the selected text
     * 
     **/
    private void delete() {
        int s = textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().getSelectionStart();
        int e = textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().getSelectionEnd();
        textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea().replaceRange(null, s, e);
    }

    /**
     * Selects all text in current text tab
     * 
     **/
    private void selectAll() {
        JTextArea t = textAreaContent.get(
                tabbedPane.getSelectedIndex()).getTextArea();
        t.selectAll();
    }

    /**
     * Opens file/replace dialog for searching and replacing operations
     * 
     **/
    private void findDialog() {
        new FindReplaceDialog(this, false, textAreaContent.get(
                tabbedPane.getSelectedIndex()).getTextArea());
    }

    /**
     * Opens dialog for Font options
     * 
     **/
    private void formatDialog() {
        new FontChooser(this, textAreaContent.get(tabbedPane.getSelectedIndex()).getTextArea());
    }

    private void showAbout() {
        AboutDialog.showAboutDialog();
    }
}
